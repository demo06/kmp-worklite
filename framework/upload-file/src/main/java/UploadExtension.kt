import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

abstract class UploadExtension(objects: ObjectFactory) {
    val appId: Property<String> = objects.property<String>()
    val appSecret: Property<String> = objects.property<String>()
    val remoteFolderNode: Property<String> = objects.property<String>()
}

enum class UploadType {
    ALL,
    AWS_S3_BY_LAMBDA,
    LARK
}