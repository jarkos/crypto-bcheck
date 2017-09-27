package com.jarkos.stock.service;

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

    protected abstract BigDecimal getEuroAfterWithdrawalProv(BigDecimal numberOfEuroToWithdraw);

    public BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayLtcStockData bitBayLtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                       BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            // EURO EXTERNAL STOCK -> BTC
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = amountOfBtcBoughtFromEuroOnExternalStockAfterProvs(btcEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterLtcSellAfterTradeProv);
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
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStockAfterProvs(bccEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterLtcSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
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
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStockAfterProvs(bccEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterEthSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
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
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStockAfterProvs(bccEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterBtcSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getBid()));
            BigDecimal numberOfMoneyFromBccSellAfterProv = numberOfMoneyFromBitBayBccSell.subtract((numberOfMoneyFromBitBayBccSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getBid());
            return bitBayBtcBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyBtcSellOnBitBayRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface abstractBtcEurStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);
        //EUR EXTERNAL STOCK -> BTC
        BigDecimal numberOfBtcWithdrawAfterProv = amountOfBtcBoughtFromEuroOnExternalStockAfterProvs(abstractBtcEurStockData, stockTradeProv,
                                                                                                     numberOfEurExchangedOnWalutomatAfterProv);

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
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStockAfterProvs(ltcEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterBtcSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLctPlnStockData.getBid()));
            BigDecimal numberOfMoneyFromLtcSellAfterProv = numberOfMoneyFromBitBayLtcSell.subtract((numberOfMoneyFromBitBayLtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLctPlnStockData.getBid());
            return bitBayBtcBuyAndLtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    private void displayDependOnRoi(BigDecimal eurBuyAndBtcSellRoi, String resultToDisplay) {
        if (eurBuyAndBtcSellRoi.compareTo(maxMarginCompareWarnDisplayRoi) > 0 || eurBuyAndBtcSellRoi.compareTo(minMarginCompareWarnDisplayRoi) < 0) {
            logger.warn(resultToDisplay);
        } else {
            logger.info(resultToDisplay);
        }
    }

    public BigDecimal prepareEuroBuyLtcSellOnBitBayRoi(BitBayLtcStockData bitBayLtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);

        //EURO EXTERNAL STOCK -> LTC
        BigDecimal numberOfLtcWithdrawAfterProv = amountOfLtcBoughtFromEuroOnExternalStockAfterProvs(ltcEurAbstractStockData, stockTradeProv,
                                                                                                     numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY
        BigDecimal plnMoneyAfterLtcExchange = numberOfLtcWithdrawAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
        BigDecimal plnMoneyLtcExchangedAfterProv = plnMoneyAfterLtcExchange.subtract(plnMoneyAfterLtcExchange.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi;
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyBccSellOnBitBayRoi(BitBayBccStockData bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData, BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK -> EUR
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer(MONEY_TO_EUR_BUY);

        //EUR EXTERNAL STOCK -> BCC
        BigDecimal numberOfBccWithdrawAfterProv = amountOfBccBoughtFromEuroOnExternalStockAfterProvs(abstractBccEurStockData, stockTradeProv,
                                                                                                     numberOfEurExchangedOnWalutomatAfterProv);
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
            //EUR EXTERNAL STOCK -> Dash
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProvs(dashEurAbstractStockData, stockTradeProv,
                                                                                                                         eurNumberAfterLtcSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                               bitBayDashPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    private BigDecimal amountOfDashBoughtFromEuroOnExternalStockAfterProvs(DashStockDataInterface dashEurAbstractStockData, BigDecimal stockTradeProv,
                                                                           BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getLastDashPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(stockTradeProv));
        return getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
    }

    public BigDecimal prepareBitBayEthBuyToEuroToDashSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                     BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            //EUR EXTERNAL STOCK -> Dash
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProvs(dashEurAbstractStockData, stockTradeProv,
                                                                                                                         eurNumberAfterEthSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY BTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
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
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStockAfterProvs(ltcEurAbstractStockData, stockTradeProv,
                                                                                                                       eurNumberAfterEthSellAfterTradeProv);
            //DOLICZYC TRANSFER FEE POMIEDZY PORTFELAMI?
            //BITBAY LTC
            BigDecimal numberOfMoneyFromBitBayDashSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromDashSellAfterProv = numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLtcPlnStockData.getLast());
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                                                BigDecimal stockTradeProv) {
        // BTC BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurAbstractStockData);
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterBtcSellAfterTradeProv);
        BigDecimal bitBayBtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayBtcBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayBtcBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLast() + " BTC " + getStockCodeName() + " -> " +
                           btcEurAbstractStockData.getLastBtcPrice().multiply(StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate));
        return bitBayBtcBuyAndEuroSellRoi;
    }

    private BigDecimal getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(BigDecimal eurNumberAfterBtcSellAfterTradeProv) {
        BigDecimal amountOfEuro = getEuroAfterWithdrawalProv(eurNumberAfterBtcSellAfterTradeProv);
        BigDecimal eurPlnExchangeRate = StockDataPreparer.lastSellWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = amountOfEuro.multiply(eurPlnExchangeRate);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO));
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

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(BitBayEthStockData bitBayEthPlnStockData, BigDecimal stockTradeProv,
                                                                            EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal numberOfEthBoughtOnBitBay = ETH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayEthPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
        BigDecimal ethNumberAfterWithdrawFromBitBay = ethNumberAfterTradeProvision.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSell = ethNumberAfterWithdrawFromBitBay.multiply(ethEurStockData.getLastEthPrice());
        return eurNumberAfterEthSell.subtract(eurNumberAfterEthSell.multiply(stockTradeProv));
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

    private BigDecimal amountOfBtcBoughtFromEuroOnExternalStockAfterProvs(BtcStockDataInterface btcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                          BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getLastBtcPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(stockTradeProv));
        return getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProv);
    }

    private BigDecimal amountOfLtcBoughtFromEuroOnExternalStockAfterProvs(LtcStockDataInterface ltcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                          BigDecimal eurNumberAfterEthSellAfterTradeProv) {
        BigDecimal numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastLtcPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(stockTradeProv));
        return getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProv);
    }

    private BigDecimal amountOfBccBoughtFromEuroOnExternalStockAfterProvs(BccStockDataInterface bccEurAbstractStockData, BigDecimal stockTradeProv,
                                                                          BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        // EURO - > BCC EXTERNAL STOCK
        BigDecimal numberOfBccBought = eurNumberAfterLtcSellAfterTradeProv.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer(BigDecimal amountOfMoney) {
        // WALUTOMAT
        BigDecimal eurPlnExchangeRate = StockDataPreparer.lastBuyWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = amountOfMoney.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

}
