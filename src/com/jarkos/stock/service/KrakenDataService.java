package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.kraken.KrakenStockData;
import com.jarkos.walutomat.WalutomatData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.stock.StockDataPreparer.*;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class KrakenDataService extends AbstractDataService {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";

    public static KrakenStockData getKrakenBtcEurStockData() {
        String resKraken = sendRequest(KrakenBtcEurApiUrl);
        return getKrakenMarketData(resKraken);
    }

    public static KrakenStockData getKrakenLtcEurStockData() {
        String resKraken = sendRequest(KrakenLtcEurApiUrl);
        return getKrakenMarketData(resKraken);
    }

    private static KrakenStockData getKrakenMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, KrakenStockData.class);
    }

    @Override
    public KrakenStockData getLtcEurStockData() {
        KrakenStockData krakenLtcEurStockData = getKrakenLtcEurStockData();
        return krakenLtcEurStockData;
    }

    @Override
    public String getStockCodeName() {
        return "Kraken";
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal numberOfBtcBoughtAfterTradeProv) {
        return numberOfBtcBoughtAfterTradeProv.subtract(KRAKEN_BTC_WITHDRAW_PROV);
    }

    //    TODO make abstract
    public BigDecimal prepareEurBuyBtcSellRoi(BitBayStockData bitBayBtcPlnStockData, KrakenStockData krakenBtcEurStockData, WalutomatData walutomatEurPlnData) {
        // WALUTOMAT
        BigDecimal eurPlnExchangeRate = BigDecimal.valueOf(Float.valueOf(walutomatEurPlnData.getAvg()));
        BigDecimal numberOfEurAfterExchange = MONEY_TO_EUR_BUY.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEurExchangedAfterProv = numberOfEurAfterExchange.subtract(WALUTOMAT_WITHDRAW_RATIO);
        // KRAKEN
        BigDecimal numberOfBtcBoughtOnKraken = numberOfEurExchangedAfterProv.divide(BigDecimal.valueOf(krakenBtcEurStockData.getLastBtcPrice()), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtOnKrakenAfterProv = numberOfBtcBoughtOnKraken.subtract(numberOfBtcBoughtOnKraken.multiply(KRAKEN_MAKER_TRADE_PROV));
        BigDecimal numberOfBtcWithdrawAfterProv = numberOfBtcBoughtOnKrakenAfterProv.subtract(KRAKEN_BTC_WITHDRAW_PROV);
        // BITBAY
        BigDecimal plnMoneyAfterBtcExchange = numberOfBtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        BigDecimal plnMoneyBtcExchangedAfterProv = plnMoneyAfterBtcExchange.subtract(plnMoneyAfterBtcExchange.multiply(BIT_BAY_TRADE_PROVISION));

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        System.err.println("ROI EUR Walutomat -> Kraken -> Bitbay PLN: " + eurBuyAndBtcSellRoi);
        return eurBuyAndBtcSellRoi;
    }
}
