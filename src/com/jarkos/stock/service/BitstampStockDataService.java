package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.communication.RequestSender;
import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitstamp.*;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import com.jarkos.stock.exception.NotSupportedOperationException;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT;
import static com.jarkos.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampStockDataService extends AbstractStockDataService {

    private static String BitstampLtcEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/ltceur";
    private static String BitstampBtcEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/btceur";
    private static String BitstampEthEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/etheur";
//    private static String BitstampBccEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/bcceur";

    @Override
    public BitstampLtcStockData getLtcEurStockData() {
        return getBitstampLtcEurStockData();
    }

    @Override
    public EthStockDataInterface getEthEurStockData() {
        return getBitstampEthEurStockData();
    }

    @Override
    protected BtcStockDataInterface getBtcEurStockData() {
        return getBitstampBtcEurStockData();
    }

    public BitstampEthStockData getBitstampEthEurStockData() {
        String resBitstamp = null;
        try {
            resBitstamp = RequestSender.sendRequest(BitstampEthEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitstampEthStockData(getBitstampMarketData(resBitstamp));
    }

    public BitstampLtcStockData getBitstampLtcEurStockData() {
        String resBitstamp = null;
        try {
            resBitstamp = RequestSender.sendRequest(BitstampLtcEurApiUrl);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitstampLtcStockData(getBitstampMarketData(resBitstamp));
    }

    public BitstampBccStockData getBitstampBccEurStockData() {
//        UNLOCK WHEN BCC API FROM BITSTAMP WILL BE AVAILABLE
//        String resBitstamp = null;
//        try {
//            resBitstamp = RequestSender.sendRequest(BitstampBccEurApiUrl);
//        } catch (DataFetchUnavailableException e) {
//            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
//        }
//        return new BitstampBccStockData(getBitstampMarketData(resBitstamp));
        return null;
    }

    private static BitstampStockData getBitstampMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitstampStockData.class);
    }

    public BitstampBtcStockData getBitstampBtcEurStockData() {
        String resBitstamp = null;
        try {
            resBitstamp = RequestSender.sendRequest(BitstampBtcEurApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitstampBtcStockData(getBitstampMarketData(resBitstamp));
    }

    @Override
    public String getStockCodeName() {
        return "Bitstamp";
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        // NON FEE
        return btcToSubtractWithdrawProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        // NON FEE
        return ltcToSubtractWithdrawProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        try {
            throw new NotSupportedOperationException("Exception for fetching bcc withdrawal fee from " + getStockCodeName());
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    protected BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    protected BigDecimal getEuroAfterWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT);
    }

    public BitstampDashStockData getBitstampDashEurStockData() {
        try {
            throw new NotSupportedOperationException("Exception for fetching bcc withdrawal fee from " + getStockCodeName());
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
