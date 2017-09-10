package com.jarkos.stock.service;

import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.walutomat.WalutomatData;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.stock.StockDataPreparer.*;
import static com.jarkos.stock.StockDataPreparer.MONEY_TO_EUR_BUY;
import static com.jarkos.stock.StockDataPreparer.WALUTOMAT_WITHDRAW_RATIO;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public abstract class AbstractDataService {
    private static final Logger logger = Logger.getLogger(AbstractStockData.class);

    public abstract AbstractStockData getLtcEurStockData();

    public abstract String getStockCodeName();

    public abstract BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv);

    public abstract BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv);

    public BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayStockData bitBayLtcPlnStockData, AbstractStockData btcEurAbstractStockData, BitBayStockData bitBayBtcPlnStockData,
                                                       BigDecimal stockTradeProv) throws Exception {
        AbstractStockData ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
            //EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(BigDecimal.valueOf(Float.valueOf(ltcEurStockData.getLastLtcPrice())));
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
            BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(BigDecimal.valueOf(btcEurAbstractStockData.getLastBtcPrice()), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(stockTradeProv));
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            logger.info("ROI LTC BitBay -> " + getStockCodeName() + ": " + bitBayLtcBuyAndBtcSellRoi);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        throw new Exception("NO" + getStockCodeName() + " data");
    }

    public BigDecimal prepareEuroBuyBtcSellOnBitBayRoi(BitBayStockData bitBayBtcPlnStockData, AbstractStockData abstractBtcEurStockData, WalutomatData walutomatEurPlnData,
                                                       BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(walutomatEurPlnData, MONEY_TO_EUR_BUY);
        //EXTERNAL STOCK
        BigDecimal numberOfBtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(BigDecimal.valueOf(abstractBtcEurStockData.getLastBtcPrice()), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtOnStockAfterProv = numberOfBtcBoughtOnStock.subtract(numberOfBtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfBtcWithdrawAfterProv = getBtcAfterWithdrawalProv(numberOfBtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterBtcExchange = numberOfBtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        BigDecimal plnMoneyBtcExchangedAfterProv = plnMoneyAfterBtcExchange.subtract(plnMoneyAfterBtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        logger.info("ROI EUR Walutomat BTC -> " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi);
        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareEuroBuyLtcSellOnBitBayRoi(BitBayStockData bitBayLtcPlnStockData, AbstractStockData abstractLtcEurStockData, WalutomatData walutomatEurPlnData,
                                                       BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(walutomatEurPlnData, MONEY_TO_EUR_BUY);

        //EXTERNAL STOCK
        BigDecimal numberOfLtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(BigDecimal.valueOf(abstractLtcEurStockData.getLastLtcPrice()), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtOnStockAfterProv = numberOfLtcBoughtOnStock.subtract(numberOfLtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfLtcWithdrawAfterProv = getLtcAfterWithdrawalProv(numberOfLtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterLtcExchange = numberOfLtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
        BigDecimal plnMoneyLtcExchangedAfterProv = plnMoneyAfterLtcExchange.subtract(plnMoneyAfterLtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        logger.info("ROI EUR Walutomat LTC -> " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi);
        return eurBuyAndLtcSellRoi;
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer(WalutomatData walutomatEurPlnData, BigDecimal amountOfMoney) {
        // WALUTOMAT
        BigDecimal eurPlnExchangeRate = walutomatEurPlnData.getAverageExchangeRate();
        BigDecimal numberOfEurAfterExchange = amountOfMoney.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.subtract(WALUTOMAT_WITHDRAW_RATIO))
                .subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

}
