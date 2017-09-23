package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.file.FileUpdater;
import com.jarkos.stock.dto.bitbay.*;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;
import com.jarkos.stock.dto.bitbay.general.Transaction;
import com.jarkos.stock.exception.DataFetchUnavailableException;
import org.apache.commons.io.input.ReversedLinesFileReader;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Collections;

import static com.jarkos.RequestSender.sendRequest;
import static com.jarkos.config.IndicatorsSystemConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV;
import static com.jarkos.json.JsonFetcher.UTF_8;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class BitBayDataService {
    private static String BitBayBtcPlnApiURL = "https://bitbay.net/API/Public/BTCPLN/all.json";
    private static String BitBayLtcPlnApiURL = "https://bitbay.net/API/Public/LTCPLN/all.json";
    private static String BitBayBccPlnApiURL = "https://bitbay.net/API/Public/BCCPLN/all.json";
    private static String BitBayEthPlnApiURL = "https://bitbay.net/API/Public/ETHPLN/all.json";
    private static String BitBayDashPlnApiURL = "https://bitbay.net/API/Public/DASHPLN/all.json";

    @Inject
    public BitBayDataService() {
    }


    public BitBayBtcStockData getBtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBtcPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitBayBtcStockData(getBitBayMarketData(resBitBay));
    }

    public BitBayLtcStockData getLtcPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayLtcPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitBayLtcStockData(getBitBayMarketData(resBitBay));
    }

    public BitBayBccStockData getBccPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayBccPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitBayBccStockData(getBitBayMarketData(resBitBay));
    }

    public BitBayEthStockData getEthPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayEthPlnApiURL);
        } catch (DataFetchUnavailableException e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitBayEthStockData(getBitBayMarketData(resBitBay));
    }

    public BitBayDashStockData getDashPlnStockData() {
        String resBitBay = null;
        try {
            resBitBay = sendRequest(BitBayDashPlnApiURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getStockCodeName()));
        }
        return new BitBayDashStockData(getBitBayMarketData(resBitBay));
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

}
