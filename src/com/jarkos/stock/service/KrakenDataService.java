package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.IndicatorsSystemConfig.KRAKEN_BTC_WITHDRAW_PROV;
import static com.jarkos.config.IndicatorsSystemConfig.KRAKEN_LTC_WITHDRAW_PROV;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class KrakenDataService extends AbstractDataService {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";

    public KrakenBtcStockData getKrakenBtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenBtcEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new KrakenBtcStockData(getKrakenMarketData(resKraken));
    }

    public KrakenLtcStockData getKrakenLtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenLtcEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new KrakenLtcStockData(getKrakenMarketData(resKraken));
    }

    private static KrakenStockData getKrakenMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, KrakenStockData.class);
    }

    @Override
    public KrakenLtcStockData getLtcEurStockData() {
        return getKrakenLtcEurStockData();
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
