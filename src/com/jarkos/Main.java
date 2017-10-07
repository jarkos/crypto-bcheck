package com.jarkos;

import com.jarkos.mail.JavaMailSender;
import com.jarkos.stock.StockRoiPreparer;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.jarkos.config.AppConfig.HALF_MINUTE_IN_MILLIS;
import static com.jarkos.config.AppConfig.marginMailNotificationCallForTransferRoi;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static final String LAST_BB_BTC_MACD_INDICATOR = "Last BB BTC MACD indicator: ";

    static Double lastMACD = 0d;
    public static BigDecimal bitBayLtcBuyToKrakenSellToBtcWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToBitstampSellToBtcWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToKrakenBtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToKrakenLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToBitstampBtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToBitstampLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToKrakenSellToEurToBccBitBaySellRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayEthBuyToKrakenSellAndBccSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToKrakenSellToEurToBccBitBaySellRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToKrakenEurSellToLtcBuyWithdrawalToBitBayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToBitstampEurSellToLtcBuyWithdrawalToBitBayPlnRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToKrakenSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToBitstampSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToExternalStockBccSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToEuroSellAndDashSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayDashBuyToKrakenSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayEthBuyToKrakenSellEuroToDashSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayEthBuyToToKrakenEuroSellToLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayEthBuyToBitstampEuroSellToLtcSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToExternalStockSellToEuroWithdrawalRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToKrakenEthSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal euroBuyToBitstampEthSellOnBitBayRoi = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayEthBuyToCoinroomPlnSell = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayBtcBuyToCoinroomPlnSell = BigDecimal.valueOf(0d);
    public static BigDecimal bitBayLtcBuyToCoinroomPlnSell;
    public static BigDecimal bitBayDashBuyToCoinroomPlnSell;

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {
        CandlestickChart.start();
        while (true) {
            try {
                new StockRoiPreparer().fetchAndPrintStockData();
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
        Map<String, BigDecimal> internalIndicators = innitInternalIndicatorsList();

        if (lastMACD < -180.0d || internalIndicators.values().stream().anyMatch(i -> i.compareTo(marginMailNotificationCallForTransferRoi) > 0)) {
            StringBuilder sb = new StringBuilder();
            internalIndicators.forEach((k, v) -> sb.append(k).append(" ").append(v).append(System.getProperty("line.separator")));
            JavaMailSender.sendMail("MACD BitBay: " + lastMACD.toString() + " " + sb.toString());
        }
    }

    private static Map<String, BigDecimal> innitInternalIndicatorsList() {
        Map<String, BigDecimal> indicatorsMap = new HashMap<>();
        Field[] allFields = Main.class.getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isPublic(field.getModifiers()) && field.getType().equals(BigDecimal.class)) {
                BigDecimal indicator;
                try {
                    indicator = (BigDecimal) field.get(BigDecimal.class);
                    indicatorsMap.put(field.getName(), indicator);
                } catch (IllegalAccessException e) {
                    // DO NOTHING, GO AHEAD
                }
            }
        }
        return indicatorsMap;
    }

}
