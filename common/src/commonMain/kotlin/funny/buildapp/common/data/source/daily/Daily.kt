package funny.buildapp.common.data.source.daily

// TODO: change here
//@Entity(tableName = "daily")
public data class Daily(
//    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val todoId: Long = 0,
    val planId: Long = 0,
//    val changeDate: Long = System.currentTimeMillis(),
    val state: Boolean = false
)
