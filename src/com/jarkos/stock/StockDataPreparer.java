package com.jarkos.stock;

import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampStockData;
import com.jarkos.stock.dto.huobi.HuobiStockData;
import com.jarkos.stock.dto.kraken.KrakenStockData;
import com.jarkos.stock.service.*;
import com.jarkos.walutomat.WalutomatData;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
public class StockDataPreparer {
    private static final Logger logger = Logger.getLogger(StockDataPreparer.class);

    private static String ConmarketcapBtcURL = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
    //TODO wynies do configu
    public static final BigDecimal BTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal LTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal MONEY_TO_EUR_BUY = BigDecimal.valueOf(1000F);

    public static final BigDecimal BITBAY_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0035F);
    public static final BigDecimal BITBAY_BTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00045F);
    public static final BigDecimal BITBAY_LTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);

    public static final BigDecimal BITSTAMP_WITHDRAW_PROV = BigDecimal.valueOf(0F);
    public static final BigDecimal BITSTAMP_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0025F);

    public static final BigDecimal HUOBI_TRADE_PROVISION = BigDecimal.valueOf(0.002F);
    public static final BigDecimal HUOBI_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0001F);

    public static final BigDecimal KRAKEN_MAKER_TRADE_PROV = BigDecimal.valueOf(0.0016F);
    public static final BigDecimal KRAKEN_BTC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_LTC_WITHDRAW_PROV = BigDecimal.valueOf(0.02F);
    public static final BigDecimal KRAKEN_BTC_TO_EUR_TAKER_PROV_PERCENTAGE = BigDecimal.valueOf(0.0026F);
    public static final BigDecimal KRAKEN_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal WALUTOMAT_WITHDRAW_RATIO = BigDecimal.valueOf(0.998F);
    public static final BigDecimal ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT = BigDecimal.valueOf(5.0F);

    public void fetchAndPrintStockData() throws Exception {
        BitBayStockData bitBayBtcPlnStockData = new BitBayDataService().getBtcPlnStockData();
        BitBayStockData bitBayLtcPlnStockData = new BitBayDataService().getLtcPlnStockData();
        HuobiStockData huobiBtcCnyStockData = new HuobiDataService().getHuobiBtcCnyStockData();
        KrakenStockData krakenBtcEurStockData = new KrakenDataService().getKrakenBtcEurStockData();
        KrakenStockData krakenLtcEurStockData = new KrakenDataService().getKrakenLtcEurStockData();
        BitstampStockData bitstampBtcEurStockData = new BitstampDataService().getBitstampBtcEurStockData();
        BitstampStockData bitstampLtcEurStockData = new BitstampDataService().getBitstampLtcEurStockData();
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
                new KrakenDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, krakenLtcEurStockData, walutomatEurPlnData, KRAKEN_MAKER_TRADE_PROV);
            }
            if (walutomatEurPlnData != null && bitstampBtcEurStockData != null) {
                Main.lastBitstampEurToBtcRoi = new BitstampDataService()
                        .prepareEuroBuyBtcSellOnBitBayRoi(bitBayBtcPlnStockData, bitstampBtcEurStockData, walutomatEurPlnData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
                new BitstampDataService()
                        .prepareEuroBuyLtcSellOnBitBayRoi(bitBayLtcPlnStockData, bitstampLtcEurStockData, walutomatEurPlnData, BITSTAMP_TRADE_PROVISION_PERCENTAGE);
            }
            //            if (krakenBtcEurData != null && walutomatEurPlnData != null) {
            //                prepareRoiBBtoKraken(bitBayBtcPlnStockData, krakenBtcEurData, walutomatEurPlnData);
            //                printRoiBitstamp(bitBayBtcPlnStockData, walutomatEurPlnData);
            //            }
        }
    }

    //    TODO uporzadkowac PLN to EUR
    private void printRoiBitstamp(BitBayStockData bitBayStockData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitBayStockData.getAsk();
        BitstampStockData bitstampStockData = new BitstampDataService().getBitstampBtcEurStockData();
        Float bitstampKursBtcEurRate = Float.valueOf(bitstampStockData.getLast());
        System.out.println("Dif BB vs Bitstamp: " + (((bitstampKursBtcEurRate) * Float.valueOf(walutomatEurPlnData.getAvg())) - sellBitBayLowest) + " PLN");
    }

    private String prepareRoiBBtoKraken(BitBayStockData bitBayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        return countRoiBBtoKraken(bitBayStockData, krakenBtcEurData, walutomatEurPlnData);
    }

    private String countRoiBBtoKraken(BitBayStockData bitBayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitBayStockData.getAsk();
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
