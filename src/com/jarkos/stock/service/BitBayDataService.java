package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.file.FileUpdater;
import com.jarkos.stock.dto.bitbay.BitBayStockData;
import com.jarkos.stock.dto.bitbay.Transaction;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Collections;

import static com.jarkos.Main.BIT_BAY_BTC_DATA_REPOSITORIES_CSV;
import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.json.JsonFetcher.UTF_8;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitBayDataService {
    private static String BitBayBtcPlnApiURL = "https://bitbay.net/API/Public/BTCPLN/all.json";
    private static String BitBayLtcPlnApiURL = "https://bitbay.net/API/Public/LTCPLN/all.json";


    public BitBayStockData getBtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBtcPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.printf(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitBayMarketData(resBitBay);
    }

    public BitBayStockData getLtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayLtcPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.printf(e.getMessage().concat(" " + getStockCodeName()));
        }
        return getBitBayMarketData(resBitBay);
    }

    public String getStockCodeName() {
        return "Bitbay";
    }

    public static void addNewBitBayTransactionsToCSV(BitBayStockData bitBayStockData) {
        Long lastCsvUpdateTimeStamp = getLastTransactionUpdateTime();
        Collections.sort(bitBayStockData.getTransactions());
        bitBayStockData.getTransactions().stream().forEach(t -> saveDataRowIfNotAvailable(t, lastCsvUpdateTimeStamp));
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
        System.out.println(new DateTime(t.getDate() * 1000L).toString() + " " + res);
        return res;
    }

    private static BitBayStockData getBitBayMarketData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitBayStockData.class);
    }

}
