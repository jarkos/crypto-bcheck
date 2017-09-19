package com.jarkos;

import com.jarkos.stock.enums.CurrencyPairsEnum;
import com.jarkos.stock.enums.StockNamesEnum;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jkostrzewa on 2017-09-18.
 */
public class CoinmarketcapPriceCompare {

    private static final Logger logger = Logger.getLogger(CoinmarketcapPriceCompare.class);

    public void compare() {
        List<MarketTableRow> marketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin/#markets");
        compareBitBayBtcPrice(marketsData);

    }

    private void compareBitBayBtcPrice(List<MarketTableRow> marketsData) {
        MarketTableRow bitBayBTCPLNData = getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.BitBay.name(), CurrencyPairsEnum.BTCPLN);

        List<MarketTableRow> marketTableRows = Arrays.asList(getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Bitfinex.name(), CurrencyPairsEnum.BTCUSD),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Bitstamp.name(), CurrencyPairsEnum.BTCUSD),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Bitstamp.name(), CurrencyPairsEnum.BTCEUR),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.GDAX.name(), CurrencyPairsEnum.BTCEUR),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.GDAX.name(), CurrencyPairsEnum.BTCUSD),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Kraken.name(), CurrencyPairsEnum.BTCEUR),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Kraken.name(), CurrencyPairsEnum.BTCUSD),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Gatecoin.name(), CurrencyPairsEnum.BTCEUR),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Gatecoin.name(), CurrencyPairsEnum.BTCUSD),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.LitBit, CurrencyPairsEnum.BTCEUR),
                                                             getPriceInUsdByStockAndCurrencyPair(marketsData, StockNamesEnum.Bittrex.name(), CurrencyPairsEnum.BTCUSDT));
        marketTableRows.forEach(d -> {
            BigDecimal diff = bitBayBTCPLNData.getPrice().divide(d.getPrice(), 3, RoundingMode.HALF_DOWN);
            String resultToDisplay = bitBayBTCPLNData.getStockName() + " " + bitBayBTCPLNData.getExchangePair() + " diff " + d.getStockName() + " :" + diff;
            if (diff.compareTo(BigDecimal.valueOf(1.045)) > 0) {
                logger.warn(resultToDisplay);
            } else if (diff.compareTo(BigDecimal.ONE) > 0) {
                logger.trace(resultToDisplay);
            }
        });
    }

    List<MarketTableRow> getMarketsData(String url) {
        List<MarketTableRow> marketTableRows = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element marketsTable = doc.getElementById("markets-table");
            Elements tableRows = marketsTable.getElementsByTag("tr");
            tableRows.stream().forEach(element -> {
                Elements allTdElements = element.getElementsByTag("td");
                if (!allTdElements.isEmpty()) {
                    marketTableRows.add(new MarketTableRow(allTdElements.get(1).text(), allTdElements.get(2).text(),
                                                           new BigDecimal(allTdElements.get(4).text().replace("* ", "").replace("*", "").replace("*", "").substring(1))));
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

    MarketTableRow getPriceInUsdByStockAndCurrencyPair(List<MarketTableRow> marketTableRows, String stockCode, CurrencyPairsEnum currencyPair) {
        MarketTableRow marketTableRow = marketTableRows.stream().filter(m -> (stockCode.equals(m.getStockName()) && currencyPair.toString().equals(m.getExchangePair())))
                                                       .findFirst().get();
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
