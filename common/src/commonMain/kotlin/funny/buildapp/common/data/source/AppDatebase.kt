package funny.buildapp.progress.data.source


//@Database(entities = [Plan::class, Todo::class, Daily::class], version = 1) // 定义当前db的版本以及数据库表（数组可定义多张表）
//@TypeConverters(DateConverter::class, ListConverter::class) // 定义类型转换器
public abstract class AppDatabase /* :RoomDatabase()*/ {
//    abstract fun todoDao(): TodoDao
//    abstract fun planDao(): PlanDao
//    abstract fun dailyDao(): DailyDao
}
