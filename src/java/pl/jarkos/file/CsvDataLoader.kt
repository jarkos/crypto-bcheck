package pl.jarkos.file

import eu.verdelhan.ta4j.Tick
import eu.verdelhan.ta4j.TimeSeries
import org.joda.time.DateTime
import org.joda.time.Period
import java.lang.Double
import java.lang.Long
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CsvDataLoader {

    fun loadStockSeries(filePath: String): TimeSeries {
        println("Loading csv file...")
        val startTime = System.nanoTime()
        var lines: List<*>? = CsvReader().getLines(filePath)

        var ticks: List<Tick>? = null
        if (lines != null && !lines.isEmpty()) {
            ticks = buildListOfTicks(lines)
        }
        val endTime = System.nanoTime()
        println("Ended loading of csv file: " + (endTime - startTime) / 1000000000L + " sek")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val now = LocalDateTime.now()
        val formatDateTime = now.format(formatter)
        println(formatDateTime)
        return TimeSeries("TS", ticks!!)
    }

    private fun buildListOfTicks(lines: List<*>): List<Tick>? {
        var ticks: MutableList<Tick>?
        var beginTime = DateTime(Long.parseLong((lines[0] as Array<String>)[0]) * 1000L)
        var endTime = DateTime(Long.parseLong((lines[lines.size - 1] as Array<String>)[0]) * 1000L)
        if (beginTime.isAfter(endTime)) {
            val beginInstant = beginTime.toInstant()
            val endInstant = endTime.toInstant()
            beginTime = DateTime(endInstant)
            endTime = DateTime(beginInstant)
        }

        ticks = buildEmptyTicks(beginTime, endTime, 300)
        val var23 = lines.iterator()

        while (var23.hasNext()) {
            val tradeLine = var23.next() as Array<String>
            if (!tradeLine[0].isEmpty()) {
                val tradeTimestamp = DateTime(Long.parseLong(tradeLine[0]) * 1000L)
                val var9 = ticks.iterator()

                while (var9.hasNext()) {
                    val tick = var9.next()
                    if (tick.inPeriod(tradeTimestamp)) {
                        val tradePrice = Double.parseDouble(tradeLine[1])
                        val tradeAmount = Double.parseDouble(tradeLine[2])
                        tick.addTrade(tradeAmount, tradePrice)
                    }
                }
            }
        }

        removeEmptyTicks(ticks)
        return ticks
    }

    private fun buildEmptyTicks(beginTime: DateTime, endTime: DateTime, duration: Int): MutableList<Tick> {
        val emptyTicks = ArrayList<Tick>()
        val tickTimePeriod = Period.seconds(duration)
        var tickEndTime = beginTime

        do {
            tickEndTime = tickEndTime.plus(tickTimePeriod)
            emptyTicks.add(Tick(tickTimePeriod, tickEndTime))
        } while (tickEndTime.isBefore(endTime))

        return emptyTicks
    }

    private fun removeEmptyTicks(ticks: MutableList<Tick>) {
        ticks.indices.reversed()
                .filter { ticks[it].trades == 0 }
                .forEach { ticks.removeAt(it) }

    }

}
