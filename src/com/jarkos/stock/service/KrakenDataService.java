package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.kraken.KrakenBccStockData;
import com.jarkos.stock.dto.kraken.KrakenBtcStockData;
import com.jarkos.stock.dto.kraken.KrakenEthStockData;
import com.jarkos.stock.dto.kraken.KrakenLtcStockData;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.IndicatorsSystemConfig.*;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class KrakenDataService extends AbstractDataService {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";
    private static String KrakenBccEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=BCHEUR";
    private static String KrakenEthEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=ETHEUR";

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

    public KrakenBccStockData getKrakenBccEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenBccEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new KrakenBccStockData(getKrakenMarketData(resKraken));
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
    public EthStockDataInterface getEthEurStockData() {
        return getKrakenEthEurStockData();
    }

    private EthStockDataInterface getKrakenEthEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenEthEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new KrakenEthStockData(getKrakenMarketData(resKraken));
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

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        return bccToSubtractWithdrawProv.subtract(KRAKEN_BCC_WITHDRAW_PROV);
    }

}
