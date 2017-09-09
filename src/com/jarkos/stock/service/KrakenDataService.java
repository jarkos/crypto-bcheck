package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.stock.dto.AbstractStockData;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.kraken.KrakenStockData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.stock.StockDataPreparer.*;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class KrakenDataService extends AbstractDataSerivce {

    private static String KrakenBtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=XBTEUR";
    private static String KrakenLtcEurApiUrl = "https://api.kraken.com/0/public/Ticker?pair=LTCEUR";

    public static KrakenStockData getKrakenBtcEurStockData() {
        String resKraken = sendRequest(KrakenBtcEurApiUrl);
        return getKrakenMarketData(resKraken);
    }

    public static KrakenStockData getKrakenLtcEurStockData() {
        String resKraken = sendRequest(KrakenLtcEurApiUrl);
        return getKrakenMarketData(resKraken);
    }

    public static BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayStockData bitBayLtcPlnStockData, KrakenStockData krakenBtcEurStockData, BitBayStockData bitBayBtcPlnStockData)
    throws Exception {
        KrakenStockData krakenLtcEurStockData = getKrakenLtcEurStockData();
        if (krakenLtcEurStockData.getResult().getXLTCZEUR() != null) {
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION)));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BIT_BAY_LTC_WITHDRAW_PROV);

            BigDecimal eurNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay
                    .multiply(BigDecimal.valueOf(Float.valueOf(krakenLtcEurStockData.getResult().getXLTCZEUR().getLastTradePrice().get(0))));
            BigDecimal eurNumberAfterLtcSellAfterTradeProv = eurNumberAfterLtcSell.subtract(eurNumberAfterLtcSell.multiply(KRAKEN_MAKER_TRADE_PROV));
            BigDecimal numberOfBtcBought = eurNumberAfterLtcSellAfterTradeProv
                    .divide(BigDecimal.valueOf(Float.valueOf(krakenBtcEurStockData.getResult().getXXBTZEUR().getLastTradePrice().get(0))), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract(numberOfBtcBought.multiply(KRAKEN_MAKER_TRADE_PROV));
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = numberOfBtcBoughtAfterTradeProv.subtract(KRAKEN_BTC_WITHDRAW_PROV);

            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell
                    .subtract((numberOfMoneyFromBitBayBtcSell.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION))));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            System.err.println("ROI LTC BitBay -> Kraken: " + bitBayLtcBuyAndBtcSellRoi);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        throw new Exception("NO Kraken data");
    }

    private static KrakenStockData getKrakenMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, KrakenStockData.class);
    }

    @Override
    public KrakenStockData getLtcEurStockData() {
        KrakenStockData krakenLtcEurStockData = getKrakenLtcEurStockData();
        return krakenLtcEurStockData;
    }
}
