package funny.buildapp.common.data

import funny.buildapp.Plans
import funny.buildapp.common.database.database

public class PlanRepository {

    public fun insert(plan: Plans): Unit = database.planQueries.insert(plan)

    public fun delete(id: Long): Unit = database.planQueries.delete(id)

    public fun update(plan: Plans): Unit = database.planQueries.update(
        title = plan.title,
        startDate = plan.startDate,
        endDate = plan.endDate,
        initialValue = plan.initialValue,
        targetValue = plan.targetValue,
        autoAdjust = plan.autoAdjust,
        state = plan.state,
        id = plan.id
    )

    public fun selectAll(): List<Plans> = database.planQueries.selectALL().executeAsList()
    public fun selectById(id: Long): Plans =
        database.planQueries.selectById(id).executeAsOne()


}