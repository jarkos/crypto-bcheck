package pl.jarkos.backend.stock.service.abstractional;

import pl.jarkos.backend.stock.abstractional.api.*;
import pl.jarkos.backend.stock.exception.NotSupportedOperationException;

public interface GeneralStockDataService {

    String getStockCodeName();

    BtcStockDataInterface getBtcStockData(String res);

    BccStockDataInterface getBccStockData(String res);

    EthStockDataInterface getEthStockData(String res);

    LtcStockDataInterface getLtcStockData(String res);

    DashStockDataInterface getDashStockData(String res);

    default Object throwNotSupportedOperationAndReturnNull() {
        try {
            throw new NotSupportedOperationException("Exception for this action from " + getStockCodeName() + ". See stack trace for details. ");
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
