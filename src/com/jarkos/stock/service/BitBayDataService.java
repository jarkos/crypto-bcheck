package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.file.FileUpdater;
import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;
import com.jarkos.stock.dto.bitbay.general.Transaction;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Collections;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.IndicatorsSystemConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV;
import static com.jarkos.config.IndicatorsSystemConfig.bigSpreadMarginNotificationCallForTransferRoi;
import static com.jarkos.json.JsonFetcher.UTF_8;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitBayDataService {

    private static final Logger logger = Logger.getLogger(BitBayDataService.class);

    private static String BitBayBtcPlnApiURL = "https://bitbay.net/API/Public/BTCPLN/all.json";
    private static String BitBayLtcPlnApiURL = "https://bitbay.net/API/Public/LTCPLN/all.json";
    private static String BitBayBccPlnApiURL = "https://bitbay.net/API/Public/BCCPLN/all.json";
    private static String BitBayEthPlnApiURL = "https://bitbay.net/API/Public/ETHPLN/all.json";
    private static String BitBayDashPlnApiURL = "https://bitbay.net/API/Public/DASHPLN/all.json";

    public BitBayBtcStockData getBtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBtcPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        BitBayStockData bitBayMarketData = getBitBayMarketData(resBitBay);
        bigSpreadRecognizer(bitBayMarketData, "BTC");
        return new BitBayBtcStockData(bitBayMarketData);
    }

    public BitBayLtcStockData getLtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayLtcPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        BitBayStockData bitBayMarketData = getBitBayMarketData(resBitBay);
        bigSpreadRecognizer(bitBayMarketData, "LTC");
        return new BitBayLtcStockData(bitBayMarketData);
    }

    public BitBayBccStockData getBccPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBccPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        BitBayStockData bitBayMarketData = getBitBayMarketData(resBitBay);
        bigSpreadRecognizer(bitBayMarketData, "BCC");
        return new BitBayBccStockData(bitBayMarketData);
    }

    public BitBayEthStockData getEthPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayEthPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        BitBayStockData bitBayMarketData = getBitBayMarketData(resBitBay);
        bigSpreadRecognizer(bitBayMarketData, "ETH");
        return new BitBayEthStockData(bitBayMarketData);
    }

    public BitBayDashStockData getDashPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayDashPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        BitBayStockData bitBayMarketData = getBitBayMarketData(resBitBay);
        bigSpreadRecognizer(bitBayMarketData, "DASH");
        return new BitBayDashStockData(bitBayMarketData);
    }

    public String getStockCodeName() {
        return "Bitbay";
    }

    public static void addNewBitBayTransactionsToCSV(BitBayStockData bitbayStockData) {
        Long lastCsvUpdateTimeStamp = getLastTransactionUpdateTime();
        Collections.sort(bitbayStockData.getTransactions());
        bitbayStockData.getTransactions().stream().forEach(t -> saveDataRowIfNotAvailable(t, lastCsvUpdateTimeStamp));
        System.out.println("Last BitBay BTC price PLN: " + bitbayStockData.getLast());
    }


    private static Long getLastTransactionUpdateTime() {
        long lastTimeStamp = 0l;
        try {
            File file = new File(BIT_BAY_BTC_DATA_REPOSITORY_CSV);
            ReversedLinesFileReader object = new ReversedLinesFileReader(file, Charset.forName(UTF_8));
            String[] line = object.readLine().split(",");
            lastTimeStamp = Long.parseLong(line[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastTimeStamp;
    }

    private static void saveDataRowIfNotAvailable(Transaction t, Long lastCsvUpdateTimeStamp) {
        if (t.getDate() > lastCsvUpdateTimeStamp) {
            FileUpdater.addResultData(getTransactionDataRowResult(t), BIT_BAY_BTC_DATA_REPOSITORY_CSV);
        }
    }

    private static String getTransactionDataRowResult(Transaction t) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        String res = t.getDate() + "," + t.getPrice() + "," + nf.format(t.getAmount()).replace(",", ".");
        //        System.out.println(new DateTime(t.getDate() * 1000L).toString() + " " + res);
        return res;
    }

    private static BitBayStockData getBitBayMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitBayStockData.class);
    }

    public void bigSpreadRecognizer(BitBayStockData bitBayStockData, String currency) {
        if (BigDecimal.valueOf(bitBayStockData.getAsk()).divide(BigDecimal.valueOf(bitBayStockData.getBid()), 4, RoundingMode.HALF_DOWN)
                      .compareTo(bigSpreadMarginNotificationCallForTransferRoi) > 0) {
            logger.warn(">>> BIG SPREAD na " + getStockCodeName() + " " + currency + ": " +
                        BigDecimal.valueOf(bitBayStockData.getAsk()).divide(BigDecimal.valueOf(bitBayStockData.getBid()), 4, RoundingMode.HALF_DOWN));
        }
    }

}
