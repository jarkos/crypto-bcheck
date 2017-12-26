package pl.jarkos.backend.stock.service.abstractional;

import pl.jarkos.backend.communication.RequestSender;
import pl.jarkos.backend.stock.abstractional.api.*;

import java.math.BigDecimal;

public interface PlnStockDataService extends GeneralStockDataService {

    String getBtcPlnApiUrl();

    String getBccPlnApiUrl();

    String getEthPlnApiUrl();

    String getLtcPlnApiUrl();

    String getDashPlnApiUrl();

    default DashStockDataInterface getDashPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getDashPlnApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getDashStockData(res);
    }

    default BtcStockDataInterface getBtcPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getBtcPlnApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBtcStockData(res);
    }

    default BccStockDataInterface getBccPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getBccPlnApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBccStockData(res);
    }

    default EthStockDataInterface getEthPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getEthPlnApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getEthStockData(res);
    }

    default LtcStockDataInterface getLtcPlnStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getLtcPlnApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getLtcStockData(res);
    }

    default BigDecimal getPlnAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw) {
        //default no fee
        return numberOfEuroToWithdraw;
    }
}
