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

class UploadFilePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        if (pluginManager.hasPlugin("com.android.application")) {
            val uploadToLarkTask = tasks.register("uploadToS3") {
                it.group = "upload"
            }

            // generate git info
            val generateGitInfo = tasks.register<Exec>("generateReleaseInfoByGit") {
                group = "upload"

                val infoFile = layout.buildDirectory.file("outputs/git_info.txt").get().asFile
                commandLine = listOfNotNull(
                    "git",
                    "log",
                    "--pretty=oneline",
                    "--abbrev-commit",
                    gitPrevTag()?.let { "${it}..HEAD" },
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

                        var flavorNameWithName = variant.flavorName
                        if (flavorNameWithName.equals("staging", true)) {
                            flavorNameWithName = "test"
                        }

                        val appName = listOfNotNull(
                            "imtt",//FIXME to project name  -- like : mydroid/ worklite /todo_list/....
                            flavorNameWithName,
                            variant.buildType.name,
                            defaultConfig.versionName,
                            gitCommit()
                        ).joinToString(separator = "_") { it }

                        output.outputFileName = "${appName}.apk"

                        val typeName = variant.flavorName.capitalized() +
                                variant.buildType.name.capitalized()

                        // copy git info to apk/debug dir
                        val copyGitInfoTask = tasks.register<Copy>("copyGitInfoTo${typeName}") {
                            from(layout.buildDirectory.file("outputs"))
                            into(output.outputFile.parentFile)
                            include("git_info.txt")
                            rename { "${appName}_note.txt" }
                            dependsOn(tasks.getByName("create${typeName}ApkListingFileRedirect"))
                            dependsOn(generateGitInfo)
                        }
                        variant.assembleProvider.dependsOn(copyGitInfoTask)

                        val uploadToLarkTaskOne = tasks.register("upload${typeName}ToS3") {
                            it.group = "upload"
                        }
                        uploadToLarkTask.dependsOn(uploadToLarkTaskOne)
                        val uploadApkToLarkTask =
                            tasks.register<UploadFileTask>("upload${typeName}ApkToS3") {
                                group = "upload"
                                flavor = provider { flavorNameWithName }
                                file = provider { output.outputFile }
                                dependsOn(variant.assembleProvider)
                            }
                        uploadToLarkTaskOne.dependsOn(uploadApkToLarkTask)
                        val uploadNoteToLarkTask =
                            tasks.register<UploadFileTask>("upload${typeName}NoteToS3") {
                                group = "upload"
                                flavor = provider { flavorNameWithName }
                                file = provider {
                                    File(
                                        output.outputFile.parentFile,
                                        "${appName}_note.txt"
                                    )
                                }
                                dependsOn(copyGitInfoTask)
                                dependsOn(variant.assembleProvider)
                            }
                        uploadToLarkTaskOne.dependsOn(uploadNoteToLarkTask)

                        if (variant.buildType.name.equals("release", true)) {
                            val uploadMappingToLarkTask =
                                tasks.register<UploadFileTask>("upload${typeName}MappingToS3") {
                                    group = "upload"
                                    flavor = provider { variant.flavorName }
                                    file = layout.buildDirectory
                                        .file("outputs/mapping/${variant.flavorName}${variant.buildType.name.capitalized()}/mapping.txt")
                                        .map { it.asFile }
                                    dependsOn(variant.assembleProvider)
                                }
                            uploadToLarkTaskOne.dependsOn(uploadMappingToLarkTask)
                        }
                    }
                }
            }
        }
    }
}

private fun Project.gitCommit(): String? {
    val byteOut = ByteArrayOutputStream()
    val errorOut = ByteArrayOutputStream()
    val result = exec {
        it.commandLine = listOf("git", "rev-parse", "--short", "HEAD")
        it.standardOutput = byteOut
        it.isIgnoreExitValue = true
        it.errorOutput = errorOut
    }
    if (result.exitValue == 0) {
        return String(byteOut.toByteArray()).trim()
    }
    println("gitPrevTag error: ${String(errorOut.toByteArray()).trim()}")
    return null
}

private fun Project.gitPrevTag(): String? {
    val byteOut = ByteArrayOutputStream()
    val errorOut = ByteArrayOutputStream()
    val result = exec {
        it.commandLine = listOf("git", "describe", "--tags", "--abbrev=0")
        it.standardOutput = byteOut
        it.isIgnoreExitValue = true
        it.errorOutput = errorOut
    }
    if (result.exitValue == 0) {
        return String(byteOut.toByteArray()).trim()
    }
    println("gitPrevTag error: ${String(errorOut.toByteArray()).trim()}")
    return null
}

