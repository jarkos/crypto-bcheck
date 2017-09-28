package com.jarkos;

import com.jarkos.mail.JavaMailSender;
import com.jarkos.stock.StockDataPreparer;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.jarkos.config.IndicatorsSystemConfig.HALF_MINUTE_IN_MILLIS;
import static com.jarkos.config.IndicatorsSystemConfig.marginMailNotificationCallForTransferRoi;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    public static final String LAST_BB_BTC_MACD_INDICATOR = "Last BB BTC MACD indicator: ";

    public static Double lastMACD = 0d;

    public static BigDecimal lastHuobiLtcToBitbayBtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToKrakenBtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToKrakenLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToBitstampBtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToBitstampLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayLtcToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayEthToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayBtcToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToExternalStockBccSellOnBitBayRoi = BigDecimal.valueOf(0d);

    public static void main(String[] args) throws InterruptedException {

        CandlestickChart.start();
        while (true) {
            try {
                //
                new StockDataPreparer().fetchAndPrintStockData();
                new CoinmarketcapPriceCompare().compare();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("PREPARE DATA EXCEPTION! " + e.getMessage());
            }
            CandlestickChart.refresh();

            goodIndicatorsMailNotify();

            logger.info(LAST_BB_BTC_MACD_INDICATOR + lastMACD);
            Thread.sleep(2 * HALF_MINUTE_IN_MILLIS);
        }
    }

    private static void goodIndicatorsMailNotify() {
        List<BigDecimal> internalIndicators = innitInternalIndicatorsList();
        if (lastMACD < -250.0d || internalIndicators.stream().anyMatch(i -> i.compareTo(marginMailNotificationCallForTransferRoi) > 0)) {
            JavaMailSender.sendMail(
                    " MACD BitBay: " + lastMACD.toString() + " Huobi LTC ROI: " + lastHuobiLtcToBitbayBtcRoi + " Kraken LTC ROI: " + bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi +
                    " Kraken EUR BTC ROI: " + euroBuyToKrakenBtcSellOnBitBayRoi + " Bitstamp EUR BTC ROI: " + euroBuyToBitstampBtcSellOnBitBayRoi +
                    " Bitstamp LTC Bitbay BTC ROI: " + bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi + " Kraken EUR to Bitbay LTC ROI: " + euroBuyToKrakenLtcSellOnBitBayRoi +
                    " Bitstamp Eur to Bitbay LTC ROI: " + euroBuyToBitstampLtcSellOnBitBayRoi + " Kraken Ltc to Bitbay BBC ROI: " + lastBitbayLtcToKrakenBccToBitbayPlnRoi +
                    " Kraken Eth to Bitbay BBC ROI: " + lastBitbayEthToKrakenBccToBitbayPlnRoi + " Bitbay Btc to Kraken BBC ROI: " + lastBitbayBtcToKrakenBccToBitbayPlnRoi);
        }
    }

    private static List<BigDecimal> innitInternalIndicatorsList() {
        return Arrays.asList(
                //                lastHuobiLtcToBitbayBtcRoi,
                bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi, bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi, lastBitbayLtcToKrakenBccToBitbayPlnRoi,
                lastBitbayEthToKrakenBccToBitbayPlnRoi, lastBitbayBtcToKrakenBccToBitbayPlnRoi

                //                , euroBuyToKrakenBtcSellOnBitBayRoi,
                //                euroBuyToBitstampBtcSellOnBitBayRoi,
                //                euroBuyToKrakenLtcSellOnBitBayRoi
                //                euroBuyToBitstampLtcSellOnBitBayRoi
                            );
    }

}
