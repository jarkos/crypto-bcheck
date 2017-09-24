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
    public static BigDecimal lastKrakenLtcToBitbayBtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitstampLtcToBitbayBtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastKrakenEurToBtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastKrakenEurToLtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitstampEurToBtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitstampEurToLtcRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayLtcToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayEthToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitbayBtcToKrakenBccToBitbayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitBayBtcBuyKrakenEurSellLtcBuyToBitBayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitBayBtcBuyBitstampEurSellLtcBuyToBitBayPlnRoi = BigDecimal.valueOf(0d);

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
            JavaMailSender.sendMail(" MACD BitBay: " + lastMACD.toString() + " Huobi LTC ROI: " + lastHuobiLtcToBitbayBtcRoi + " Kraken LTC ROI: " + lastKrakenLtcToBitbayBtcRoi +
                                    " Kraken EUR BTC ROI: " + lastKrakenEurToBtcRoi + " Bitstamp EUR BTC ROI: " + lastBitstampEurToBtcRoi + " Bitstamp LTC Bitbay BTC ROI: " +
                                    lastBitstampLtcToBitbayBtcRoi + " Kraken EUR to Bitbay LTC ROI: " + lastKrakenEurToLtcRoi + " Bitstamp Eur to Bitbay LTC ROI: " +
                                    lastBitstampEurToLtcRoi + " Kraken Ltc to Bitbay BBC ROI: " + lastBitbayLtcToKrakenBccToBitbayPlnRoi + " Kraken Eth to Bitbay BBC ROI: " +
                                    lastBitbayEthToKrakenBccToBitbayPlnRoi + " Bitbay Btc to Kraken BBC ROI: " + lastBitbayBtcToKrakenBccToBitbayPlnRoi);
        }
    }

    private static List<BigDecimal> innitInternalIndicatorsList() {
        return Arrays.asList(
                //                lastHuobiLtcToBitbayBtcRoi,
                lastKrakenLtcToBitbayBtcRoi, lastBitstampLtcToBitbayBtcRoi, lastBitbayLtcToKrakenBccToBitbayPlnRoi, lastBitbayEthToKrakenBccToBitbayPlnRoi,
                lastBitbayBtcToKrakenBccToBitbayPlnRoi

                //                , lastKrakenEurToBtcRoi,
                //                lastBitstampEurToBtcRoi,
                //                lastKrakenEurToLtcRoi
                //                lastBitstampEurToLtcRoi
                            );
    }

}
