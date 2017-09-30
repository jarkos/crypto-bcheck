package com.jarkos.config;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-28.
 */
public class AppConfig {

    public static final String BIT_BAY_BTC_DATA_REPOSITORY_CSV = "C:\\Repositories\\BCHECK\\bitbayPLN.csv";
    public static final int HALF_MINUTE_IN_MILLIS = 30000;
    public static final BigDecimal marginMailNotificationCallForTransferRoi = BigDecimal.valueOf(1.05d);
    public static final BigDecimal bigSpreadMarginNotificationCallForTransferRoi = BigDecimal.valueOf(1.01d);
    public static final BigDecimal maxMarginCompareWarnDisplayRoi = BigDecimal.valueOf(1.025d);
    public static final BigDecimal minMarginCompareWarnDisplayRoi = BigDecimal.valueOf(0.965d);

    public static final BigDecimal BTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal LTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal ETH_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal DASH_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal MONEY_TO_EUR_BUY = BigDecimal.valueOf(1000F);

    public static final BigDecimal MARKET_MIN_VOLUME_TO_CONSIDER = BigDecimal.valueOf(88000);

}
