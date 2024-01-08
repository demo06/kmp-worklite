package funny.buildapp.common.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime


public fun daysBetweenDates(endDate: String, startDate: String): Int {
    return endDate.toLocalDate().toEpochDays()-startDate.toLocalDate().toEpochDays()
}

public fun calculateDaysBetweenTwoLongs(startTime: Long, endTime: Long): Long {
    return (endTime - startTime + 1000) / (24 * 60 * 60 * 1000)
}

public fun Long.timeStampToDate():String{
    val instant= Instant.fromEpochMilliseconds(this)
    val localDate=instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.toString()
}