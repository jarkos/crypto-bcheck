package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.dto.kraken.KrakenStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.stock.StockDataPreparer.KRAKEN_BTC_WITHDRAW_PROV;
import static com.jarkos.stock.StockDataPreparer.KRAKEN_LTC_WITHDRAW_PROV;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class KrakenDataService extends AbstractDataService {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";

    public KrakenStockData getKrakenBtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenBtcEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getKrakenMarketData(resKraken);
    }

    public KrakenStockData getKrakenLtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenLtcEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
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
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(KRAKEN_BTC_WITHDRAW_PROV);
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(KRAKEN_LTC_WITHDRAW_PROV);
    }

}
