package com.jarkos.file;

import au.com.bytecode.opencsv.CSVReader;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Period;
import ta4jexamples.loaders.CsvTradesLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jkostrzewa on 2017-09-03.
 */
public class CsvTradesLoaderCustom extends CsvTradesLoader {

    public static final String UTF_8 = "UTF-8";

    public static TimeSeries loadBitstampSeries(String filePath) {
        System.out.println("Loading csv file...");
        long startTime = System.nanoTime();
        CSVReader csvReader = null;
        List lines = null;
        try {
            InputStream stream = new FileInputStream(filePath);

            csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName(UTF_8)), ',');
            lines = csvReader.readAll();
            lines.remove(0);
        } catch (IOException var21) {
            Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", var21);
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException var20) {
                    ;
                }
            }

        }

        List<Tick> ticks = null;
        if (lines != null && !lines.isEmpty()) {
            DateTime beginTime = new DateTime(Long.parseLong(((String[]) lines.get(0))[0]) * 1000L);
            DateTime endTime = new DateTime(Long.parseLong(((String[]) lines.get(lines.size() - 1))[0]) * 1000L);
            if (beginTime.isAfter(endTime)) {
                Instant beginInstant = beginTime.toInstant();
                Instant endInstant = endTime.toInstant();
                beginTime = new DateTime(endInstant);
                endTime = new DateTime(beginInstant);
            }

            ticks = buildEmptyTicks(beginTime, endTime, 300);
            Iterator var23 = lines.iterator();

            while (var23.hasNext()) {
                String[] tradeLine = (String[]) var23.next();
                if (!tradeLine[0].isEmpty()) {
                    DateTime tradeTimestamp = new DateTime(Long.parseLong(tradeLine[0]) * 1000L);
                    Iterator var9 = ticks.iterator();

                    while (var9.hasNext()) {
                        Tick tick = (Tick) var9.next();
                        if (tick.inPeriod(tradeTimestamp)) {
                            double tradePrice = Double.parseDouble(tradeLine[1]);
                            double tradeAmount = Double.parseDouble(tradeLine[2]);
                            tick.addTrade(tradeAmount, tradePrice);
                        }
                    }
                }
            }

            removeEmptyTicks(ticks);
        }
        long endTime = System.nanoTime();
        System.out.println("Ended loading of csv file: " + ((endTime - startTime))/1000000000l + " sek");
        System.out.println(LocalDateTime.now());
        return new TimeSeries("bitstamp_trades", ticks);
    }

    private static List<Tick> buildEmptyTicks(DateTime beginTime, DateTime endTime, int duration) {
        List<Tick> emptyTicks = new ArrayList();
        Period tickTimePeriod = Period.seconds(duration);
        DateTime tickEndTime = beginTime;

        do {
            tickEndTime = tickEndTime.plus(tickTimePeriod);
            emptyTicks.add(new Tick(tickTimePeriod, tickEndTime));
        } while (tickEndTime.isBefore(endTime));

        return emptyTicks;
    }

    private static void removeEmptyTicks(List<Tick> ticks) {
        for (int i = ticks.size() - 1; i >= 0; --i) {
            if (((Tick) ticks.get(i)).getTrades() == 0) {
                ticks.remove(i);
            }
        }

    }

    public static void main(String[] args) {
        TimeSeries series = loadBitstampSeries();
        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
        System.out.println("Number of ticks: " + series.getTickCount());
        System.out.println("First tick: \n\tVolume: " + series.getTick(0).getVolume() + "\n" + "\tNumber of trades: " + series.getTick(0).getTrades() + "\n" + "\tClose price: " +
                           series.getTick(0).getClosePrice());
    }

}
