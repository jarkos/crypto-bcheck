package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.coinroom.*;
import com.jarkos.stock.exception.NotSupportedOperationException;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.*;

public class CoinroomStockDataService extends AbstractStockDataService {

    private static String CoinroomBtcPlnApiUrl = "https://coinroom.com/api/ticker/BTC/PLN";
    private static String CoinroomLtcPlnApiUrl = "https://coinroom.com/api/ticker/LTC/PLN";
    private static String CoinroomEthPlnApiUrl = "https://coinroom.com/api/ticker/ETH/PLN";
    private static String CoinroomDashPlnApiUrl = "https://coinroom.com/api/ticker/DASH/PLN";
    private static String CoinroomBccPlnApiUrl = "https://coinroom.com/api/ticker/BCC/PLN";

    @Override
    public String getStockCodeName() {
        return "Coinroom";
    }

    @Override
    public CoinroomLtcStockStockData getLtcEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomLtcPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new CoinroomLtcStockStockData(getCoinroomMarketData(res));
    }

    @Override
    public CoinroomEthStockStockData getEthEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomEthPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new CoinroomEthStockStockData(getCoinroomMarketData(res));
    }

    public CoinroomEthStockStockData getEthPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomEthPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new CoinroomEthStockStockData(getCoinroomMarketData(res));
    }

    public CoinroomBccStockStockData getBccPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomBccPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        CoinroomStockData coinroomMarketData = getCoinroomMarketData(res);
        return new CoinroomBccStockStockData(coinroomMarketData);
    }

    @Override
    protected BtcStockDataInterface getBtcEurStockData() {
        try {
            throw new NotSupportedOperationException("Exception for fetching btc eur data from " + getStockCodeName());
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CoinroomBtcStockStockData getBtcPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomBtcPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new CoinroomBtcStockStockData(getCoinroomMarketData(res));
    }

    public CoinroomDashStockStockData getDashPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(CoinroomDashPlnApiUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new CoinroomDashStockStockData(getCoinroomMarketData(res));
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(COINROOM_BTC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(COINROOM_LTC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        return bccToSubtractWithdrawProv.subtract(COINROOM_BCC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    protected BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(COINROOM_DASH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    protected BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(COINROOM_ETH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    protected BigDecimal getEuroAfterWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        return numberOfEuroToWithdraw.subtract(COINROOM_EUR_WITHDRAW_PROV_AMOUNT);
    }


    private static CoinroomStockData getCoinroomMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, CoinroomStockData.class);
    }

    public CoinroomDashStockStockData getDashEurStockData() {
        return null;
    }
}
