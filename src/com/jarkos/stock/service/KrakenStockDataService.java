package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.kraken.*;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;
import com.jarkos.stock.service.abstractional.AbstractStockDataService;
import com.jarkos.stock.service.abstractional.EurStockDataService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.KRAKEN_EUR_WITHDRAW_PROV_AMOUNT;

public class KrakenStockDataService extends AbstractStockDataService implements EurStockDataService {

    private static final String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static final String KrakenBccEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=BCHEUR";
    private static final String KrakenEthEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=ETHEUR";
    private static final String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";
    private static final String KrakenDashEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=DASHEUR";
    private static final Logger logger = Logger.getLogger(KrakenStockDataService.class);

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
    public BtcStockDataInterface getBtcEurStockDataInterface() {
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
    protected BigDecimal getEuroAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(KRAKEN_EUR_WITHDRAW_PROV_AMOUNT);
    }

    private static KrakenStockData getKrakenMarketData(String response) {
        KrakenStockData krakenStockData = null;
        try {
            Gson gson = new Gson();
            krakenStockData = gson.fromJson(response, KrakenStockData.class);
        } catch (JsonSyntaxException jse) {
            logger.error(jse.getMessage());
        }
        return krakenStockData;
    }
}
