package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampBccStockData;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.kraken.KrakenBccStockData;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenDashStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.service.BitBayDataService;
import com.jarkos.stock.service.BitstampStockDataService;
import com.jarkos.stock.service.KrakenStockDataService;
import com.jarkos.stock.service.WalutomatDataService;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;
import static com.jarkos.config.StockConfig.KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
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
        BitBayStockData bitBayLiskPlnStockData = new BitBayDataService().getLiskPlnStockData();
        KrakenBtcStockData krakenBtcEurStockData = new KrakenStockDataService().getKrakenBtcEurStockData();
        KrakenBccStockData krakenBccEurStockData = new KrakenStockDataService().getKrakenBccEurStockData();
        KrakenLtcStockData krakenLtcEurStockData = new KrakenStockDataService().getKrakenLtcEurStockData();
        KrakenDashStockData krakenDashEurStockData = new KrakenStockDataService().getKrakenDashEurStockData();
        BitstampBtcStockData bitstampBtcEurStockData = new BitstampStockDataService().getBitstampBtcEurStockData();
        BitstampLtcStockData bitstampLtcEurStockData = new BitstampStockDataService().getBitstampLtcEurStockData();
        BitBayEthStockData bitBayEthPlnStockData = new BitBayDataService().getEthPlnStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);

            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);

                Main.bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = new KrakenStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = new BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);


                Main.bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = new BitstampStockDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null) {
                if (krakenBtcEurStockData != null) {
                    Main.euroBuyToKrakenBtcSellOnBitBayRoi = new KrakenStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                    Main.euroBuyToKrakenLtcSellOnBitBayRoi = new KrakenStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                }
                if (bitstampBtcEurStockData != null) {
                    Main.euroBuyToBitstampBtcSellOnBitBayRoi = new BitstampStockDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                    Main.euroBuyToBitstampLtcSellOnBitBayRoi = new BitstampStockDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                }
            }
            if (krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new KrakenStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                if (bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
            if (bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new BitstampStockDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
        }
        if (lastBuyWalutomatEurPlnExchangeRate != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
            Main.euroBuyToExternalStockBccSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }

        if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(bitBayEthPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                 KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi;
            }
        }
        if (krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = new KrakenStockDataService()
                    .prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayBtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData,
                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi;
            }
        }

        if (bitBayLtcPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            BigDecimal bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            Main.bitBayDashBuyToKrakenSellToEuroWithdrawalRoi = new KrakenStockDataService()
                    .prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            BigDecimal bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(bitBayEthPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData,
                                                                                     KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            BigDecimal bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi = new KrakenStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayEthBuyToToKrakenEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToKrakenEuroSellToLtcSellOnBitBayRoi;
            }
            Main.bitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi = new KrakenStockDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            BigDecimal bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = new BitstampStockDataService()
                    .prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(bitBayEthPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData,
                                                                                    BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            if (bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi;
            }
        }
    }

}
