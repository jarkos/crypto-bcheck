package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.dto.kraken.KrakenStockData;

import java.math.BigDecimal;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.stock.StockDataPreparer.KRAKEN_BTC_WITHDRAW_PROV;

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

}
