package com.jarkos.stock;

import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampEthStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.coinroom.*;
import com.jarkos.stock.dto.kraken.*;
import com.jarkos.stock.service.*;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;
import static com.jarkos.config.StockConfig.KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;

public class StockRoiPreparer {

    public static BigDecimal lastBuyWalutomatEurPlnExchangeRate;
    public static BigDecimal lastSellWalutomatEurPlnExchangeRate;

    public void fetchAndPrintStockData() {
        lastBuyWalutomatEurPlnExchangeRate = new WalutomatDataService().getWalutomatEurToPlnData().getBuyExchangeRate();
        lastSellWalutomatEurPlnExchangeRate = new WalutomatDataService().getWalutomatEurToPlnData().getSellExchangeRate();

        BitBayBtcStockData bitBayBtcPlnStockData = new BitBayDataService().getBtcPlnStockData();
        BitBayLtcStockData bitBayLtcPlnStockData = new BitBayDataService().getLtcPlnStockData();
        BitBayBccStockData bitBayBccPlnStockData = new BitBayDataService().getBccPlnStockData();
        BitBayDashStockData bitBayDashPlnStockData = new BitBayDataService().getDashPlnStockData();
        BitBayEthStockData bitBayEthPlnStockData = new BitBayDataService().getEthPlnStockData();
        KrakenBtcStockData krakenBtcEurStockData = new KrakenStockDataService().getKrakenBtcEurStockData();
        KrakenBccStockData krakenBccEurStockData = new KrakenStockDataService().getKrakenBccEurStockData();
        KrakenLtcStockData krakenLtcEurStockData = new KrakenStockDataService().getKrakenLtcEurStockData();
        KrakenEthStockData krakenEthEurStockData = new KrakenStockDataService().getEthEurStockData();
        KrakenDashStockData krakenDashEurStockData = new KrakenStockDataService().getKrakenDashEurStockData();
        BitstampBtcStockData bitstampBtcEurStockData = new BitstampStockDataService().getBitstampBtcEurStockData();
        BitstampLtcStockData bitstampLtcEurStockData = new BitstampStockDataService().getBitstampLtcEurStockData();
        BitstampEthStockData bitstampEthEurStockData = new BitstampStockDataService().getBitstampEthEurStockData();
        CoinroomEthStockStockData coinroomEthPlnStockData = new CoinroomStockDataService().getEthPlnStockData();
        CoinroomBtcStockStockData coinroomBtcPlnStockData = new CoinroomStockDataService().getBtcPlnStockData();
        CoinroomLtcStockStockData coinroomLtcPlnStockData = new CoinroomStockDataService().getLtcEurStockData();
        CoinroomDashStockStockData coinroomDashPlnStockData = new CoinroomStockDataService().getDashPlnStockData();
        CoinroomBccStockStockData coinroomBccPlnStockData = new CoinroomStockDataService().getBccPlnStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);

            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);

                Indicators.bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = new BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);


                Indicators.bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = new BitstampStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null) {
                if (krakenBtcEurStockData != null) {
                    Indicators.euroBuyToKrakenBtcSellOnBitBayRoi = new KrakenStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                    Indicators.euroBuyToKrakenLtcSellOnBitBayRoi = new KrakenStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                }
                if (bitstampBtcEurStockData != null) {
                    Indicators.euroBuyToBitstampBtcSellOnBitBayRoi = new BitstampStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                    Indicators.euroBuyToBitstampLtcSellOnBitBayRoi = new BitstampStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                }
            }
            if (krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                if (bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Indicators.bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
            if (bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Indicators.bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
        }
        if (lastBuyWalutomatEurPlnExchangeRate != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
            Indicators.euroBuyToExternalStockBccSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }

        if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(bitBayEthPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                 KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi;
            }
        }
        if (krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = new KrakenStockDataService()
                    .prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayBtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi;
            }
        }

        if (bitBayLtcPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            BigDecimal bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            Indicators.bitBayDashBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService()
                    .prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            BigDecimal bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(bitBayEthPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData,
                                                                                     KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            BigDecimal bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData,
                                                                                    KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayEthBuyToToKrakenEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi;
            }
            Indicators.bitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            BigDecimal bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = new BitstampStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData,
                                                                                    BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            if (bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenEthEurStockData != null) {
            Indicators.euroBuyToKrakenEthSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, krakenEthEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && bitstampEthEurStockData != null) {
            Indicators.euroBuyToBitstampEthSellOnBitBayRoi = new BitstampStockDataService()
                    .prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, bitstampEthEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && coinroomEthPlnStockData != null) {
            Indicators.bitBayEthBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayEthBuyToExternalStockPlnSellRoi(bitBayEthPlnStockData, coinroomEthPlnStockData);
        }
        if (bitBayBtcPlnStockData != null && coinroomBtcPlnStockData != null) {
            Indicators.bitBayBtcBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayBtcBuyToExternalStockPlnSellRoi(bitBayBtcPlnStockData, coinroomBtcPlnStockData);
        }
        if (bitBayLtcPlnStockData != null && coinroomLtcPlnStockData != null) {
            Indicators.bitBayLtcBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayLtcBuyToExternalStockPlnSellRoi(bitBayLtcPlnStockData, coinroomLtcPlnStockData);
        }
        if (bitBayDashPlnStockData != null && coinroomDashPlnStockData != null) {
            Indicators.bitBayDashBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayDashBuyToExternalStockPlnSellRoi(bitBayDashPlnStockData, coinroomDashPlnStockData);
        }
        if (bitBayDashPlnStockData != null && coinroomBccPlnStockData != null) {
            Indicators.bitBayBccBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayBccBuyToExternalStockPlnSellRoi(bitBayBccPlnStockData, coinroomBccPlnStockData);
        }
    }

}
