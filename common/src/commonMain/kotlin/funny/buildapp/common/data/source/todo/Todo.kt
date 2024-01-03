package funny.buildapp.common.data.source.todo

//@Entity(tableName = "todos")
public data class Todo(
//    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
//    val startDate: Long = getCurrentDate(),
//    val endDate: Long = getCurrentDate(false),
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = true,
    val associateId: Long = 0,
    val status: Int = 0,
)
