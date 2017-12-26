@file:JvmName("Main")

package pl.jarkos

import org.apache.log4j.LogManager
import org.joda.time.DateTime
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.view.InternalResourceViewResolver
import pl.jarkos.backend.coinmarket.CoinmarketcapPriceCompare
import pl.jarkos.backend.config.AppConfig.*
import pl.jarkos.backend.file.FileRetention
import pl.jarkos.backend.file.FileUpdater
import pl.jarkos.backend.mail.JavaMailSender
import pl.jarkos.backend.stock.Indicators
import pl.jarkos.backend.stock.StockRoiPreparer
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.concurrent.schedule


@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
open class Main {
    companion object {
        private val logger = LogManager.getLogger("Main")
        private val LAST_BB_BTC_MACD_INDICATOR = "Last BB BTC MACD indicator: "
        var lastMACD: Double = 0.0
        private val appTimer = Timer("App timer", false)
        private val retentionScheduler = Timer("Retention scheduler", true)

        @Throws(InterruptedException::class, IllegalAccessException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Main::class.java, *args)
//        CandlestickChart.start()
            val internalIndicators = innitCurrentRoiIndicatorsMap()
//        XyRoiChart.start(internalIndicators)

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
//            CandlestickChart.refresh()
//            XyRoiChart.refresh(indicatorsMap)
//            highRoiMailNotify(indicatorsMap)

//                logger.info(LAST_BB_BTC_MACD_INDICATOR + lastMACD)
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

    @Bean
    open fun resolver(): InternalResourceViewResolver {
        val resolver = InternalResourceViewResolver()
        resolver.setPrefix("/webapp/")
        resolver.setSuffix(".jsp")
        return resolver
    }

}
