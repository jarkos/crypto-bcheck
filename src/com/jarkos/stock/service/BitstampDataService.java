package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitstamp.BitstampBtcStockData;
import com.jarkos.stock.dto.bitstamp.BitstampDashStockData;
import com.jarkos.stock.dto.bitstamp.BitstampEthStockData;
import com.jarkos.stock.dto.bitstamp.BitstampLtcStockData;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import com.jarkos.stock.exception.NotSupportedOperationException;

import java.math.BigDecimal;

import static com.jarkos.config.IndicatorsSystemConfig.BITSTAMP_WITHDRAW_PROV;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitstampDataService extends AbstractDataService {

    private static String BitstampLtcEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/ltceur";
    private static String BitstampBtcEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/btceur";
    private static String BitstampEthEurApiUrl = "https://www.bitstamp.net/api/v2/ticker/etheur";

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

    private EthStockDataInterface getBitstampEthEurStockData() {
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

    public BitstampDashStockData getBitstampDashEurStockData() {
        try {
            throw new NotSupportedOperationException("Exception for fetching bcc withdrawal fee from " + getStockCodeName());
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
