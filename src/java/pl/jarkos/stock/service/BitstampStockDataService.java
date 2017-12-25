package pl.jarkos.stock.service;

import com.google.gson.Gson;
import pl.jarkos.stock.abstractional.api.*;
import pl.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import pl.jarkos.stock.dto.bitstamp.BitstampEthStockData;
import pl.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import pl.jarkos.stock.dto.bitstamp.general.BitstampStockData;
import pl.jarkos.stock.service.abstractional.AbstractStockDataService;
import pl.jarkos.stock.service.abstractional.EurStockDataService;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT;

public class BitstampStockDataService extends AbstractStockDataService implements EurStockDataService {

    @Override
    public String getStockCodeName() {
        return "Bitstamp";
    }

    @Override
    public String getBtcEurApiUrl() {
        return "https://www.bitstamp.net/api/v2/ticker/btceur";
    }

    @Override
    public String getBccEurApiUrl() {
        return (String) throwNotSupportedOperationAndReturnNull();
    }

    @Override
    public String getEthEurApiUrl() {
        return "https://www.bitstamp.net/api/v2/ticker/etheur";
    }

    @Override
    public String getLtcEurApiUrl() {
        return "https://www.bitstamp.net/api/v2/ticker/ltceur";
    }

    @Override
    public String getDashEurApiUrl() {
        return (String) throwNotSupportedOperationAndReturnNull();
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
        BitstampStockData bitstampMarketData = getMarketDataFromJson(response);
        return bitstampMarketData != null ? new BitstampBtcStockData(bitstampMarketData) : null;
    }

    @Override
    public BccStockDataInterface getBccStockData(String response) {
        return (BccStockDataInterface) throwNotSupportedOperationAndReturnNull();
    }

    @Override
    public EthStockDataInterface getEthStockData(String response) {
        BitstampStockData bitstampMarketData = getMarketDataFromJson(response);
        return bitstampMarketData != null ? new BitstampEthStockData(bitstampMarketData) : null;
    }

    @Override
    public LtcStockDataInterface getLtcStockData(String response) {
        BitstampStockData bitstampMarketData = getMarketDataFromJson(response);
        return bitstampMarketData != null ? new BitstampLtcStockData(bitstampMarketData) : null;
    }

    @Override
    public DashStockDataInterface getDashStockData(String response) {
        return (DashStockDataInterface) throwNotSupportedOperationAndReturnNull();
    }

    @Override
    public BigDecimal getEuroAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT);
    }

    private static BitstampStockData getMarketDataFromJson(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, BitstampStockData.class);
    }

}
