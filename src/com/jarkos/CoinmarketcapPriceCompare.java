package com.jarkos;

import com.jarkos.stock.enums.BtcCurrencyPairEnum;
import com.jarkos.stock.enums.LtcCurrencyPairEnum;
import com.jarkos.stock.enums.StockNameEnum;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jkostrzewa on 2017-09-18.
 */
public class CoinmarketcapPriceCompare {

    private static final Logger logger = Logger.getLogger(CoinmarketcapPriceCompare.class);

    public void compare() {
        logger.trace("*** BTC");
        List<MarketTableRow> btcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin/#markets");
        final Set<String> btcCurrencyPairEnums = Arrays.stream(BtcCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(btcMarketsData, StockNameEnum.BitBay, BtcCurrencyPairEnum.BTCPLN.toString(), btcCurrencyPairEnums);

        logger.trace("*** LTC");
        List<MarketTableRow> ltcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/litecoin/#markets");
        final Set<String> ltcCurrencyParisToCompare = Arrays.stream(LtcCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(ltcMarketsData, StockNameEnum.BitBay, LtcCurrencyPairEnum.LTCPLN.toString(), ltcCurrencyParisToCompare);

    }

    private void compareStockPrice(List<MarketTableRow> marketsData, StockNameEnum mainStockName, String mainCurrencyPair, Set<String> currencyParisToCompare) {
        MarketTableRow mainStockAndCurrencyData = getPriceInUsdByStockAndCurrencyPair(marketsData, mainStockName.name(), mainCurrencyPair);
        final Set<StockNameEnum> stocksToCompare = new HashSet<>(Arrays.asList(StockNameEnum.values()));

        stocksToCompare.stream().forEach(s -> currencyParisToCompare.stream().forEach(c -> {
            MarketTableRow priceInUsdByStockAndCurrencyPair = getPriceInUsdByStockAndCurrencyPair(marketsData, s.toString(), c);
            if (priceInUsdByStockAndCurrencyPair != null) {
                BigDecimal diff = mainStockAndCurrencyData.getPrice().divide(priceInUsdByStockAndCurrencyPair.getPrice(), 3, RoundingMode.HALF_DOWN);
                String resultToDisplay =
                        mainStockAndCurrencyData.getStockName() + " " + mainStockAndCurrencyData.getExchangePair() + " diff " + priceInUsdByStockAndCurrencyPair.getStockName() +
                        " " + priceInUsdByStockAndCurrencyPair.getExchangePair() + ":" + diff;
                if (diff.compareTo(BigDecimal.valueOf(1.045)) > 0) {
                    logger.warn(resultToDisplay);
                } else if (diff.compareTo(BigDecimal.ONE) > 0) {
                    logger.trace(resultToDisplay);
                }
            }
        }));

    }

    List<MarketTableRow> getMarketsData(String url) {
        List<MarketTableRow> marketTableRows = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element marketsTable = doc.getElementById("markets-table");
            Elements tableRows = marketsTable.getElementsByTag("tr");
            tableRows.stream().forEach(element -> {
                Elements allTdElements = element.getElementsByTag("td");
                if (!allTdElements.isEmpty() && (allTdElements.get(4).text().indexOf(',') < 0)) {
                    //                    System.err.println(allTdElements.get(4).text());
                    marketTableRows.add(new MarketTableRow(allTdElements.get(1).text(), allTdElements.get(2).text(),
                                                           new BigDecimal(allTdElements.get(4).text().replace("* ", "").replace(" *", "").replace("*", "").substring(1))));
                }
            });
        } catch (MalformedURLException mue) {
            System.out.println("Ouch - stockName MalformedURLException happened.");
            mue.printStackTrace();
        } catch (java.lang.NumberFormatException nmb) {
            System.out.println("Ouch - stockName NumberFormatException happened.");
            nmb.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
        }

        return marketTableRows;
    }

    MarketTableRow getPriceInUsdByStockAndCurrencyPair(List<MarketTableRow> marketTableRows, String stockCode, String currencyPair) {
        MarketTableRow marketTableRow = null;
        try {
            marketTableRow = marketTableRows.stream().filter(m -> (stockCode.equals(m.getStockName()) && currencyPair.equals(m.getExchangePair()))).findFirst().get();
        } catch (NoSuchElementException nsee) {
            //Do nothing
        }
        return marketTableRow;
    }

    @Getter
    class MarketTableRow {
        String stockName;
        String exchangePair;
        BigDecimal price;

        MarketTableRow(String stockName, String exchangePair, BigDecimal price) {
            this.stockName = stockName;
            this.exchangePair = exchangePair;
            this.price = price;
        }
    }
}
