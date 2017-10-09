package com.jarkos.stock;

import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.service.BitBayStockDataService;
import com.jarkos.stock.service.BitstampStockDataService;
import com.jarkos.stock.service.CoinroomStockDataService;
import com.jarkos.stock.service.KrakenStockDataService;
import com.jarkos.walutomat.WalutomatDataService;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;
import static com.jarkos.config.StockConfig.KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;

public class StockRoiPreparer {

    public static BigDecimal lastBuyWalutomatEurPlnExchangeRate;
    public static BigDecimal lastSellWalutomatEurPlnExchangeRate;

    //TODO refactor - to complex
    public void fetchAndPrintStockData() {
        lastBuyWalutomatEurPlnExchangeRate = new WalutomatDataService().getWalutomatEurToPlnData().getBuyExchangeRate();
        lastSellWalutomatEurPlnExchangeRate = new WalutomatDataService().getWalutomatEurToPlnData().getSellExchangeRate();

        BtcStockDataInterface bitBayBtcPlnStockData = new BitBayStockDataService().getBtcPlnStockData();
        LtcStockDataInterface bitBayLtcPlnStockData = new BitBayStockDataService().getLtcPlnStockData();
        BccStockDataInterface bitBayBccPlnStockData = new BitBayStockDataService().getBccPlnStockData();
        DashStockDataInterface bitBayDashPlnStockData = new BitBayStockDataService().getDashPlnStockData();
        EthStockDataInterface bitBayEthPlnStockData = new BitBayStockDataService().getEthPlnStockData();
        BtcStockDataInterface krakenBtcEurStockData = new KrakenStockDataService().getBtcEurStockData();
        BccStockDataInterface krakenBccEurStockData = new KrakenStockDataService().getBccEurStockData();
        LtcStockDataInterface krakenLtcEurStockData = new KrakenStockDataService().getLtcEurStockData();
        EthStockDataInterface krakenEthEurStockData = new KrakenStockDataService().getEthEurStockData();
        DashStockDataInterface krakenDashEurStockData = new KrakenStockDataService().getDashEurStockData();
        BtcStockDataInterface bitstampBtcEurStockData = new BitstampStockDataService().getBtcEurStockData();
        LtcStockDataInterface bitstampLtcEurStockData = new BitstampStockDataService().getLtcEurStockData();
        EthStockDataInterface bitstampEthEurStockData = new BitstampStockDataService().getEthEurStockData();
        EthStockDataInterface coinroomEthPlnStockData = new CoinroomStockDataService().getEthPlnStockData();
        BtcStockDataInterface coinroomBtcPlnStockData = new CoinroomStockDataService().getBtcPlnStockData();
        LtcStockDataInterface coinroomLtcPlnStockData = new CoinroomStockDataService().getLtcPlnStockData();
        DashStockDataInterface coinroomDashPlnStockData = new CoinroomStockDataService().getDashPlnStockData();
        BccStockDataInterface coinroomBccPlnStockData = new CoinroomStockDataService().getBccPlnStockData();

        if (bitBayBtcPlnStockData != null) {
            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);

                Indicators.bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Indicators.bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = new BitstampStockDataService().prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);


                Indicators.bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = new BitstampStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null) {
                if (krakenBtcEurStockData != null) {
                    Indicators.euroBuyToKrakenBtcSellOnBitBayRoi = new KrakenStockDataService().prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                    Indicators.euroBuyToKrakenLtcSellOnBitBayRoi = new KrakenStockDataService().prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                }
                if (bitstampBtcEurStockData != null) {
                    Indicators.euroBuyToBitstampBtcSellOnBitBayRoi = new BitstampStockDataService().prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                    Indicators.euroBuyToBitstampLtcSellOnBitBayRoi = new BitstampStockDataService().prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
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
            Indicators.euroBuyToExternalStockBccSellOnBitBayRoi = new KrakenStockDataService().prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
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
            BigDecimal bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = new KrakenStockDataService().prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Indicators.bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            Indicators.bitBayDashBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService().prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
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
            Indicators.bitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi = new KrakenStockDataService().prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
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
            Indicators.euroBuyToKrakenEthSellOnBitBayRoi = new KrakenStockDataService().prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, krakenEthEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && bitstampEthEurStockData != null) {
            Indicators.euroBuyToBitstampEthSellOnBitBayRoi = new BitstampStockDataService().prepareEuroBuyToExternalStockEthSellOnBitBayRoi(bitBayEthPlnStockData, bitstampEthEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
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
            Indicators.bitBayDashBuyToCoinroomPlnSell = new CoinroomStockDataService()
                    .prepareBitBayDashBuyToExternalStockPlnSellRoi(bitBayDashPlnStockData, coinroomDashPlnStockData);
        }
        if (bitBayDashPlnStockData != null && coinroomBccPlnStockData != null) {
            Indicators.bitBayBccBuyToCoinroomPlnSell = new CoinroomStockDataService().prepareBitBayBccBuyToExternalStockPlnSellRoi(bitBayBccPlnStockData, coinroomBccPlnStockData);
        }
        if (bitBayEthPlnStockData != null && krakenEthEurStockData != null) {
            Indicators.prepareBitBayEthBuyToExternalStockSellToEuroWithdrawal = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellToEuroWithdrawalRoi(bitBayEthPlnStockData, krakenEthEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
    }

}
