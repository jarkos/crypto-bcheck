package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.kraken.*;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;

import java.math.BigDecimal;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.StockConfig.*;

public class KrakenStockDataService extends AbstractStockDataService {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";
    private static String KrakenBccEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=BCHEUR";
    private static String KrakenEthEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=ETHEUR";
    private static String KrakenDashEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=DASHEUR";

    public KrakenBtcStockData getKrakenBtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenBtcEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        KrakenStockData krakenMarketData = getKrakenMarketData(resKraken);
        return krakenMarketData != null ? new KrakenBtcStockData(krakenMarketData) : null;
    }

    public KrakenLtcStockData getKrakenLtcEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenLtcEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        KrakenStockData krakenMarketData = getKrakenMarketData(resKraken);
        return krakenMarketData != null ? new KrakenLtcStockData(krakenMarketData) : null;
    }

    public KrakenBccStockData getKrakenBccEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenBccEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        KrakenStockData krakenMarketData = getKrakenMarketData(resKraken);
        return krakenMarketData != null ? new KrakenBccStockData(krakenMarketData) : null;
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
    public KrakenEthStockData getEthEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenEthEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new KrakenEthStockData(getKrakenMarketData(resKraken));
    }

    @Override
    protected BtcStockDataInterface getBtcEurStockData() {
        return getKrakenBtcEurStockData();
    }

    public KrakenDashStockData getKrakenDashEurStockData() {
        String resKraken = null;
        try {
            resKraken = sendRequest(KrakenDashEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        KrakenStockData krakenMarketData = getKrakenMarketData(resKraken);
        return krakenMarketData != null ? new KrakenDashStockData(krakenMarketData) : null;
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

    @Override
    protected BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(KRAKEN_DASH_WITHDRAW_PROV);
    }

    @Override
    protected BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(KRAKEN_ETH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    protected BigDecimal getEuroAfterWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(KRAKEN_EUR_WITHDRAW_PROV_AMOUNT);
    }
}
