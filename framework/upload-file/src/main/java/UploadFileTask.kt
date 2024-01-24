import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.buffer
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import kotlin.math.ceil


abstract class UploadFileTask : DefaultTask() {

    @get:Input
    lateinit var file: Provider<File>

    @get:Input
    lateinit var flavor: Provider<String>

    @TaskAction
    fun execute() {
        println("flavor: ${flavor.get()} fileInfo: ${file.get().name.substringAfterLast('/')} size:${file.get().length()}")

        if (!file.get().exists()) {
            println("file not exit: ${file.get()}")
            return
        }

        if (file.get().isChunkUploadRequired) {
            uploadFileWithChunk(file.get())
        } else {
            uploadFile(file.get())
        }
    }

    private fun uploadFile(file: File) {
        val client = OkHttpClient()

        val fileName = file.name.substringAfterLast('/')

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName, file.asRequestBody(mediaType))
            .addFormDataPart("env", "imtt/client/${flavor}")
            .addFormDataPart("type", "file")
            .addFormDataPart("appType", "Android")
            .addFormDataPart("fullName", fileName)
            .build()

        val request = Request.Builder()
            .url(AWO_UPLOAD_URL)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
        }
    }

    private fun uploadFileWithChunk(file: File) {
        val client = OkHttpClient()

        val fileName = file.name.substringAfterLast('/')
        val fileMd5 = file.md5()

        val chunkSize = MAX_CHUNK_SIZE
        val buffer = ByteArray(chunkSize)
        val totalChunks = ceil(file.length().toDouble() / chunkSize).toInt()

        RandomAccessFile(file, "r").use { raf ->
            var chunkNumber = 1
            val totalLength = raf.length()
            while (raf.filePointer < totalLength) {
                val bytesRead = raf.read(buffer, 0, chunkSize)

                println("upload $chunkNumber/${totalChunks} size=${bytesRead}")

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        fileName,
                        buffer.toRequestBody(byteCount = bytesRead, contentType = mediaType)
                    )
                    .addFormDataPart("env", "imtt/client/prod")
                    .addFormDataPart("type", "file")
                    .addFormDataPart("appType", "Android")
                    .addFormDataPart("chunkNumber", chunkNumber++.toString())
                    .addFormDataPart("totalChunks", totalChunks.toString())
                    .addFormDataPart("md5", fileMd5)
                    .addFormDataPart("fullName", fileName)
                    .build()

                val request = Request.Builder()
                    .url(AWO_UPLOAD_URL)
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
                }
            }
        }
    }

    private fun File.md5(): String {
        try {
            val byteString = source().buffer().use { it.readByteString() }
            return byteString.md5().hex()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    private val File.isChunkUploadRequired: Boolean
        get() = length() > MAX_CHUNK_SIZE

    companion object {
        private const val MAX_CHUNK_SIZE = 4 * 1024 * 1024 // 4MB in bytes
         //FIXME to Extension set upload url from input (build.gradle)
        private const val AWO_UPLOAD_URL =
            "https://iecuvvnpawpvwrz2ky7mq3qtne0dmdmf.lambda-url.ap-south-1.on.aws/"

        private val mediaType = "application/octet-stream".toMediaTypeOrNull()
    }
}