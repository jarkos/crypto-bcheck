package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.BitbayBccStockData;
import com.jarkos.stock.dto.bitbay.BitbayBtcStockData;
import com.jarkos.stock.dto.bitbay.BitbayEthStockData;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.kraken.KrakenBccStockData;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.service.BitBayDataService;
import com.jarkos.stock.service.BitstampDataService;
import com.jarkos.stock.service.KrakenDataService;
import com.jarkos.stock.service.WalutomatDataService;
import com.jarkos.walutomat.WalutomatData;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

import static com.jarkos.config.IndicatorsSystemConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;
import static com.jarkos.config.IndicatorsSystemConfig.KRAKEN_MAKER_TRADE_PROV;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
public class StockDataPreparer {
    private static final Logger logger = Logger.getLogger(StockDataPreparer.class);

    public void fetchAndPrintStockData() {
        BitbayBtcStockData bitBayBtcPlnStockData = new BitBayDataService().getBtcPlnStockData();
        BitbayStockData bitBayLtcPlnStockData = new BitBayDataService().getLtcPlnStockData();
        BitbayBccStockData bitBayBccPlnStockData = new BitBayDataService().getBccPlnStockData();
        KrakenBtcStockData krakenBtcEurStockData = new KrakenDataService().getKrakenBtcEurStockData();
        KrakenBccStockData krakenBccEurStockData = new KrakenDataService().getKrakenBccEurStockData();
        KrakenLtcStockData krakenLtcEurStockData = new KrakenDataService().getKrakenLtcEurStockData();
        BitstampBtcStockData bitstampBtcEurStockData = new BitstampDataService().getBitstampBtcEurStockData();
        BitstampLtcStockData bitstampLtcEurStockData = new BitstampDataService().getBitstampLtcEurStockData();
        WalutomatData walutomatEurPlnData = new WalutomatDataService().getWalutomatEurToPlnData();
        BitbayEthStockData bitBayEthPlnStockData = new BitBayDataService().getEthPlnStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);
            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData, KRAKEN_MAKER_TRADE_PROV);
                if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastKrakenLtcToBitbayBtcRoi = bitBayLtcBuyAndBtcSellRoi;
                }
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new BitstampDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitstampLtcToBitbayBtcRoi = bitBayLtcBuyAndBtcSellRoi;
                }
            }
            // Eur on Walutomat -> External to BTC/LTC -> BTC/LTC sell on Bitbay
            if (walutomatEurPlnData != null && krakenBtcEurStockData != null) {
                Main.lastKrakenEurToBtcRoi = new KrakenDataService()
                        .prepareEuroBuyBtcSellOnBitBayRoi(bitBayBtcPlnStockData, krakenBtcEurStockData, walutomatEurPlnData, KRAKEN_MAKER_TRADE_PROV);
                Main.lastKrakenEurToLtcRoi = new KrakenDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, walutomatEurPlnData, KRAKEN_MAKER_TRADE_PROV);
            }
            if (walutomatEurPlnData != null && bitstampBtcEurStockData != null) {
                Main.lastBitstampEurToBtcRoi = new BitstampDataService()
                        .prepareEuroBuyBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, walutomatEurPlnData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                Main.lastBitstampEurToLtcRoi = new BitstampDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, walutomatEurPlnData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            if (walutomatEurPlnData != null && bitBayBccPlnStockData != null && krakenBccEurStockData != null) {
                new KrakenDataService().prepareEuroBuyBccSellOnBitBayRoi(bitBayBccPlnStockData, krakenBccEurStockData, walutomatEurPlnData, KRAKEN_MAKER_TRADE_PROV);
            }
            if (bitBayLtcPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
                //                Main.lastBitbayLtcToKrakenBccToBitbayPlnRoi;
                BigDecimal bitBayLtcBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBccSellRoi(bitBayLtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV);
                if (bitBayLtcBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitbayLtcToKrakenBccToBitbayPlnRoi = bitBayLtcBuyAndBtcSellRoi;
                }
            }
            if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
                BigDecimal bitBayEthBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayEthBuyAndBccSellRoi(bitBayEthPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV);
                if (bitBayEthBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitbayEthToKrakenBccToBitbayPlnRoi = bitBayEthBuyAndBtcSellRoi;
                }
            }
            if (bitBayEthPlnStockData != null && krakenBccEurStockData != null && bitBayBccPlnStockData != null) {
                BigDecimal bitBayEthBuyAndBtcSellRoi = new KrakenDataService()
                        .prepareBitBayBtcBuyAndBccSellRoi(bitBayBtcPlnStockData, krakenBccEurStockData, bitBayBccPlnStockData, KRAKEN_MAKER_TRADE_PROV);
                if (bitBayEthBuyAndBtcSellRoi.compareTo(BigDecimal.ZERO) > 0) {
                    Main.lastBitbayBtcToKrakenBccToBitbayPlnRoi = bitBayEthBuyAndBtcSellRoi;
                }
            }
        }
    }

}
