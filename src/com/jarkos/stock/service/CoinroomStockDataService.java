package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.coinroom.*;
import com.jarkos.stock.service.abstractional.AbstractStockDataService;
import com.jarkos.stock.service.abstractional.EurStockDataService;
import com.jarkos.stock.service.abstractional.PlnStockDataService;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.COINROOM_EUR_WITHDRAW_PROV_AMOUNT;

public class CoinroomStockDataService extends AbstractStockDataService implements PlnStockDataService, EurStockDataService {

    @Override
    public String getStockCodeName() {
        return "Coinroom";
    }

    @Override
    public String getDashPlnApiUrl() {
        return "https://coinroom.com/api/ticker/DASH/PLN";
    }

    @Override
    public String getBtcPlnApiUrl() {
        return "https://coinroom.com/api/ticker/BTC/PLN";
    }

    @Override
    public String getBccPlnApiUrl() {
        return "https://coinroom.com/api/ticker/BCC/PLN";
    }

    @Override
    public String getEthPlnApiUrl() {
        return "https://coinroom.com/api/ticker/ETH/PLN";
    }

    @Override
    public String getLtcPlnApiUrl() {
        return "https://coinroom.com/api/ticker/LTC/PLN";
    }

    @Override
    public BtcStockDataInterface getBtcStockData(String res) {
        CoinroomStockData marketData = getMarketDataFromJson(res);
        return marketData != null ? new CoinroomBtcStockData(marketData) : null;
    }

    @Override
    public BccStockDataInterface getBccStockData(String res) {
        CoinroomStockData marketData = getMarketDataFromJson(res);
        return marketData != null ? new CoinroomBccStockData(marketData) : null;
    }

    @Override
    public EthStockDataInterface getEthStockData(String res) {
        CoinroomStockData marketData = getMarketDataFromJson(res);
        return marketData != null ? new CoinroomEthStockData(marketData) : null;
    }

    @Override
    public LtcStockDataInterface getLtcStockData(String res) {
        CoinroomStockData marketData = getMarketDataFromJson(res);
        return marketData != null ? new CoinroomLtcStockData(marketData) : null;
    }

    @Override
    public DashStockDataInterface getDashStockData(String res) {
        CoinroomStockData marketData = getMarketDataFromJson(res);
        return marketData != null ? new CoinroomDashStockData(marketData) : null;
    }

    @Override
    public String getBtcEurApiUrl() {
        return "https://coinroom.com/api/ticker/BTC/EUR";
    }

    @Override
    public String getBccEurApiUrl() {
        return "https://coinroom.com/api/ticker/BCC/EUR";
    }

    @Override
    public String getEthEurApiUrl() {
        return "https://coinroom.com/api/ticker/ETH/EUR";
    }

    @Override
    public String getLtcEurApiUrl() {
        return "https://coinroom.com/api/ticker/LTC/EUR";
    }

    @Override
    public String getDashEurApiUrl() {
        return (String) throwNotSupportedOperationAndReturnNull();
    }

    @Override
    public BigDecimal getEuroAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(COINROOM_EUR_WITHDRAW_PROV_AMOUNT);
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

    private static CoinroomStockData getMarketDataFromJson(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, CoinroomStockData.class);
    }

}
