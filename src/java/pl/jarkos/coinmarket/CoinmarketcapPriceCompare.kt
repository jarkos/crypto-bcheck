package pl.jarkos.coinmarket

import lombok.Getter
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import pl.jarkos.coinmarket.enums.BlackListCoinmarketCurrencies
import pl.jarkos.coinmarket.enums.BlackListCoinmarketStocks
import pl.jarkos.coinmarket.enums.StockNameEnum
import pl.jarkos.coinmarket.enums.currencies.*
import pl.jarkos.config.AppConfig.*
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.MalformedURLException
import java.util.*

class CoinmarketcapPriceCompare {

    private val btcMarketsData: List<MarketTableRow>
    private val ltcMarketsData: List<MarketTableRow>
    private val ethMarketsData: List<MarketTableRow>
    private val bccMarketsData: List<MarketTableRow>
    private val liskMarketsData: List<MarketTableRow>
    private val dashMarketsData: List<MarketTableRow>
    private val gameMarketsData: List<MarketTableRow>

    companion object {
        private val log = Logger.getLogger(CoinmarketcapPriceCompare::class.java)
    }

    init {
        btcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin/#markets")
        ltcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/litecoin/#markets")
        ethMarketsData = getMarketsData("https://coinmarketcap.com/currencies/ethereum/#markets")
        bccMarketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin-cash/#markets")
        liskMarketsData = getMarketsData("https://coinmarketcap.com/currencies/lisk/#markets")
        dashMarketsData = getMarketsData("https://coinmarketcap.com/currencies/dash/#markets")
        gameMarketsData = getMarketsData("https://coinmarketcap.com/currencies/gamecredits/#markets")
    }

    fun compare() {
        log.error("*** BTC")
        val btcCurrencyPairEnums = BtcCurrencyPairEnum.values().map { it.name }.toSet()
        compareStockPrice(btcMarketsData, StockNameEnum.BitBay, BtcCurrencyPairEnum.BTCPLN.toString(), btcCurrencyPairEnums)
        recognizeMinMaxPrices(btcMarketsData, "BTC MAX/MIN stocks:")

        log.error("*** LTC")
        val ltcCurrencyParisToCompare = LtcCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(ltcMarketsData, StockNameEnum.BitBay, LtcCurrencyPairEnum.LTCPLN.toString(), ltcCurrencyParisToCompare)
        recognizeMinMaxPrices(ltcMarketsData, "LTC MAX/MIN stocks:")

        log.error("*** ETH")
        val ethCurrencyParisToCompare = EthCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(ethMarketsData, StockNameEnum.BitBay, EthCurrencyPairEnum.ETHPLN.toString(), ethCurrencyParisToCompare)
        recognizeMinMaxPrices(ethMarketsData, "ETH MAX/MIN stocks:")

        log.error("*** BCC")
        val bccCurrencyParisToCompare = BccCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(bccMarketsData, StockNameEnum.BitBay, BccCurrencyPairEnum.BCCPLN.toString(), bccCurrencyParisToCompare)
        recognizeMinMaxPrices(bccMarketsData, "BCC MAX/MIN stocks:")

        log.error("*** LISK")
        val liskCurrencyParisToCompare = LiskCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(liskMarketsData, StockNameEnum.BitBay, LiskCurrencyPairEnum.LSKPLN.toString(), liskCurrencyParisToCompare)
        recognizeMinMaxPrices(liskMarketsData, "LISK MAX/MIN stocks:")

        log.error("*** DASH")
        val dashCurrencyParisToCompare = DashCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(dashMarketsData, StockNameEnum.BitBay, DashCurrencyPairEnum.DASHPLN.toString(), dashCurrencyParisToCompare)
        recognizeMinMaxPrices(dashMarketsData, "DASH MAX/MIN stocks:")

        log.error("*** GAME")
        val gameCurrencyParisToCompare = GameCurrencyPairEnum.values().map { it.toString() }.toSet()
        compareStockPrice(gameMarketsData, StockNameEnum.BitBay, GameCurrencyPairEnum.GAMEPLN.toString(), gameCurrencyParisToCompare)
        //        recognizeMinMaxPrices(gameMarketsData, "GAME MAX/MIN stocks:");

    }

    private fun recognizeMinMaxPrices(marketList: List<MarketTableRow>, nameToDisplay: String) {
        println(nameToDisplay)
        val marketsToRecognize: List<MarketTableRow> = marketList.filter { s -> s.volume > MARKET_MIN_VOLUME_TO_CONSIDER_COMPARING }
        val blackListCurrencies = BlackListCoinmarketCurrencies.values().map { it.name }.toList()

        val filteredMarkets = marketsToRecognize.filter { m -> blackListCurrencies.none { c -> m.exchangePair.contains(c) } }
        filteredMarkets.sorted()
        filteredMarkets.subList(0, 4).forEach { m -> print(m.stockName + " " + m.price + " " + m.exchangePair + " ") }
        println()
        filteredMarkets.subList(filteredMarkets.size - 5, filteredMarkets.size - 1) //not safe, possible IOoBE
                .forEach { m -> print(m.stockName + " " + m.price + " " + m.exchangePair + " ") }
        println()
    }

