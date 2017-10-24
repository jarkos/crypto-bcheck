package com.jarkos.stock

import com.jarkos.stock.service.BitBayStockDataService
import com.jarkos.stock.service.BitstampStockDataService
import com.jarkos.stock.service.CoinroomStockDataService
import com.jarkos.stock.service.KrakenStockDataService
import com.jarkos.walutomat.WalutomatDataService
import java.math.BigDecimal

class StockRoiPreparer {

    companion object {

        var lastBuyWalutomatEurPlnExchangeRate: BigDecimal? = null
        var lastSellWalutomatEurPlnExchangeRate: BigDecimal? = null
    }

    fun fetchAndPrintStockData() {
        lastBuyWalutomatEurPlnExchangeRate = WalutomatDataService().walutomatEurToPlnData.buyExchangeRate
        lastSellWalutomatEurPlnExchangeRate = WalutomatDataService().walutomatEurToPlnData.sellExchangeRate

        val bitBayBtcPlnStockData = BitBayStockDataService().btcPlnStockData
        val bitBayLtcPlnStockData = BitBayStockDataService().ltcPlnStockData
        val bitBayBccPlnStockData = BitBayStockDataService().bccPlnStockData
        val bitBayDashPlnStockData = BitBayStockDataService().dashPlnStockData
        val bitBayEthPlnStockData = BitBayStockDataService().ethPlnStockData
        val krakenBtcEurStockData = KrakenStockDataService().btcEurStockData
        val krakenBccEurStockData = KrakenStockDataService().bccEurStockData
        val krakenLtcEurStockData = KrakenStockDataService().ltcEurStockData
        val krakenEthEurStockData = KrakenStockDataService().ethEurStockData
        val krakenDashEurStockData = KrakenStockDataService().dashEurStockData
        val bitstampBtcEurStockData = BitstampStockDataService().btcEurStockData
        val bitstampLtcEurStockData = BitstampStockDataService().ltcEurStockData
        val bitstampEthEurStockData = BitstampStockDataService().ethEurStockData
        val coinroomEthPlnStockData = CoinroomStockDataService().ethPlnStockData
        val coinroomBtcPlnStockData = CoinroomStockDataService().btcPlnStockData
        val coinroomLtcPlnStockData = CoinroomStockDataService().ltcPlnStockData
        val coinroomDashPlnStockData = CoinroomStockDataService().dashPlnStockData
        val coinroomBccPlnStockData = CoinroomStockDataService().bccPlnStockData

        if (bitBayBtcPlnStockData != null) {
            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData)

                Indicators.bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = KrakenStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData)
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData)

                Indicators.bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = BitstampStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData)
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null) {
                if (krakenBtcEurStockData != null && krakenLtcEurStockData != null) {
                    Indicators.euroBuyToKrakenBtcSellOnBitBayRoi = KrakenStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData)
                    Indicators.euroBuyToKrakenLtcSellOnBitBayRoi = KrakenStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData)
                }
                if (bitstampBtcEurStockData != null && bitstampLtcEurStockData != null) {
                    Indicators.euroBuyToBitstampBtcSellOnBitBayRoi = BitstampStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData)
                    Indicators.euroBuyToBitstampLtcSellOnBitBayRoi = BitstampStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData)
                }
            }
            if (krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                val bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData)
                if (bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi > BigDecimal.ZERO) {
                    Indicators.bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi
                }
            }
            if (bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                val bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData)
                if (bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi > BigDecimal.ZERO) {
                    Indicators.bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi
                }
            }
        }
        if (lastBuyWalutomatEurPlnExchangeRate != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
            Indicators.euroBuyToExternalStockBccSellOnBitBayRoi = KrakenStockDataService()
                    .prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData)
        }

        if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            val bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData)
            if (bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi > BigDecimal.ZERO) {
                Indicators.bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi
            }
        }
        if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            val bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(bitBayEthPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData)
            if (bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi > BigDecimal.ZERO) {
                Indicators.bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi
            }
        }
        if (krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            val bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = KrakenStockDataService()
                    .prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayBtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData)
            if (bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi > BigDecimal.ZERO) {
                Indicators.bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi
            }
        }

        if (bitBayLtcPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            val bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = KrakenStockDataService()
                    .prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData)
            if (bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi > BigDecimal.ZERO) {
                Indicators.bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi
            }
        }
        if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            Indicators.bitBayDashBuyToKrakenSellToEuroWithdrawalRoi = KrakenStockDataService()
                    .prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData)
            val bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(bitBayEthPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData)
            if (bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi > BigDecimal.ZERO) {
                Indicators.bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi
            }
        }
        if (bitBayEthPlnStockData != null && krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            val bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi = KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData)
            if (bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi > BigDecimal.ZERO) {
                Indicators.bitBayEthBuyToToKrakenEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi
            }
            Indicators.bitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi = KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayLtcPlnStockData, krakenLtcEurStockData)
        }
        if (bitBayEthPlnStockData != null && bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            val bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = BitstampStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData)
            if (bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi > BigDecimal.ZERO) {
                Indicators.bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi
            }
        }
        if (bitBayEthPlnStockData != null && krakenEthEurStockData != null) {
            Indicators.euroBuyToKrakenEthSellOnBitBayRoi = KrakenStockDataService()
                    .prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, krakenEthEurStockData)
        }
        if (bitBayEthPlnStockData != null && bitstampEthEurStockData != null) {
            Indicators.euroBuyToBitstampEthSellOnBitBayRoi = BitstampStockDataService()
                    .prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, bitstampEthEurStockData)
        }
        if (bitBayEthPlnStockData != null && coinroomEthPlnStockData != null) {
            Indicators.bitBayEthBuyToCoinroomPlnSell = CoinroomStockDataService().prepareBitBayEthBuyToExternalStockPlnSellRoi(bitBayEthPlnStockData, coinroomEthPlnStockData)
        }
        if (bitBayBtcPlnStockData != null && coinroomBtcPlnStockData != null) {
            Indicators.bitBayBtcBuyToCoinroomPlnSell = CoinroomStockDataService().prepareBitBayBtcBuyToExternalStockPlnSellRoi(bitBayBtcPlnStockData, coinroomBtcPlnStockData)
        }
        if (bitBayLtcPlnStockData != null && coinroomLtcPlnStockData != null) {
            Indicators.bitBayLtcBuyToCoinroomPlnSell = CoinroomStockDataService().prepareBitBayLtcBuyToExternalStockPlnSellRoi(bitBayLtcPlnStockData, coinroomLtcPlnStockData)
        }
        if (bitBayDashPlnStockData != null && coinroomDashPlnStockData != null) {
            Indicators.bitBayDashBuyToCoinroomPlnSell = CoinroomStockDataService()
                    .prepareBitBayDashBuyToExternalStockPlnSellRoi(bitBayDashPlnStockData, coinroomDashPlnStockData)
            Indicators.coinroomDashBuyToBitbayPlnSell = CoinroomStockDataService()
                    .prepareBitBayDashBuyToExternalStockPlnSellRoi(coinroomDashPlnStockData, bitBayDashPlnStockData)
        }
        if (bitBayDashPlnStockData != null && coinroomBccPlnStockData != null) {
            Indicators.bitBayBccBuyToCoinroomPlnSell = CoinroomStockDataService().prepareBitBayBccBuyToExternalStockPlnSellRoi(bitBayBccPlnStockData, coinroomBccPlnStockData)
        }
        if (bitBayEthPlnStockData != null && krakenEthEurStockData != null) {
            Indicators.prepareBitBayEthBuyToExternalStockSellToEuroWithdrawal = KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellToEuroWithdrawalRoi(bitBayEthPlnStockData, krakenEthEurStockData)
        }
    }

}
