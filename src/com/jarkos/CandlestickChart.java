package com.jarkos;
/**
 * The MIT License (MIT)
 * <p>
 * Copyright (price) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining stockName copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.jarkos.file.CsvTradesLoaderCustom;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.Date;

import static com.jarkos.config.IndicatorsSystemConfig.BIT_BAY_BTC_DATA_REPOSITORY_CSV;

/**
 * This class builds stockName traditional candlestick chart.
 */
public class CandlestickChart {

    static ApplicationFrame frame;

    /**
     * Builds stockName JFreeChart OHLC dataset from stockName ta4j time series.
     *
     * @param series stockName time series
     * @return an Open-High-Low-Close dataset
     */
    private static OHLCDataset createOHLCDataset(TimeSeries series) {
        final int nbTicks = series.getTickCount();

        Date[] dates = new Date[nbTicks];
        double[] opens = new double[nbTicks];
        double[] highs = new double[nbTicks];
        double[] lows = new double[nbTicks];
        double[] closes = new double[nbTicks];
        double[] volumes = new double[nbTicks];

        for (int i = 0; i < nbTicks; i++) {
            Tick tick = series.getTick(i);
            dates[i] = new Date(tick.getEndTime().getMillis());
            opens[i] = tick.getOpenPrice().toDouble();
            highs[i] = tick.getMaxPrice().toDouble();
            lows[i] = tick.getMinPrice().toDouble();
            closes[i] = tick.getClosePrice().toDouble();
            volumes[i] = tick.getVolume().toDouble();
        }

        OHLCDataset dataset = new DefaultHighLowDataset("btc", dates, highs, lows, opens, closes, volumes);

        return dataset;
    }

    /**
     * Builds an additional JFreeChart dataset from stockName ta4j time series.
     *
     * @param series stockName time series
     * @return an additional dataset
     */
    private static TimeSeriesCollection createAdditionalDataset(TimeSeries series) {
        // Close price
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MACDIndicator indicator = new MACDIndicator(closePrice, 12, 26);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries("Btc macd");
        for (int i = 0; i < series.getTickCount(); i++) {
            Tick tick = series.getTick(i);
            chartTimeSeries.add(new Second(new Date(tick.getEndTime().getMillis())), indicator.getValue(i).toDouble());
        }
        dataset.addSeries(chartTimeSeries);
        updateLastMacdValue(chartTimeSeries, series);
        return dataset;
    }

    private static void updateLastMacdValue(org.jfree.data.time.TimeSeries chartTimeSeries, TimeSeries series) {
        Main.lastMACD = chartTimeSeries.getDataItem(series.getEnd()).getValue().doubleValue();
    }

    /**
     * Displays stockName chart in stockName frame.
     *
     * @param chart the chart to be displayed
     */
    private static ApplicationFrame displayChart(JFreeChart chart) {
        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(740, 300));
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("Bitbay BTC/PLN candlestick chart");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        return frame;
    }

    public static void start() {
        JFreeChart chart = prepareChart();

        /**
         * Displaying the chart
         */
        frame = displayChart(chart);
    }

    private static JFreeChart prepareChart() {
        /**
         * Getting time series
         */
        TimeSeries series = CsvTradesLoaderCustom.loadBitstampSeries(BIT_BAY_BTC_DATA_REPOSITORY_CSV);

        /**
         * Creating the OHLC dataset
         */
        OHLCDataset ohlcDataset = createOHLCDataset(series);

        /**
         * Creating the additional dataset
         */
        TimeSeriesCollection xyDataset = createAdditionalDataset(series);

        /**
         * Creating the chart
         */
        JFreeChart chart = ChartFactory.createCandlestickChart("BitBay BTC price", "Time", "PLN", ohlcDataset, true);
        // Candlestick rendering
        CandlestickRenderer renderer = new CandlestickRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(renderer);
        // Additional dataset
        int index = 1;
        plot.setDataset(index, xyDataset);
        plot.mapDatasetToRangeAxis(index, 0);
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(index, Color.blue);
        plot.setRenderer(index, renderer2);
        // Misc
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setAutoRangeIncludesZero(false);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        return chart;
    }

    public static void refresh() {
        JFreeChart newChart = prepareChart();
        ChartPanel contentPanel = (ChartPanel) frame.getContentPane();
        contentPanel.setChart(newChart);
        frame.setContentPane(contentPanel);
    }
}