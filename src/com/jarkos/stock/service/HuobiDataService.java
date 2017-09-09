package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.RequestSender;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.huobi.HuobiStockData;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.jarkos.stock.StockDataPreparer.*;


/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class HuobiDataService {

    private static String HuobiBtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_btc_json.js";
    private static String HuobiLtcCnyApiUrl = "http://api.huobi.com/staticmarket/detail_ltc_json.js";

    public static HuobiStockData getHuobiBtcCnyStockData() {
        String resHuobi = RequestSender.sendRequest(HuobiBtcCnyApiUrl);
        return getHuobiMarketData(resHuobi);
    }

    public static BigDecimal prepareBitBayLtcBuyAndBtcSellRoi(BitBayStockData bitBayLtcPlnStockData, HuobiStockData huobiBtcCnyStockData, BitBayStockData bitBayBtcPlnStockData)
    throws Exception {
        String resHuobi = RequestSender.sendRequest(HuobiLtcCnyApiUrl);
        HuobiStockData huobiLtcCnyStockData = getHuobiMarketData(resHuobi);
        if (huobiLtcCnyStockData != null) {
            BigDecimal numberOfLtcBoughtOnBitBay = LTC_BUY_MONEY.divide(BigDecimal.valueOf(bitBayLtcPlnStockData.getLast()), 2, RoundingMode.HALF_DOWN);
            BigDecimal ltcNumberAfterTradeProvision = numberOfLtcBoughtOnBitBay.subtract(numberOfLtcBoughtOnBitBay.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION)));
            BigDecimal ltcNumberAfterWithdrawFromBitBay = ltcNumberAfterTradeProvision.subtract(BIT_BAY_LTC_WITHDRAW_PROV);

            BigDecimal yenNumberAfterLtcSell = ltcNumberAfterWithdrawFromBitBay.multiply(BigDecimal.valueOf(huobiLtcCnyStockData.getPLast()));
            BigDecimal yenNumberAfterLtcSellAfterTradeProv = yenNumberAfterLtcSell.subtract(yenNumberAfterLtcSell.multiply(HUOBI_TRADE_PROVISION));
            BigDecimal numberOfBtcBought = yenNumberAfterLtcSellAfterTradeProv.divide(BigDecimal.valueOf(huobiBtcCnyStockData.getPLast()), 5, RoundingMode.HALF_DOWN);
            BigDecimal numberOfBtcBoughtAfterTradeProv = numberOfBtcBought.subtract((numberOfBtcBought.multiply(HUOBI_TRADE_PROVISION)));
            BigDecimal numberOfBtcBoughtWithdrawToBitBayAfterProv = numberOfBtcBoughtAfterTradeProv.subtract((numberOfBtcBoughtAfterTradeProv.multiply(HUOBI_WITHDRAW_PROV)));

            BigDecimal numberOfMoneyFromBitBayBtcSell = numberOfBtcBoughtWithdrawToBitBayAfterProv.multiply(BigDecimal.valueOf(bitBayBtcPlnStockData.getLast()));
            BigDecimal numberOfMoneyFromBtcSellAfterProv = numberOfMoneyFromBitBayBtcSell
                    .subtract((numberOfMoneyFromBitBayBtcSell.multiply(BigDecimal.valueOf(BIT_BAY_TRADE_PROVISION))));
            BigDecimal bitBayLtcBuyAndBtcSellRoi = numberOfMoneyFromBtcSellAfterProv.divide(LTC_BUY_MONEY, 4, RoundingMode.HALF_DOWN);
            System.err.println("ROI LTC BitBay -> Huobi: " + bitBayLtcBuyAndBtcSellRoi);
            return bitBayLtcBuyAndBtcSellRoi;
        }
        throw new Exception("NO Huobi data");
    }

    private static HuobiStockData getHuobiMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, HuobiStockData.class);
    }

}
