package com.jarkos;

import com.jarkos.stock.enums.BlackListCoinmarketCurrencies;
import com.jarkos.stock.enums.BlackListCoinmarketStocks;
import com.jarkos.stock.enums.StockNameEnum;
import com.jarkos.stock.enums.currencies.*;
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

import static com.jarkos.config.AppConfig.*;

/**
 * Created by jkostrzewa on 2017-09-18.
 */
class CoinmarketcapPriceCompare {

    private static final Logger log = Logger.getLogger(CoinmarketcapPriceCompare.class);

    private List<MarketTableRow> btcMarketsData;
    private List<MarketTableRow> ltcMarketsData;
    private List<MarketTableRow> ethMarketsData;
    private List<MarketTableRow> bccMarketsData;
    private List<MarketTableRow> liskMarketsData;
    private List<MarketTableRow> dashMarketsData;
    private List<MarketTableRow> gameMarketsData;

    CoinmarketcapPriceCompare() {
        btcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin/#markets");
        ltcMarketsData = getMarketsData("https://coinmarketcap.com/currencies/litecoin/#markets");
        ethMarketsData = getMarketsData("https://coinmarketcap.com/currencies/ethereum/#markets");
        bccMarketsData = getMarketsData("https://coinmarketcap.com/currencies/bitcoin-cash/#markets");
        liskMarketsData = getMarketsData("https://coinmarketcap.com/currencies/lisk/#markets");
        dashMarketsData = getMarketsData("https://coinmarketcap.com/currencies/dash/#markets");
        gameMarketsData = getMarketsData("https://coinmarketcap.com/currencies/gamecredits/#markets");
    }

