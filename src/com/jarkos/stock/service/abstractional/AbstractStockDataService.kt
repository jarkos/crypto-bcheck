package com.jarkos.stock.service.abstractional

import com.jarkos.config.AppConfig.*
import com.jarkos.config.StockConfig.ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT
import com.jarkos.config.StockConfig.WALUTOMAT_WITHDRAW_RATIO
import com.jarkos.stock.StockRoiPreparer
import com.jarkos.stock.abstractional.api.*
import org.apache.log4j.Logger
import java.math.BigDecimal
import java.math.RoundingMode

abstract class AbstractStockDataService {

    abstract val stockCodeName: String

    abstract val ltcEurStockData: LtcStockDataInterface?

    abstract val ethEurStockData: EthStockDataInterface?

    protected abstract val btcEurStockData: BtcStockDataInterface?

    protected abstract fun getEuroAfterMoneyWithdrawalProv(numberOfEuroToWithdraw: BigDecimal): BigDecimal

    companion object {

        private val logger = Logger.getLogger(AbstractStockDataService::class.java)
    }

    fun prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData: LtcStockDataInterface, btcEurAbstractStockData: BtcStockDataInterface,
                                                                 bitBayBtcPlnStockData: BtcStockDataInterface): BigDecimal {
        val ltcEurStockData = ltcEurStockData
        if (ltcEurStockData != null && ltcEurStockData.ltcEurStockData != null) {
            //BITBAY LTC -> EURO EXTERNAL STOCK
            val eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, ltcEurStockData)
            val eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, ltcEurStockData)
            // EURO EXTERNAL STOCK -> BTC
            val numberOfBtcBoughtWithdrawToBitBayAfterProv = amountOfBtcBoughtFromEuroOnExternalStock(btcEurAbstractStockData, eurNumberAfterLtcSellAfterTradeProv)
            val numberOfBtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBtcBoughtFromEuroOnExternalStockPessimistic(btcEurAbstractStockData,
                    eurNumberAfterLtcSellAfterTradeProvPessimistic)
            // BITBAY BTC -> PLN
            val numberOfMoneyFromBtcSellAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromBtcSellAfterProvPessimistic = getPlnFromBitBayBtcSellPessimistic(bitBayBtcPlnStockData,
                    numberOfBtcBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBtcSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI LTC " + bitBayLtcPlnStockData.stockName + "  -> $stockCodeName BTC : $bitBayLtcBuyAndBtcSellRoi {$bitBayLtcBuyAndBtcSellRoiPessimistic} -> BitBay PLN"
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay)
            return bitBayLtcBuyAndBtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayLtcPlnStockData: LtcStockDataInterface, bccEurAbstractStockData: BccStockDataInterface,
                                                                      bitBayBccPlnStockData: BccStockDataInterface): BigDecimal {
        val ltcEurStockData = ltcEurStockData
        if (ltcEurStockData != null && ltcEurStockData.ltcEurStockData != null) {
            //BITBAY LTC
            val eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, ltcEurStockData)
            val eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, ltcEurStockData)
            // EURO - > BCC EXTERNAL STOCK
            val numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, eurNumberAfterLtcSellAfterTradeProv)
            val numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData,
                    eurNumberAfterLtcSellAfterTradeProvPessimistic)
            //BITBAY BCC -> PLN
            val numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                    numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBccSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI LTC " + bitBayLtcPlnStockData.stockName + " -> $stockCodeName BCC -> Bitbay PLN : $bitBayLtcBuyAndBtcSellRoi {$bitBayLtcBuyAndBtcSellRoiPessimistic}"
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay)
            println("LTC " + bitBayLtcPlnStockData.stockName + " -> " + bitBayLtcPlnStockData.lastPrice + " {" + bitBayLtcPlnStockData.askPrice.setScale(2, RoundingMode.HALF_DOWN) + "} LTC " + stockCodeName + " -> " +
                    ltcEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                    stockCodeName + " -> " +
                    bccEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " BCC BitBay -> " + bitBayBccPlnStockData.lastPrice + " {" + bitBayBccPlnStockData.bidPrice + "} ")
            return bitBayLtcBuyAndBtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(bitBayEthPlnStockData: EthStockDataInterface, bccEurAbstractStockData: BccStockDataInterface,
                                                                    bitBayBccPlnStockData: BccStockDataInterface): BigDecimal {
        val ethEurStockData = ethEurStockData
        if (ethEurStockData != null && ethEurStockData.ethEurStockData != null) {
            //BITBAY LTC
            val eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, ethEurStockData)
            val eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, ethEurStockData)
            // EURO - > BCC EXTERNAL STOCK
            val numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, eurNumberAfterEthSellAfterTradeProv)
            val numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData,
                    eurNumberAfterEthSellAfterTradeProvPessimistic)
            //BITBAY BCC
            val numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                    numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayEthBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayEthBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI ETH " + bitBayEthPlnStockData.stockName + "  -> $stockCodeName BCC -> Bitbay PLN : $bitBayEthBuyAndBccSellRoi {$bitBayEthBuyAndBccSellRoiPessimistic}"
            displayDependOnRoi(bitBayEthBuyAndBccSellRoi, resultToDisplay)
            println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.lastPrice + " {" + bitBayEthPlnStockData.askPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "} ETH  " +
                            stockCodeName + " -> " +
                            ethEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                            stockCodeName + " -> " +
                            bccEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                            " BCC BitBay -> " + bitBayBccPlnStockData.lastPrice + " {" + bitBayBccPlnStockData.bidPrice + ")")
            return bitBayEthBuyAndBccSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayBtcPlnStockData: BtcStockDataInterface, bccEurAbstractStockData: BccStockDataInterface,
                                                                      bitBayBccPlnStockData: BccStockDataInterface): BigDecimal {
        val btcEurStockData = btcEurStockData
        if (btcEurStockData!!.btcEurStockData != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            val eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, btcEurStockData)
            val eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, btcEurStockData)
            // EURO - > BCC EXTERNAL STOCK
            val numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, eurNumberAfterBtcSellAfterTradeProv)
            val numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData,
                    eurNumberAfterBtcSellAfterTradeProvPessimistic)
            //BITBAY BCC -> PLN
            val numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                    numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayBtcBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI BTC BitBay -> " + stockCodeName + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi + " {" +
                    bitBayBtcBuyAndBccSellRoiPessimistic.setScale(3, BigDecimal.ROUND_HALF_DOWN) + "}"
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay)
            println(
                    "BTC BitBay -> " + bitBayBtcPlnStockData.lastPrice + " {" + bitBayBtcPlnStockData.askPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "} BTC " +
                            stockCodeName + " -> " +
                            btcEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                            stockCodeName + " -> " +
                            bccEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                            " BCC BitBay -> " + bitBayBccPlnStockData.lastPrice + " {" + bitBayBtcPlnStockData.bidPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}")
            return bitBayBtcBuyAndBccSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData: BtcStockDataInterface, abstractBtcEurStockData: BtcStockDataInterface): BigDecimal {
        // WALUTOMAT & BANK
        val numberOfEurExchangedOnWalutomatAfterProv = amountOfEuroAfterExchangeAndSepaTransfer
        //EUR EXTERNAL STOCK -> BTC
        val numberOfBtcWithdrawAfterProv = amountOfBtcBoughtFromEuroOnExternalStock(abstractBtcEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        val numberOfBtcWithdrawAfterProvPessimistic = amountOfBtcBoughtFromEuroOnExternalStockPessimistic(abstractBtcEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        // BITBAY BTC -> PLN
        val plnMoneyBtcExchangedAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcWithdrawAfterProv)
        val plnMoneyBtcExchangedAfterProvPessimistic = getPlnFromBitBayBtcSellPessimistic(bitBayBtcPlnStockData, numberOfBtcWithdrawAfterProvPessimistic)

        val eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val eurBuyAndBtcSellRoiPessimistic = plnMoneyBtcExchangedAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI EUR Walutomat -> BTC " + stockCodeName + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi + " {" +
                eurBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
        displayDependOnRoi(eurBuyAndBtcSellRoi, resultToDisplay)

        return eurBuyAndBtcSellRoi
    }

    fun prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData: EthStockDataInterface, abstractEthEurStockData: EthStockDataInterface): BigDecimal {
        // WALUTOMAT & BANK
        val numberOfEurExchangedOnWalutomatAfterProv = amountOfEuroAfterExchangeAndSepaTransfer
        //EUR EXTERNAL STOCK -> ETH
        val numberOfEthWithdrawAfterProv = amountOfEthBoughtFromEuroOnExternalStock(abstractEthEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        val numberOfEthWithdrawAfterProvPessimistic = amountOfEthBoughtFromEuroOnExternalStockPessimistic(abstractEthEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        // BITBAY BTC -> PLN
        val plnMoneyEthExchangedAfterProv = getPlnFromBitBayEthSell(bitBayEthPlnStockData, numberOfEthWithdrawAfterProv)
        val plnMoneyEthExchangedAfterProvPessimistic = getPlnFromBitBayEthSell(bitBayEthPlnStockData, numberOfEthWithdrawAfterProvPessimistic)

        val eurBuyAndEthSellRoi = plnMoneyEthExchangedAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val eurBuyAndEthSellRoiPessimistic = plnMoneyEthExchangedAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI EUR Walutomat -> ETH " + stockCodeName + " -> Bitbay PLN: " + eurBuyAndEthSellRoi + " {" +
                eurBuyAndEthSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
        displayDependOnRoi(eurBuyAndEthSellRoi, resultToDisplay)

        return eurBuyAndEthSellRoi
    }

    fun prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData: BtcStockDataInterface,
                                                                                  ltcEurAbstractStockData: LtcStockDataInterface,
                                                                                  bitBayLctPlnStockData: LtcStockDataInterface): BigDecimal {
        val btcEurStockData = btcEurStockData
        if (btcEurStockData != null && btcEurStockData.btcEurStockData != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            val eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, btcEurStockData)
            val eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, btcEurStockData)
            // EURO - > LTC EXTERNAL STOCK
            val numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, eurNumberAfterBtcSellAfterTradeProv)
            val numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData,
                    eurNumberAfterBtcSellAfterTradeProvPessimistic)
            //BITBAY LTC -> PLN
            val numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLctPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromLtcSellAfterProvPessimistic = getPlnFromBitBayLtcSellPessimistic(bitBayLctPlnStockData,
                    numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayBtcBuyAndLtcSellRoiPessimistic = numberOfMoneyFromLtcSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI BTC BitBay -> " + stockCodeName + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi + " {" +
                    bitBayBtcBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay)
            println("BTC BitBay -> " + bitBayBtcPlnStockData.lastPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                    bitBayBtcPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "} BTC " + stockCodeName + " -> " +
                    btcEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # LTC " +
                    stockCodeName + " -> " +
                    ltcEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " LTC BitBay -> " + bitBayLctPlnStockData.lastPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                    bitBayBtcPlnStockData.bidPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}")
            return bitBayBtcBuyAndLtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData: LtcStockDataInterface, ltcEurAbstractStockData: LtcStockDataInterface): BigDecimal {
        // WALUTOMAT & BANK
        val numberOfEurExchangedOnWalutomatAfterProv = amountOfEuroAfterExchangeAndSepaTransfer

        //EURO EXTERNAL STOCK -> LTC
        val numberOfLtcWithdrawAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, numberOfEurExchangedOnWalutomatAfterProv)
        val numberOfLtcWithdrawAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData, numberOfEurExchangedOnWalutomatAfterProv)
        // BITBAY LTC -> PLN
        val plnMoneyLtcExchangedAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcWithdrawAfterProv)
        val plnMoneyLtcExchangedAfterProvPessimistic = getPlnFromBitBayLtcSellPessimistic(bitBayLtcPlnStockData, numberOfLtcWithdrawAfterProvPessimistic)

        val eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val eurBuyAndLtcSellRoiPessimistic = plnMoneyLtcExchangedAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI EUR Walutomat -> LTC " + stockCodeName + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi + " {" +
                eurBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay)
        return eurBuyAndLtcSellRoi
    }

    fun prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData: BccStockDataInterface, abstractBccEurStockData: BccStockDataInterface): BigDecimal {
        // WALUTOMAT & BANK -> EUR
        val numberOfEurExchangedOnWalutomatAfterProv = amountOfEuroAfterExchangeAndSepaTransfer

        //EUR EXTERNAL STOCK -> BCC
        val numberOfBccWithdrawAfterProv = amountOfBccBoughtFromEuroOnExternalStock(abstractBccEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        val numberOfBccWithdrawAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(abstractBccEurStockData, numberOfEurExchangedOnWalutomatAfterProv)
        // BITBAY BCC -> PLN
        val plnMoneyBccExchangedAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccWithdrawAfterProv)
        val plnMoneyBccExchangedAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData, numberOfBccWithdrawAfterProvPessimistic)

        val eurBuyAndBccSellRoi = plnMoneyBccExchangedAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val eurBuyAndBccSellRoiPessimistic = plnMoneyBccExchangedAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI EUR Walutomat -> BCC " + stockCodeName + " -> Bitbay PLN: " + eurBuyAndBccSellRoi + " {" +
                eurBuyAndBccSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
        displayDependOnRoi(eurBuyAndBccSellRoi, resultToDisplay)
        return eurBuyAndBccSellRoi
    }


    fun prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData: LtcStockDataInterface, dashEurAbstractStockData: DashStockDataInterface,
                                                            bitBayDashPlnStockData: DashStockDataInterface): BigDecimal {
        val ltcEurStockData = ltcEurStockData
        if (ltcEurStockData != null && ltcEurStockData.ltcEurStockData != null) {
            //BITBAY LTC
            val eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, ltcEurStockData)
            val eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, ltcEurStockData)
            //EUR EXTERNAL STOCK -> Dash
            val numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProv(dashEurAbstractStockData,
                    eurNumberAfterLtcSellAfterTradeProv)
            val numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic = amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(dashEurAbstractStockData,
                    eurNumberAfterLtcSellAfterTradeProvPessimistic)
            //BITBAY BTC
            val numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromDashSellAfterProvPessimistic = getPlnFromBitBayDashSellPessimistic(bitBayDashPlnStockData,
                    numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI LTC BitBay -> " + stockCodeName + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi + " {" +
                    bitBayLtcBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay)
            println(
                    "LTC BitBay -> " + bitBayLtcPlnStockData.lastPrice + " {" + bitBayLtcPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + "LTC" +
                            stockCodeName + " -> " +
                            ltcEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # DASH " +
                            stockCodeName + " -> " +
                            dashEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                            " DASH BitBay -> " + bitBayDashPlnStockData.lastPrice + " {" + bitBayDashPlnStockData.bidPrice + "}")
            return bitBayLtcBuyAndBtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(bitBayEthPlnStockData: EthStockDataInterface, dashEurAbstractStockData: DashStockDataInterface,
                                                                        bitBayDashPlnStockData: DashStockDataInterface): BigDecimal {
        val ethEurStockData = ethEurStockData
        if (ethEurStockData != null && ethEurStockData.ethEurStockData != null) {
            //BITBAY LTC
            val eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, ethEurStockData)
            val eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, ethEurStockData)
            //EUR EXTERNAL STOCK -> Dash
            val numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProv(dashEurAbstractStockData,
                    eurNumberAfterEthSellAfterTradeProv)
            val numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic = amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(dashEurAbstractStockData,
                    eurNumberAfterEthSellAfterTradeProvPessimistic)
            //BITBAY DASH -> PLN
            val numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromDashSellAfterProvPessimistic = getPlnFromBitBayDashSellPessimistic(bitBayDashPlnStockData,
                    numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayEthBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI ETH BitBay -> " + stockCodeName + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi + " {" +
                    bitBayEthBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay)
            println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.lastPrice + " {" + bitBayEthPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " ETH " +
                            stockCodeName + " -> " +
                            ethEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # DASH " +
                            stockCodeName + " -> " +
                            dashEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                            " DASH BitBay -> " + bitBayDashPlnStockData.lastPrice + " {" + bitBayEthPlnStockData.bidPrice + "}")
            return bitBayEthBuyAndBtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData: EthStockDataInterface, ltcEurAbstractStockData: LtcStockDataInterface,
                                                                       bitBayLtcPlnStockData: LtcStockDataInterface): BigDecimal {
        val ethEurStockData = ethEurStockData
        if (ethEurStockData != null && ethEurStockData.ethEurStockData != null) {
            //BITBAY ETH -> EUR EXTERNAL STOCK
            val eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, ethEurStockData)
            val eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, ethEurStockData)
            // EURO -> LTC
            val numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, eurNumberAfterEthSellAfterTradeProv)
            val numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData,
                    eurNumberAfterEthSellAfterTradeProvPessimistic)
            //BITBAY LTC -> PLN
            val numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv)
            val numberOfMoneyFromLtcSellAfterProvPessimistic = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic)

            val bitBayEthBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val bitBayEthBuyAndLtcSellRoiPessimistic = numberOfMoneyFromLtcSellAfterProvPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
            val resultToDisplay = "ROI ETH BitBay -> " + stockCodeName + " LTC -> Bitbay PLN : " + bitBayEthBuyAndLtcSellRoi + " {" +
                    bitBayEthBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
            displayDependOnRoi(bitBayEthBuyAndLtcSellRoi, resultToDisplay)
            println("ETH BitBay -> " + bitBayEthPlnStockData.lastPrice + " ETH " + stockCodeName + " -> " +
                    ethEurStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # LTC " +
                    stockCodeName + " -> " +
                    ltcEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " LTC BitBay -> " + bitBayLtcPlnStockData.lastPrice + " {" + bitBayLtcPlnStockData.bidPrice + "}")
            return bitBayEthBuyAndLtcSellRoi
        }
        println("NO $stockCodeName data")
        return BigDecimal.ZERO
    }

    fun prepareBitBayEthBuyToExternalStockPlnSellRoi(bitBayEthPlnStockData: EthStockDataInterface, ethStockDataInterface: EthStockDataInterface): BigDecimal {
        val amountOfEthBoughtOnBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData)
        val amountOfEthBoughtOnBitBayPessimistic = getAmountOfEthBoughtOnBitBayPessimistic(bitBayEthPlnStockData)
        val plnFromCoinroomEthSell = getPlnFromCoinroomEthSell(ethStockDataInterface, amountOfEthBoughtOnBitBay)
        val plnFromCoinroomEthSellPessimistic = getPlnFromCoinroomEthSellPessimistic(ethStockDataInterface, amountOfEthBoughtOnBitBayPessimistic)
        val bitBayEthBuyAndCoinroomPlnSellRoi = plnFromCoinroomEthSell.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayEthBuyAndCoinroomPlnSellRoiPessimistic = plnFromCoinroomEthSellPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI ETH BitBay -> $stockCodeName PLN -> Bank PLN: $bitBayEthBuyAndCoinroomPlnSellRoi {$bitBayEthBuyAndCoinroomPlnSellRoiPessimistic}"
        displayDependOnRoi(bitBayEthBuyAndCoinroomPlnSellRoi, resultToDisplay)
        println("ETH BitBay -> " + bitBayEthPlnStockData.lastPrice + " {" + bitBayEthPlnStockData.askPrice + "}" + " ETH " + stockCodeName + " -> " +
                ethStockDataInterface.lastPrice.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                ethStockDataInterface.bidPrice.setScale(2, RoundingMode.HALF_DOWN) + "}")

        return bitBayEthBuyAndCoinroomPlnSellRoi
    }

    fun prepareBitBayBtcBuyToExternalStockPlnSellRoi(bitBayBtcPlnStockData: BtcStockDataInterface, btcPlnStockDataInterface: BtcStockDataInterface): BigDecimal {
        val amountOfBtcBoughtOnBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData)
        val amountOfBtcBoughtOnBitBayPessimistic = getAmountOfBtcBoughtOnBitBayPessimistic(bitBayBtcPlnStockData)
        val plnFromCoinroomBtcSell = getPlnFromExternalStockBtcSell(btcPlnStockDataInterface, amountOfBtcBoughtOnBitBay)
        val plnFromCoinroomBtcSellPessimistic = getPlnFromExternalStockBtcSellPessimistic(btcPlnStockDataInterface, amountOfBtcBoughtOnBitBayPessimistic)
        val bitBayBtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomBtcSell.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayBtcBuyAndExternalStockPlnSellRoiPessimistic = plnFromCoinroomBtcSellPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI BTC BitBay -> " + stockCodeName + " PLN -> Bank PLN: " + bitBayBtcBuyAndExternalStockPlnSellRoi + " {" +
                bitBayBtcBuyAndExternalStockPlnSellRoiPessimistic + "}"
        displayDependOnRoi(bitBayBtcBuyAndExternalStockPlnSellRoi, resultToDisplay)
        return bitBayBtcBuyAndExternalStockPlnSellRoi
    }

    fun prepareBitBayLtcBuyToExternalStockPlnSellRoi(bitBayLtcPlnStockData: LtcStockDataInterface, ltcPlnStockDataInterface: LtcStockDataInterface): BigDecimal {
        val amountOfLtcBoughtOnBitBay = getAmountOfLtcBoughtOnBitBay(bitBayLtcPlnStockData)
        val amountOfLtcBoughtOnBitBayPessimistic = getAmountOfLtcBoughtOnBitBayPessimistic(bitBayLtcPlnStockData)
        val plnFromCoinroomLtcSell = getPlnFromExternalStockLtcSell(ltcPlnStockDataInterface, amountOfLtcBoughtOnBitBay)
        val plnFromCoinroomLtcSellPessimistic = getPlnFromExternalStockLtcSellPessimistic(ltcPlnStockDataInterface, amountOfLtcBoughtOnBitBayPessimistic)
        val bitBayLtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomLtcSell.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayLtcBuyAndExternalStockPlnSellRoiPessimistic = plnFromCoinroomLtcSellPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI LTC BitBay -> " + stockCodeName + " PLN -> Bank PLN: " + bitBayLtcBuyAndExternalStockPlnSellRoi + "{" +
                bitBayLtcBuyAndExternalStockPlnSellRoiPessimistic + "}"
        displayDependOnRoi(bitBayLtcBuyAndExternalStockPlnSellRoi, resultToDisplay)
        println(
                "BCC BitBay -> " + bitBayLtcPlnStockData.lastPrice + " {" + bitBayLtcPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " PLN " +
                        stockCodeName + " -> " + ltcPlnStockDataInterface.lastPrice.setScale(2, RoundingMode.HALF_DOWN) + " {" +
                        ltcPlnStockDataInterface.bidPrice.setScale(2, RoundingMode.HALF_DOWN) + "}")
        return bitBayLtcBuyAndExternalStockPlnSellRoi
    }

    fun prepareBitBayDashBuyToExternalStockPlnSellRoi(firstStockDashPlnData: DashStockDataInterface, secondDashPlnStockDataInterface: DashStockDataInterface): BigDecimal {
        val amountOfDashBoughtOnBitBay = getAmountOfDashBoughtOnBitBay(firstStockDashPlnData)
        val amountOfDashBoughtOnBitBayPessimistic = getAmountOfDashBoughtOnBitBayPessimistic(firstStockDashPlnData)
        val plnFromExternalStockDashSell = getPlnFromExternalStockDashSell(secondDashPlnStockDataInterface, amountOfDashBoughtOnBitBay)
        val plnFromExternalStockDashSellPessimistic = getPlnFromExternalStockDashSellPessimistic(secondDashPlnStockDataInterface, amountOfDashBoughtOnBitBayPessimistic)
        val bitBayDashBuyAndExternalStockPlnSellRoi = plnFromExternalStockDashSell.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayDashBuyAndExternalStockPlnSellRoiPessimistic = plnFromExternalStockDashSellPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI DASH " + firstStockDashPlnData.stockName + " -> " + secondDashPlnStockDataInterface.stockName + " PLN -> Bank PLN: " + bitBayDashBuyAndExternalStockPlnSellRoi + "{" +
                bitBayDashBuyAndExternalStockPlnSellRoiPessimistic + "}"
        displayDependOnRoi(bitBayDashBuyAndExternalStockPlnSellRoi, resultToDisplay)
        return bitBayDashBuyAndExternalStockPlnSellRoi
    }

    fun prepareBitBayBccBuyToExternalStockPlnSellRoi(bitBayBccPlnStockData: BccStockDataInterface, bccPlnStockDataInterface: BccStockDataInterface): BigDecimal {
        val amountOfBccBoughtOnBitBay = getAmountOfBccBoughtOnBitBay(bitBayBccPlnStockData)
        val amountOfBccBoughtOnBitBayPessimistic = getAmountOfBccBoughtOnBitBayPessimistic(bitBayBccPlnStockData)
        val plnFromExternalStockBccSell = getPlnFromExternalStockBccSell(bccPlnStockDataInterface, amountOfBccBoughtOnBitBay)
        val plnFromExternalStockBccSellPessimistic = getPlnFromExternalStockBccSellPessimistic(bccPlnStockDataInterface, amountOfBccBoughtOnBitBayPessimistic)
        val bitBayBccBuyAndExternalStockPlnSellRoi = plnFromExternalStockBccSell.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayBccBuyAndExternalStockPlnSellRoiPessimistic = plnFromExternalStockBccSellPessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val resultToDisplay = "ROI BCC BitBay -> " + stockCodeName + " PLN -> Bank PLN: " + bitBayBccBuyAndExternalStockPlnSellRoi + " {" +
                bitBayBccBuyAndExternalStockPlnSellRoiPessimistic + "}"
        displayDependOnRoi(bitBayBccBuyAndExternalStockPlnSellRoi, resultToDisplay)
        println(
                "BCC BitBay -> " + bitBayBccPlnStockData.lastPrice + " {" + bitBayBccPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " PLN " +
                        stockCodeName + " -> " + bccPlnStockDataInterface.lastPrice.setScale(2, RoundingMode.HALF_DOWN) + " {" +
                        bccPlnStockDataInterface.bidPrice.setScale(2, RoundingMode.HALF_DOWN) + "}")
        return bitBayBccBuyAndExternalStockPlnSellRoi
    }

    fun prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData: BtcStockDataInterface, btcEurAbstractStockData: BtcStockDataInterface): BigDecimal {
        // BTC BITBAY -> EURO EXTERNAL STOCK
        val eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, btcEurAbstractStockData)
        val eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, btcEurAbstractStockData)
        val amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterBtcSellAfterTradeProv)
        val amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(
                eurNumberAfterBtcSellAfterTradeProvPessimistic)
        val bitBayBtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayBtcBuyAndEuroSellRoiPessimistic = amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)

        val resultToDisplay = "ROI BTC BitBay -> " + stockCodeName + " EURO -> Bank EURO: " + bitBayBtcBuyAndEuroSellRoi + " {" +
                bitBayBtcBuyAndEuroSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}"
        displayDependOnRoi(bitBayBtcBuyAndEuroSellRoi, resultToDisplay)
        println(
                "BTC BitBay -> " + bitBayBtcPlnStockData.lastPrice + " {" + bitBayBtcPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " BTC " +
                        stockCodeName + " -> " +
                        btcEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, RoundingMode.HALF_DOWN) + " {" +
                        btcEurAbstractStockData.bidPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, RoundingMode.HALF_DOWN) + "}")
        return bitBayBtcBuyAndEuroSellRoi
    }

    fun prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData: DashStockDataInterface, dashEurAbstractStockData: DashStockDataInterface): BigDecimal {
        // DASH BITBAY -> EURO EXTERNAL STOCK
        val eurNumberAfterDashSellAfterTradeProv = getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(bitBayDashPlnStockData, dashEurAbstractStockData)
        val eurNumberAfterDashSellAfterTradeProvPessimistic = getEuroFromBuyDashBitbayAndSellForEuroOnExternalStockPessimistic(bitBayDashPlnStockData,
                dashEurAbstractStockData)
        //  EURO EXTERNAL STOCK -> BANK
        val amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterDashSellAfterTradeProv)
        val amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(
                eurNumberAfterDashSellAfterTradeProvPessimistic)
        val bitBayDashBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayDashBuyAndEuroSellRoiPessimistic = amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)

        val resultToDisplay = "ROI DASH BitBay -> $stockCodeName EURO -> Bank EURO: $bitBayDashBuyAndEuroSellRoi {$bitBayDashBuyAndEuroSellRoiPessimistic}"
        displayDependOnRoi(bitBayDashBuyAndEuroSellRoi, resultToDisplay)
        println(
                "DASH BitBay -> " + bitBayDashPlnStockData.lastPrice + " {" + bitBayDashPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " DASH " +
                        stockCodeName + " -> " +
                        dashEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                        dashEurAbstractStockData.bidPrice.setScale(3, RoundingMode.HALF_DOWN).multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!)
                                .setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}")
        return bitBayDashBuyAndEuroSellRoi
    }

    fun prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayLtcPlnStockData: LtcStockDataInterface, ltcEurAbstractStockData: LtcStockDataInterface): BigDecimal {
        // LTC BITBAY -> EURO EXTERNAL STOCK
        val eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, ltcEurAbstractStockData)
        val eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, ltcEurAbstractStockData)
        //  EURO EXTERNAL STOCK -> BANK
        val amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterLtcSellAfterTradeProv)
        val amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(
                eurNumberAfterLtcSellAfterTradeProvPessimistic)
        val bitBayLtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayLtcBuyAndEuroSellRoiPessimistic = amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)

        val resultToDisplay = "ROI LTC BitBay -> $stockCodeName EURO -> Bank EURO: $bitBayLtcBuyAndEuroSellRoi {$bitBayLtcBuyAndEuroSellRoiPessimistic}"
        displayDependOnRoi(bitBayLtcBuyAndEuroSellRoi, resultToDisplay)
        println(
                "LTC BitBay -> " + bitBayLtcPlnStockData.lastPrice + " {" + bitBayLtcPlnStockData.askPrice.setScale(3, RoundingMode.HALF_DOWN) + "}" + " DASH " +
                        stockCodeName + " -> " +
                        ltcEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                        ltcEurAbstractStockData.bidPrice.setScale(3, RoundingMode.HALF_DOWN).multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!)
                                .setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}")
        return bitBayLtcBuyAndEuroSellRoi
    }

    fun prepareBitBayEthBuyToExternalStockSellToEuroWithdrawalRoi(bitBayEthPlnStockData: EthStockDataInterface, ethEurAbstractStockData: EthStockDataInterface): BigDecimal {
        // Eth BITBAY -> EURO EXTERNAL STOCK
        val eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, ethEurAbstractStockData)
        val eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, ethEurAbstractStockData)
        //  EURO EXTERNAL STOCK -> BANK
        val amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterEthSellAfterTradeProv)
        val amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(
                eurNumberAfterEthSellAfterTradeProvPessimistic)
        val bitBayEthBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)
        val bitBayEthBuyAndEuroSellRoiPessimistic = amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic.divide(MONEY_FOR_CRYPTO_BUY, 3, RoundingMode.HALF_DOWN)

        val resultToDisplay = "ROI Eth BitBay -> $stockCodeName EURO -> Bank EURO: $bitBayEthBuyAndEuroSellRoi {$bitBayEthBuyAndEuroSellRoiPessimistic}"
        displayDependOnRoi(bitBayEthBuyAndEuroSellRoi, resultToDisplay)
        println("ETH BitBay -> " + bitBayEthPlnStockData.lastPrice + " ETH " + stockCodeName + " -> " +
                ethEurAbstractStockData.lastPrice.multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                ethEurAbstractStockData.bidPrice.setScale(3, RoundingMode.HALF_DOWN).multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate!!)
                        .setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}")
        return bitBayEthBuyAndEuroSellRoi
    }

    //  ##########################################################################################################################
    //  ##################################################### HELPER METHODS #####################################################
    //  ##########################################################################################################################
    //Pessimistic value is the same
    private fun getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterBtcSellAfterTradeProv: BigDecimal): BigDecimal {
        val amountOfEuro = getEuroAfterMoneyWithdrawalProv(eurNumberAfterBtcSellAfterTradeProv)
        val eurPlnExchangeRate = StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate
        val numberOfEurAfterExchange = amountOfEuro.multiply(eurPlnExchangeRate!!)
        return numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO)
    }

    private fun getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData: BtcStockDataInterface, btcEurStockData: BtcStockDataInterface): BigDecimal {
        //BITBAY BTC
        val btcNumberAfterWithdrawFromBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData)
        //EXTERNAL STOCK BTC -> EUR
        val eurNumberAfterBtcSell = btcNumberAfterWithdrawFromBitBay.multiply(btcEurStockData.lastPrice)
        return eurNumberAfterBtcSell.subtract(eurNumberAfterBtcSell.multiply(btcEurStockData.makerProvision))
    }

    private fun getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData: BtcStockDataInterface, btcEurStockData: BtcStockDataInterface): BigDecimal {
        //BITBAY BTC
        val btcNumberAfterWithdrawFromBitBayPessimistic = getAmountOfBtcBoughtOnBitBayPessimistic(bitBayBtcPlnStockData)
        //EXTERNAL STOCK BTC -> EUR
        val eurNumberAfterBtcSellPessimistic = btcNumberAfterWithdrawFromBitBayPessimistic.multiply(btcEurStockData.bidPrice)
        return eurNumberAfterBtcSellPessimistic.subtract(eurNumberAfterBtcSellPessimistic.multiply(btcEurStockData.takerProvision))
    }

    private fun getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData: BtcStockDataInterface): BigDecimal {
        val numberOfBtcBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayBtcPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val btcNumberAfterTradeProvision = numberOfBtcBoughtOnBitBay.subtract(numberOfBtcBoughtOnBitBay.multiply(bitBayBtcPlnStockData.makerProvision))
        return bitBayBtcPlnStockData.getBtcAfterWithdrawalProv(btcNumberAfterTradeProvision)
    }

    private fun getAmountOfBtcBoughtOnBitBayPessimistic(bitBayBtcPlnStockData: BtcStockDataInterface): BigDecimal {
        val numberOfBtcBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayBtcPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val btcNumberAfterTradeProvision = numberOfBtcBoughtOnBitBay.subtract(numberOfBtcBoughtOnBitBay.multiply(bitBayBtcPlnStockData.takerProvision))
        return bitBayBtcPlnStockData.getBtcAfterWithdrawalProv(btcNumberAfterTradeProvision)
    }

    private fun getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData: EthStockDataInterface): BigDecimal {
        val numberOfEthBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayEthPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(bitBayEthPlnStockData.makerProvision))
        return bitBayEthPlnStockData.getEthAfterWithdrawalProv(ethNumberAfterTradeProvision)
    }

    private fun getAmountOfEthBoughtOnBitBayPessimistic(bitBayEthPlnStockData: EthStockDataInterface): BigDecimal {
        val numberOfEthBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayEthPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(bitBayEthPlnStockData.takerProvision))
        return bitBayEthPlnStockData.getEthAfterWithdrawalProv(ethNumberAfterTradeProvision)
    }

    private fun getAmountOfLtcBoughtOnBitBay(bitBayLtcPlnStockData: LtcStockDataInterface): BigDecimal {
        val numberOfLtcBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayLtcPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(bitBayLtcPlnStockData.takerProvision))
        return bitBayLtcPlnStockData.getLtcAfterWithdrawalProv(ltcNumberAfterTradeProvision)
    }

    private fun getAmountOfLtcBoughtOnBitBayPessimistic(bitBayLtcPlnStockData: LtcStockDataInterface): BigDecimal {
        val numberOfLtcBoughtOnBitBayPessimistic = MONEY_FOR_CRYPTO_BUY.divide(bitBayLtcPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val ltcNumberAfterTradeProvisionPessimistic = numberOfLtcBoughtOnBitBayPessimistic
                .subtract(numberOfLtcBoughtOnBitBayPessimistic.multiply(bitBayLtcPlnStockData.takerProvision))
        return bitBayLtcPlnStockData.getLtcAfterWithdrawalProv(ltcNumberAfterTradeProvisionPessimistic)
    }

    private fun getAmountOfDashBoughtOnBitBay(bitBayDashPlnStockData: DashStockDataInterface): BigDecimal {
        val numberOfDashBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayDashPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val dashNumberAfterTradeProvision = numberOfDashBoughtOnBitBay.subtract(numberOfDashBoughtOnBitBay.multiply(bitBayDashPlnStockData.makerProvision))
        return bitBayDashPlnStockData.getDashAfterWithdrawalProv(dashNumberAfterTradeProvision)
    }

    private fun getAmountOfDashBoughtOnBitBayPessimistic(bitBayDashPlnStockData: DashStockDataInterface): BigDecimal {
        val numberOfDashBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayDashPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val dashNumberAfterTradeProvision = numberOfDashBoughtOnBitBay.subtract(numberOfDashBoughtOnBitBay.multiply(bitBayDashPlnStockData.makerProvision))
        return bitBayDashPlnStockData.getDashAfterWithdrawalProv(dashNumberAfterTradeProvision)
    }

    private fun getAmountOfBccBoughtOnBitBay(bitBayBccPlnStockData: BccStockDataInterface): BigDecimal {
        val numberOfBccBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayBccPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val bccNumberAfterTradeProvision = numberOfBccBoughtOnBitBay.subtract(numberOfBccBoughtOnBitBay.multiply(bitBayBccPlnStockData.makerProvision))
        return bitBayBccPlnStockData.getBccAfterWithdrawalProv(bccNumberAfterTradeProvision)
    }

    private fun getAmountOfBccBoughtOnBitBayPessimistic(bitBayBccPlnStockData: BccStockDataInterface): BigDecimal {
        val numberOfBccBoughtOnBitBayPessimistic = MONEY_FOR_CRYPTO_BUY.divide(bitBayBccPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val bccNumberAfterTradeProvisionPessimistic = numberOfBccBoughtOnBitBayPessimistic
                .subtract(numberOfBccBoughtOnBitBayPessimistic.multiply(bitBayBccPlnStockData.takerProvision))
        return bitBayBccPlnStockData.getBccAfterWithdrawalProv(bccNumberAfterTradeProvisionPessimistic)
    }

    private fun getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData: EthStockDataInterface, ethEurStockData: EthStockDataInterface): BigDecimal {
        //BITBAY ETH
        val ethNumberAfterWithdrawFromBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData)
        //EXTERNAL STOCK ETH -> EURO
        val eurNumberAfterEthSell = ethNumberAfterWithdrawFromBitBay.multiply(ethEurStockData.lastPrice)
        return eurNumberAfterEthSell.subtract(eurNumberAfterEthSell.multiply(ethEurStockData.makerProvision))
    }

    private fun getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData: EthStockDataInterface, ethEurStockData: EthStockDataInterface): BigDecimal {
        //BITBAY ETH
        val numberOfEthBoughtOnBitBayPessimistic = MONEY_FOR_CRYPTO_BUY.divide(bitBayEthPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val ethNumberAfterTradeProvisionPessimistic = numberOfEthBoughtOnBitBayPessimistic
                .subtract(numberOfEthBoughtOnBitBayPessimistic.multiply(bitBayEthPlnStockData.takerProvision))
        val ethNumberAfterWithdrawFromBitBayPessimistic = bitBayEthPlnStockData.getEthAfterWithdrawalProv(ethNumberAfterTradeProvisionPessimistic)
        //EXTERNAL STOCK ETH -> EURO
        val eurNumberAfterEthSellPessimistic = ethNumberAfterWithdrawFromBitBayPessimistic.multiply(ethEurStockData.askPrice)
        return eurNumberAfterEthSellPessimistic.subtract(eurNumberAfterEthSellPessimistic.multiply(ethEurStockData.takerProvision))
    }

    private fun getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData: LtcStockDataInterface, ltcEurStockData: LtcStockDataInterface): BigDecimal {
        val numberOfLtcBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayLtcPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(bitBayLtcPlnStockData.makerProvision))
        val ltcNumberAfterWithdrawFromBitBay = bitBayLtcPlnStockData.getLtcAfterWithdrawalProv(ltcNumberAfterTradeProvision)
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        val eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.lastPrice)
        return eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(ltcEurStockData.makerProvision))
    }

    private fun getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData: LtcStockDataInterface, ltcEurStockData: LtcStockDataInterface): BigDecimal {
        val numberOfLtcBoughtOnBitBayPessimistic = MONEY_FOR_CRYPTO_BUY.divide(bitBayLtcPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)

        val ltcNumberAfterTradeProvisionPessimistic = numberOfLtcBoughtOnBitBayPessimistic
                .subtract(numberOfLtcBoughtOnBitBayPessimistic.multiply(bitBayLtcPlnStockData.takerProvision))

        val ltcNumberAfterWithdrawFromBitBayPessimistic = bitBayLtcPlnStockData.getLtcAfterWithdrawalProv(ltcNumberAfterTradeProvisionPessimistic)

        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        val eurNumberAfterLtcSellPessimistic = ltcNumberAfterWithdrawFromBitBayPessimistic.multiply(ltcEurStockData.askPrice)
        return eurNumberAfterLtcSellPessimistic.subtract(eurNumberAfterLtcSellPessimistic.multiply(ltcEurStockData.takerProvision))
    }

    private fun getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(bitBayDashPlnStockData: DashStockDataInterface, dashEurStockData: DashStockDataInterface): BigDecimal {
        val amountOfDashBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayDashPlnStockData.lastPrice, 4, RoundingMode.HALF_DOWN)
        val dashAmountAfterTradeProvision = amountOfDashBoughtOnBitBay.subtract(amountOfDashBoughtOnBitBay.multiply(bitBayDashPlnStockData.makerProvision))
        val dashNumberAfterWithdrawFromBitBay = bitBayDashPlnStockData.getDashAfterWithdrawalProv(dashAmountAfterTradeProvision)
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        val eurNumberAfterDashSell = dashNumberAfterWithdrawFromBitBay.multiply(dashEurStockData.lastPrice)
        return eurNumberAfterDashSell.subtract(eurNumberAfterDashSell.multiply(dashEurStockData.makerProvision))
    }

    private fun getEuroFromBuyDashBitbayAndSellForEuroOnExternalStockPessimistic(bitBayDashPlnStockData: DashStockDataInterface, dashEurStockData: DashStockDataInterface): BigDecimal {
        val amountOfDashBoughtOnBitBay = MONEY_FOR_CRYPTO_BUY.divide(bitBayDashPlnStockData.askPrice, 4, RoundingMode.HALF_DOWN)
        val dashAmountAfterTradeProvision = amountOfDashBoughtOnBitBay.subtract(amountOfDashBoughtOnBitBay.multiply(bitBayDashPlnStockData.makerProvision))
        val dashNumberAfterWithdrawFromBitBay = bitBayDashPlnStockData.getDashAfterWithdrawalProv(dashAmountAfterTradeProvision)
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        val eurNumberAfterDashSell = dashNumberAfterWithdrawFromBitBay.multiply(dashEurStockData.bidPrice)
        return eurNumberAfterDashSell.subtract(eurNumberAfterDashSell.multiply(dashEurStockData.takerProvision))
    }

    private fun amountOfBtcBoughtFromEuroOnExternalStock(btcEurAbstractStockData: BtcStockDataInterface, eurNumberAfterLtcSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.lastPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(btcEurAbstractStockData.makerProvision))
        return btcEurAbstractStockData.getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProv)
    }

    private fun amountOfBtcBoughtFromEuroOnExternalStockPessimistic(btcEurAbstractStockData: BtcStockDataInterface, eurNumberAfterLtcSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfBtcBoughtPessimistic = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.askPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfBtcBoughtAfterTradeProvPessimistic = numberOfBtcBoughtPessimistic
                .subtract(numberOfBtcBoughtPessimistic.multiply(btcEurAbstractStockData.takerProvision))
        return btcEurAbstractStockData.getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProvPessimistic)
    }

    private fun amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData: LtcStockDataInterface, eurNumberAfterEthSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.lastPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfLtcBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(ltcEurAbstractStockData.makerProvision))
        return ltcEurAbstractStockData.getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProv)
    }

    private fun amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData: LtcStockDataInterface, eurNumberAfterEthSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfLtcBoughtPessimistic = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.askPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfLtcBoughtAfterTradeProvPessimistic = numberOfLtcBoughtPessimistic
                .subtract(numberOfLtcBoughtPessimistic.multiply(ltcEurAbstractStockData.takerProvision))
        return ltcEurAbstractStockData.getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProvPessimistic)
    }

    private fun amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData: BccStockDataInterface, euroAmount: BigDecimal): BigDecimal {
        // EURO - > BCC EXTERNAL STOCK
        val numberOfBccBought = euroAmount.divide(bccEurAbstractStockData.lastPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(bccEurAbstractStockData.makerProvision))
        return bccEurAbstractStockData.getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv)
    }

    private fun amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData: BccStockDataInterface, euroAmount: BigDecimal): BigDecimal {
        // EURO - > BCC EXTERNAL STOCK
        val numberOfBccBoughtPessimistic = euroAmount.divide(bccEurAbstractStockData.askPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfBccBoughtAfterTradeProvPessimistic = numberOfBccBoughtPessimistic
                .subtract(numberOfBccBoughtPessimistic.multiply(bccEurAbstractStockData.takerProvision))
        return bccEurAbstractStockData.getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProvPessimistic)
    }

    private fun amountOfEthBoughtFromEuroOnExternalStock(ethEurAbstractStockData: EthStockDataInterface, euroAmount: BigDecimal): BigDecimal {
        // EURO - > ETH EXTERNAL STOCK
        val numberOfEthBought = euroAmount.divide(ethEurAbstractStockData.lastPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfEthBoughtAfterTradeProv = numberOfEthBought.subtract(numberOfEthBought.multiply(ethEurAbstractStockData.makerProvision))
        return ethEurAbstractStockData.getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProv)
    }

    private fun amountOfEthBoughtFromEuroOnExternalStockPessimistic(ethEurAbstractStockData: EthStockDataInterface, euroAmount: BigDecimal): BigDecimal {
        // EURO - > ETH EXTERNAL STOCK
        val numberOfEthBoughtPessimistic = euroAmount.divide(ethEurAbstractStockData.askPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfEthBoughtAfterTradeProvPessimistic = numberOfEthBoughtPessimistic
                .subtract(numberOfEthBoughtPessimistic.multiply(ethEurAbstractStockData.takerProvision))
        return ethEurAbstractStockData.getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProvPessimistic)
    }

    private fun amountOfDashBoughtFromEuroOnExternalStockAfterProv(dashEurAbstractStockData: DashStockDataInterface, eurNumberAfterLtcSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.lastPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(dashEurAbstractStockData.makerProvision))
        return dashEurAbstractStockData.getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv)
    }

    private fun amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(dashEurAbstractStockData: DashStockDataInterface,
                                                                              eurNumberAfterLtcSellAfterTradeProv: BigDecimal): BigDecimal {
        val numberOfDashBoughtPessimistic = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.askPrice, 5, RoundingMode.HALF_DOWN)
        val numberOfDashBoughtAfterTradeProvPessimistic = numberOfDashBoughtPessimistic
                .subtract(numberOfDashBoughtPessimistic.multiply(dashEurAbstractStockData.takerProvision))
        return dashEurAbstractStockData.getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProvPessimistic)
    }

    private val amountOfEuroAfterExchangeAndSepaTransfer: BigDecimal
        get() {
            val eurPlnExchangeRate = StockRoiPreparer.lastSellWalutomatEurPlnExchangeRate
            val numberOfEurAfterExchange = MONEY_FOR_CRYPTO_BUY.divide(eurPlnExchangeRate, 4, RoundingMode.HALF_DOWN)
            return numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN))
        }

    private fun getPlnFromBitBayBtcSell(bitBayBtcPlnStockData: BtcStockDataInterface, numberOfBtcBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayBtcPlnStockData.lastPrice)
        return numberOfMoneyFromBitBayBtcSell.subtract(numberOfMoneyFromBitBayBtcSell.multiply(bitBayBtcPlnStockData.makerProvision))
    }

    private fun getPlnFromBitBayBtcSellPessimistic(bitBayBtcPlnStockData: BtcStockDataInterface, numberOfBtcBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayBtcSellPessimistic = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayBtcPlnStockData.bidPrice)
        return numberOfMoneyFromBitBayBtcSellPessimistic.subtract(numberOfMoneyFromBitBayBtcSellPessimistic.multiply(bitBayBtcPlnStockData.takerProvision))
    }

    private fun getPlnFromBitBayBccSell(bitBayBccPlnStockData: BccStockDataInterface, numberOfBccBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(bitBayBccPlnStockData.lastPrice)
        return numberOfMoneyFromBitBayBccSell.subtract(numberOfMoneyFromBitBayBccSell.multiply(bitBayBccPlnStockData.makerProvision))
    }

    private fun getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData: BccStockDataInterface, numberOfBccBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayBccSellPessimistic = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(bitBayBccPlnStockData.bidPrice)
        return numberOfMoneyFromBitBayBccSellPessimistic.subtract(numberOfMoneyFromBitBayBccSellPessimistic.multiply(bitBayBccPlnStockData.takerProvision))
    }

    private fun getPlnFromBitBayLtcSell(bitBayLctPlnStockData: LtcStockDataInterface, numberOfLtcBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayLctPlnStockData.lastPrice)
        return numberOfMoneyFromBitBayLtcSell.subtract(numberOfMoneyFromBitBayLtcSell.multiply(bitBayLctPlnStockData.makerProvision))
    }

    private fun getPlnFromBitBayLtcSellPessimistic(bitBayLctPlnStockData: LtcStockDataInterface, numberOfLtcBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayLtcSellPessimistic = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayLctPlnStockData.bidPrice)
        return numberOfMoneyFromBitBayLtcSellPessimistic.subtract(numberOfMoneyFromBitBayLtcSellPessimistic.multiply(bitBayLctPlnStockData.takerProvision))
    }

    private fun getPlnFromBitBayEthSell(bitBayEthPlnStockData: EthStockDataInterface, numberOfEthBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayEthSell = numberOfEthBoughtWithdrawToBitBayAfterProv.multiply(bitBayEthPlnStockData.lastPrice)
        return numberOfMoneyFromBitBayEthSell.subtract(numberOfMoneyFromBitBayEthSell.multiply(bitBayEthPlnStockData.makerProvision))
    }

    private fun getPlnFromCoinroomEthSell(coinroomEthPlnStockData: EthStockDataInterface, numberOfEthBoughtWithdrawToCoinroomAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromCoinroomEthSell = numberOfEthBoughtWithdrawToCoinroomAfterProv.multiply(coinroomEthPlnStockData.lastPrice)
        return numberOfMoneyFromCoinroomEthSell.subtract(numberOfMoneyFromCoinroomEthSell.multiply(coinroomEthPlnStockData.takerProvision))
    }

    private fun getPlnFromCoinroomEthSellPessimistic(coinroomEthPlnStockData: EthStockDataInterface, numberOfEthBoughtWithdrawToCoinroomAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromCoinroomEthSell = numberOfEthBoughtWithdrawToCoinroomAfterProv.multiply(coinroomEthPlnStockData.bidPrice)
        return numberOfMoneyFromCoinroomEthSell.subtract(numberOfMoneyFromCoinroomEthSell.multiply(coinroomEthPlnStockData.takerProvision))
    }

    private fun getPlnFromExternalStockBtcSell(btcPlnStockData: BtcStockDataInterface, numberOfBtcBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromBtcSell = numberOfBtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.lastPrice)
        return numberOfMoneyFromBtcSell.subtract(numberOfMoneyFromBtcSell.multiply(btcPlnStockData.makerProvision))
    }

    private fun getPlnFromExternalStockBtcSellPessimistic(btcPlnStockData: BtcStockDataInterface, numberOfBtcBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromBtcSell = numberOfBtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.bidPrice)
        return numberOfMoneyFromBtcSell.subtract(numberOfMoneyFromBtcSell.multiply(btcPlnStockData.takerProvision))
    }

    private fun getPlnFromExternalStockLtcSell(ltcPlnStockData: LtcStockDataInterface, numberOfLtcBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSell = numberOfLtcBoughtWithdrawToExternalStock.multiply(ltcPlnStockData.lastPrice)
        return numberOfMoneyFromLtcSell.subtract(numberOfMoneyFromLtcSell.multiply(ltcPlnStockData.makerProvision))
    }

    private fun getPlnFromExternalStockLtcSellPessimistic(ltcPlnStockData: LtcStockDataInterface, numberOfLtcBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSell = numberOfLtcBoughtWithdrawToExternalStock.multiply(ltcPlnStockData.bidPrice)
        return numberOfMoneyFromLtcSell.subtract(numberOfMoneyFromLtcSell.multiply(ltcPlnStockData.takerProvision))
    }

    private fun getPlnFromExternalStockDashSell(dashPlnStockData: DashStockDataInterface, numberOfDashBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSell = numberOfDashBoughtWithdrawToExternalStock.multiply(dashPlnStockData.lastPrice)
        return numberOfMoneyFromLtcSell.subtract(numberOfMoneyFromLtcSell.multiply(dashPlnStockData.makerProvision))
    }

    private fun getPlnFromExternalStockDashSellPessimistic(dashPlnStockData: DashStockDataInterface, numberOfDashBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSell = numberOfDashBoughtWithdrawToExternalStock.multiply(dashPlnStockData.bidPrice)
        return numberOfMoneyFromLtcSell.subtract(numberOfMoneyFromLtcSell.multiply(dashPlnStockData.makerProvision))
    }

    private fun getPlnFromExternalStockBccSell(bccPlnStockData: BccStockDataInterface, numberOfBccBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSell = numberOfBccBoughtWithdrawToExternalStock.multiply(bccPlnStockData.lastPrice)
        return numberOfMoneyFromLtcSell.subtract(numberOfMoneyFromLtcSell.multiply(bccPlnStockData.makerProvision))
    }

    private fun getPlnFromExternalStockBccSellPessimistic(bccPlnStockData: BccStockDataInterface, numberOfBccBoughtWithdrawToExternalStock: BigDecimal): BigDecimal {
        val numberOfMoneyFromLtcSellPessimistic = numberOfBccBoughtWithdrawToExternalStock.multiply(bccPlnStockData.bidPrice)
        return numberOfMoneyFromLtcSellPessimistic.subtract(numberOfMoneyFromLtcSellPessimistic.multiply(bccPlnStockData.takerProvision))
    }

    private fun getPlnFromBitBayDashSell(bitBayDashPlnStockData: DashStockDataInterface, numberOfDashBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(bitBayDashPlnStockData.lastPrice)
        return numberOfMoneyFromBitBayDashSell.subtract(numberOfMoneyFromBitBayDashSell.multiply(bitBayDashPlnStockData.makerProvision))
    }

    private fun getPlnFromBitBayDashSellPessimistic(bitBayDashPlnStockData: DashStockDataInterface, numberOfDashBoughtWithdrawToBitBayAfterProv: BigDecimal): BigDecimal {
        val numberOfMoneyFromBitBayDashSellPessimistic = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(bitBayDashPlnStockData.bidPrice)
        return numberOfMoneyFromBitBayDashSellPessimistic.subtract(numberOfMoneyFromBitBayDashSellPessimistic.multiply(bitBayDashPlnStockData.takerProvision))
    }

    private fun displayDependOnRoi(eurBuyAndBtcSellRoi: BigDecimal, resultToDisplay: String) {
        if (eurBuyAndBtcSellRoi > maxRoiValueForWarnDisplay || eurBuyAndBtcSellRoi < minRoiValueForWarnDisplay) {
            logger.warn(resultToDisplay)
        } else {
            logger.info(resultToDisplay)
        }
    }
}
