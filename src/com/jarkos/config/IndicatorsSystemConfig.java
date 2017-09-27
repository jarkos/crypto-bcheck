package com.jarkos.config;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class IndicatorsSystemConfig {

    public static final String BIT_BAY_BTC_DATA_REPOSITORY_CSV = "C:\\Repositories\\BCHECK\\bitbayPLN.csv";
    public static final int HALF_MINUTE_IN_MILLIS = 30000;
    public static BigDecimal marginMailNotificationCallForTransferRoi = BigDecimal.valueOf(1.045d);
    public static BigDecimal maxMarginCompareWarnDisplayRoi = BigDecimal.valueOf(1.025d);
    public static BigDecimal minMarginCompareWarnDisplayRoi = BigDecimal.valueOf(0.96d);

    public static final BigDecimal BTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal LTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal ETH_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal DASH_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal MONEY_TO_EUR_BUY = BigDecimal.valueOf(1000F);

    public static final BigDecimal BITBAY_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0035F);
    public static final BigDecimal BITBAY_BTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00045F);
    public static final BigDecimal BITBAY_LTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);
    public static final BigDecimal BITBAY_ETH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00126F);
    public static final BigDecimal BITBAY_DASH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.001F);

    public static final BigDecimal BITSTAMP_WITHDRAW_PROV = BigDecimal.valueOf(0F); //NO FEE YEAAH
    public static final BigDecimal BITSTAMP_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0025F);
    public static final BigDecimal BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal HUOBI_TRADE_PROVISION = BigDecimal.valueOf(0.002F);
    public static final BigDecimal HUOBI_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0001F);

    public static final BigDecimal KRAKEN_MAKER_TRADE_PROV_PERCENTAGE = BigDecimal.valueOf(0.0016d);
    public static final BigDecimal KRAKEN_TAKER_TRADE_PROV_PERCENTAGE = BigDecimal.valueOf(0.0026d);
    public static final BigDecimal KRAKEN_BTC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_BCC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_DASH_WITHDRAW_PROV = BigDecimal.valueOf(0.005F);
    public static final BigDecimal KRAKEN_LTC_WITHDRAW_PROV = BigDecimal.valueOf(0.02F);
    public static final BigDecimal KRAKEN_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal WALUTOMAT_WITHDRAW_RATIO = BigDecimal.valueOf(0.998F);
    public static final BigDecimal ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT = BigDecimal.valueOf(5.3F);

    public static final BigDecimal MARKET_MIN_VOLUME_TO_CONSIDER = BigDecimal.valueOf(88000);

}
