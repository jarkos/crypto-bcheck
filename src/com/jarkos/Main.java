package com.jarkos;

import com.jarkos.mail.JavaMailSender;
import com.jarkos.stock.StockDataPreparer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String BIT_BAY_BTC_DATA_REPOSITORIES_CSV = "C:\\Repositories\\BCHECK\\bitbayPLN.csv";
    public static final int HALF_MINUTE_IN_MILLIS = 30000;
    public static Double lastMACD = 0d;
    public static BigDecimal marginRoiNotificationCall = BigDecimal.valueOf(1.05d);
    public static BigDecimal lastHuobiRoiLTC = BigDecimal.valueOf(0d);
    public static BigDecimal lastKrakenRoiLTC = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitstampRoiLTC = BigDecimal.valueOf(0d);
    public static BigDecimal lastKrakenEurRoiBTC = BigDecimal.valueOf(0d);
    public static BigDecimal lastBitstampEurRoiBTC = BigDecimal.valueOf(0d);

    public static void main(String[] args) throws InterruptedException {

        CandlestickChart.start();
        while (true) {
            try {
                StockDataPreparer stockDataPreparer = new StockDataPreparer();
                stockDataPreparer.fetchAndPrintStockData();
            } catch (Exception e) {
                System.out.println("PREPARE DATA EXCEPTION! " + e.getMessage());
            }
            CandlestickChart.refresh();
            List<BigDecimal> internalIndicators = innitInternalIndicatorsList();
            if (lastMACD < -50.0d || internalIndicators.stream().anyMatch(i -> i.compareTo(marginRoiNotificationCall) > 0)) {
                JavaMailSender.sendMail(
                        " MACD BitBay: " + lastMACD.toString() + " Huobi LTC ROI: " + lastHuobiRoiLTC + " Kraken LTC ROI: " + lastKrakenRoiLTC + " Kraken EUR BTC ROI: " +
                        lastKrakenEurRoiBTC + " Bitstamp EUR BTC ROI: " + lastBitstampEurRoiBTC);
            }

            System.err.println("Last BB BTC MACD indicator: " + lastMACD);
            Thread.sleep(HALF_MINUTE_IN_MILLIS);
        }
    }

    private static List<BigDecimal> innitInternalIndicatorsList() {
        return Arrays.asList(lastHuobiRoiLTC, lastKrakenRoiLTC, lastBitstampRoiLTC
                //                             ,lastKrakenEurRoiBTC, lastBitstampEurRoiBTC
                            );
    }

}
