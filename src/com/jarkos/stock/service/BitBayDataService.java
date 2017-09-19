package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.file.FileUpdater;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;
import com.jarkos.stock.dto.bitbay.general.BitbayBccStockData;
import com.jarkos.stock.dto.bitbay.general.Transaction;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Collections;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.IndicatorsSystemConfig.BIT_BAY_BTC_DATA_REPOSITORIES_CSV;
import static com.jarkos.json.JsonFetcher.UTF_8;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitBayDataService {
    private static String BitBayBtcPlnApiURL = "https://bitbay.net/API/Public/BTCPLN/all.json";
    private static String BitBayLtcPlnApiURL = "https://bitbay.net/API/Public/LTCPLN/all.json";
    private static String BitBayBccPlnApiURL = "https://bitbay.net/API/Public/BCCPLN/all.json";


    public BitbayStockData getBtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBtcPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitBayMarketData(resBitBay);
    }

    public BitbayStockData getLtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayLtcPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitBayMarketData(resBitBay);
    }

    public BitbayBccStockData getBccPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBccPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitbayBccStockData(getBitBayMarketData(resBitBay));
    }

    public String getStockCodeName() {
        return "Bitbay";
    }

    public static void addNewBitBayTransactionsToCSV(BitbayStockData bitbayStockData) {
        Long lastCsvUpdateTimeStamp = getLastTransactionUpdateTime();
        Collections.sort(bitbayStockData.getTransactions());
        bitbayStockData.getTransactions().stream().forEach(t -> saveDataRowIfNotAvailable(t, lastCsvUpdateTimeStamp));
    }


    private static Long getLastTransactionUpdateTime() {
        long lastTimeStamp = 0l;
        try {
            File file = new File(BIT_BAY_BTC_DATA_REPOSITORIES_CSV);
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
            FileUpdater.addResultData(getTransactionDataRowResult(t), BIT_BAY_BTC_DATA_REPOSITORIES_CSV);
        }
    }

    private static String getTransactionDataRowResult(Transaction t) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        String res = t.getDate() + "," + t.getPrice() + "," + nf.format(t.getAmount()).replace(",", ".");
//        System.out.println(new DateTime(t.getDate() * 1000L).toString() + " " + res);
        return res;
    }

    private static BitbayStockData getBitBayMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitbayStockData.class);
    }

}
