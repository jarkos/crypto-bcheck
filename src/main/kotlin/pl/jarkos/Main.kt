@file:JvmName("Main")

package pl.jarkos

import org.apache.log4j.LogManager
import org.joda.time.DateTime
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import pl.jarkos.backend.coinmarket.CoinmarketcapPriceCompare
import pl.jarkos.backend.config.AppConfig.HALF_MINUTE_IN_MILLIS
import pl.jarkos.backend.config.AppConfig.ROI_DATA_REPOSITORY_CSV
import pl.jarkos.backend.file.CsvReader
import pl.jarkos.backend.file.FileUpdater
import pl.jarkos.backend.scheduler.Scheduler
import pl.jarkos.backend.stock.StockRoiPreparer
import pl.jarkos.backend.stock.service.RoiIndicatorsService
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.schedule

@SpringBootApplication
open class Main {
    companion object {
        private val logger = LogManager.getLogger("Main")
        private val appTimer = Timer("App timer", false)
        private var indicatorService = RoiIndicatorsService(CsvReader())

        @Throws(InterruptedException::class, IllegalAccessException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Main::class.java, *args)
            appTimer.schedule(0L, 2 * HALF_MINUTE_IN_MILLIS) {
                try {
                    StockRoiPreparer().prepareStocksData()
                    CoinmarketcapPriceCompare().compare()
                    val indicatorsMap = indicatorService.fetchCurrentRoiIndicatorsMap()
                    saveIndicators(indicatorsMap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("PREPARE DATA EXCEPTION! " + e.message)
                }
//            highRoiMailNotify(indicatorsMap)
            }
            Scheduler().scheduleRetention()
        }

        private fun saveIndicators(innitInternalIndicatorsList: Map<String, BigDecimal>) {
            val date = DateTime().millis
            innitInternalIndicatorsList.forEach { k, v ->
                FileUpdater.addResultData(getIndicatorNewRow(k, v, date), ROI_DATA_REPOSITORY_CSV)
            }
        }

        private fun getIndicatorNewRow(key: String, value: BigDecimal, date: Long): String {
            return date.toString() + "," + key + "," + value.toDouble()
        }

//        private fun highRoiMailNotify(internalIndicators: Map<String, BigDecimal>) {
//            if (lastMACD < -180.0 || internalIndicators.values.stream().anyMatch { i -> i > marginTransferRoiValueForMailNotification }) {
//                val sb = StringBuilder()
//                internalIndicators.forEach { k, v -> sb.append(k).append(" ").append(v).append(System.getProperty("line.separator")) }
//                JavaMailSender.sendMail("ROI BB: " + lastMACD.toString() + " " + sb.toString())
//            }
//        }

    }

}
