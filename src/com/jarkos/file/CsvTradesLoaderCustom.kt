package com.jarkos.file

import au.com.bytecode.opencsv.CSVReader
import eu.verdelhan.ta4j.Tick
import eu.verdelhan.ta4j.TimeSeries
import org.joda.time.DateTime
import org.joda.time.Period
import ta4jexamples.loaders.CsvTradesLoader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class CsvTradesLoaderCustom : CsvTradesLoader() {
    companion object {

        val UTF_8 = "UTF-8"

        fun loadStockSeries(filePath: String): TimeSeries {
            println("Loading csv file...")
            val startTime = System.nanoTime()
            var csvReader: CSVReader? = null
            var lines: MutableList<*>? = null
            try {
                val stream = FileInputStream(filePath)

                csvReader = CSVReader(InputStreamReader(stream, Charset.forName(UTF_8)), ',')
                lines = csvReader.readAll()
                lines!!.removeAt(0)
            } catch (var21: IOException) {
                Logger.getLogger(CsvTradesLoader::class.java.name).log(Level.SEVERE, "Unable to load trades from CSV", var21)
            } finally {
                if (csvReader != null) {
                    try {
                        csvReader.close()
                    } catch (ignored: IOException) {
                    }

                }

            }

            var ticks: MutableList<Tick>? = null
            if (lines != null && !lines.isEmpty()) {
                var beginTime = DateTime(java.lang.Long.parseLong((lines[0] as Array<String>)[0]) * 1000L)
                var endTime = DateTime(java.lang.Long.parseLong((lines[lines.size - 1] as Array<String>)[0]) * 1000L)
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
                        val tradeTimestamp = DateTime(java.lang.Long.parseLong(tradeLine[0]) * 1000L)
                        val var9 = ticks.iterator()

                        while (var9.hasNext()) {
                            val tick = var9.next()
                            if (tick.inPeriod(tradeTimestamp)) {
                                val tradePrice = java.lang.Double.parseDouble(tradeLine[1])
                                val tradeAmount = java.lang.Double.parseDouble(tradeLine[2])
                                tick.addTrade(tradeAmount, tradePrice)
                            }
                        }
                    }
                }

                removeEmptyTicks(ticks)
            }
            val endTime = System.nanoTime()
            println("Ended loading of csv file: " + (endTime - startTime) / 1000000000L + " sek")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val now = LocalDateTime.now()
            val formatDateTime = now.format(formatter)
            println(formatDateTime)
            return TimeSeries("bitstamp_trades", ticks!!)
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
            for (i in ticks.indices.reversed()) {
                if (ticks[i].trades == 0) {
                    ticks.removeAt(i)
                }
            }

        }

    }

}
