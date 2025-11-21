package com.example.finance.util

import java.util.Calendar
import java.util.Date

object DateUtils {
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameMonth(date: Date, month: Int, year: Int): Boolean {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
    }

    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH)
    
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
    
    fun getStartOfDay(date: Date): Date {
        val cal = Calendar.getInstance().apply { time = date }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
}
