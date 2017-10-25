package com.jarkos.file

import com.jarkos.config.AppConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV
import com.jarkos.config.AppConfig.DAYS_BACK_CSV_FILE_RETENTION
import org.joda.time.DateTime
import java.io.File
import java.util.*

class FileRetention : TimerTask() {

    override fun run() {
        removeOldRows()
    }

    private fun removeOldRows() {
        println("Removing old lines")
        val entriesDateToRemove = DateTime.now().minusDays(DAYS_BACK_CSV_FILE_RETENTION).millis / 1000
        //not efficient, but OK
        val filteredList = File(BIT_BAY_BTC_DATA_REPOSITORY_CSV).readLines().filter {
            isNotOlderThanDate(it, entriesDateToRemove)
        }.toList()
        val text = filteredList.joinToString(System.lineSeparator())
        File(BIT_BAY_BTC_DATA_REPOSITORY_CSV).writeText(text)
    }

    private fun isNotOlderThanDate(it: String, currentUnixTimestampDay: Long): Boolean {
        try {
            return it.split(",")[0].toLong() > currentUnixTimestampDay
        } catch (ignore: NumberFormatException) {
            // DO NOTHING
        }
        return false
    }

}