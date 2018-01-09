import org.junit.Assert;
import org.junit.Test;
import pl.jarkos.backend.file.CsvReader;
import pl.jarkos.backend.stock.service.RoiIndicatorsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartRoiDataTest {

    private RoiIndicatorsService roiIndicatorsService = new RoiIndicatorsService(new CsvReader());

    @Test
    public void indicatorDataNotEmpty() {
        ArrayList<String[]> allLinesOfSavedRoi = roiIndicatorsService.getAllLinesOfSavedRoi();
        Assert.assertFalse(allLinesOfSavedRoi.isEmpty());
    }

    @Test
    public void allIndicatorsHaveAtLeastOneValue() {
        Map<String, BigDecimal> currentIndicatorsMap = roiIndicatorsService.fetchCurrentRoiIndicatorsMap();
        Map<String, Map<String, String>> allIndicatorsCodeValuesMap = roiIndicatorsService.fetchAllIndicatorCodeAndValuesMap(currentIndicatorsMap);
        allIndicatorsCodeValuesMap.forEach((k, v) -> Assert.assertFalse(v.isEmpty()));
    }

    @Test
    public void allDatesHaveTheSameNumberOfValues() throws Exception {
        Map<Long, Map<String, String>> allTimestampsAndIndicatorsValuesMap = roiIndicatorsService.getAllTimestampsAndIndicatorsValuesMap();
        Long firstTimestamp = allTimestampsAndIndicatorsValuesMap.keySet().iterator().next();
        int sizeOfFirstValues = allTimestampsAndIndicatorsValuesMap.get(firstTimestamp).size();
        allTimestampsAndIndicatorsValuesMap.values().forEach(v -> Assert.assertTrue(v.size() == sizeOfFirstValues));
    }

    @Test
    public void allDataHaveTheSameOrder() throws Exception {
        List<String> indicatorsNames = roiIndicatorsService.fetchRoiIndicatorsNames();
        LinkedHashMap<Long, Map<String, String>> allDatesAndIndicatorsValuesMap = roiIndicatorsService.getAllTimestampsAndIndicatorsValuesMap();
        indicatorsNames.forEach(indicatorName -> {
            int indexOfIndicatorKey = indicatorsNames.indexOf(indicatorName);
            allDatesAndIndicatorsValuesMap.values().forEach(k -> {
                int positionOnMap = new ArrayList<>(k.keySet()).indexOf(indicatorName);
                Assert.assertTrue(indexOfIndicatorKey == positionOnMap);
            });
        });
    }
}

