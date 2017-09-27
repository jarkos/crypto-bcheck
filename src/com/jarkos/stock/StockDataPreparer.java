package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.*;
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

import static com.jarkos.config.IndicatorsSystemConfig.*;

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
                new KrakenDataService().prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastKrakenLtcToBitbayBtcRoi = bitBayLtcBuyAndBtcSellRoi;
                }
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                new BitstampDataService()
                        .prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new BitstampDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitstampLtcToBitbayBtcRoi = bitBayLtcBuyAndBtcSellRoi;
                }
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (lastBuyWalutomatEurPlnExchangeRate != null && krakenBtcEurStockData != null) {
                Main.lastKrakenEurToBtcRoi = new KrakenDataService()
                        .prepareEuroBuyBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                Main.lastKrakenEurToLtcRoi = new KrakenDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (lastBuyWalutomatEurPlnExchangeRate != null && bitstampBtcEurStockData != null) {
                Main.lastBitstampEurToBtcRoi = new BitstampDataService()
                        .prepareEuroBuyBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                Main.lastBitstampEurToLtcRoi = new BitstampDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            if (lastBuyWalutomatEurPlnExchangeRate != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
                new KrakenDataService().prepareEuroBuyBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }

            if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBccSellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
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

            if (krakenLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyKrakenEurSellLtcBuyToBitBayPlnRoi = new KrakenDataService()
                        .prepareBitBayBtcBuyAndLtcSellRoi(bitBayBtcPlnStockData, krakenLtcEurStockData, bitBayLtcPlnStockData, KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
                if (bitBayBtcBuyKrakenEurSellLtcBuyToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitBayBtcBuyKrakenEurSellLtcBuyToBitBayPlnRoi = bitBayBtcBuyKrakenEurSellLtcBuyToBitBayPlnRoi;
                }
            }
            if (bitstampLtcEurStockData != null && bitBayLtcPlnStockData != null) {
                final BigDecimal bitBayBtcBuyBitstampEurSellLtcBuyToBitBayPlnRoi = new BitstampDataService()
                        .prepareBitBayBtcBuyAndLtcSellRoi(bitBayBtcPlnStockData, bitstampLtcEurStockData, bitBayLtcPlnStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayBtcBuyBitstampEurSellLtcBuyToBitBayPlnRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitBayBtcBuyBitstampEurSellLtcBuyToBitBayPlnRoi = bitBayBtcBuyBitstampEurSellLtcBuyToBitBayPlnRoi;
                }
            }
            if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayDashPlnStockData != null) {
                new KrakenDataService().prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(bitBayLtcPlnStockData, krakenDashEurStockData, bitBayDashPlnStockData,
                                                                                            KRAKEN_MAKER_TRADE_PROV_PERCENTAGE);
            }
            if (bitBayEthPlnStockData != null && krakenDashEurStockData != null && bitBayDashPlnStockData != null) {
                new KrakenDataService()
                        .prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(bitBayDashPlnStockData, krakenDashEurStockData, KRAKEN_TAKER_TRADE_PROV_PERCENTAGE);
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

}
