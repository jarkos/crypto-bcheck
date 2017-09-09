package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.huobi.HuobiStockData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.stock.StockDataPreparer.*;


/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class HuobiDataService extends AbstractDataSerivce {

    private static String HuobiBtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_btc_json.js";
    private static String HuobiLtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_ltc_json.js";

    public static HuobiStockData getHuobiBtcCnyStockData() {
        String resHuobi = RequestSender.sendRequest(HuobiBtcCnyApiUrl);
        return getHuobiMarketData(resHuobi);
    }

    public static HuobiStockData getHuobiLtcCnyStockData() {
        String resHuobi = RequestSender.sendRequest(HuobiLtcCnyApiUrl);
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
}
