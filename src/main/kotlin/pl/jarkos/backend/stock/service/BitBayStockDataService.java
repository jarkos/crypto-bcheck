package pl.jarkos.backend.stock.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.log4j.Logger;
import pl.jarkos.backend.file.FileUpdater;
import pl.jarkos.backend.stock.abstractional.api.*;
import pl.jarkos.backend.stock.dto.bitbay.*;
import pl.jarkos.backend.stock.dto.bitbay.general.BitBayStockData;
import pl.jarkos.backend.stock.dto.bitbay.general.Transaction;
import pl.jarkos.backend.stock.service.abstractional.PlnStockDataService;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import static pl.jarkos.backend.config.AppConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV;
import static pl.jarkos.backend.config.AppConfig.bigSpreadMarginDisplayWarnForCompareRoi;

public class BitBayStockDataService implements PlnStockDataService {

    private static final Logger logger = Logger.getLogger(BitBayStockDataService.class);

    private static String BitBayBtcPlnApiURL = "https://bitbay.net/API/Public/BTCPLN/all.json";
    private static String BitBayLtcPlnApiURL = "https://bitbay.net/API/Public/LTCPLN/all.json";
    private static String BitBayBccPlnApiURL = "https://bitbay.net/API/Public/BCCPLN/all.json";
    private static String BitBayEthPlnApiURL = "https://bitbay.net/API/Public/ETHPLN/all.json";
    private static String BitBayDashPlnApiURL = "https://bitbay.net/API/Public/DASHPLN/all.json";
    //    private static String BitBayLiskPlnApiURL = "https://bitbay.net/API/Public/LSKPLN/all.json";

    public String getStockCodeName() {
        return "Bitbay";
    }

    @Override
    public String getBtcPlnApiUrl() {
        return BitBayBtcPlnApiURL;
    }

    @Override
    public String getBccPlnApiUrl() {
        return BitBayBccPlnApiURL;
    }

    @Override
    public String getEthPlnApiUrl() {
        return BitBayEthPlnApiURL;
    }

    @Override
    public String getLtcPlnApiUrl() {
        return BitBayLtcPlnApiURL;
    }

    @Override
    public String getDashPlnApiUrl() {
        return BitBayDashPlnApiURL;
    }

    public BtcStockDataInterface getBtcPlnStockData() {
        BtcStockDataInterface bitBayMarketData = PlnStockDataService.super.getBtcPlnStockData();
        bigSpreadRecognizer(bitBayMarketData, "BTC");
        return bitBayMarketData;
    }

    public LtcStockDataInterface getLtcPlnStockData() {
        LtcStockDataInterface bitBayMarketData = PlnStockDataService.super.getLtcPlnStockData();
        bigSpreadRecognizer(bitBayMarketData, "LTC");
        return bitBayMarketData;
    }

    public BccStockDataInterface getBccPlnStockData() {
        BccStockDataInterface bitBayMarketData = PlnStockDataService.super.getBccPlnStockData();
        bigSpreadRecognizer(bitBayMarketData, "BCC");
        return bitBayMarketData;
    }

    public EthStockDataInterface getEthPlnStockData() {
        EthStockDataInterface bitBayMarketData = PlnStockDataService.super.getEthPlnStockData();
        bigSpreadRecognizer(bitBayMarketData, "ETH");
        return bitBayMarketData;
    }

    public DashStockDataInterface getDashPlnStockData() {
        DashStockDataInterface bitBayMarketData = PlnStockDataService.super.getDashPlnStockData();
        bigSpreadRecognizer(bitBayMarketData, "DASH");
        return bitBayMarketData;
    }

    @Override
    public BtcStockDataInterface getBtcStockData(String res) {
        BitBayStockData bitBayStockData = getMarketDataFromJson(res);
        if (bitBayStockData != null && CollectionUtils.isNotEmpty(bitBayStockData.getTransactions())) {
            addNewBitBayTransactionsToCSV(bitBayStockData);
            return new BitBayBtcStockData(bitBayStockData);
        }
        return null;
    }

    @Override
    public BccStockDataInterface getBccStockData(String res) {
        return new BitBayBccStockData(getMarketDataFromJson(res));
    }

    @Override
    public EthStockDataInterface getEthStockData(String res) {
        return new BitBayEthStockData(getMarketDataFromJson(res));
    }

    @Override
    public LtcStockDataInterface getLtcStockData(String res) {
        return new BitBayLtcStockData(getMarketDataFromJson(res));
    }

    @Override
    public DashStockDataInterface getDashStockData(String res) {
        return new BitBayDashStockData(getMarketDataFromJson(res));
    }

    private BitBayStockData getMarketDataFromJson(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, BitBayStockData.class);
    }

    public static void addNewBitBayTransactionsToCSV(BitBayStockData bitbayStockData) {
        Long lastCsvUpdateTimeStamp = getLastTransactionUpdateTime();
        Collections.sort(bitbayStockData.getTransactions());
        bitbayStockData.getTransactions().forEach(t -> saveDataRowIfNotAvailable(t, lastCsvUpdateTimeStamp));
        System.out.println("Last BitBay BTC price PLN: " + bitbayStockData.getLast());
    }

    private static Long getLastTransactionUpdateTime() {
        long lastTimeStamp = 0L;
        try {
            File file = new File(BIT_BAY_BTC_DATA_REPOSITORY_CSV);
            ReversedLinesFileReader object = new ReversedLinesFileReader(file);
            String fullLine = object.readLine();
            if (fullLine == null) {
                return LocalDateTime.now().minusDays(30).toEpochSecond(ZoneOffset.UTC);
            }
            String[] lineArray = object.readLine().split(",");
            lastTimeStamp = Long.parseLong(lineArray[0]);
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
        //        System.out.println(new DateTime(t.getDate() * 1000L).toString() + " " + res);
        return t.getDate() + "," + t.getPrice() + "," + nf.format(t.getAmount()).replace(",", ".");
    }

    private void bigSpreadRecognizer(GeneralStockDataInterface bitBayStockData, String currency) {
        if (bitBayStockData != null && bitBayStockData.getAskPrice().divide(bitBayStockData.getBidPrice(), 4, RoundingMode.HALF_DOWN).compareTo(bigSpreadMarginDisplayWarnForCompareRoi) > 0) {
            logger.warn(">>> BIG SPREAD na " + getStockCodeName() + " " + currency + ": " +
                    bitBayStockData.getAskPrice().divide(bitBayStockData.getBidPrice(), 4, RoundingMode.HALF_DOWN) + "  " + bitBayStockData.getAskPrice() + "/" +
                    bitBayStockData.getBidPrice());
        }
    }

}
