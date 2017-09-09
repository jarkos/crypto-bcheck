package com.jarkos.stock;

import com.google.gson.Gson;
import com.jarkos.Main;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.bitstamp.BitstampStockData;
import com.jarkos.stock.dto.huobi.HuobiStockData;
import com.jarkos.stock.dto.kraken.KrakenStockData;
import com.jarkos.stock.service.BitBayDataService;
import com.jarkos.stock.service.HuobiDataService;
import com.jarkos.stock.service.KrakenDataService;
import com.jarkos.walutomat.WalutomatData;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static com.jarkos.RequestSender.sendRequest;

/**
 * Created by jkostrzewa on 2017-09-02.
 */
public class StockDataPreparer {

    private static String BitstampURL = "https://www.bitstamp.net/api/v2/ticker/btceur/";
    private static String ConmarketcapBtcURL = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";

    public static final Float BTC_BUY_MONEY = 10000F;
    public static final BigDecimal LTC_BUY_MONEY = BigDecimal.valueOf(1000F);

    public static final Float BIT_BAY_TRADE_PROVISION = 0.0035F;
    public static final Float BIT_BAY_BTC_WITHDRAW_PROV = 0.00045F;
    public static final BigDecimal BIT_BAY_LTC_WITHDRAW_PROV = BigDecimal.valueOf(0.005F);

    public static final BigDecimal HUOBI_TRADE_PROVISION = BigDecimal.valueOf(0.002F);
    public static final BigDecimal HUOBI_WITHDRAW_PROV = BigDecimal.valueOf(0.005F);

    public static final BigDecimal KRAKEN_MAKER_TRADE_PROV = BigDecimal.valueOf(0.0016F);
    public static final BigDecimal KRAKEN_BTC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final Float KRAKEN_BTC_TO_EUR_PROV = 0.0026F;
    public static final Float KRAKEN_EUR_WITHDRAW_PROV = 0.09F;

    public static final Float WALUTOMAT_WITHDRAW_RATIO = 0.998F;

