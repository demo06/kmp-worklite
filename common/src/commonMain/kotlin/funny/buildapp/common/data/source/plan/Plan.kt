package funny.buildapp.common.data.source.plan

// TODO: change here
//@Entity(tableName = "plans")
public data class Plan(
//    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
//    val startDate: Long = getCurrentDate(),
//    val endDate: Long = getCurrentDate(false),
    val initialValue: Int = 0,
    val targetValue: Int = 0,
    val autoAdjust: Boolean = false,
    val status: Int = 0,
)
