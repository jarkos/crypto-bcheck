package pl.jarkos.stock.service.abstractional;

import pl.jarkos.communication.RequestSender;
import pl.jarkos.stock.abstractional.api.*;

public interface EurStockDataService extends GeneralStockDataService {

    String getBtcEurApiUrl();

    String getBccEurApiUrl();

    String getEthEurApiUrl();

    String getLtcEurApiUrl();

    String getDashEurApiUrl();

    default BtcStockDataInterface getBtcEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getBtcEurApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBtcStockData(res);
    }

    default BccStockDataInterface getBccEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getBccEurApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBccStockData(res);
    }

    default EthStockDataInterface getEthEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getEthEurApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getEthStockData(res);
    }

    default LtcStockDataInterface getLtcEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getLtcEurApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getLtcStockData(res);
    }

    default DashStockDataInterface getDashEurStockData() {
        String res = null;
        try {
            res = RequestSender.sendRequest(getDashEurApiUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getDashStockData(res);
    }

}