    public void fetchAndPrintStockData() throws Exception {
        BitBayStockData bitBayBtcPlnStockData = BitBayDataService.getBtcPlnStockData();
        BitBayStockData bitBayLtcPlnStockData = BitBayDataService.getLtcPlnStockData();
        HuobiStockData huobiBtcCnyStockData = HuobiDataService.getHuobiBtcCnyStockData();
        KrakenStockData krakenBtcEurStockData = KrakenDataService.getKrakenBtcEurStockData();
        //        WalutomatData walutomatEurPlnData = WalutomatDataService.getWalutomatEurToPlnData();
        //
        //        KrakenStockData krakenBtcEurData = KrakenDataService.getKrakenBtcEurStockData();

        if (bitBayBtcPlnStockData != null) {
            BitBayDataService.addNewBitBayTransactionsToCSV(bitBayBtcPlnStockData);

            if (huobiBtcCnyStockData != null && bitBayLtcPlnStockData != null) {
                Main.lastHuobiRoiLTC = new HuobiDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, huobiBtcCnyStockData, bitBayBtcPlnStockData, HUOBI_TRADE_PROVISION, HUOBI_WITHDRAW_PROV);
            }
            if (krakenBtcEurStockData != null && bitBayLtcPlnStockData != null) {
                Main.lastKrakenRoiLTC = new KrakenDataService()
                        .prepareBitBayLtcBuyAndBtcSellRoi(bitBayLtcPlnStockData, krakenBtcEurStockData, bitBayBtcPlnStockData, KRAKEN_MAKER_TRADE_PROV,
                                                          KRAKEN_BTC_WITHDRAW_PROV);
            }

            //            if (krakenBtcEurData != null && walutomatEurPlnData != null) {
            //                prepareRoiBBtoKraken(bitBayBtcPlnStockData, krakenBtcEurData, walutomatEurPlnData);
            //                printRoiBitstamp(bitBayBtcPlnStockData, walutomatEurPlnData);
            //            }
        }
    }

    private void printRoiBitstamp(BitBayStockData bitBayStockData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitBayStockData.getAsk();
        String resBitstamp = sendRequest(BitstampURL);
        BitstampStockData bitstampStockData = getBitstampData(resBitstamp);
        Float bitstampKursBtcEurRate = Float.valueOf(bitstampStockData.getLast());
        System.out.println("Dif BB vs Bitstamp: " + (((bitstampKursBtcEurRate) * Float.valueOf(walutomatEurPlnData.getAvg())) - sellBitBayLowest) + " PLN");
    }

    private String prepareRoiBBtoKraken(BitBayStockData bitBayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        return countRoiBBtoKraken(bitBayStockData, krakenBtcEurData, walutomatEurPlnData);
    }

    private String countRoiBBtoKraken(BitBayStockData bitBayStockData, KrakenStockData krakenBtcEurData, WalutomatData walutomatEurPlnData) {
        Float sellBitBayLowest = bitBayStockData.getAsk();
        Float btcCanBeBought = BTC_BUY_MONEY / sellBitBayLowest;
        Float btcBoughtAfterExchangeProvisionPLNtoBTCFromBB = btcCanBeBought - (btcCanBeBought * BIT_BAY_TRADE_PROVISION);
        Float btcAfterWidthdrawProvisionFromBB = btcBoughtAfterExchangeProvisionPLNtoBTCFromBB - BIT_BAY_BTC_WITHDRAW_PROV;
        //        Doliczyć fee ponmiedzy portfelami?

        //        KRAKEN
        Float krakenExchangeBtcEurRate = Float.valueOf(krakenBtcEurData.getResult().getXXBTZEUR().getLastTradePrice().get(0));
        Float krakenBalanceInEuroAfterSell = krakenExchangeBtcEurRate * btcAfterWidthdrawProvisionFromBB;
        Float krakenBalanceInEuroAfterSellAfterExchangeProvBTCtoEUR = krakenBalanceInEuroAfterSell - (krakenExchangeBtcEurRate * KRAKEN_BTC_TO_EUR_PROV);
        Float moneyExchangedToEURAfterWirdhtrawProv = krakenBalanceInEuroAfterSellAfterExchangeProvBTCtoEUR - KRAKEN_EUR_WITHDRAW_PROV;
        Float moneyAfterExchange = moneyExchangedToEURAfterWirdhtrawProv * Float.valueOf(walutomatEurPlnData.getAvg());
        Float moneyAfterExchangeWalutomatProv = moneyAfterExchange * WALUTOMAT_WITHDRAW_RATIO;

        String result = printKrakenRoiResult(walutomatEurPlnData, sellBitBayLowest, krakenExchangeBtcEurRate, moneyAfterExchangeWalutomatProv);
        System.out.println(result);
        return result;
    }

    private String printKrakenRoiResult(WalutomatData walutomatEurPlnData, Float sellBitBayLowest, Float krakenExchangeBtcEurRate, Float moneyAfterExchangeWalutomatProv) {
        //        DISPLAY
        Float roi = ((moneyAfterExchangeWalutomatProv - BTC_BUY_MONEY) / BTC_BUY_MONEY) * 100;

        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();

        System.out.println("Time: " + currentTime.toString());
        System.out.println("Walutomat currency: " + walutomatEurPlnData.getAvg());
        System.out.println("Dif BB vs Kraken: " + (((krakenExchangeBtcEurRate) * Float.valueOf(walutomatEurPlnData.getAvg())) - sellBitBayLowest) + " PLN");
        System.err.println("Zarobek: : " + getZarobek(roi) + " PLN");
        String roiValueToDispaly = String.format("%.2f", roi);
        System.err.println("ROI: : " + roiValueToDispaly + "%");

        //        RESULT
        return "" + currentTime.toString() + "   " + BTC_BUY_MONEY + "   " + moneyAfterExchangeWalutomatProv + "   " + roiValueToDispaly + "   " + getZarobek(roi) + "   " +
               walutomatEurPlnData.getAvg();
    }

    private float getZarobek(Float roi) {

        if (roi < 0F) {
            return -(BTC_BUY_MONEY * Math.abs(roi / 100F));
        }
        return BTC_BUY_MONEY * roi / 100F;
    }

    private BitstampStockData getBitstampData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitstampStockData.class);
    }
}
