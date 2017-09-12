package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.general.BitbayBccStockData;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;
import com.jarkos.stock.dto.huobi.HuobiBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenBccStockData;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.service.*;
import com.jarkos.walutomat.WalutomatData;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

import static com.jarkos.config.IndicatorsSystemConfig.*;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
public class StockDataPreparer {
    private static final Logger logger = Logger.getLogger(StockDataPreparer.class);

    public void fetchAndPrintStockData() throws Exception {
        BitbayStockData bitBayBtcPlnStockData = new BitBayDataService().getBtcPlnStockData();
        BitbayStockData bitBayLtcPlnStockData = new BitBayDataService().getLtcPlnStockData();
        BitbayBccStockData bitBayBccPlnStockData = new BitBayDataService().getBccPlnStockData();
        HuobiBtcStockData huobiBtcCnyStockData = new HuobiDataService().getHuobiBtcCnyStockData();
        KrakenBtcStockData krakenBtcEurStockData = new KrakenDataService().getKrakenBtcEurStockData();
        KrakenBccStockData krakenBccEurStockData = new KrakenDataService().getKrakenBccEurStockData();
        KrakenLtcStockData krakenLtcEurStockData = new KrakenDataService().getKrakenLtcEurStockData();
        BitstampBtcStockData bitstampBtcEurStockData = new BitstampDataService().getBitstampBtcEurStockData();
        BitstampLtcStockData bitstampLtcEurStockData = new BitstampDataService().getBitstampLtcEurStockData();
        WalutomatData walutomatEurPlnData = new WalutomatDataService().getWalutomatEurToPlnData();
        //
        //        KrakenStockData krakenBtcEurData = KrakenDataService.getKrakenBtcEurStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);
            // LTC on Bitbay -> External LTC to BTC -> BTC sell on Bitbay
            if (huobiBtcCnyStockData != null && bitBayLtcPlnStockData != null) {
                Main.lastHuobiLtcToBitbayBtcRoi = new HuobiDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, huobiBtcCnyStockData, bitBayBtcPlnStockData, HUOBI_TRADE_PROVISION);
            }
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.lastKrakenLtcToBitbayBtcRoi = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData, KRAKEN_MAKER_TRADE_PROV);
            }
            if (bitstampBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.lastBitstampLtcToBitbayBtcRoi = new BitstampDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, bitstampBtcEurStockData, bitBayBtcPlnStockData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
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
            //            if (krakenBtcEurData != null && walutomatEurPlnData != null) {
            //                prepareRoiBBtoKraken(bitBayBtcPlnStockData, krakenBtcEurData, walutomatEurPlnData);
            //                printRoiBitstamp(bitBayBtcPlnStockData, walutomatEurPlnData);
            //            }
        }
    }

    //    TODO uporzadkowac PLN to EUR
    private void printRoiBitstamp(BitbayStockData bitbayStockData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitbayStockData.getAsk();
        BitstampStockData bitstampStockData = new BitstampDataService().getBitstampBtcEurStockData();
        Float bitstampKursBtcEurRate = Float.valueOf(bitstampStockData.getLast());
        System.out.println("Dif BB vs Bitstamp: " + (((bitstampKursBtcEurRate) * Float.valueOf(walutomatEurPlnData.getAvg())) - sellBitBayLowest) + " PLN");
    }

    private String prepareRoiBBtoKraken(BitbayStockData bitbayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        return countRoiBBtoKraken(bitbayStockData, krakenBtcEurData, walutomatEurPlnData);
    }

    private String countRoiBBtoKraken(BitbayStockData bitbayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitbayStockData.getAsk();
        Float btcCanBeBought = BTC_BUY_MONEY.floatValue() / sellBitBayLowest;
        Float btcBoughtAfterExchangeProvisionPLNtoBTCFromBB = btcCanBeBought - (btcCanBeBought * BITBAY_TRADE_PROVISION_PERCENTAGE.floatValue());
        Float btcAfterWithdrawProvisionFromBB = btcBoughtAfterExchangeProvisionPLNtoBTCFromBB - BITBAY_BTC_WITHDRAW_PROV_AMOUNT.floatValue();
        //        Doliczyć fee ponmiedzy portfelami?

        //        KRAKEN
        Float krakenExchangeBtcEurRate = Float.valueOf(krakenBtcEurData.getResult().getXXBTZEUR().getLastTradePrice().get(0));
        Float krakenBalanceInEuroAfterSell = krakenExchangeBtcEurRate * btcAfterWithdrawProvisionFromBB;
        Float krakenBalanceInEuroAfterSellAfterExchangeProvBTCtoEUR =
                krakenBalanceInEuroAfterSell - (krakenExchangeBtcEurRate * KRAKEN_BTC_TO_EUR_TAKER_PROV_PERCENTAGE.floatValue());
        Float moneyExchangedToEURAfterWirdhtrawProv = krakenBalanceInEuroAfterSellAfterExchangeProvBTCtoEUR - KRAKEN_EUR_WITHDRAW_PROV_AMOUNT.floatValue();
        Float moneyAfterExchange = moneyExchangedToEURAfterWirdhtrawProv * Float.valueOf(walutomatEurPlnData.getAvg());
        Float moneyAfterExchangeWalutomatProv = moneyAfterExchange * WALUTOMAT_WITHDRAW_RATIO.floatValue();

        String result = printKrakenRoiResult(walutomatEurPlnData, sellBitBayLowest, krakenExchangeBtcEurRate, moneyAfterExchangeWalutomatProv);
        System.out.println(result);
        return result;
    }

    private String printKrakenRoiResult(WalutomatData walutomatEurPlnData, Float sellBitBayLowest, Float krakenExchangeBtcEurRate, Float moneyAfterExchangeWalutomatProv) {
        //        DISPLAY
        Float roi = ((moneyAfterExchangeWalutomatProv - BTC_BUY_MONEY.floatValue()) / BTC_BUY_MONEY.floatValue()) * 100;

        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();

        System.out.println("Time: " + currentTime.toString());
        System.out.println("Walutomat currency: " + walutomatEurPlnData.getAvg());
        System.out.println("Dif BB vs Kraken: " + (((krakenExchangeBtcEurRate) * Float.valueOf(walutomatEurPlnData.getAvg())) - sellBitBayLowest) + " PLN");
        logger.info("Zarobek: : " + getZarobek(roi) + " PLN");
        String roiValueToDispaly = String.format("%.2f", roi);
        logger.info("ROI: : " + roiValueToDispaly + "%");

        //        RESULT
        return "" + currentTime.toString() + "   " + BTC_BUY_MONEY + "   " + moneyAfterExchangeWalutomatProv + "   " + roiValueToDispaly + "   " + getZarobek(roi) + "   " +
               walutomatEurPlnData.getAvg();
    }

    private float getZarobek(Float roi) {

        if (roi < 0F) {
            return -(BTC_BUY_MONEY.floatValue() * Math.abs(roi / 100F));
        }
        return BTC_BUY_MONEY.floatValue() * roi / 100F;
    }
}