    void compare() {
        log.error("*** BTC");
        final Set<String> btcCurrencyPairEnums = Arrays.stream(BtcCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(btcMarketsData, StockNameEnum.BitBay, BtcCurrencyPairEnum.BTCPLN.toString(), btcCurrencyPairEnums);
        recognizeMinMaxExchange(btcMarketsData, "BTC MAX/MIN stocks:");

        log.error("*** LTC");
        final Set<String> ltcCurrencyParisToCompare = Arrays.stream(LtcCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(ltcMarketsData, StockNameEnum.BitBay, LtcCurrencyPairEnum.LTCPLN.toString(), ltcCurrencyParisToCompare);
        recognizeMinMaxExchange(ltcMarketsData, "LTC MAX/MIN stocks:");

        log.error("*** ETH");
        final Set<String> ethCurrencyParisToCompare = Arrays.stream(EthCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(ethMarketsData, StockNameEnum.BitBay, EthCurrencyPairEnum.ETHPLN.toString(), ethCurrencyParisToCompare);
        recognizeMinMaxExchange(ethMarketsData, "ETH MAX/MIN stocks:");

        log.error("*** BCC");
        final Set<String> bccCurrencyParisToCompare = Arrays.stream(BccCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(bccMarketsData, StockNameEnum.BitBay, BccCurrencyPairEnum.BCCPLN.toString(), bccCurrencyParisToCompare);
        recognizeMinMaxExchange(bccMarketsData, "BCC MAX/MIN stocks:");

        log.error("*** LISK");
        final Set<String> liskCurrencyParisToCompare = Arrays.stream(LiskCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(liskMarketsData, StockNameEnum.BitBay, LiskCurrencyPairEnum.LSKPLN.toString(), liskCurrencyParisToCompare);
        recognizeMinMaxExchange(liskMarketsData, "LISK MAX/MIN stocks:");

        log.error("*** DASH");
        final Set<String> dashCurrencyParisToCompare = Arrays.stream(DashCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(dashMarketsData, StockNameEnum.BitBay, DashCurrencyPairEnum.DASHPLN.toString(), dashCurrencyParisToCompare);
        recognizeMinMaxExchange(dashMarketsData, "DASH MAX/MIN stocks:");

        log.error("*** GAME");
        final Set<String> gameCurrencyParisToCompare = Arrays.stream(GameCurrencyPairEnum.values()).map(Enum::toString).collect(Collectors.toSet());
        compareStockPrice(gameMarketsData, StockNameEnum.BitBay, GameCurrencyPairEnum.GAMEPLN.toString(), gameCurrencyParisToCompare);
        //        recognizeMinMaxExchange(gameMarketsData, "GAME MAX/MIN stocks:");

    }

    private void recognizeMinMaxExchange(List<MarketTableRow> marketList, String nameToDisplay) {
        System.out.println(nameToDisplay);
        List<MarketTableRow> marketsToRecognize = marketList.stream().filter(s -> s.getVolume().compareTo(MARKET_MIN_VOLUME_TO_CONSIDER_COMPARING) > 0).collect(Collectors.toList());
        final List<String> blackListCurrencies = Arrays.stream(BlackListCoinmarketCurrencies.values()).map(Enum::name).collect(Collectors.toList());

        final List<MarketTableRow> filteredMarkets = marketsToRecognize.stream().filter(m -> blackListCurrencies.stream().noneMatch(c -> m.getExchangePair().contains(c)))
                                                                       .collect(Collectors.toList());
        Collections.sort(filteredMarkets);
        filteredMarkets.subList(0, 4).forEach(m -> System.out.print(m.getStockName() + " " + m.getPrice() + " " + m.getExchangePair() + " "));
        System.out.println();
        filteredMarkets.subList(filteredMarkets.size() - 5, filteredMarkets.size() - 1)
                       .forEach(m -> System.out.print(m.getStockName() + " " + m.getPrice() + " " + m.getExchangePair() + " "));
        System.out.println();
    }

    private void compareStockPrice(List<MarketTableRow> marketsData, StockNameEnum mainStockName, String mainCurrencyPair, Set<String> currencyParisToCompare) {
        MarketTableRow mainStockAndCurrencyData = getPriceInUsdByStockAndCurrencyPair(marketsData, mainStockName.name(), mainCurrencyPair);
        final Set<StockNameEnum> stocksToCompare = new HashSet<>(Arrays.asList(StockNameEnum.values()));

        stocksToCompare.forEach(s -> currencyParisToCompare.forEach(c -> {
            MarketTableRow priceInUsdByStockAndCurrencyPair = getPriceInUsdByStockAndCurrencyPair(marketsData, s.toString(), c);
            if (priceInUsdByStockAndCurrencyPair != null && !Objects.equals(mainStockName.name(), s.name())) {
                BigDecimal diff = mainStockAndCurrencyData.getPrice().divide(priceInUsdByStockAndCurrencyPair.getPrice(), 3, RoundingMode.HALF_DOWN);
                boolean reversedRoi = false;
                if (diff.compareTo(BigDecimal.ONE) < 1) {
                    diff = priceInUsdByStockAndCurrencyPair.getPrice().divide(mainStockAndCurrencyData.getPrice(), 3, RoundingMode.HALF_DOWN);
                    reversedRoi = true;
                }
                String resultToDisplay =
                        mainStockAndCurrencyData.getStockName() + " " + mainStockAndCurrencyData.getExchangePair() + " diff " + priceInUsdByStockAndCurrencyPair.getStockName() +
                        " " + priceInUsdByStockAndCurrencyPair.getExchangePair() + ":" + diff + " " + (reversedRoi ? " %REVERSED" : "");
                if (diff.compareTo(maxRoiValueForWarnDisplay) > 0 || diff.compareTo(minRoiValueForWarnDisplay) < 0) {
                    log.warn(resultToDisplay);
                } else {
                    log.error(resultToDisplay);
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
                if (!allTdElements.isEmpty() && (allTdElements.get(4).text().indexOf(',') < 0) && !isOnBlackList(allTdElements.get(1).text())) {
                    //                    System.err.println(allTdElements.get(4).text());
                    marketTableRows.add(new MarketTableRow(allTdElements.get(1).text(), allTdElements.get(2).text(), new BigDecimal(parseValue(allTdElements.get(4).text())),
                                                           new BigDecimal(parseVolume(allTdElements.get(3).text()))));
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

    private boolean isOnBlackList(String stockName) {
        return Arrays.stream(BlackListCoinmarketStocks.values()).anyMatch(s -> s.name().equals(stockName));
    }

    String parseValue(String s) {
        return s.replace("* ", "").replace(" *", "").replace("*", "").substring(1);
    }

    String parseVolume(String s) {
        return s.replace("* ", "").replace(" *", "").replace("*", "").substring(1).replace(",", "");
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
    class MarketTableRow implements Comparable<MarketTableRow> {
        String stockName;
        String exchangePair;
        BigDecimal price;
        BigDecimal volume;

        MarketTableRow(String stockName, String exchangePair, BigDecimal price, BigDecimal volume) {
            this.stockName = stockName;
            this.exchangePair = exchangePair;
            this.price = price;
            this.volume = volume;
        }

        @Override
        public int compareTo(MarketTableRow o) {
            return Math.toIntExact(this.getPrice().longValue() - o.getPrice().longValue());
        }
    }
}
