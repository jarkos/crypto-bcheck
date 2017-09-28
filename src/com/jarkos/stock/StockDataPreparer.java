package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.kraken.KrakenBccStockData;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenDashStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.service.BitBayDataService;
import com.jarkos.stock.service.BitstampDataService;
import com.jarkos.stock.service.KrakenDataService;
import com.jarkos.stock.service.WalutomatDataService;

import java.math.BigDecimal;

import static com.jarkos.config.IndicatorsSystemConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;
import static com.jarkos.config.IndicatorsSystemConfig.KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
public class StockDataPreparer {

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
        KrakenBtcStockData krakenBtcEurStockData = new KrakenDataService().getKrakenBtcEurStockData();
        KrakenBccStockData krakenBccEurStockData = new KrakenDataService().getKrakenBccEurStockData();
        KrakenLtcStockData krakenLtcEurStockData = new KrakenDataService().getKrakenLtcEurStockData();
        KrakenDashStockData krakenDashEurStockData = new KrakenDataService().getKrakenDashEurStockData();
        BitstampBtcStockData bitstampBtcEurStockData = new BitstampDataService().getBitstampBtcEurStockData();
        BitstampLtcStockData bitstampLtcEurStockData = new BitstampDataService().getBitstampLtcEurStockData();
        BitBayEthStockData bitBayEthPlnStockData = new BitBayDataService().getEthPlnStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);

            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = new KrakenDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);

                Main.bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = new BitstampDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);


                Main.bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = new BitstampDataService()
                        .prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData,
                                                                                  BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null) {
                if (krakenBtcEurStockData != null) {
                    Main.euroBuyToKrakenBtcSellOnBitBayRoi = new KrakenDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                    Main.euroBuyToKrakenLtcSellOnBitBayRoi = new KrakenDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                }
                if (bitstampBtcEurStockData != null) {
                    Main.euroBuyToBitstampBtcSellOnBitBayRoi = new BitstampDataService()
                            .prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                    Main.euroBuyToBitstampLtcSellOnBitBayRoi = new BitstampDataService()
                            .prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                }
            }
            if (krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new KrakenDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                if (bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
            if (bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = new BitstampDataService()
                        .prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(bitBayBtcPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData,
                                                                                                   BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi;
                }
            }
        }
        if (lastBuyWalutomatEurPlnExchangeRate != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
            Main.euroBuyToExternalStockBccSellOnBitBayRoi = new KrakenDataService()
                    .prepareEuroBuyToExternalStockBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }

        if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayLtcBuyAndBtcSellRoi = new KrakenDataService()
                    .prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.lastBitbayLtcToKrakenBccToBitbayPlnRoi = bitBayLtcBuyAndBtcSellRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayEthBuyAndBtcSellRoi = new KrakenDataService()
                    .prepareBitBayEthBuyAndBccSellRoi(bitBayEthPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.lastBitbayEthToKrakenBccToBitbayPlnRoi = bitBayEthBuyAndBtcSellRoi;
            }
        }
        if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
            BigDecimal bitBayEthBuyAndBtcSellRoi = new KrakenDataService()
                    .prepareBitBayBtcBuyAndBccSellRoi(bitBayBtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            if (bitBayEthBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                Main.lastBitbayBtcToKrakenBccToBitbayPlnRoi = bitBayEthBuyAndBtcSellRoi;
            }
        }

        if (bitBayLtcPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            new KrakenDataService()
                    .prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
            new KrakenDataService().prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            new KrakenDataService()
                    .prepareBitBayEthBuyToEuroToDashSellOnBitBayRoi(bitBayEthPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            new KrakenDataService()
                    .prepareBitBayEthBuyToEuroToLtcSellOnBitBayRoi(bitBayEthPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
        }
        if (bitBayEthPlnStockData != null && krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
            new BitstampDataService()
                    .prepareBitBayEthBuyToEuroToLtcSellOnBitBayRoi(bitBayEthPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
        }
    }

}
