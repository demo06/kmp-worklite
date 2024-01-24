import com.lark.oapi.Client
import com.lark.oapi.core.utils.Jsons
import com.lark.oapi.service.drive.v1.enums.FileUploadInfoParentTypeEnum
import com.lark.oapi.service.drive.v1.enums.UploadAllFileParentTypeEnum
import com.lark.oapi.service.drive.v1.model.FileUploadInfo
import com.lark.oapi.service.drive.v1.model.UploadAllFileReq
import com.lark.oapi.service.drive.v1.model.UploadAllFileReqBody
import com.lark.oapi.service.drive.v1.model.UploadFinishFileReq
import com.lark.oapi.service.drive.v1.model.UploadFinishFileReqBody
import com.lark.oapi.service.drive.v1.model.UploadPartFileReq
import com.lark.oapi.service.drive.v1.model.UploadPartFileReqBody
import com.lark.oapi.service.drive.v1.model.UploadPrepareFileReq
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.RandomAccessFile
abstract class UploadToLarkTask : DefaultTask() {

    @get:Input
    abstract val appId: Property<String>

    @get:Input
    abstract val appSecret: Property<String>

    @get:Input
    abstract val remoteFolderNode: Property<String>

    @get:Input
    lateinit var file: Provider<File>

    @TaskAction
    fun execute() {
        val client = Client.newBuilder(appId.get(), appSecret.get())
            .build()

        println("appId:${appId.get()}, appSecret:${appSecret.get()}")
        println("fileInfo: ${file.get().name.substringAfterLast('/')} size:${file.get().length()}")
        println("saveFolder: ${remoteFolderNode.get()}")

        if (file.get().isChunkUploadRequired) {
            chunkUploadFile(client, file.get())
        } else {
            uploadFile(client, file.get())
        }
    }

    private fun uploadFile(client: Client, file: File) {
        val request = UploadAllFileReq.newBuilder()
            .uploadAllFileReqBody(
                UploadAllFileReqBody.newBuilder()
                    .parentType(UploadAllFileParentTypeEnum.EXPLORER)
                    .parentNode(remoteFolderNode.get())
                    .fileName(file.name.substringAfterLast('/'))
                    .file(file)
                    .size(file.length().toInt())
                    .build()
            )
            .build()
        val response = client.drive().file().uploadAll(request)
        if (!response.success()) {
            logger.error("upload error: ${Jsons.DEFAULT.toJson(response)}")
            return
        }
    }

    private fun chunkUploadFile(client: Client, file: File) {
        val responsePrepare = client.drive().file().uploadPrepare(
            UploadPrepareFileReq.Builder()
                .fileUploadInfo(
                    FileUploadInfo.Builder()
                        .parentType(FileUploadInfoParentTypeEnum.EXPLORER)
                        .parentNode(remoteFolderNode.get())
                        .fileName(file.name.substringAfterLast('/'))
                        .size(file.length().toInt())
                        .build()
                )
                .build()
        )
        if (!responsePrepare.success()) {
            logger.error("upload prepare error: ${Jsons.DEFAULT.toJson(responsePrepare)}")
            return
        }

        val chunkSize = responsePrepare.data.blockSize
        var chunkNumber = 0
        RandomAccessFile(file, "r").use { raf ->
            val totalLength = raf.length()
            while (raf.filePointer < totalLength) {
                // 创建临时文件
                val temp = File.createTempFile("chunk", ".tmp")
                try {
                    temp.outputStream().use { fos ->
                        val buffer = ByteArray(chunkSize)
                        val bytesRead = raf.read(buffer, 0, chunkSize)
                        fos.write(buffer, 0, bytesRead)

                        // 文件块数据存储在temp 中，你现在可以上传这一块
                        val responsePart = client.drive().file().uploadPart(
                            UploadPartFileReq.Builder()
                                .uploadPartFileReqBody(
                                    UploadPartFileReqBody.Builder()
                                        .uploadId(responsePrepare.data.uploadId)
                                        .seq(chunkNumber)
                                        .size(bytesRead)
                                        .file(temp)
                                        .build()
                                )
                                .build()
                        )
                        if (!responsePart.success()) {
                            logger.error("upload part error: ${Jsons.DEFAULT.toJson(responsePart)}")
                            return
                        }
                    }
                } finally {
                    // 确保无论上传是否成功，临时文件都会被删除
                    temp.delete()
                }
                chunkNumber++
            }
        }
        if (chunkNumber != responsePrepare.data.blockNum) {
            logger.error("chunk num is diff: ${chunkNumber}-${responsePrepare.data.blockNum}")
            return
        }

        val responseFinish = client.drive().file().uploadFinish(
            UploadFinishFileReq.Builder()
                .uploadFinishFileReqBody(
                    UploadFinishFileReqBody.Builder()
                        .uploadId(responsePrepare.data.uploadId)
                        .blockNum(chunkNumber)
                        .build()
                )
                .build()
        )
        if (!responseFinish.success()) {
            logger.error("upload finish error: ${Jsons.DEFAULT.toJson(responseFinish)}")
            return
        }
    }

    private val File.isChunkUploadRequired: Boolean
        get() = length() > MAX_CHUNK_SIZE

    companion object {
        private const val MAX_CHUNK_SIZE = 4 * 1024 * 1024 // 4MB in bytes
    }
}