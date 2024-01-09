package funny.buildapp.common.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime


public fun daysBetweenDates(endDate: String, startDate: String): Int {
    return endDate.toLocalDate().toEpochDays() - startDate.toLocalDate().toEpochDays()
}

public fun Long.timeStampToDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.toString()
}

public fun String.dateToTimeStamp(): Long {
    return this.toLocalDate().atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

public fun currentDate(): String {
    val now = Clock.System.now()
    val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.toString()
}