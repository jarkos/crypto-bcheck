package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.bitstamp.BitstampStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.stock.StockDataPreparer.BITSTAMP_WITHDRAW_PROV;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitstampDataService extends AbstractDataService {

    private static String BitstampLtcCnyApiUrl = "https://www.bitstamp.net/api/v2/ticker/ltceur";
    private static String BitstampBtcCnyApiUrl = "https://www.bitstamp.net/api/v2/ticker/btceur";

    @Override
    public AbstractStockData getLtcEurStockData() {
        return getBitstampLtcCnyStockData();
    }

    public BitstampStockData getBitstampLtcCnyStockData() {
        String resBitstamp = null;
        try {
            resBitstamp = RequestSender.sendRequest(BitstampLtcCnyApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.printf(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitstampMarketData(resBitstamp);
    }

    private static BitstampStockData getBitstampMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitstampStockData.class);
    }

    public BitstampStockData getBitstampBtcEurStockData() {
        String resBitstamp = null;
        try {
            resBitstamp = RequestSender.sendRequest(BitstampBtcCnyApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.printf(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitstampMarketData(resBitstamp);
    }

    @Override
    public String getStockCodeName() {
        return "Bitstamp";
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractTradeProv) {
        // NON FEE
        return btcToSubtractTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }
}
