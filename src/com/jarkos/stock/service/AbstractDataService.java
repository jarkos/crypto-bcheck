package com.jarkos.stock.service;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitbayBccStockData;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;
import com.jarkos.walutomat.WalutomatData;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.config.IndicatorsSystemConfig.*;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public abstract class AbstractDataService {
    private static final Logger logger = Logger.getLogger(AbstractDataService.class);

    public abstract String getStockCodeName();

    public abstract LtcStockDataInterface getLtcEurStockData();

    public abstract BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv);

    public abstract BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv);

    public abstract BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv);

    public BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitbayStockData bitBayLtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData, BitbayStockData bitBayBtcPlnStockData,
                                                       BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
            //EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.getLastLtcPrice());
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
            BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getLastBtcPrice(), 5, RoundingMode.HALF_DOWN);
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
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayLtcBuyAndBccSellRoi(BitbayStockData bitBayLtcPlnStockData, BccStockDataInterface bccEurAbstractStockData, BitbayStockData bitBayBccPlnStockData,
                                                       BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
            //EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.getLastLtcPrice());
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
            BigDecimal numberOfBccBought = eurNumberAfterLtcSellAfterTradeProv.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            logger.info("ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(BigDecimal.valueOf(4.28d)) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(BigDecimal.valueOf(4.28d)) + " BCC BitBay -> " + bitBayBccPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyBtcSellOnBitBayRoi(BitbayStockData bitBayBtcPlnStockData, BtcStockDataInterface abstractBtcEurStockData, WalutomatData walutomatEurPlnData,
                                                       BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(walutomatEurPlnData, MONEY_TO_EUR_BUY);
        //EXTERNAL STOCK
        BigDecimal numberOfBtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractBtcEurStockData.getLastBtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtOnStockAfterProv = numberOfBtcBoughtOnStock.subtract(numberOfBtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfBtcWithdrawAfterProv = getBtcAfterWithdrawalProv(numberOfBtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterBtcExchange = numberOfBtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        BigDecimal plnMoneyBtcExchangedAfterProv = plnMoneyAfterBtcExchange.subtract(plnMoneyAfterBtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        logger.info("ROI EUR Walutomat -> BTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi);
        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareEuroBuyLtcSellOnBitBayRoi(BitbayStockData bitBayLtcPlnStockData, LtcStockDataInterface abstractLtcEurStockData, WalutomatData walutomatEurPlnData,
                                                       BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(walutomatEurPlnData, MONEY_TO_EUR_BUY);

        //EXTERNAL STOCK
        BigDecimal numberOfLtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractLtcEurStockData.getLastLtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtOnStockAfterProv = numberOfLtcBoughtOnStock.subtract(numberOfLtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfLtcWithdrawAfterProv = getLtcAfterWithdrawalProv(numberOfLtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterLtcExchange = numberOfLtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
        BigDecimal plnMoneyLtcExchangedAfterProv = plnMoneyAfterLtcExchange.subtract(plnMoneyAfterLtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        logger.info("ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyBccSellOnBitBayRoi(BitbayBccStockData bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData, WalutomatData walutomatEurPlnData,
                                                       BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(walutomatEurPlnData, MONEY_TO_EUR_BUY);

        //EXTERNAL STOCK
        BigDecimal numberOfBccBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractBccEurStockData.getLastBccPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtOnStockAfterProv = numberOfBccBoughtOnStock.subtract(numberOfBccBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfBccWithdrawAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterBccExchange = numberOfBccWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
        BigDecimal plnMoneyBccExchangedAfterProv = plnMoneyAfterBccExchange.subtract(plnMoneyAfterBccExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndBccSellRoi = plnMoneyBccExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        logger.info("ROI EUR Walutomat -> BCC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBccSellRoi);
        return eurBuyAndBccSellRoi;
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer(WalutomatData walutomatEurPlnData, BigDecimal amountOfMoney) {
        // WALUTOMAT
        BigDecimal eurPlnExchangeRate = walutomatEurPlnData.getAverageExchangeRate();
        BigDecimal numberOfEurAfterExchange = amountOfMoney.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.subtract(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

}
