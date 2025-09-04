package com.example.famchat.extensions

import java.util.*

fun Calendar.numDaysBetween(fromTime: Long, toTime: Long): Int {
    var result = 0
    if (toTime <= fromTime) return result
    this.timeInMillis = toTime
    val toYear = this[Calendar.YEAR]
    result += this[Calendar.DAY_OF_YEAR]
    this.timeInMillis = fromTime
    result -= this[Calendar.DAY_OF_YEAR]
    while (this[Calendar.YEAR] < toYear) {
        result += this.getActualMaximum(Calendar.DAY_OF_YEAR)
        this.add(Calendar.YEAR, 1)
    }
    return result
}

fun Calendar.getMonthsBetween(startDate: Calendar, endDate: Calendar): List<Calendar> {
    val monthsBetween = mutableListOf<Calendar>()
    val currentMonth = startDate.clone() as Calendar
    currentMonth.set(Calendar.DAY_OF_MONTH, 1)
    while (!currentMonth.after(endDate)) {
        monthsBetween.add(currentMonth.clone() as Calendar)
        currentMonth.add(Calendar.MONTH, 1)
    }
    return monthsBetween
}