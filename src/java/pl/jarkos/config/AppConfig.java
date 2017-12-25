package pl.jarkos.config;

import java.math.BigDecimal;

public class AppConfig {

    public static final String BIT_BAY_BTC_DATA_REPOSITORY_CSV = "C:\\Repos\\BCHECK\\bitbayPLN.csv";
    public static final String ROI_DATA_REPOSITORY_CSV = "C:\\Repos\\BCHECK\\roi.csv";
    public static final Long HALF_MINUTE_IN_MILLIS = 30000L;
    public static final Long TWENTY_FOUR_HOURS_IN_MILLIS = 86400000L;
    public static final BigDecimal marginTransferRoiValueForMailNotification = BigDecimal.valueOf(1.05d);
    public static final BigDecimal bigSpreadMarginDisplayWarnForCompareRoi = BigDecimal.valueOf(1.01d);
    public static final BigDecimal maxRoiValueForWarnDisplay = BigDecimal.valueOf(1.025d);
    public static final BigDecimal minRoiValueForWarnDisplay = BigDecimal.valueOf(0.955d);

    public static final BigDecimal MONEY_FOR_CRYPTO_BUY = BigDecimal.valueOf(1000F);
    public static final BigDecimal MARKET_MIN_VOLUME_TO_CONSIDER_COMPARING = BigDecimal.valueOf(88000);
    public static final int DAYS_BACK_CSV_FILE_RETENTION = 30;

}
