package com.jarkos;

import com.jarkos.mail.JavaMailSender;
import com.jarkos.stock.StockDataPreparer;

import java.math.BigDecimal;

public class Main {

    public static final String BIT_BAY_BTC_DATA_REPOSITORIES_CSV = "C:\\Repositories\\BCHECK\\bitbayPLN.csv";
    public static final int HALF_MINUTE_IN_MILLIS = 30000;
    public static Double lastMACD = 0d;
    public static BigDecimal lastHuobiRoiLTC = BigDecimal.valueOf(0d);
    public static BigDecimal lastKrakenRoiLTC = BigDecimal.valueOf(0d);
    public static BigDecimal marginRoiNotificationCall = BigDecimal.valueOf(1.045d);

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

            if (lastMACD < -50.0d || (lastHuobiRoiLTC.compareTo(marginRoiNotificationCall) > 0) || (lastKrakenRoiLTC.compareTo(marginRoiNotificationCall) > 0)) {
                JavaMailSender.sendMail(" MACD BitBay: " + lastMACD.toString() + " Huobi LTC ROI: " + lastHuobiRoiLTC + " Kraken LTC ROI: " + lastKrakenRoiLTC);
            }

            System.err.println("Last BB BTC MACD indicator: " + lastMACD);
            Thread.sleep(HALF_MINUTE_IN_MILLIS);
        }
    }

}
