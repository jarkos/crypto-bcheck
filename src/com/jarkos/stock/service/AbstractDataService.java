package com.jarkos.stock.service;

import com.jarkos.Main;
import com.jarkos.stock.StockDataPreparer;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.bitbay.*;
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

    public abstract EthStockDataInterface getEthEurStockData();

    protected abstract BtcStockDataInterface getBtcEurStockData();

    public abstract BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv);

    public abstract BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv);

    public abstract BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv);

    protected abstract BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv);

    public BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayLtcStockData bitBayLtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                       BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getLastBtcPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(stockTradeProv));
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " BTC : " + bitBayLtcBuyAndBtcSellRoi + "-> BitBay PLN";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayLtcBuyAndBccSellRoi(BitBayLtcStockData bitBayLtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                       BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal numberOfBccBought = eurNumberAfterLtcSellAfterTradeProv.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyAndBccSellRoi(BitBayEthStockData bitBayEthPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                       BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            BigDecimal numberOfBccBought = eurNumberAfterEthSellAfterTradeProv.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getLast());
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayBtcBuyAndBccSellRoi(BitBayBtcStockData bitBayBtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                       BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        BtcStockDataInterface btcEurStockData = getBtcEurStockData();
        if (btcEurStockData.getBtcEurStockData() != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBought = eurNumberAfterBtcSellAfterTradeProv.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getBid()));
            BigDecimal numberOfMoneyFromBccSellAfterProv = numberOfMoneyFromBitBayBccSell.subtract((numberOfMoneyFromBitBayBccSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getBid());
            return bitBayBtcBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyBtcSellOnBitBayRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface abstractBtcEurStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);
        //EXTERNAL STOCK
        BigDecimal numberOfBtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractBtcEurStockData.getLastBtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtOnStockAfterProv = numberOfBtcBoughtOnStock.subtract(numberOfBtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfBtcWithdrawAfterProv = getBtcAfterWithdrawalProv(numberOfBtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterBtcExchange = numberOfBtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        BigDecimal plnMoneyBtcExchangedAfterProv = plnMoneyAfterBtcExchange.subtract(plnMoneyAfterBtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi;
        displayDependOnRoi(eurBuyAndBtcSellRoi, resultToDisplay);

        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyAndLtcSellRoi(BitBayBtcStockData bitBayBtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                       BitBayLtcStockData bitBayLctPlnStockData, BigDecimal stockTradeProv) {
        BtcStockDataInterface btcEurStockData = getBtcEurStockData();
        if (btcEurStockData.getBtcEurStockData() != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurStockData);
            // EURO - > LTC EXTERNAL STOCK
            BigDecimal numberOfLtcBought = eurNumberAfterBtcSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastLtcPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfLtcBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(stockTradeProv));
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLctPlnStockData.getBid()));
            BigDecimal numberOfMoneyFromLtcSellAfterProv = numberOfMoneyFromBitBayLtcSell.subtract((numberOfMoneyFromBitBayLtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLctPlnStockData.getBid());
            return bitBayBtcBuyAndLtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    private void displayDependOnRoi(BigDecimal eurBuyAndBtcSellRoi, String resultToDisplay) {
        if (eurBuyAndBtcSellRoi.compareTo(Main.marginCompareWarnDisplayRoi) > 0) {
            logger.warn(resultToDisplay);
        } else {
            logger.info(resultToDisplay);
        }
    }

    public BigDecimal prepareEuroBuyLtcSellOnBitBayRoi(BitBayLtcStockData bitBayLtcPlnStockData, LtcStockDataInterface abstractLtcEurStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);

        //EXTERNAL STOCK
        BigDecimal numberOfLtcBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractLtcEurStockData.getLastLtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtOnStockAfterProv = numberOfLtcBoughtOnStock.subtract(numberOfLtcBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfLtcWithdrawAfterProv = getLtcAfterWithdrawalProv(numberOfLtcBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterLtcExchange = numberOfLtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
        BigDecimal plnMoneyLtcExchangedAfterProv = plnMoneyAfterLtcExchange.subtract(plnMoneyAfterLtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi;
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyBccSellOnBitBayRoi(BitBayBccStockData bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);

        //EXTERNAL STOCK
        BigDecimal numberOfBccBoughtOnStock = numberOfEurExchangedAfterProv.divide(abstractBccEurStockData.getLastBccPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtOnStockAfterProv = numberOfBccBoughtOnStock.subtract(numberOfBccBoughtOnStock.multiply(stockTradeProv));
        BigDecimal numberOfBccWithdrawAfterProv = getBccAfterWithdrawalProv(numberOfBccBoughtOnStockAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterBccExchange = numberOfBccWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
        BigDecimal plnMoneyBccExchangedAfterProv = plnMoneyAfterBccExchange.subtract(plnMoneyAfterBccExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndBccSellRoi = plnMoneyBccExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BCC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBccSellRoi;
        displayDependOnRoi(eurBuyAndBccSellRoi, resultToDisplay);
        return eurBuyAndBccSellRoi;
    }


    public BigDecimal prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(BitBayLtcStockData bitBayLtcPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                          BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getLastDashPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(stockTradeProv));
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                               bitBayDashPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    private BigDecimal getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv,
                                                                            LtcStockDataInterface ltcEurStockData) {
        BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
        BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.getLastLtcPrice());
        return eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
    }

    public BigDecimal prepareBitBayEthBuyToEuroToDashSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                     BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            BigDecimal numberOfDashBought = eurNumberAfterEthSellAfterTradeProv.divide(dashEurAbstractStockData.getLastDashPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(stockTradeProv));
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                               bitBayDashPlnStockData.getLast());
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToEuroToLtcSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                    BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY ETH -> EUR EXTERNAL STOCK
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            // EURO -> LTC
            BigDecimal numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastLtcPrice(), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfDashBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(stockTradeProv));
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = getLtcAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY LTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLtcPlnStockData.getLast());
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(BitBayEthStockData bitBayEthPlnStockData, BigDecimal stockTradeProv, EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal numberOfEthBoughtOnBitBay = ETH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayEthPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
        BigDecimal ethNumberAfterWithdrawFromBitBay = ethNumberAfterTradeProvision.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSell = ethNumberAfterWithdrawFromBitBay.multiply(ethEurStockData.getLastEthPrice());
        return eurNumberAfterEthSell.subtract(eurNumberAfterEthSell.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv,
                                                                            BtcStockDataInterface btcEurStockData) {
        //BITBAY BTC
        BigDecimal numberOfBtcBoughtOnBitBay = BTC_BUY_MONEY.divide(bitBayBtcPlnStockData.getLastBtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal btcNumberAfterTradeProvision = numberOfBtcBoughtOnBitBay.subtract(numberOfBtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
        BigDecimal btcNumberAfterWithdrawFromBitBay = btcNumberAfterTradeProvision.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK BTC -> EUR
        BigDecimal eurNumberAfterBtcSell = btcNumberAfterWithdrawFromBitBay.multiply(btcEurStockData.getLastBtcPrice());
        return eurNumberAfterBtcSell.subtract(eurNumberAfterBtcSell.multiply(stockTradeProv));
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer(BigDecimal amountOfMoney) {
        // WALUTOMAT
        BigDecimal eurPlnExchangeRate = StockDataPreparer.lastAverageWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = amountOfMoney.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.subtract(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

}
