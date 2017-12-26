package pl.jarkos.backend.file

import org.joda.time.DateTime
import pl.jarkos.backend.config.AppConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV
import pl.jarkos.backend.config.AppConfig.DAYS_BACK_CSV_FILE_RETENTION
import java.io.File
import java.util.*

class FileRetention : TimerTask() {

    override fun run() {
        removeOldRows()
    }

    private fun removeOldRows() {
        println("Removing old lines")
        val limitDateForClean = DateTime.now().minusDays(DAYS_BACK_CSV_FILE_RETENTION).millis / 1000
        //not efficient, but OK
        val filteredList = File(BIT_BAY_BTC_DATA_REPOSITORY_CSV).readLines().filter { line ->
            isNotOlderThanDate(line, limitDateForClean)
        }.toList()
        val text = filteredList.joinToString(System.lineSeparator())
        File(BIT_BAY_BTC_DATA_REPOSITORY_CSV).writeText(text)
    }

    private fun isNotOlderThanDate(line: String, currentUnixTimestampDay: Long): Boolean {
        try {
            return line.split(",")[0].toLong() > currentUnixTimestampDay
        } catch (ignore: NumberFormatException) {
            // DO NOTHING
        }
        return false
    }

}