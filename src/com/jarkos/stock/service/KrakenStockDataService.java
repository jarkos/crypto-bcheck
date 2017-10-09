package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.kraken.*;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.service.abstractional.AbstractStockDataService;
import com.jarkos.stock.service.abstractional.EurStockDataService;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.*;

public class KrakenStockDataService extends AbstractStockDataService implements EurStockDataService {

    private static final String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static final String KrakenBccEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=BCHEUR";
    private static final String KrakenEthEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=ETHEUR";
    private static final String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";
    private static final String KrakenDashEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=DASHEUR";

    @Override
    public String getStockCodeName() {
        return "Kraken";
    }

    @Override
    public String getBtcEurApiUrl() {
        return KrakenBtcEurApiUrl;
    }

    @Override
    public String getBccEurApiUrl() {
        return KrakenBccEurApiUrl;
    }

    @Override
    public String getEthEurApiUrl() {
        return KrakenEthEurApiUrl;
    }

    @Override
    public String getLtcEurApiUrl() {
        return KrakenLtcEurApiUrl;
    }

    @Override
    public String getDashEurApiUrl() {
        return KrakenDashEurApiUrl;
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return EurStockDataService.super.getBtcEurStockData();
    }

    @Override
    public EthStockDataInterface getEthEurStockData() {
        return EurStockDataService.super.getEthEurStockData();
    }

    @Override
    public LtcStockDataInterface getLtcEurStockData() {
        return EurStockDataService.super.getLtcEurStockData();
    }

    @Override
    public BtcStockDataInterface getBtcStockData(String response) {
        KrakenStockData krakenMarketData = getKrakenMarketData(response);
        return krakenMarketData != null ? new KrakenBtcStockData(krakenMarketData) : null;
    }

    @Override
    public BccStockDataInterface getBccStockData(String response) {
        KrakenStockData krakenMarketData = getKrakenMarketData(response);
        return krakenMarketData != null ? new KrakenBccStockData(krakenMarketData) : null;
    }

    @Override
    public EthStockDataInterface getEthStockData(String response) {
        KrakenStockData krakenMarketData = getKrakenMarketData(response);
        return krakenMarketData != null ? new KrakenEthStockData(krakenMarketData) : null;
    }

    @Override
    public LtcStockDataInterface getLtcStockData(String response) {
        KrakenStockData krakenMarketData = getKrakenMarketData(response);
        return krakenMarketData != null ? new KrakenLtcStockData(krakenMarketData) : null;
    }

    @Override
    public DashStockDataInterface getDashStockData(String response) {
        KrakenStockData krakenMarketData = getKrakenMarketData(response);
        return krakenMarketData != null ? new KrakenDashStockData(krakenMarketData) : null;
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
    protected BigDecimal getEuroAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(KRAKEN_EUR_WITHDRAW_PROV_AMOUNT);
    }

    private static KrakenStockData getKrakenMarketData(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, KrakenStockData.class);
    }
}
