package com.jarkos.stock.service;

import com.jarkos.stock.StockRoiPreparer;
import com.jarkos.stock.abstractional.api.*;
import com.jarkos.stock.dto.bitbay.*;
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
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            // EURO EXTERNAL STOCK -> BTC
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = amountOfBtcBoughtFromEuroOnExternalStock(btcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterLtcSellAfterTradeProv);
            // BITBAY BTC -> PLN
            BigDecimal numberOfMoneyFromBtcSellAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " BTC : " + bitBayLtcBuyAndBtcSellRoi + "-> BitBay PLN";
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayLtcBuyToExternalStockSellToEurToBccBitBaySellRoi(BitBayLtcStockData bitBayLtcPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                    BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        LtcStockDataInterface ltcEurStockData = getLtcEurStockData();
        if (ltcEurStockData.getLtcEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = getEuroFromBuyLtcBitbayAndSellForEuroOnExternalStock(bitBayLtcPlnStockData, stockTradeProv, ltcEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterLtcSellAfterTradeProv);
            //BITBAY BCC -> PLN
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBccSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellAndBccSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, BccStockDataInterface bccEurAbstractStockData,
                                                                                  BitBayBccStockData bitBayBccPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterEthSellAfterTradeProv);
            //BITBAY BCC
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayEthBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayEthBuyAndBccSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBccSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getLast());
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
            // EURO - > BCC EXTERNAL STOCK
            BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv = amountOfBccBoughtFromEuroOnExternalStock(bccEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterBtcSellAfterTradeProv);
            //BITBAY BCC -> PLN
            BigDecimal numberOfMoneyFromBccSellAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayBtcBuyAndBccSellRoi = numberOfMoneyFromBccSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " BCC -> Bitbay PLN : " + bitBayBtcBuyAndBccSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndBccSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # BCC " + getStockCodeName() + " ->" +
                               bccEurAbstractStockData.getLastBccPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " BCC BitBay -> " +
                               bitBayBccPlnStockData.getBid());
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
        // BITBAY BTC -> PLN
        BigDecimal plnMoneyBtcExchangedAfterProv = getPlnFromBitBayBtcSell(bitBayBtcPlnStockData, numberOfBtcWithdrawAfterProv);

        BigDecimal eurBuyAndBtcSellRoi = plnMoneyBtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> BTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndBtcSellRoi;
        displayDependOnRoi(eurBuyAndBtcSellRoi, resultToDisplay);

        return eurBuyAndBtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockEthSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, EthStockDataInterface abstractEthEurStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();
        //EUR EXTERNAL STOCK -> ETH
        BigDecimal numberOfEthWithdrawAfterProv = amountOfEthBoughtFromEuroOnExternalStock(abstractEthEurStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY BTC -> PLN
        BigDecimal plnMoneyEthExchangedAfterProv = getPlnFromBitBayEthSell(bitBayEthPlnStockData, numberOfEthWithdrawAfterProv);

        BigDecimal eurBuyAndEthSellRoi = plnMoneyEthExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> ETH " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndEthSellRoi;
        displayDependOnRoi(eurBuyAndEthSellRoi, resultToDisplay);

        return eurBuyAndEthSellRoi;
    }

    public BigDecimal prepareBitBayBtcBuyToExternalStockEurSellToLtcBuyWithdrawalToBitBayPlnRoi(BitBayBtcStockData bitBayBtcPlnStockData,
                                                                                                LtcStockDataInterface ltcEurAbstractStockData,
                                                                                                BitBayLtcStockData bitBayLctPlnStockData, BigDecimal stockTradeProv) {
        BtcStockDataInterface btcEurStockData = getBtcEurStockData();
        if (btcEurStockData.getBtcEurStockData() != null) {
            // BTC BITBAY -> EURO EXTERNAL STOCK
            BigDecimal eurNumberAfterBtcSellAfterTradeProv = getEuroFromBuyBtcBitbayAndSellForEuroOnExternalStock(bitBayBtcPlnStockData, stockTradeProv, btcEurStockData);
            // EURO - > LTC EXTERNAL STOCK
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterBtcSellAfterTradeProv);
            //BITBAY LTC -> PLN
            BigDecimal numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLctPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayBtcBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(BTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI BTC BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayBtcBuyAndLtcSellRoi;
            displayDependOnRoi(bitBayBtcBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("BTC BitBay -> " + bitBayBtcPlnStockData.getLastBtcPrice() + " BTC " + getStockCodeName() + " -> " +
                               btcEurStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLctPlnStockData.getBid());
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
        // BITBAY LTC -> PLN
        BigDecimal plnMoneyLtcExchangedAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcWithdrawAfterProv);

        BigDecimal eurBuyAndLtcSellRoi = plnMoneyLtcExchangedAfterProv.divide(MONEY_TO_EUR_BUY, 4, RoundingMode.HALF_DOWN);
        String resultToDisplay = "ROI EUR Walutomat -> LTC " + getStockCodeName() + " -> Bitbay PLN: " + eurBuyAndLtcSellRoi;
        displayDependOnRoi(eurBuyAndLtcSellRoi, resultToDisplay);
        return eurBuyAndLtcSellRoi;
    }

    public BigDecimal prepareEuroBuyToExternalStockBccSellOnBitBayRoi(BitBayBccStockData bitBayBccPlnStockData, BccStockDataInterface abstractBccEurStockData,
                                                                      BigDecimal stockTradeProv) {
        // WALUTOMAT & BANK -> EUR
        BigDecimal numberOfEurExchangedOnWalutomatAfterProv = getAmountOfEuroAfterExchangeAndSepaTransfer();

        //EUR EXTERNAL STOCK -> BCC
        BigDecimal numberOfBccWithdrawAfterProv = amountOfBccBoughtFromEuroOnExternalStock(abstractBccEurStockData, stockTradeProv, numberOfEurExchangedOnWalutomatAfterProv);
        // BITBAY BCC -> PLN
        BigDecimal plnMoneyBccExchangedAfterProv = getPlnFromBitBayBccSell(bitBayBccPlnStockData, numberOfBccWithdrawAfterProv);

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
            //BITBAY BTC
            BigDecimal numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI LTC BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayLtcBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayLtcBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("LTC BitBay -> " + bitBayLtcPlnStockData.getLast() + " LTC " + getStockCodeName() + " -> " +
                               ltcEurStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                               bitBayDashPlnStockData.getLast());
            return bitBayLtcBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockSellEuroToDashSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, DashStockDataInterface dashEurAbstractStockData,
                                                                                      BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY LTC
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            //EUR EXTERNAL STOCK -> Dash
            BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv = amountOfDashBoughtFromEuroOnExternalStockAfterProvs(dashEurAbstractStockData, stockTradeProv,
                                                                                                                         eurNumberAfterEthSellAfterTradeProv);
            //BITBAY DASH -> PLN
            BigDecimal numberOfMoneyFromDashSellAfterProv = getPlnFromBitBayDashSell(bitBayDashPlnStockData, numberOfDashBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayEthBuyAndBtcSellRoi = numberOfMoneyFromDashSellAfterProv.divide(ETH_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " DASH -> Bitbay PLN : " + bitBayEthBuyAndBtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndBtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # DASH " + getStockCodeName() + " ->" +
                               dashEurAbstractStockData.getLastDashPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " DASH BitBay -> " +
                               bitBayDashPlnStockData.getLast());
            return bitBayEthBuyAndBtcSellRoi;
        }
        System.out.println("NO" + getStockCodeName() + " data");
        return BigDecimal.ZERO;
    }

    public BigDecimal prepareBitBayEthBuyToExternalStockEuroSellToLtcSellOnBitBayRoi(BitBayEthStockData bitBayEthPlnStockData, LtcStockDataInterface ltcEurAbstractStockData,
                                                                                     BitBayLtcStockData bitBayLtcPlnStockData, BigDecimal stockTradeProv) {
        EthStockDataInterface ethEurStockData = getEthEurStockData();
        if (ethEurStockData.getEthEurStockData() != null) {
            //BITBAY ETH -> EUR EXTERNAL STOCK
            BigDecimal eurNumberAfterEthSellAfterTradeProv = getEuroFromBuyEthBitbayAndSellForEuroOnExternalStock(bitBayEthPlnStockData, stockTradeProv, ethEurStockData);
            // EURO -> LTC
            BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv = amountOfLtcBoughtFromEuroOnExternalStock(ltcEurAbstractStockData, stockTradeProv,
                                                                                                             eurNumberAfterEthSellAfterTradeProv);
            //BITBAY LTC -> PLN
            BigDecimal numberOfMoneyFromLtcSellAfterProv = getPlnFromBitBayLtcSell(bitBayLtcPlnStockData, numberOfLtcBoughtWithdrawToBitBayAfterProv);

            BigDecimal bitBayEthBuyAndLtcSellRoi = numberOfMoneyFromLtcSellAfterProv.divide(ETH_BUY_MONEY, 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = "ROI ETH BitBay -> " + getStockCodeName() + " LTC -> Bitbay PLN : " + bitBayEthBuyAndLtcSellRoi;
            displayDependOnRoi(bitBayEthBuyAndLtcSellRoi, resultToDisplay);
            System.out.println("ETH BitBay -> " + bitBayEthPlnStockData.getLast() + " ETH " + getStockCodeName() + " -> " +
                               ethEurStockData.getLastEthPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " # LTC " + getStockCodeName() + " ->" +
                               ltcEurAbstractStockData.getLastLtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate) + " LTC BitBay -> " +
                               bitBayLtcPlnStockData.getLast());
            return bitBayEthBuyAndLtcSellRoi;
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
                           btcEurAbstractStockData.getLastBtcPrice().multiply(StockRoiPreparer.lastBuyWalutomatEurPlnExchangeRate));
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

    private BigDecimal getEuroFromBuyDashBitbayAndSellForEuroOnExternalStock(BitBayDashStockData bitBayDashPlnStockData, BigDecimal stockTradeProv,
                                                                             DashStockDataInterface dashEurStockData) {
        BigDecimal amountOfDashBoughtOnBitBay = DASH_BUY_MONEY.divide(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()), 4, RoundingMode.HALF_DOWN);
        BigDecimal dashAmountAfterTradeProvision = amountOfDashBoughtOnBitBay.subtract(amountOfDashBoughtOnBitBay.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE));
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

    private BigDecimal amountOfLtcBoughtFromEuroOnExternalStock(LtcStockDataInterface ltcEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal eurNumberAfterEthSellAfterTradeProv) {
        BigDecimal numberOfLtcBought = eurNumberAfterEthSellAfterTradeProv.divide(ltcEurAbstractStockData.getLastLtcPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfLtcBoughtAfterTradeProv = numberOfLtcBought.subtract(numberOfLtcBought.multiply(stockTradeProv));
        return getLtcAfterWithdrawalProv(numberOfLtcBoughtAfterTradeProv);
    }

    private BigDecimal amountOfBccBoughtFromEuroOnExternalStock(BccStockDataInterface bccEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal euroAmount) {
        // EURO - > BCC EXTERNAL STOCK
        BigDecimal numberOfBccBought = euroAmount.divide(bccEurAbstractStockData.getLastBccPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfBccBoughtAfterTradeProv = numberOfBccBought.subtract(numberOfBccBought.multiply(stockTradeProv));
        return getBccAfterWithdrawalProv(numberOfBccBoughtAfterTradeProv);
    }

    private BigDecimal amountOfEthBoughtFromEuroOnExternalStock(EthStockDataInterface ethEurAbstractStockData, BigDecimal stockTradeProv,
                                                                BigDecimal euroAmount) {
        // EURO - > ETH EXTERNAL STOCK
        BigDecimal numberOfEthBought = euroAmount.divide(ethEurAbstractStockData.getLastEthPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfEthBoughtAfterTradeProv = numberOfEthBought.subtract(numberOfEthBought.multiply(stockTradeProv));
        return getEthAfterWithdrawalProv(numberOfEthBoughtAfterTradeProv);
    }

    private BigDecimal amountOfDashBoughtFromEuroOnExternalStockAfterProvs(DashStockDataInterface dashEurAbstractStockData, BigDecimal stockTradeProv,
                                                                           BigDecimal eurNumberAfterLtcSellAfterTradeProv) {
        BigDecimal numberOfDashBought = eurNumberAfterLtcSellAfterTradeProv.divide(dashEurAbstractStockData.getLastDashPrice(), 5, RoundingMode.HALF_DOWN);
        BigDecimal numberOfDashBoughtAfterTradeProv = numberOfDashBought.subtract(numberOfDashBought.multiply(stockTradeProv));
        return getDashAfterWithdrawalProv(numberOfDashBoughtAfterTradeProv);
    }

    private BigDecimal getAmountOfEuroAfterExchangeAndSepaTransfer() {
        BigDecimal eurPlnExchangeRate = StockRoiPreparer.lastSellWalutomatEurPlnExchangeRate;
        BigDecimal numberOfEurAfterExchange = MONEY_TO_EUR_BUY.divide(eurPlnExchangeRate, 4, RoundingMode.HALF_DOWN);
        return (numberOfEurAfterExchange.multiply(WALUTOMAT_WITHDRAW_RATIO)).subtract(ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT.divide(eurPlnExchangeRate, 2, RoundingMode.HALF_DOWN));
    }

    private BigDecimal getPlnFromBitBayBtcSell(BitBayBtcStockData bitBayBtcPlnStockData, BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
        return numberOfMoneyFromBitBayBtcSell.subtract((numberOfMoneyFromBitBayBtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
    }

    private BigDecimal getPlnFromBitBayBccSell(BitBayBccStockData bitBayBccPlnStockData, BigDecimal numberOfBccBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayBccSell = numberOfBccBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBccPlnStockData.getLast()));
        return numberOfMoneyFromBitBayBccSell.subtract((numberOfMoneyFromBitBayBccSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
    }

    private BigDecimal getPlnFromBitBayLtcSell(BitBayLtcStockData bitBayLctPlnStockData, BigDecimal numberOfLtcBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayLtcSell = numberOfLtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayLctPlnStockData.getLast()));
        return numberOfMoneyFromBitBayLtcSell.subtract((numberOfMoneyFromBitBayLtcSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
    }

    private BigDecimal getPlnFromBitBayEthSell(BitBayEthStockData bitBayEthPlnStockData, BigDecimal numberOfEthBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayEthSell = numberOfEthBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayEthPlnStockData.getLast()));
        return numberOfMoneyFromBitBayEthSell.subtract((numberOfMoneyFromBitBayEthSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
    }

    private BigDecimal getPlnFromBitBayDashSell(BitBayDashStockData bitBayDashPlnStockData, BigDecimal numberOfDashBoughtWithdrawToBitBayAfterProv) {
        BigDecimal numberOfMoneyFromBitBayDashSell = numberOfDashBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayDashPlnStockData.getLast()));
        return numberOfMoneyFromBitBayDashSell.subtract((numberOfMoneyFromBitBayDashSell.multiply(BITBAY_TRADE_PROVISION_PERCENTAGE)));
    }

    private void displayDependOnRoi(BigDecimal eurBuyAndBtcSellRoi, String resultToDisplay) {
        if (eurBuyAndBtcSellRoi.compareTo(maxMarginCompareWarnDisplayRoi) > 0 || eurBuyAndBtcSellRoi.compareTo(minMarginCompareWarnDisplayRoi) < 0) {
            logger.warn(resultToDisplay);
        } else {
            logger.info(resultToDisplay);
        }
    }

}
