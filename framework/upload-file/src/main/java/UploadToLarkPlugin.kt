import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream
import java.io.File

class UploadToLarkPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create("uploadToLark", UploadExtension::class.java)

        if (pluginManager.hasPlugin("com.android.application")) {
            val uploadToLarkTask = tasks.register("uploadToLark") {
                it.group = "upload"
            }

            // generate git info
            val generateGitInfo = tasks.register<Exec>("generateReleaseInfoByGit") {
                group = "upload"

                val infoFile = layout.buildDirectory.file("outputs/git_info.text").get().asFile
                commandLine = listOf(
                    "git",
                    "log",
                    "--pretty=oneline",
                    "--abbrev-commit",
                    "${gitPrevTag()}..HEAD",
                )
                standardOutput = infoFile.also {
                    it.parentFile.let { file ->
                        if (!file.exists()) file.mkdirs()
                    }
                }.outputStream()
            }
            extensions.configure<AppExtension> {
                applicationVariants.configureEach { variant ->
                    variant.outputs.configureEach { output ->
                        output as ApkVariantOutputImpl

                        val appName = "tg_" +
                                "${variant.flavorName}_" +
                                "${variant.buildType.name}_" +
                                "${defaultConfig.versionName}_" +
                                gitCommit()

                        output.outputFileName = "${appName}.apk"

                        val typeName = variant.flavorName?.capitalized() +
                                variant.buildType.name.capitalized()

                        // copy git info to apk/debug dir
                        val copyGitInfoTask = tasks.register<Copy>("copyGitInfoTo${typeName}") {
                            from(layout.buildDirectory.file("outputs"))
                            into(output.outputFile.parentFile)
                            include("git_info.text")
                            rename { "${appName}_note.text" }
                            dependsOn(tasks.getByName("create${typeName}ApkListingFileRedirect"))
                            dependsOn(generateGitInfo)
                        }
                        variant.assembleProvider.dependsOn(copyGitInfoTask)

                        // if have key, upload apk&note to lark
                        if (extension.appId.get().isNotEmpty()
                            && extension.appSecret.get().isNotEmpty()
                        ) {
                            val uploadToLarkTaskOne = tasks.register("upload${typeName}ToLark") {
                                it.group = "upload"
                            }
                            uploadToLarkTask.dependsOn(uploadToLarkTask)
                            val uploadApkToLarkTask =
                                tasks.register<UploadToLarkTask>("upload${typeName}ApkToLark") {
                                    group = "upload"
                                    appId.set(extension.appId)
                                    appSecret.set(extension.appSecret)
                                    remoteFolderNode.set(extension.remoteFolderNode)
                                    file = provider { output.outputFile }
                                    dependsOn(variant.assembleProvider)
                                }
                            uploadToLarkTaskOne.dependsOn(uploadApkToLarkTask)
                            val uploadNoteToLarkTask =
                                tasks.register<UploadToLarkTask>("upload${typeName}NoteToLark") {
                                    group = "upload"
                                    appId.set(extension.appId)
                                    appSecret.set(extension.appSecret)
                                    remoteFolderNode.set(extension.remoteFolderNode)
                                    file = provider {
                                        File(
                                            output.outputFile.parentFile,
                                            "${appName}_note.text"
                                        )
                                    }
                                    dependsOn(copyGitInfoTask)
                                    dependsOn(variant.assembleProvider)
                                }
                            uploadToLarkTaskOne.dependsOn(uploadNoteToLarkTask)
                        }
                    }
                }
            }
        }
    }
}

private fun Project.gitCommit(): String {
    val byteOut = ByteArrayOutputStream();
    exec {
        it.commandLine = "git rev-parse --short HEAD".split(" ")
        it.standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

private fun Project.gitPrevTag(): String {
    val byteOut = ByteArrayOutputStream();
    exec {
        it.commandLine = "git describe --tags --abbrev=0".split(" ")
        it.standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

