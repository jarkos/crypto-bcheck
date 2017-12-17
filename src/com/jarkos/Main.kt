package com.jarkos

import com.jarkos.chart.CandlestickChart
import com.jarkos.chart.XyRoiChart
import com.jarkos.coinmarket.CoinmarketcapPriceCompare
import com.jarkos.config.AppConfig.*
import com.jarkos.file.FileRetention
import com.jarkos.file.FileUpdater
import com.jarkos.mail.JavaMailSender
import com.jarkos.stock.Indicators
import com.jarkos.stock.StockRoiPreparer
import org.apache.log4j.Logger
import org.joda.time.DateTime
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.schedule


object Main {

    private val logger = Logger.getLogger(Main::class.java)
    private val LAST_BB_BTC_MACD_INDICATOR = "Last BB BTC MACD indicator: "
    var lastMACD: Double = 0.0
    private val appTimer = Timer("App timer", false)
    private val retentionScheduler = Timer("Retention scheduler", true)

    @Throws(InterruptedException::class, IllegalAccessException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        CandlestickChart.start()
        val internalIndicators = innitCurrentRoiIndicatorsMap()
        XyRoiChart.start(internalIndicators)

        appTimer.schedule(0L, 2 * HALF_MINUTE_IN_MILLIS) {
            try {
                StockRoiPreparer().prepareStocksData()
                CoinmarketcapPriceCompare().compare()

            } catch (e: Exception) {
                e.printStackTrace()
                println("PREPARE DATA EXCEPTION! " + e.message)
            }
            val indicatorsMap = innitCurrentRoiIndicatorsMap()
            saveIndicators(indicatorsMap)
            CandlestickChart.refresh()
            XyRoiChart.refresh(indicatorsMap)
//            highRoiMailNotify(indicatorsMap)

            logger.info(LAST_BB_BTC_MACD_INDICATOR + lastMACD)
        }
        retentionScheduler.schedule(FileRetention(), getNextMidnight(), TWENTY_FOUR_HOURS_IN_MILLIS)
    }

    private fun saveIndicators(innitInternalIndicatorsList: Map<String, BigDecimal>) {
        val date = DateTime().millis;
        innitInternalIndicatorsList.forEach { k, v ->
            FileUpdater.addResultData(getIndicatorNewRow(k, v, date), ROI_DATA_REPOSITORY_CSV)
        }
    }

    private fun getIndicatorNewRow(key: String, value: BigDecimal, date: Long): String {
        return date.toString() + "," + key + "," + value.toDouble()
    }

    private fun highRoiMailNotify(internalIndicators: Map<String, BigDecimal>) {
        if (lastMACD < -180.0 || internalIndicators.values.stream().anyMatch { i -> i > marginTransferRoiValueForMailNotification }) {
            val sb = StringBuilder()
            internalIndicators.forEach { k, v -> sb.append(k).append(" ").append(v).append(System.getProperty("line.separator")) }
            JavaMailSender.sendMail("ROI BB: " + lastMACD.toString() + " " + sb.toString())
        }
    }

    private fun innitCurrentRoiIndicatorsMap(): Map<String, BigDecimal> {
        val indicatorsMap = HashMap<String, BigDecimal>()
        val allFields = Indicators::class.java.declaredFields
        for (field in allFields) {
            if (Modifier.isPublic(field.modifiers) && field.type == BigDecimal::class.java) {
                val indicator: BigDecimal
                try {
                    indicator = field.get(BigDecimal::class.java) as BigDecimal
                    indicatorsMap.put(field.name, indicator)
                } catch (ignored: IllegalAccessException) {
                    // DO NOTHING, GO AHEAD
                }
            }
        }
        return indicatorsMap
    }

    private fun getNextMidnight(): Date {
        val now = LocalDateTime.now() // current date and time
        val midnight = now.toLocalDate().atStartOfDay().plusDays(1L)
        return Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant())
    }

}
