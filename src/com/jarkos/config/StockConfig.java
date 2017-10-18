package com.jarkos.config;

import java.math.BigDecimal;

public class StockConfig {

    public static final BigDecimal BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER = BigDecimal.valueOf(0.0035F);
    public static final BigDecimal BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER = BigDecimal.valueOf(0.0024F);
    public static final BigDecimal BITBAY_BTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00045F);
    public static final BigDecimal BITBAY_BCC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0006F);
    public static final BigDecimal BITBAY_LTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);
    public static final BigDecimal BITBAY_ETH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.00126F);
    public static final BigDecimal BITBAY_DASH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.001F);

    public static final BigDecimal COINROOM_BTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0005F);
    public static final BigDecimal COINROOM_LTC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.001F);
    public static final BigDecimal COINROOM_DASH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.002F);
    public static final BigDecimal COINROOM_ETH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);
    public static final BigDecimal COINROOM_BCC_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.0005F);
    public static final BigDecimal COINROOM_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(2.0F);
    public static final BigDecimal COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER = BigDecimal.valueOf(0.0025F);
    public static final BigDecimal COINROOM_TRADE_PROVISION_PERCENTAGE_MAKER = BigDecimal.valueOf(0.0006F);

    public static final BigDecimal BITSTAMP_WITHDRAW_PROV = BigDecimal.valueOf(0F); //NO FEE YEAAH
    public static final BigDecimal BITSTAMP_TRADE_PROVISION_PERCENTAGE = BigDecimal.valueOf(0.0025F);
    public static final BigDecimal BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal KRAKEN_MAKER_TRADE_PROV_PERCENTAGE = BigDecimal.valueOf(0.0016d);
    public static final BigDecimal KRAKEN_TAKER_TRADE_PROV_PERCENTAGE = BigDecimal.valueOf(0.0026d);
    public static final BigDecimal KRAKEN_BTC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_BCC_WITHDRAW_PROV = BigDecimal.valueOf(0.001F);
    public static final BigDecimal KRAKEN_DASH_WITHDRAW_PROV = BigDecimal.valueOf(0.005F);
    public static final BigDecimal KRAKEN_LTC_WITHDRAW_PROV = BigDecimal.valueOf(0.02F);
    public static final BigDecimal KRAKEN_ETH_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.005F);
    public static final BigDecimal KRAKEN_EUR_WITHDRAW_PROV_AMOUNT = BigDecimal.valueOf(0.09F);

    public static final BigDecimal WALUTOMAT_WITHDRAW_RATIO = BigDecimal.valueOf(0.998F);
    public static final BigDecimal ALIOR_SEPA_WITHDRAW_PLN_PROV_AMOUNT = BigDecimal.valueOf(5.3F);

}
