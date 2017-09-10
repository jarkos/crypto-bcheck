package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.huobi.HuobiStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.stock.StockDataPreparer.HUOBI_WITHDRAW_PROV;


/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class HuobiDataService extends AbstractDataService {

    private static String HuobiBtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_btc_json.js";
    private static String HuobiLtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_ltc_json.js";

    public HuobiStockData getHuobiBtcCnyStockData() {
        String resHuobi = null;
        try {
            resHuobi = RequestSender.sendRequest(HuobiBtcCnyApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getHuobiMarketData(resHuobi);
    }

    public HuobiStockData getHuobiLtcCnyStockData() {
        String resHuobi = null;
        try {
            resHuobi = RequestSender.sendRequest(HuobiLtcCnyApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getHuobiMarketData(resHuobi);
    }

    private static HuobiStockData getHuobiMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, HuobiStockData.class);
    }

    @Override
    public AbstractStockData getLtcEurStockData() {
        return getHuobiLtcCnyStockData();
    }

    @Override
    public String getStockCodeName() {
        return "Huobi";
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractTradeProv) {
        return btcToSubtractTradeProv.subtract(HUOBI_WITHDRAW_PROV);
    }
}