    private fun compareStockPrice(marketsData: List<MarketTableRow>, mainStockName: StockNameEnum, mainCurrencyPair: String, currencyParisToCompare: Set<String>) {
        val mainStockAndCurrencyData = getPriceInUsdByStockAndCurrencyPair(marketsData, mainStockName.name, mainCurrencyPair)
        val stocksToCompare = HashSet(Arrays.asList(*StockNameEnum.values()))

        stocksToCompare.forEach { s ->
            currencyParisToCompare.forEach { c ->
                val priceInUsdByStockAndCurrencyPair = getPriceInUsdByStockAndCurrencyPair(marketsData, s.toString(), c)
                if (priceInUsdByStockAndCurrencyPair != null && mainStockName.name != s.name) {
                    var diff = mainStockAndCurrencyData!!.price.divide(priceInUsdByStockAndCurrencyPair.price, 3, RoundingMode.HALF_DOWN)
                    var reversedRoi = false
                    if (diff.compareTo(BigDecimal.ONE) < 1) {
                        diff = priceInUsdByStockAndCurrencyPair.price.divide(mainStockAndCurrencyData.price, 3, RoundingMode.HALF_DOWN)
                        reversedRoi = true
                    }
                    val resultToDisplay = mainStockAndCurrencyData.stockName + " " + mainStockAndCurrencyData.exchangePair + " diff " + priceInUsdByStockAndCurrencyPair.stockName +
                            " " + priceInUsdByStockAndCurrencyPair.exchangePair + ":" + diff + " " + if (reversedRoi) " %REVERSED" else ""
                    if (diff > maxRoiValueForWarnDisplay || diff < minRoiValueForWarnDisplay) {
                        log.warn(resultToDisplay)
                    } else {
                        log.error(resultToDisplay)
                    }
                }
            }
        }
    }

    private fun getMarketsData(url: String): List<MarketTableRow> {
        val marketTableRows = ArrayList<MarketTableRow>()
        try {
            val doc = Jsoup.connect(url).get()
            val marketsTable = doc.getElementById("markets-table")
            val tableRows = marketsTable.getElementsByTag("tr")
            tableRows.stream().forEach { element ->
                val allTdElements = element.getElementsByTag("td")
                if (!allTdElements.isEmpty() && allTdElements[4].text().indexOf(',') < 0 && !isOnBlackList(allTdElements[1].text())) {
                    //                    System.err.println(allTdElements.get(4).text());
                    marketTableRows.add(MarketTableRow(allTdElements[1].text(), allTdElements[2].text(), BigDecimal(parseValue(allTdElements[4].text())),
                            BigDecimal(parseVolume(allTdElements[3].text()))))
                }
            }
        } catch (mue: MalformedURLException) {
            println("Ouch - stockName MalformedURLException happened.")
            mue.printStackTrace()
        } catch (nmb: java.lang.NumberFormatException) {
            println("Ouch - stockName NumberFormatException happened.")
            nmb.printStackTrace()
        } catch (ioe: IOException) {
            println("Oops- an IOException happened.")
            ioe.printStackTrace()
        }

        return marketTableRows
    }

    private fun isOnBlackList(stockName: String): Boolean {
        return BlackListCoinmarketStocks.values().any { s -> s.name == stockName }
    }

    private fun parseValue(s: String): String {
        return s.replace("* ", "").replace(" *", "").replace("*", "").substring(1)
    }

    private fun parseVolume(s: String): String {
        return s.replace("* ", "").replace(" *", "").replace("*", "").substring(1).replace(",", "")
    }

    private fun getPriceInUsdByStockAndCurrencyPair(marketTableRows: List<MarketTableRow>, stockCode: String, currencyPair: String): MarketTableRow? {
        var marketTableRow: MarketTableRow? = null
        try {
            marketTableRow = marketTableRows.first { m -> stockCode == m.stockName && currencyPair == m.exchangePair }

        } catch (ignore: NoSuchElementException) {
            //Do nothing
        }

        return marketTableRow
    }

    @Getter
    internal inner class MarketTableRow(var stockName: String, var exchangePair: String, var price: BigDecimal, var volume: BigDecimal) : Comparable<MarketTableRow> {

        override fun compareTo(other: MarketTableRow): Int {
            return Math.toIntExact(this.price.toLong() - other.price.toLong())
        }
    }

}
