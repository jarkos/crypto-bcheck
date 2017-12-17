package com.jarkos.chart;

import com.jarkos.file.CsvReader;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.jarkos.config.AppConfig.ROI_DATA_REPOSITORY_CSV;

// TODO make abstract with CandleChart
public class XyRoiChart {

    private static ApplicationFrame frame;

    public static void start(Map<String, BigDecimal> internalIndicators) {
        JFreeChart chart = prepareChart(internalIndicators);
        frame = displayChart(chart);
    }

    private static JFreeChart prepareChart(Map<String, BigDecimal> indicatorsMap) {
        CsvReader csvReader = new CsvReader();
        TimeSeriesCollection timeSeriesCollection = prepareTimeSeriesCollection(indicatorsMap, csvReader);
        JFreeChart chart = ChartFactory.createCandlestickChart("ROIs", "Time", "Value", null, true);
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(1, timeSeriesCollection);
        plot.mapDatasetToRangeAxis(1, 0);
        XYSplineRenderer xySplineRenderer = new XYSplineRenderer();
        xySplineRenderer.setSeriesPaint(1, getRandomColor());
        plot.setRenderer(xySplineRenderer);
        return chart;
    }

    @NotNull
    private static TimeSeriesCollection prepareTimeSeriesCollection(Map<String, BigDecimal> indicatorsMap, CsvReader csvReader) {
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        List<String[]> lines = (ArrayList<String[]>) csvReader.getLines(ROI_DATA_REPOSITORY_CSV);

        indicatorsMap.forEach((k, v) -> {
            if (lines != null) {
                List<String[]> results = lines.stream().filter(l -> l[1].equals(k) && isMoreThanZero(l[2])).collect(Collectors.toList());
                TimeSeries singleTimeSeries = new TimeSeries(k);
                results.forEach(r -> singleTimeSeries.addOrUpdate(new Second(new Date(Long.valueOf(r[0]))), Double.valueOf(r[2])));
                timeSeriesCollection.addSeries(singleTimeSeries);
            }
        });
        return timeSeriesCollection;
    }

    private static boolean isMoreThanZero(String s) {
        Double value = Double.valueOf(s);
        return value.compareTo(0.0) > 0;
    }

    private static Color getRandomColor() {
        return new Color(ThreadLocalRandom.current().nextInt(0, 255 + 1),
                ThreadLocalRandom.current().nextInt(0, 255 + 1),
                ThreadLocalRandom.current().nextInt(0, 255 + 1));
    }

    private static ApplicationFrame displayChart(JFreeChart chart) {
        // Chart panel
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(740, 300));
        // Application frame
        ApplicationFrame frame = new ApplicationFrame("ROI XY chart");
        frame.setContentPane(panel);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
        return frame;
    }

    public static void refresh(Map<String, BigDecimal> innitInternalIndicatorsList) {
        JFreeChart newChart = prepareChart(innitInternalIndicatorsList);
        ChartPanel contentPanel = (ChartPanel) frame.getContentPane();
        contentPanel.setChart(newChart);
        frame.setContentPane(contentPanel);
    }

}
