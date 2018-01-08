import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.jarkos.backend.file.CsvReader;
import pl.jarkos.backend.stock.service.RoiIndicatorsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class XyRoiChartTest {

    private RoiIndicatorsService roiIndicatorsService = new RoiIndicatorsService(new CsvReader());
    private ArrayList<String[]> allLinesOfSavedRoi;

    @Before
    public void runBeforeTestMethod() {
        allLinesOfSavedRoi = roiIndicatorsService.getAllLinesOfSavedRoi();
    }

    @Test
    public void indicatorDataNotEmpty() {
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

}

