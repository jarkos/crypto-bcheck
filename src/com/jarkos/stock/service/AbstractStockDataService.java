package com.jarkos.stock.service;

import com.jarkos.stock.StockRoiPreparer;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.coinroom.CoinroomEthStockStockData;
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

    protected abstract BigDecimal getEuroAfterWithdrawalProv(BigDecimal numberOfEuroToWithdraw);

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToBtcWithdrawalRoi(BitBayLtcStockData bitBayLtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
                                                                               BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBtcSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI LTC BitBay -> " + getStockCodeName() + " BTC : " + bitBayLtcBuyAndBtcSellRoi + " {" + bitBayLtcBuyAndBtcSellRoiPessimistic + "} " + "-> BitBay PLN";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(BitBayLtcStockData bitBayLtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                    BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBccSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi + " {" + bitBayLtcBuyAndBtcSellRoiPessimistic + "}";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " {" + bitBayLtcPlnStockData.getAskPrice().setScale(2, RoundingMode.HALF_DOWN) + "} LTC " +
                               getStockCodeName() + " -> " + ltcEurStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " +
                               getStockCodeName() + " ->" + bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) +
                               " BCC BitBay -> " + bitBayBccPlnStockData.getLast() + " {" + bitBayBccPlnStockData.getBid() + "} ");
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                  BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayEthBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayEthBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay =
                    "ROI ETH BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayEthBuyAndBccSellRoi + " {" + bitBayEthBuyAndBccSellRoiPessimistic + "}";
            displayDependOnRoi(bitBayEthBuyAndBccSellRoi, resultToDisplay);
            System.out.println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " {" + bitBayEthPlnStockData.getAskEthPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "} ETH  " +
                    getStockCodeName() + " -> " + ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() +
                    " ->" + bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                    bitBayBccPlnStockData.getLast() + " {" + bitBayBccPlnStockData.getBid() + ")");
            return bitBayEthBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockSellToEurToBccBitBaySellRoi(BitBayBtcStockData bitBayBtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                    BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayBtcBuyAndBccSellRoiPessimistic = numberOfMoneyFromBccSellAfterProvPessimistic.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi + " {" +
                                     bitBayBtcBuyAndBccSellRoiPessimistic.setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}";
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " {" + bitBayBtcPlnStockData.getAskPrice() + "} BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getLastBccPrice() + " {" + bitBayBtcPlnStockData.getBidPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN) + "}");
            return bitBayBtcBuyAndBccSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyToExternalStockBtcSellOnBitBayRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface abstractBtcEurStockData,
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

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndBtcSellRoiPessimistic = plnMoneyBtcExchangedAfterProvPessimistic.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi + " {" +
                                 eurBuyAndBtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndBtcSellRoi, resultToDisplay);

        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockEthSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, EthStockDataInterface abstractEthEurStockData,
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

        BigDecimal eurBuyAndEthSellRoi = plnMoneyEthExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndEthSellRoiPessimistic = plnMoneyEthExchangedAfterProvPessimistic.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> ETH " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndEthSellRoi + " {" +
                                 eurBuyAndEthSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndEthSellRoi, resultToDisplay);

        return eurBuyAndEthSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(BitBayBtcStockData bitBayBtcPlnStockData,
                                                                                                LtcStockDataInterface ltcEurAbstractStockData,
                                                                                                BitBayLtcStockData bitBayLctPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayBtcBuyAndLtcSellRoiPessimistic = numberOfMoneyFromLtcSellAfterProvPessimistic.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi + " {" +
                                     bitBayBtcBuyAndLtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay);
            System.out.println(
                    "BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " {" + bitBayBtcPlnStockData.getAskPrice().setScale(2, RoundingMode.HALF_DOWN) + "} BTC " +
                    getStockCodeName() + " -> " + btcEurStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() +
                    " ->" + ltcEurAbstractStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                    bitBayLctPlnStockData.getLastLtcPrice() + " {" + bitBayBtcPlnStockData.getBidPrice() + "}");
            return bitBayBtcBuyAndLtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareEuroBuyToExternalStockLtcSellOnBitBayRoi(BitBayLtcStockData bitBayLtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
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

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndLtcSellRoiPessimistic = plnMoneyLtcExchangedAfterProvPessimistic.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi + " {" +
                                 eurBuyAndLtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockBccSellOnBitBayRoi(BitBayBccStockData bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData,
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

        BigDecimal eurBuyAndBccSellRoi = plnMoneyBccExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        BigDecimal eurBuyAndBccSellRoiPessimistic = plnMoneyBccExchangedAfterProvPessimistic.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BCC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBccSellRoi + " {" +
                                 eurBuyAndBccSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(eurBuyAndBccSellRoi, resultToDisplay);
        return eurBuyAndBccSellRoi;
    }


    public BigDecimal prepareBitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi(BitBayLtcStockData bitBayLtcPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                          BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayLtcBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi + " {" +
                                     bitBayLtcBuyAndBtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " {" + bitBayLtcPlnStockData.getAskPrice().setScale(2, RoundingMode.HALF_DOWN) + "}" + "LTC" +
                               getStockCodeName() + " ->" + ltcEurStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " +
                               getStockCodeName() + " ->" + dashEurAbstractStockData.getLastDashPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) +
                               " DASH BitBay -> " + bitBayDashPlnStockData.getLast() + " {" + bitBayDashPlnStockData.getBid() + "}");
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                                      BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
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

            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            BigDecimal bitBayEthBuyAndBtcSellRoiPessimistic = numberOfMoneyFromDashSellAfterProvPessimistic.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi + " {" +
                                     bitBayEthBuyAndBtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println(
                    "ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " {" + bitBayEthPlnStockData.getAskEthPrice().setScale(2, RoundingMode.HALF_DOWN) + "}" + " ETH " +
                    getStockCodeName() + " -> " + ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " +
                    getStockCodeName() + " ->" + dashEurAbstractStockData.getLastDashPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                    bitBayDashPlnStockData.getLast() + " {" + bitBayEthPlnStockData.getBid() + "}");
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                                     BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv) {
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
                                     bitBayEthBuyAndLtcSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
            displayDependOnRoi(bitBayEthBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLtcPlnStockData.getLast() + " {" + bitBayLtcPlnStockData.getBid() + "}");
            return bitBayEthBuyAndLtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockPlnSellRoi(BitBayEthStockData bitBayEthPlnStockData, CoinroomEthStockStockData ethStockDataInterface) {
        BigDecimal amountOfEthBoughtOnBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData);
        BigDecimal plnFromCoinroomEthSell = getPlnFromCoinroomEthSell(ethStockDataInterface, amountOfEthBoughtOnBitBay);
        BigDecimal bitBayEthBuyAndCoinroomPlnSellRoi = plnFromCoinroomEthSell.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayEthBuyAndCoinroomPlnSellRoi;
        displayDependOnRoi(bitBayEthBuyAndCoinroomPlnSellRoi, resultToDisplay);
        return bitBayEthBuyAndCoinroomPlnSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockPlnSellRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface btcPlnStockDataInterface) {
        BigDecimal amountOfBtcBoughtOnBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData);
        BigDecimal plnFromCoinroomBtcSell = getPlnFromExternalStockBtcSell(btcPlnStockDataInterface, amountOfBtcBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayBtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomBtcSell.divide(BTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayBtcBuyAndExternalStockPlnSellRoi;
        displayDependOnRoi(bitBayBtcBuyAndExternalStockPlnSellRoi, resultToDisplay);
        return bitBayBtcBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockPlnSellRoi(BitBayLtcStockData bitBayLtcPlnStockData, LtcStockDataInterface ltcPlnStockDataInterface) {
        BigDecimal amountOfBtcBoughtOnBitBay = getAmountOfLtcBoughtOnBitBay(bitBayLtcPlnStockData);
        BigDecimal plnFromCoinroomLtcSell = getPlnFromExternalStockLtcSell(ltcPlnStockDataInterface, amountOfBtcBoughtOnBitBay, COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER);
        BigDecimal bitBayLtcBuyAndExternalStockPlnSellRoi = plnFromCoinroomLtcSell.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " PLN -> Bank PLN: " + bitBayLtcBuyAndExternalStockPlnSellRoi;
        displayDependOnRoi(bitBayLtcBuyAndExternalStockPlnSellRoi, resultToDisplay);
        return bitBayLtcBuyAndExternalStockPlnSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockSellToEuroWithdrawalRoi(BitBayBtcStockData bitBayBtcPlnStockData, BtcStockDataInterface btcEurAbstractStockData,
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
                                 bitBayBtcBuyAndEuroSellRoiPessimistic.setScale(2, RoundingMode.HALF_DOWN) + "}";
        displayDependOnRoi(bitBayBtcBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLast() + " {" + bitBayBtcPlnStockData.getAskPrice().setScale(2, RoundingMode.HALF_DOWN) + "}" + " BTC " +
                           getStockCodeName() + " -> " + btcEurAbstractStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " {" +
                           btcEurAbstractStockData.getBidPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + "}");
        return bitBayBtcBuyAndEuroSellRoi;
    }

    public BigDecimal prepareBitBayDashBuyToExternalStockSellToEuroWithdrawalRoi(BitBayDashStockData bitBayDashPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                                 BigDecimal stockTradeProv) {
        // DASH BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterDashSellAfterTradeProv = getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(bitBayDashPlnStockData, stockTradeProv, dashEurAbstractStockData);
        //  EURO EXTERNAL STOCK -> BANK
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterDashSellAfterTradeProv);
        BigDecimal bitBayDashBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(DASH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI DASH BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayDashBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayDashBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("DASH BitBay -> " + bitBayDashPlnStockData.getLast() + " DASH " + getStockCodeName() + " -> " +
                           dashEurAbstractStockData.getLastDashPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate));
        return bitBayDashBuyAndEuroSellRoi;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi(BitBayLtcStockData bitBayLtcPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                                BigDecimal stockTradeProv) {
        // LTC BITBAY -> EURO EXTERNAL STOCK
        BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurAbstractStockData);
        //  EURO EXTERNAL STOCK -> BANK
        BigDecimal amountOfPlnAfterWithdrawalToBankAccountAndExchange = getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(eurNumberAfterLtcSellAfterTradeProv);
        BigDecimal bitBayLtcBuyAndEuroSellRoi = amountOfPlnAfterWithdrawalToBankAccountAndExchange.divide(LTC_BUY_MONEY, 3, RoundingMode.HALF_DOWN);

        String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " EURO -> Bank EURO: " + bitBayLtcBuyAndEuroSellRoi;
        displayDependOnRoi(bitBayLtcBuyAndEuroSellRoi, resultToDisplay);
        System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " DASH " + getStockCodeName() + " -> " +
                           ltcEurAbstractStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate));
        return bitBayLtcBuyAndEuroSellRoi;
    }

    //  ##########################################################################################################################
    //  ##################################################### HELPER METHODS #####################################################
    //  ##########################################################################################################################

    private BigDecimal getAmountOfPlnAfterWithdrawalToBankAccountAndExchange(BigDecimal eurNumberAfterBtcSellAfterTradeProv) {
        BigDecimal amountOfEuro = getEuroAfterWithdrawalProv(eurNumberAfterBtcSellAfterTradeProv);
        BigDecimal eurPlnExchangeRate = StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = amountOfEuro.multiply(eurPlnExchangeRate);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO));
    }

    private BigDecimal getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv,
                                                                            BtcStockDataInterface btcEurStockData) {
        //BITBAY BTC
        BigDecimal btcNumberAfterWithdrawFromBitBay = getAmountOfBtcBoughtOnBitBay(bitBayBtcPlnStockData);
        //EXTERNAL STOCK BTC -> EUR
        BigDecimal eurNumberAfterBtcSell = btcNumberAfterWithdrawFromBitBay.multiply(btcEurStockData.getLastBtcPrice());
        return eurNumberAfterBtcSell.subtract(eurNumberAfterBtcSell.multiply(stockTradeProv));
    }

    private BigDecimal getAmountOfBtcBoughtOnBitBay(BitBayBtcStockData bitBayBtcPlnStockData) {
        BigDecimal numberOfBtcBoughtOnBitBay = BTC_BUY_MONEY.divide(bitBayBtcPlnStockData.getLastBtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal btcNumberAfterTradeProvision = numberOfBtcBoughtOnBitBay.subtract(numberOfBtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return btcNumberAfterTradeProvision.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfEthBoughtOnBitBay(BitBayEthStockData bitBayEthPlnStockData) {
        BigDecimal numberOfEthBoughtOnBitBay = ETH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayEthPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvision = numberOfEthBoughtOnBitBay.subtract(numberOfEthBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return ethNumberAfterTradeProvision.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getAmountOfLtcBoughtOnBitBay(BitBayLtcStockData bitBayLtcPlnStockData) {
        BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(bitBayLtcPlnStockData.getLastLtcPrice(), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        return ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
    }

    private BigDecimal getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStockPessimistic(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal stockTradeProv,
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

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(BitBayEthStockData bitBayEthPlnStockData, BigDecimal stockTradeProv,
                                                                            EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal ethNumberAfterWithdrawFromBitBay = getAmountOfEthBoughtOnBitBay(bitBayEthPlnStockData);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSell = ethNumberAfterWithdrawFromBitBay.multiply(ethEurStockData.getLastEthPrice());
        return eurNumberAfterEthSell.subtract(eurNumberAfterEthSell.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyEthBitbayAndSellForEuroOnExternalStockPessimistic(BitBayEthStockData bitBayEthPlnStockData, BigDecimal stockTradeProv,
                                                                                       EthStockDataInterface ethEurStockData) {
        //BITBAY ETH
        BigDecimal numberOfEthBoughtOnBitBayPessimistic = ETH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayEthPlnStockData.getAsk()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ethNumberAfterTradeProvisionPessimistic = numberOfEthBoughtOnBitBayPessimistic
                .subtract(numberOfEthBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));
        BigDecimal ethNumberAfterWithdrawFromBitBayPessimistic = ethNumberAfterTradeProvisionPessimistic.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK ETH -> EURO
        BigDecimal eurNumberAfterEthSellPessimistic = ethNumberAfterWithdrawFromBitBayPessimistic.multiply(ethEurStockData.getAskEthPrice());
        return eurNumberAfterEthSellPessimistic.subtract(eurNumberAfterEthSellPessimistic.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv,
                                                                            LtcStockDataInterface ltcEurStockData) {
        BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(ltcEurStockData.getLastLtcPrice());
        return eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStockPessimistic(BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv,
                                                                                       LtcStockDataInterface ltcEurStockData) {
        BigDecimal numberOfLtcBoughtOnBitBayPessimistic = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getAsk()), 4, RoundingMode.HALF_DOWN);

        BigDecimal ltcNumberAfterTradeProvisionPessimistic = numberOfLtcBoughtOnBitBayPessimistic
                .subtract(numberOfLtcBoughtOnBitBayPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER));

        BigDecimal ltcNumberAfterWithdrawFromBitBayPessimistic = ltcNumberAfterTradeProvisionPessimistic.subtract(BITBAY_LTC_WITHDRAW_PROV_AMOUNT);

        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterLtcSellPessimistic = ltcNumberAfterWithdrawFromBitBayPessimistic.multiply(ltcEurStockData.getAskPrice());
        return eurNumberAfterLtcSellPessimistic.subtract(eurNumberAfterLtcSellPessimistic.multiply(stockTradeProv));
    }

    private BigDecimal getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv,
                                                                             DashStockDataInterface dashEurStockData) {
        BigDecimal amountOfDashBoughtOnBitBay = DASH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal dashAmountAfterTradeProvision = amountOfDashBoughtOnBitBay.subtract(amountOfDashBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER));
        BigDecimal dashNumberAfterWithdrawFromBitBay = dashAmountAfterTradeProvision.subtract(BITBAY_DASH_WITHDRAW_PROV_AMOUNT);
        //EXTERNAL STOCK LTC to EUR -> EUR to DASH
        BigDecimal eurNumberAfterDashSell = dashNumberAfterWithdrawFromBitBay.multiply(dashEurStockData.getLastDashPrice());
        return eurNumberAfterDashSell.subtract(eurNumberAfterDashSell.multiply(stockTradeProv));
    }

    private BigDecimal amountOfBtcBoughtFromEuroOnExternalStock(BtcStockDataInterface btcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv.divide(btcEurAbstractStockData.getLastBtcPrice(), 5, RoundingMode.HALF_DOWN);
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
        BigDecimal numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastLtcPrice(), 5, RoundingMode.HALF_DOWN);
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
        BigDecimal numberOfBccBought = euroAmount.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
    }

    private BigDecimal amountOfBccBoughtFromEuroOnExternalStockPessimistic(BccStockDataInterface bccEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > BCC EXTERNAL STOCK
        BigDecimal numberOfBccBoughtPessimistic = euroAmount.divide(bccEurAbstractStockData.getAskBccPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProvPessimistic = numberOfBccBoughtPessimistic.subtract(numberOfBccBoughtPessimistic.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfEthBoughtFromEuroOnExternalStock(EthStockDataInterface ethEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > ETH EXTERNAL STOCK
        BigDecimal numberOfEthBought = euroAmount.divide(ethEurAbstractStockData.getLastEthPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEthBoughtAfterTradeProv = numberOfEthBought.subtract(numberOfEthBought.multiply(stockTradeProv));
        return getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProv);
    }

    private BigDecimal amountOfEthBoughtFromEuroOnExternalStockPessimistic(EthStockDataInterface ethEurAbstractStockData, BigDecimal stockTradeProv, BigDecimal euroAmount) {
        // EURO - > ETH EXTERNAL STOCK
        BigDecimal numberOfEthBoughtPessimistic = euroAmount.divide(ethEurAbstractStockData.getAskEthPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEthBoughtAfterTradeProvPessimistic = numberOfEthBoughtPessimistic.subtract(numberOfEthBoughtPessimistic.multiply(stockTradeProv));
        return getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProvPessimistic);
    }

    private BigDecimal amountOfDashBoughtFromEuroOnExternalStockAfterProv(DashStockDataInterface dashEurAbstractStockData, BigDecimal stockTradeProv,
                                                                          BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getLastDashPrice(), 5, RoundingMode.HALF_DOWN);
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
        BigDecimal numberOfEurAfterExchange = MONEY_TO_EUR_BUY.divide(eurPlnExchangeRate, 4, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

    private BigDecimal getPlnFromBitBayBtcSell(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        return numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayBtcSellPessimistic(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBtcSellPessimistic = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getBid()));
        return numberOfMoneyFromBitBayBtcSellPessimistic.subtract((numberOfMoneyFromBitBayBtcSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayBccSell(BitBayBccStockData bitBayBccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
        return numberOfMoneyFromBitBayBccSell.subtract((numberOfMoneyFromBitBayBccSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayBccSellPessimistic(BitBayBccStockData bitBayBccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBccSellPessimistic = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getBid()));
        return numberOfMoneyFromBitBayBccSellPessimistic.subtract((numberOfMoneyFromBitBayBccSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayLtcSell(BitBayLtcStockData bitBayLctPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLctPlnStockData.getLast()));
        return numberOfMoneyFromBitBayLtcSell.subtract((numberOfMoneyFromBitBayLtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayLtcSellPessimistic(BitBayLtcStockData bitBayLctPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayLtcSellPessimistic = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLctPlnStockData.getBid()));
        return numberOfMoneyFromBitBayLtcSellPessimistic.subtract((numberOfMoneyFromBitBayLtcSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromBitBayEthSell(BitBayEthStockData bitBayEthPlnStockData, BigDecimal numberOfEthBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayEthSell = numberOfEthBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayEthPlnStockData.getLast()));
        return numberOfMoneyFromBitBayEthSell.subtract((numberOfMoneyFromBitBayEthSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromCoinroomEthSell(CoinroomEthStockStockData coinroomEthPlnStockData, BigDecimal numberOfEthBoughtWithdrawToCoinroomAfterProv) {
        BigDecimal numberOfMoneyFromCoinroomEthSell = numberOfEthBoughtWithdrawToCoinroomAfterProv.multiply(BigDecimal.valueOf(coinroomEthPlnStockData.getLast()));
        return numberOfMoneyFromCoinroomEthSell.subtract((numberOfMoneyFromCoinroomEthSell.multiply(COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private BigDecimal getPlnFromExternalStockBtcSell(BtcStockDataInterface btcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromBtcSell = numberOfBtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.getLastBtcPrice());
        return numberOfMoneyFromBtcSell.subtract((numberOfMoneyFromBtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromExternalStockLtcSell(LtcStockDataInterface btcPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToExternalStock, BigDecimal makerProvision) {
        BigDecimal numberOfMoneyFromLtcSell = numberOfLtcBoughtWithdrawToExternalStock.multiply(btcPlnStockData.getLastLtcPrice());
        return numberOfMoneyFromLtcSell.subtract((numberOfMoneyFromLtcSell.multiply(makerProvision)));
    }

    private BigDecimal getPlnFromBitBayDashSell(BitBayDashStockData bitBayDashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
        return numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER)));
    }

    private BigDecimal getPlnFromBitBayDashSellPessimistic(BitBayDashStockData bitBayDashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayDashSellPessimistic = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getBid()));
        return numberOfMoneyFromBitBayDashSellPessimistic.subtract((numberOfMoneyFromBitBayDashSellPessimistic.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER)));
    }

    private void displayDependOnRoi(BigDecimal eurBuyAndBtcSellRoi, String resultToDisplay) {
        if (eurBuyAndBtcSellRoi.compareTo(maxMarginCompareWarnDisplayRoi) > 0 || eurBuyAndBtcSellRoi.compareTo(minMarginCompareWarnDisplayRoi) < 0) {
            logger.warn(resultToDisplay);
        } else {
            logger.info(resultToDisplay);
        }
    }

}
