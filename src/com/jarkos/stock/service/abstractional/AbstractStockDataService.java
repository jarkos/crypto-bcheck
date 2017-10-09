package com.jarkos.stock.service.abstractional;

import com.jarkos.stock.StockRoiPreparer;
import com.jarkos.stock.abstractional.api.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.config.AppConfig.*;
import static com.jarkos.config.StockConfig.*;

public abstract class AbstractStockDataService {

    private static final Logger logger = Logger.getLogger(AbstractStockDataService.class);

    public abstract String getStockCodeName();

    public abstract LtcStockDataInterface getLtcEurStockData();

    public abstract EthStockDataInterface getEthEurStockData();

    protected abstract BtcStockDataInterface getBtcEurStockData();

    public abstract BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv);

    public abstract BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv);

    public abstract BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv);

    protected abstract BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv);

    protected abstract BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv);

    protected abstract BigDecimal getEuroAfterMoneyWithdrawalProv(BigDecimal numberOfEuroToWithdraw);

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(LtcStockDataInterface bitBayLtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                                               BtcStockDataInterface bitBayBtcPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData != null && ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, stockTradeProv,
                                                                                                                                        ltcEurStockData);
            // EURO EXTERNAL STOCK -> BTC
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = amountOfBtcBoughtFromEuroOnExternalStock(btcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterLtcSellAfterTradeProv);
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBtcBoughtFromEuroOnExternalStockPessimistic(btcEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterLtcSellAfterTradeProvPessimistic);
            // BITBAY BTC -> PLN
            BigDecimal numberOfMoneyFromBtcSellAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromBtcSellAfterProvPessimistic = getPlnFromBitBayBtcSellPessimistic(bitBayBtcPlnStockData,
                                                                                                         numberOfBtcBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBtcSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI LTC BitBay -> " + getStockCodeName() + " BTC : " + bitBayLtcBuyAndBtcSellRoi + " {" + bitBayLtcBuyAndBtcSellRoiPessimistic + "} " + "-> BitBay PLN";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(LtcStockDataInterface bitBayLtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                    BccStockDataInterface bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData != null && ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, stockTradeProv,
                                                                                                                                        ltcEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterLtcSellAfterTradeProv);
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterLtcSellAfterTradeProvPessimistic);
            //BITBAY BCC -> PLN
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                                                                                                         numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBccSellAfterProv.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi + " {" + bitBayLtcBuyAndBtcSellRoiPessimistic + "}";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLastPrice() + " {" + bitBayLtcPlnStockData.getAskPrice().setScale(2, RoundingMode.HALF_DOWN) + "} LTC " +
                               getStockCodeName() + " -> " +
                               ltcEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                               getStockCodeName() + " -> " +
                               bccEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                               " BCC BitBay -> " + bitBayBccPlnStockData.getLastPrice() + " {" + bitBayBccPlnStockData.getBidPrice() + "} ");
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(EthStockDataInterface bitBayEthPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                  BccStockDataInterface bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData != null && ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            BigDecimal eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, stockTradeProv,
                                                                                                                                        ethEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterEthSellAfterTradeProv);
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterEthSellAfterTradeProvPessimistic);
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                                                                                                         numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayEthBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayEthBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI ETH BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayEthBuyAndBccSellRoi + " {" + bitBayEthBuyAndBccSellRoiPessimistic + "}";
            displayDependOnRoi(bitBayEthBuyAndBccSellRoi, resultToDisplay);
            System.out.println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.getLastPrice() + " {" + bitBayEthPlnStockData.getAskPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "} ETH  " +
                    getStockCodeName() + " -> " +
                    ethEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                    getStockCodeName() + " -> " +
                    bccEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " BCC BitBay -> " + bitBayBccPlnStockData.getLastPrice() + " {" + bitBayBccPlnStockData.getBidPrice() + ")");
            return bitBayEthBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(BtcStockDataInterface bitBayBtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                    BccStockDataInterface bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        BtcStockDataInterface btcEurStockData = getBtcEurStockData();
        if (btcEurStockData.getBtcEurStockData() != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurStockData);
            BigDecimal eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, stockTradeProv,
                                                                                                                                        btcEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterBtcSellAfterTradeProv);
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(bccEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterBtcSellAfterTradeProvPessimistic);
            //BITBAY BCC -> PLN
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromBccSellAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData,
                                                                                                         numberOfBccBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayBtcBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi + " {" +
                                     bitBayBtcBuyAndBccSellRoiPessimistic.setScale(3, BigDecimal.ROUND_HALF_DOWN) + "}";
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay);
            System.out.println(
                    "BTC BitBay -> " + bitBayBtcPlnStockData.getLastPrice() + " {" + bitBayBtcPlnStockData.getAskPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "} BTC " +
                    getStockCodeName() + " -> " +
                    btcEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # BCC " +
                    getStockCodeName() + " -> " +
                    bccEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " BCC BitBay -> " + bitBayBccPlnStockData.getLastPrice() + " {" + bitBayBtcPlnStockData.getBidPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}");
            return bitBayBtcBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(BtcStockDataInterface bitBayBtcPlnStockData, BtcStockDataInterface abstractBtcEurStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();
        //EUR EXTERNAL STOCK -> BTC
        BigDecimal numberOfBtcWithdrawAfterProv = amountOfBtcBoughtFromEuroOnExternalStock(abstractBtcEurStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        BigDecimal numberOfBtcWithdrawAfterProvPessimistic = amountOfBtcBoughtFromEuroOnExternalStockPessimistic(abstractBtcEurStockData, stockTradeProv,
                                                                                                                 numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY BTC -> PLN
        BigDecimal plnMoneyBtcExchangedAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcWithdrawAfterProv);
        BigDecimal plnMoneyBtcExchangedAfterProvPessimistic = getPlnFromBitBayBtcSellPessimistic(bitBayBtcPlnStockData, numberOfBtcWithdrawAfterProvPessimistic);

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndBtcSellRoiPessimistic = plnMoneyBtcExchangedAfterProvPessimistic.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi + " {" +
                                 eurBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndBtcSellRoi, resultToDisplay);

        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockEthSellOnBitBayRoi(EthStockDataInterface bitBayEthPlnStockData, EthStockDataInterface abstractEthEurStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();
        //EUR EXTERNAL STOCK -> ETH
        BigDecimal numberOfEthWithdrawAfterProv = amountOfEthBoughtFromEuroOnExternalStock(abstractEthEurStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        BigDecimal numberOfEthWithdrawAfterProvPessimistic = amountOfEthBoughtFromEuroOnExternalStockPessimistic(abstractEthEurStockData, stockTradeProv,
                                                                                                                 numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY BTC -> PLN
        BigDecimal plnMoneyEthExchangedAfterProv = getPlnFromBitBayEthSell(bitBayEthPlnStockData, numberOfEthWithdrawAfterProv);
        BigDecimal plnMoneyEthExchangedAfterProvPessimistic = getPlnFromBitBayEthSell(bitBayEthPlnStockData, numberOfEthWithdrawAfterProvPessimistic);

        BigDecimal eurBuyAndEthSellRoi = plnMoneyEthExchangedAfterProv.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndEthSellRoiPessimistic = plnMoneyEthExchangedAfterProvPessimistic.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> ETH " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndEthSellRoi + " {" +
                                 eurBuyAndEthSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndEthSellRoi, resultToDisplay);

        return eurBuyAndEthSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(BtcStockDataInterface bitBayBtcPlnStockData,
                                                                                                LtcStockDataInterface ltcEurAbstractStockData,
                                                                                                LtcStockDataInterface bitBayLctPlnStockData, BigDecimal stockTradeProv) {
        BtcStockDataInterface btcEurStockData = getBtcEurStockData();
        if (btcEurStockData != null && btcEurStockData.getBtcEurStockData() != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurStockData);
            BigDecimal eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, stockTradeProv,
                                                                                                                                        btcEurStockData);
            // EURO - > LTC EXTERNAL STOCK
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterBtcSellAfterTradeProv);
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterBtcSellAfterTradeProvPessimistic);
            //BITBAY LTC -> PLN
            BigDecimal numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLctPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromLtcSellAfterProvPessimistic = getPlnFromBitBayLtcSellPessimistic(bitBayLctPlnStockData,
                                                                                                         numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayBtcBuyAndLtcSellRoiPessimistic = numberOfMoneyFromLtcSellAfterProvPessimistic.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi + " {" +
                                     bitBayBtcBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                               bitBayBtcPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "} BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # LTC " +
                               getStockCodeName() + " -> " +
                               ltcEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                               " LTC BitBay -> " + bitBayLctPlnStockData.getLastPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + " {" +
                               bitBayBtcPlnStockData.getBidPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}");
            return bitBayBtcBuyAndLtcSellRoi;
        }
        System.out.println("NO " + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(LtcStockDataInterface bitBayLtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();

        //EURO EXTERNAL STOCK -> LTC
        BigDecimal numberOfLtcWithdrawAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        BigDecimal numberOfLtcWithdrawAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData, stockTradeProv,
                                                                                                                 numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY LTC -> PLN
        BigDecimal plnMoneyLtcExchangedAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcWithdrawAfterProv);
        BigDecimal plnMoneyLtcExchangedAfterProvPessimistic = getPlnFromBitBayLtcSellPessimistic(bitBayLtcPlnStockData, numberOfLtcWithdrawAfterProvPessimistic);

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndLtcSellRoiPessimistic = plnMoneyLtcExchangedAfterProvPessimistic.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi + " {" +
                                 eurBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockBccSellOnBitBayRoi(BccStockDataInterface bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK -> EUR
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();

        //EUR EXTERNAL STOCK -> BCC
        BigDecimal numberOfBccWithdrawAfterProv = amountOfBccBoughtFromEuroOnExternalStock(abstractBccEurStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        BigDecimal numberOfBccWithdrawAfterProvPessimistic = amountOfBccBoughtFromEuroOnExternalStockPessimistic(abstractBccEurStockData, stockTradeProv,
                                                                                                                 numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY BCC -> PLN
        BigDecimal plnMoneyBccExchangedAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccWithdrawAfterProv);
        BigDecimal plnMoneyBccExchangedAfterProvPessimistic = getPlnFromBitBayBccSellPessimistic(bitBayBccPlnStockData, numberOfBccWithdrawAfterProvPessimistic);

        BigDecimal eurBuyAndBccSellRoi = plnMoneyBccExchangedAfterProv.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndBccSellRoiPessimistic = plnMoneyBccExchangedAfterProvPessimistic.divide(PLN_MONEY_TO_EUR_BUY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BCC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBccSellRoi + " {" +
                                 eurBuyAndBccSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndBccSellRoi, resultToDisplay);
        return eurBuyAndBccSellRoi;
    }


    public BigDecimal prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(LtcStockDataInterface bitBayLtcPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                          DashStockDataInterface bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData != null && ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            BigDecimal eurNumberAfterLtcSellAfterTradeProvPessimistic = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayLtcPlnStockData, stockTradeProv,
                                                                                                                                        ltcEurStockData);
            //EUR EXTERNAL STOCK -> Dash
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProv(dashEurAbstractStockData, stockTradeProv,
                                                                                                                        eurNumberAfterLtcSellAfterTradeProv);
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic = amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(dashEurAbstractStockData,
                                                                                                                                              stockTradeProv,
                                                                                                                                              eurNumberAfterLtcSellAfterTradeProvPessimistic);
            //BITBAY BTC
            BigDecimal numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromDashSellAfterProvPessimistic = getPlnFromBitBayDashSellPessimistic(bitBayDashPlnStockData,
                                                                                                           numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi + " {" +
                                     bitBayLtcBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println(
                    "LTC BitBay -> " + bitBayLtcPlnStockData.getLastPrice() + " {" + bitBayLtcPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "}" + "LTC" +
                    getStockCodeName() + " -> " +
                    ltcEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # DASH " +
                    getStockCodeName() + " -> " +
                    dashEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " DASH BitBay -> " + bitBayDashPlnStockData.getLastPrice() + " {" + bitBayDashPlnStockData.getBidPrice() + "}");
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO " + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(EthStockDataInterface bitBayEthPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                                      DashStockDataInterface bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData != null && ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            BigDecimal eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, stockTradeProv,
                                                                                                                                        ethEurStockData);
            //EUR EXTERNAL STOCK -> Dash
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProv(dashEurAbstractStockData, stockTradeProv,
                                                                                                                        eurNumberAfterEthSellAfterTradeProv);
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic = amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(dashEurAbstractStockData,
                                                                                                                                              stockTradeProv,
                                                                                                                                              eurNumberAfterEthSellAfterTradeProvPessimistic);
            //BITBAY DASH -> PLN
            BigDecimal numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromDashSellAfterProvPessimistic = getPlnFromBitBayDashSellPessimistic(bitBayDashPlnStockData,
                                                                                                           numberOfDashBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayEthBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi + " {" +
                                     bitBayEthBuyAndBtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.getLastPrice() + " {" + bitBayEthPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "}" + " ETH " +
                    getStockCodeName() + " -> " +
                    ethEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # DASH " +
                    getStockCodeName() + " -> " +
                    dashEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                    " DASH BitBay -> " + bitBayDashPlnStockData.getLastPrice() + " {" + bitBayEthPlnStockData.getBidPrice() + "}");
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(EthStockDataInterface bitBayEthPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                                     LtcStockDataInterface bitBayLtcPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData != null && ethEurStockData.getEthEurStockData() != null) {
            //BITBAY ETH -> EUR EXTERNAL STOCK
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            BigDecimal eurNumberAfterEthSellAfterTradeProvPessimistic = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(bitBayEthPlnStockData, stockTradeProv,
                                                                                                                                        ethEurStockData);
            // EURO -> LTC
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterEthSellAfterTradeProv);
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic = amountOfLtcBoughtFromEuroOnExternalStockPessimistic(ltcEurAbstractStockData, stockTradeProv,
                                                                                                                                   eurNumberAfterEthSellAfterTradeProvPessimistic);
            //BITBAY LTC -> PLN
            BigDecimal numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv);
            BigDecimal numberOfMoneyFromLtcSellAfterProvPessimistic = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProvPessimistic);

            BigDecimal bitBayEthBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            BigDecimal bitBayEthBuyAndLtcSellRoiPessimistic = numberOfMoneyFromLtcSellAfterProvPessimistic.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayEthBuyAndLtcSellRoi + " {" +
                                     bitBayEthBuyAndLtcSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayEthBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLastPrice() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) + " # LTC " +
                               getStockCodeName() + " -> " +
                               ltcEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN) +
                               " LTC BitBay -> " + bitBayLtcPlnStockData.getLastPrice() + " {" + bitBayLtcPlnStockData.getBidPrice() + "}");
            return bitBayEthBuyAndLtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockPlnSellRoi(EthStockDataInterface bitBayEthPlnStockData, EthStockDataInterface ethStockDataInterface) {
        BigDecimal amountOfEthBoughtOnBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData);
        BigDecimal plnFromCoinroomEthSell = getPlnFromCoinroomEthSell(ethStockDataInterface, amountOfEthBoughtOnBitBay);
        BigDecimal bitBayEthBuyAndCoinroomPlnSellRoi = plnFromCoinroomEthSell.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayEthBuyAndCoinroomPlnSellRoi;
        displayDependOnRoi(bitBayEthBuyAndCoinroomPlnSellRoi, resultToDisplay);
        System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLastPrice() + " ETH " + getStockCodeName() + " -> " +
                           ethStockDataInterface.getLastPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN));

        return bitBayEthBuyAndCoinroomPlnSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockPlnSellRoi(BtcStockDataInterface bitBayBtcPlnStockData, BtcStockDataInterface btcPlnStockDataInterface) {
        BigDecimal amountOfBtcBoughtOnBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData);
        BigDecimal plnFromCoinroomBtcSell = getPlnFromExternalStockBtcSell(btcPlnStockDataInterface, amountOfBtcBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayBtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomBtcSell.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayBtcBuyAndExternalStockPlnSellRoi;
        displayDependOnRoi(bitBayBtcBuyAndExternalStockPlnSellRoi, resultToDisplay);
        return bitBayBtcBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockPlnSellRoi(LtcStockDataInterface bitBayLtcPlnStockData, LtcStockDataInterface ltcPlnStockDataInterface) {
        BigDecimal amountOfLtcBoughtOnBitBay = getAmountOfLtcBoughtOnBitBay(bitBayLtcPlnStockData);
        BigDecimal amountOfLtcBoughtOnBitBayPessimistic = getAmountOfLtcBoughtOnBitBayPessimistic(bitBayLtcPlnStockData);
        BigDecimal plnFromCoinroomLtcSell = getPlnFromExternalStockLtcSell(ltcPlnStockDataInterface, amountOfLtcBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_MAKER);
        BigDecimal plnFromCoinroomLtcSellPessimistic = getPlnFromExternalStockLtcSellPessimistic(ltcPlnStockDataInterface, amountOfLtcBoughtOnBitBayPessimistic,
                                                                                                 COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayLtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomLtcSell.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        BigDecimal bitBayLtcBuyAndExternalStockPlnSellRoiPessimistic = plnFromCoinroomLtcSellPessimistic.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayLtcBuyAndExternalStockPlnSellRoi + "{" +
                                 bitBayLtcBuyAndExternalStockPlnSellRoiPessimistic + "}";
        displayDependOnRoi(bitBayLtcBuyAndExternalStockPlnSellRoi, resultToDisplay);
        System.out.println(
                "BCC BitBay -> " + bitBayLtcPlnStockData.getLastPrice() + " {" + bitBayLtcPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "}" + " PLN " +
                getStockCodeName() + " -> " + ltcPlnStockDataInterface.getLastPrice().setScale(2, RoundingMode.HALF_DOWN) + " {" +
                ltcPlnStockDataInterface.getBidPrice().setScale(2, RoundingMode.HALF_DOWN) + "}");
        return bitBayLtcBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayDashBuyToExternalStockPlnSellRoi(DashStockDataInterface bitBayDashPlnStockData, DashStockDataInterface dashPlnStockDataInterface) {
        BigDecimal amountOfDashBoughtOnBitBay = getAmountOfDashBoughtOnBitBay(bitBayDashPlnStockData);
        BigDecimal plnFromExternalStockDashSell = getPlnFromExternalStockDashSell(dashPlnStockDataInterface, amountOfDashBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayDashBuyAndExternalStockPlnSellRoi = plnFromExternalStockDashSell.divide(DASH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI DASH BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayDashBuyAndExternalStockPlnSellRoi;
        displayDependOnRoi(bitBayDashBuyAndExternalStockPlnSellRoi, resultToDisplay);
        return bitBayDashBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayBccBuyToExternalStockPlnSellRoi(BccStockDataInterface bitBayBccPlnStockData, BccStockDataInterface bccPlnStockDataInterface) {
        BigDecimal amountOfBccBoughtOnBitBay = getAmountOfBccBoughtOnBitBay(bitBayBccPlnStockData);
        BigDecimal amountOfBccBoughtOnBitBayPessimistic = getAmountOfBccBoughtOnBitBayPessimistic(bitBayBccPlnStockData);
        BigDecimal plnFromExternalStockBccSell = getPlnFromExternalStockBccSell(bccPlnStockDataInterface, amountOfBccBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_MAKER);
        BigDecimal plnFromExternalStockBccSellPessimistic = getPlnFromExternalStockBccSellPessimistic(bccPlnStockDataInterface, amountOfBccBoughtOnBitBayPessimistic,
                                                                                                      COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayBccBuyAndExternalStockPlnSellRoi = plnFromExternalStockBccSell.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        BigDecimal bitBayBccBuyAndExternalStockPlnSellRoiPessimistic = plnFromExternalStockBccSellPessimistic.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI BCC BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayBccBuyAndExternalStockPlnSellRoi + " {" +
                                 bitBayBccBuyAndExternalStockPlnSellRoiPessimistic + "}";
        displayDependOnRoi(bitBayBccBuyAndExternalStockPlnSellRoi, resultToDisplay);
        System.out.println(
                "BCC BitBay -> " + bitBayBccPlnStockData.getLastPrice() + " {" + bitBayBccPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "}" + " PLN " +
                getStockCodeName() + " -> " + bccPlnStockDataInterface.getLastPrice().setScale(2, RoundingMode.HALF_DOWN) + " {" +
                bccPlnStockDataInterface.getBidPrice().setScale(2, RoundingMode.HALF_DOWN) + "}");
        return bitBayBccBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(BtcStockDataInterface bitBayBtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                                                BigDecimal stockTradeProv) {
        // BTC BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurAbstractStockData);
        BigDecimal eurNumberAfterBtcSellAfterTradeProvPessimistic = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(bitBayBtcPlnStockData, stockTradeProv,
                                                                                                                                    btcEurAbstractStockData);
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterBtcSellAfterTradeProv);
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(
                eurNumberAfterBtcSellAfterTradeProvPessimistic);
        BigDecimal bitBayBtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        BigDecimal bitBayBtcBuyAndEuroSellRoiPessimistic = amountOfPlnAfterWithdrawalToBankAccountAndExchangePessimistic.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayBtcBuyAndEuroSellRoi + " {" +
                                 bitBayBtcBuyAndEuroSellRoiPessimistic.setScale(3, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(bitBayBtcBuyAndEuroSellRoi, resultToDisplay);
        System.out.println(
                "BTC BitBay -> " + bitBayBtcPlnStockData.getLastPrice() + " {" + bitBayBtcPlnStockData.getAskPrice().setScale(3, RoundingMode.HALF_DOWN) + "}" + " BTC " +
                getStockCodeName() + " -> " +
                btcEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, RoundingMode.HALF_DOWN) + " {" +
                btcEurAbstractStockData.getBidPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, RoundingMode.HALF_DOWN) + "}");
        return bitBayBtcBuyAndEuroSellRoi;
    }

    public BigDecimal prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(DashStockDataInterface bitBayDashPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                                 BigDecimal stockTradeProv) {
        // DASH BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterDashSellAfterTradeProv = getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(bitBayDashPlnStockData, stockTradeProv, dashEurAbstractStockData);
        //  EURO EXTERNAL STOCK -> BANK
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterDashSellAfterTradeProv);
        BigDecimal bitBayDashBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(DASH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI DASH BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayDashBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayDashBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("DASH BitBay -> " + bitBayDashPlnStockData.getLastPrice() + " DASH " + getStockCodeName() + " -> " +
                           dashEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        return bitBayDashBuyAndEuroSellRoi;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(LtcStockDataInterface bitBayLtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                                BigDecimal stockTradeProv) {
        // LTC BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurAbstractStockData);
        //  EURO EXTERNAL STOCK -> BANK
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterLtcSellAfterTradeProv);
        BigDecimal bitBayLtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayLtcBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayLtcBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLastPrice() + " DASH " + getStockCodeName() + " -> " +
                           ltcEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        return bitBayLtcBuyAndEuroSellRoi;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellToEuroWithdrawalRoi(EthStockDataInterface bitBayEthPlnStockData, EthStockDataInterface ethEurAbstractStockData,
                                                                                BigDecimal stockTradeProv) {
        // Eth BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurAbstractStockData);
        //  EURO EXTERNAL STOCK -> BANK
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterEthSellAfterTradeProv);
        BigDecimal bitBayEthBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI Eth BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayEthBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayEthBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLastPrice() + " ETH " + getStockCodeName() + " -> " +
                           ethEurAbstractStockData.getLastPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        return bitBayEthBuyAndEuroSellRoi;
    }


    //  ##########################################################################################################################
    //  ##################################################### HELPER METHODS #####################################################
    //  ##########################################################################################################################

    private BigDecimal getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(BigDecimal eurNumberAfterBtcSellAfterTradeProv) {
        BigDecimal amountOfEuro = getEuroAfterMoneyWithdrawalProv(eurNumberAfterBtcSellAfterTradeProv);
        BigDecimal eurPlnExchangeRate = StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = amountOfEuro.multiply(eurPlnExchangeRate);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO));
    }

    private BigDecimal getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(BtcStockDataInterface bitBayBtcPlnStockData, BigDecimal stockTradeProv,
                                                                            BtcStockDataInterface btcEurStockData) {
        //BITBAY BTC
        BigDecimal btcNumberAfterWithdrawFromBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData);
        //EXTERNAL STOCK BTC -> EUR
        BigDecimal eurNumberAfterBtcSell = btcNumberAfterWithdrawFromBitBay.multiply(btcEurStockData.getLastPrice());
        return eurNumberAfterBtcSell.subtract(eurNumberAfterBtcSell.multiply(stockTradeProv));
    }

    private BigDecimal getAmountOfBtcBoughtOnBitBay(BtcStockDataInterface bitBayBtcPlnStockData) {
        BigDecimal numberOfBtcBoughtOnBitBay = BTC_BUY_MONEY.divide(bitBayBtcPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal btcNumberAfterTradeProvision = numberOfBtcBoughtOnBitBay.subtract(numberOfBtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return btcNumberAfterTradeProvision.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfEthBoughtOnBitBay(EthStockDataInterface bitBayEthPlnStockData) {
        BigDecimal numberOfEthBoughtOnBitBay = ETH_BUY_MONEY.divide(bitBayEthPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return ethNumberAfterTradeProvision.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfLtcBoughtOnBitBay(LtcStockDataInterface bitBayLtcPlnStockData) {
        BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(bitBayLtcPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfLtcBoughtOnBitBayPessimistic(LtcStockDataInterface bitBayLtcPlnStockData) {
        BigDecimal numberOfLtcBoughtOnBitBayPessimistic = LTC_BUY_MONEY.divide(bitBayLtcPlnStockData.getAskPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvisionPessimistic = numberOfLtcBoughtOnBitBayPessimistic
                .subtract(numberOfLtcBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));
        return ltcNumberAfterTradeProvisionPessimistic.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfDashBoughtOnBitBay(DashStockDataInterface bitBayDashPlnStockData) {
        BigDecimal numberOfDashBoughtOnBitBay = DASH_BUY_MONEY.divide(bitBayDashPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal dashNumberAfterTradeProvision = numberOfDashBoughtOnBitBay.subtract(numberOfDashBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return dashNumberAfterTradeProvision.subtract(BITBAY_DASH_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfBccBoughtOnBitBay(BccStockDataInterface bitBayBccPlnStockData) {
        BigDecimal numberOfBccBoughtOnBitBay = BTC_BUY_MONEY.divide(bitBayBccPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal BccNumberAfterTradeProvision = numberOfBccBoughtOnBitBay.subtract(numberOfBccBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return BccNumberAfterTradeProvision.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfBccBoughtOnBitBayPessimistic(BccStockDataInterface bitBayBccPlnStockData) {
        BigDecimal numberOfBccBoughtOnBitBayPessimistic = BTC_BUY_MONEY.divide(bitBayBccPlnStockData.getAskPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal bccNumberAfterTradeProvisionPessimistic = numberOfBccBoughtOnBitBayPessimistic
                .subtract(numberOfBccBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));
        return bccNumberAfterTradeProvisionPessimistic.subtract(BITBAY_BCC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(BtcStockDataInterface bitBayBtcPlnStockData, BigDecimal stockTradeProv,
                                                                                       BtcStockDataInterface btcEurStockData) {
        //BITBAY BTC
        BigDecimal numberOfBtcBoughtOnBitBayPessimistic = BTC_BUY_MONEY.divide(bitBayBtcPlnStockData.getAskPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal btcNumberAfterTradeProvisionPessimistic = numberOfBtcBoughtOnBitBayPessimistic
                .subtract(numberOfBtcBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));
        BigDecimal btcNumberAfterWithdrawFromBitBayPessimistic = btcNumberAfterTradeProvisionPessimistic.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK BTC -> EUR
        BigDecimal eurNumberAfterBtcSellPessimistic = btcNumberAfterWithdrawFromBitBayPessimistic.multiply(btcEurStockData.getBidPrice());
        return eurNumberAfterBtcSellPessimistic.subtract(eurNumberAfterBtcSellPessimistic.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(EthStockDataInterface bitBayEthPlnStockData, BigDecimal stockTradeProv,
                                                                            EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal ethNumberAfterWithdrawFromBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSell = ethNumberAfterWithdrawFromBitBay.multiply(ethEurStockData.getLastPrice());
        return eurNumberAfterEthSell.subtract(eurNumberAfterEthSell.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(EthStockDataInterface bitBayEthPlnStockData, BigDecimal stockTradeProv,
                                                                                       EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal numberOfEthBoughtOnBitBayPessimistic = ETH_BUY_MONEY.divide(bitBayEthPlnStockData.getAskPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvisionPessimistic = numberOfEthBoughtOnBitBayPessimistic
                .subtract(numberOfEthBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));
        BigDecimal ethNumberAfterWithdrawFromBitBayPessimistic = ethNumberAfterTradeProvisionPessimistic.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSellPessimistic = ethNumberAfterWithdrawFromBitBayPessimistic.multiply(ethEurStockData.getAskPrice());
        return eurNumberAfterEthSellPessimistic.subtract(eurNumberAfterEthSellPessimistic.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(LtcStockDataInterface bitBayLtcPlnStockData, BigDecimal stockTradeProv,
                                                                            LtcStockDataInterface ltcEurStockData) {
        BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(bitBayLtcPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.getLastPrice());
        return eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(LtcStockDataInterface bitBayLtcPlnStockData, BigDecimal stockTradeProv,
                                                                                       LtcStockDataInterface ltcEurStockData) {
        BigDecimal numberOfLtcBoughtOnBitBayPessimistic = LTC_BUY_MONEY.divide(bitBayLtcPlnStockData.getAskPrice(), 4, RoundingMode.HALF_DOWN);

        BigDecimal ltcNumberAfterTradeProvisionPessimistic = numberOfLtcBoughtOnBitBayPessimistic
                .subtract(numberOfLtcBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));

        BigDecimal ltcNumberAfterWithdrawFromBitBayPessimistic = ltcNumberAfterTradeProvisionPessimistic.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);

        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterLtcSellPessimistic = ltcNumberAfterWithdrawFromBitBayPessimistic.multiply(ltcEurStockData.getAskPrice());
        return eurNumberAfterLtcSellPessimistic.subtract(eurNumberAfterLtcSellPessimistic.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(DashStockDataInterface bitBayDashPlnStockData, BigDecimal stockTradeProv,
                                                                             DashStockDataInterface dashEurStockData) {
        BigDecimal amountOfDashBoughtOnBitBay = DASH_BUY_MONEY.divide(bitBayDashPlnStockData.getLastPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal dashAmountAfterTradeProvision = amountOfDashBoughtOnBitBay.subtract(amountOfDashBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        BigDecimal dashNumberAfterWithdrawFromBitBay = dashAmountAfterTradeProvision.subtract(BITBAY_DASH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterDashSell = dashNumberAfterWithdrawFromBitBay.multiply(dashEurStockData.getLastPrice());
        return eurNumberAfterDashSell.subtract(eurNumberAfterDashSell.multiply(stockTradeProv));
    }

    private BigDecimal amountOfBtcBoughtFromEuroOnExternalStock(BtcStockDataInterface btcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getLastPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(stockTradeProv));
        return getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProv);
    }

    private BigDecimal amountOfBtcBoughtFromEuroOnExternalStockPessimistic(BtcStockDataInterface btcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                           BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfBtcBoughtPessimistic = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getAskPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBtcBoughtAfterTradeProvPessimistic = numberOfBtcBoughtPessimistic.subtract(numberOfBtcBoughtPessimistic.multiply(stockTradeProv));
        return getBtcAfterWithdrawalProv(numberOfBtcBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfLtcBoughtFromEuroOnExternalStock(LtcStockDataInterface ltcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal eurNumberAfterEthSellAfterTradeProv) {
        BigDecimal numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(stockTradeProv));
        return getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProv);
    }

    private BigDecimal amountOfLtcBoughtFromEuroOnExternalStockPessimistic(LtcStockDataInterface ltcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                           BigDecimal eurNumberAfterEthSellAfterTradeProv) {
        BigDecimal numberOfLtcBoughtPessimistic = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getAskPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtAfterTradeProvPessimistic = numberOfLtcBoughtPessimistic.subtract(numberOfLtcBoughtPessimistic.multiply(stockTradeProv));
        return getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfBccBoughtFromEuroOnExternalStock(BccStockDataInterface bccEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > BCC EXTERNAL STOCK
        BigDecimal numberOfBccBought = euroAmount.divide(bccEurAbstractStockData.getLastPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
    }

    private BigDecimal amountOfBccBoughtFromEuroOnExternalStockPessimistic(BccStockDataInterface bccEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > BCC EXTERNAL STOCK
        BigDecimal numberOfBccBoughtPessimistic = euroAmount.divide(bccEurAbstractStockData.getAskPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProvPessimistic = numberOfBccBoughtPessimistic.subtract(numberOfBccBoughtPessimistic.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfEthBoughtFromEuroOnExternalStock(EthStockDataInterface ethEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > ETH EXTERNAL STOCK
        BigDecimal numberOfEthBought = euroAmount.divide(ethEurAbstractStockData.getLastPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEthBoughtAfterTradeProv = numberOfEthBought.subtract(numberOfEthBought.multiply(stockTradeProv));
        return getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProv);
    }

    private BigDecimal amountOfEthBoughtFromEuroOnExternalStockPessimistic(EthStockDataInterface ethEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > ETH EXTERNAL STOCK
        BigDecimal numberOfEthBoughtPessimistic = euroAmount.divide(ethEurAbstractStockData.getAskPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEthBoughtAfterTradeProvPessimistic = numberOfEthBoughtPessimistic.subtract(numberOfEthBoughtPessimistic.multiply(stockTradeProv));
        return getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfDashBoughtFromEuroOnExternalStockAfterProv(DashStockDataInterface dashEurAbstractStockData, BigDecimal stockTradeProv,
                                                                          BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getLastPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(stockTradeProv));
        return getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
    }

    private BigDecimal amountOfDashBoughtFromEuroOnExternalStockAfterProvPessimistic(DashStockDataInterface dashEurAbstractStockData, BigDecimal stockTradeProv,
                                                                                     BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfDashBoughtPessimistic = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getAskPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfDashBoughtAfterTradeProvPessimistic = numberOfDashBoughtPessimistic.subtract(numberOfDashBoughtPessimistic.multiply(stockTradeProv));
        return getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer() {
        BigDecimal eurPlnExchangeRate = StockRoiPreparer.lastSellWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = PLN_MONEY_TO_EUR_BUY.divide(eurPlnExchangeRate, 4, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

    private BigDecimal getPlnFromBitBayBtcSell(BtcStockDataInterface bitBayBtcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayBtcPlnStockData.getLastPrice());
        return numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayBtcSellPessimistic(BtcStockDataInterface bitBayBtcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBtcSellPessimistic = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayBtcPlnStockData.getBidPrice());
        return numberOfMoneyFromBitBayBtcSellPessimistic.subtract((numberOfMoneyFromBitBayBtcSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayBccSell(BccStockDataInterface bitBayBccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(bitBayBccPlnStockData.getLastPrice());
        return numberOfMoneyFromBitBayBccSell.subtract((numberOfMoneyFromBitBayBccSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayBccSellPessimistic(BccStockDataInterface bitBayBccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBccSellPessimistic = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(bitBayBccPlnStockData.getBidPrice());
        return numberOfMoneyFromBitBayBccSellPessimistic.subtract((numberOfMoneyFromBitBayBccSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayLtcSell(LtcStockDataInterface bitBayLctPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayLctPlnStockData.getLastPrice());
        return numberOfMoneyFromBitBayLtcSell.subtract((numberOfMoneyFromBitBayLtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayLtcSellPessimistic(LtcStockDataInterface bitBayLctPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayLtcSellPessimistic = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(bitBayLctPlnStockData.getBidPrice());
        return numberOfMoneyFromBitBayLtcSellPessimistic.subtract((numberOfMoneyFromBitBayLtcSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayEthSell(EthStockDataInterface bitBayEthPlnStockData, BigDecimal numberOfEthBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayEthSell = numberOfEthBoughtWithdrawToBitBayAfterProv.multiply(bitBayEthPlnStockData.getLastPrice());
        return numberOfMoneyFromBitBayEthSell.subtract((numberOfMoneyFromBitBayEthSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromCoinroomEthSell(EthStockDataInterface coinroomEthPlnStockData, BigDecimal numberOfEthBoughtWithdrawToCoinroomAfterProv) {
        BigDecimal numberOfMoneyFromCoinroomEthSell = numberOfEthBoughtWithdrawToCoinroomAfterProv.multiply(coinroomEthPlnStockData.getLastPrice());
        return numberOfMoneyFromCoinroomEthSell.subtract((numberOfMoneyFromCoinroomEthSell.multiply(COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromExternalStockBtcSell(BtcStockDataInterface btcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromBtcSell = numberOfBtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.getLastPrice());
        return numberOfMoneyFromBtcSell.subtract((numberOfMoneyFromBtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockLtcSell(LtcStockDataInterface btcPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromLtcSell = numberOfLtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.getLastPrice());
        return numberOfMoneyFromLtcSell.subtract((numberOfMoneyFromLtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockLtcSellPessimistic(LtcStockDataInterface btcPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToExternalStock,
                                                                 BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromLtcSell = numberOfLtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.getBidPrice());
        return numberOfMoneyFromLtcSell.subtract((numberOfMoneyFromLtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockDashSell(DashStockDataInterface dashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromLtcSell = numberOfDashBoughtWithdrawToExternalStock.multiply(dashPlnStockData.getLastPrice());
        return numberOfMoneyFromLtcSell.subtract((numberOfMoneyFromLtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockBccSell(BccStockDataInterface BccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromLtcSell = numberOfBccBoughtWithdrawToExternalStock.multiply(BccPlnStockData.getLastPrice());
        return numberOfMoneyFromLtcSell.subtract((numberOfMoneyFromLtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockBccSellPessimistic(BccStockDataInterface bccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToExternalStock, BigDecimal provision) {
        BigDecimal numberOfMoneyFromLtcSellPessimistic = numberOfBccBoughtWithdrawToExternalStock.multiply(bccPlnStockData.getBidPrice());
        return numberOfMoneyFromLtcSellPessimistic.subtract((numberOfMoneyFromLtcSellPessimistic.multiply(provision)));
    }

    private BigDecimal getPlnFromBitBayDashSell(DashStockDataInterface bitBayDashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(bitBayDashPlnStockData.getLastPrice());
        return numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayDashSellPessimistic(DashStockDataInterface bitBayDashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayDashSellPessimistic = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(bitBayDashPlnStockData.getBidPrice());
        return numberOfMoneyFromBitBayDashSellPessimistic.subtract((numberOfMoneyFromBitBayDashSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private void displayDependOnRoi(BigDecimal eurBuyAndBtcSellRoi, String resultToDisplay) {
        if (eurBuyAndBtcSellRoi.compareTo(maxRoiValueForWarnDisplay) > 0 || eurBuyAndBtcSellRoi.compareTo(minRoiValueForWarnDisplay) < 0) {
            logger.warn(resultToDisplay);
        } else {
            logger.info(resultToDisplay);
        }
    }

}
