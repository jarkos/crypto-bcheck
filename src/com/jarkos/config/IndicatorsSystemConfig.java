package com.jarkos.config;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class IndicatorsSystemConfig {

    public static final String BIT_BAY_BTC_DATA_REPOSITORIES_CSV = "C:\\Repositories\\BCHECK\\bitbayPLN.csv";
    public static final int HALF_MINUTE_IN_MILLIS = 30000;

    public static final BigDecimal BTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal LTC_BUY_MONEY = BigDecimal.valueOf(1000F);
    public static final BigDecimal MONEY_TO_EUR_BUY = BigDecimal.valueOf(1000F);

    public static final BigDecimal BITBAY_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0035F);
    public static final BigDecimal BITBAY_BTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00045F);
    public static final BigDecimal BITBAY_LTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);

    public static final BigDecimal BITSTAMP_WITHDRAW_PROV = BigDecimal.valueOf(0F);
    public static final BigDecimal BITSTAMP_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0025F);

    public static final BigDecimal HUOBI_TRADE_PROVISION = BigDecimal.valueOf(0.002F);
    public static final BigDecimal HUOBI_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0001F);

    public static final BigDecimal KRAKEN_MAKER_TRADE_PROV = BigDecimal.valueOf(0.0016F);
    public static final BigDecimal KRAKEN_BTC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_LTC_WITHDRAW_PROV = BigDecimal.valueOf(0.02F);
    public static final BigDecimal KRAKEN_BTC_TO_EUR_TAKER_PROV_PERCENTAGE = BigDecimal.valueOf(0.0026F);
    public static final BigDecimal KRAKEN_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal WALUTOMAT_WITHDRAW_RATIO = BigDecimal.valueOf(0.998F);
    public static final BigDecimal ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT = BigDecimal.valueOf(5.0F);

}
