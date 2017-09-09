package com.jarkos.stock.service;

import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.bitbay.BitBayStockData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.stock.StockDataPreparer.BIT_BAY_LTC_WITHDRAW_PROV;
import static com.jarkos.stock.StockDataPreparer.BIT_BAY_TRADE_PROVISION;
import static com.jarkos.stock.StockDataPreparer.LTC_BUY_MONEY;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public abstract class AbstractDataSerivce {

    public abstract AbstractStockData getLtcEurStockData();

    public BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayStockData bitBayLtcPlnStockData, AbstractStockData btcEurAbstractStockData,
                                                       BitBayStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv, BigDecimal stockWithdrawProv) throws Exception {
        AbstractStockData ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION)));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BIT_BAY_LTC_WITHDRAW_PROV);
            //ABSTARCT STOCK
            BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(BigDecimal.valueOf(Float.valueOf(ltcEurStockData.getLastLtcPrice())));
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
            BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(BigDecimal.valueOf(btcEurAbstractStockData.getLastBtcPrice()), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(stockTradeProv));
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = numberOfBtcBoughtAfterTradeProv.subtract(stockWithdrawProv);
            //DOLICZYC TRANSFER FEE?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell
                    .subtract((numberOfMoneyFromBitBayBtcSell.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION))));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            System.err.println("ROI LTC BitBay -> Kraken: " + bitBayLtcBuyAndBtcSellRoi);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        throw new Exception("NO Kraken data");
    }

}
