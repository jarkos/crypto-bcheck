package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.dto.huobi.HuobiBtcStockData;
import com.jarkos.stock.dto.huobi.HuobiLtcStockData;
import com.jarkos.stock.dto.huobi.general.HuobiStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.config.IndicatorsSystemConfig.HUOBI_WITHDRAW_PROV_AMOUNT;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class HuobiDataService extends AbstractDataService {

    private static String HuobiBtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_btc_json.js";
    private static String HuobiLtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_ltc_json.js";

    public HuobiBtcStockData getHuobiBtcCnyStockData() {
        String resHuobi = null;
        try {
            resHuobi = RequestSender.sendRequest(HuobiBtcCnyApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new HuobiBtcStockData(getHuobiMarketData(resHuobi));
    }

    public HuobiLtcStockData getHuobiLtcCnyStockData() {
        String resHuobi = null;
        try {
            resHuobi = RequestSender.sendRequest(HuobiLtcCnyApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new HuobiLtcStockData(getHuobiMarketData(resHuobi));
    }

    private static HuobiStockData getHuobiMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, HuobiStockData.class);
    }

    @Override
    public HuobiLtcStockData getLtcEurStockData() {
        return getHuobiLtcCnyStockData();
    }

    @Override
    public String getStockCodeName() {
        return "Huobi";
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(HUOBI_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(HUOBI_WITHDRAW_PROV_AMOUNT);
    }
}
