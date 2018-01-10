package pl.jarkos.backend.stock.service;

import org.springframework.stereotype.Service;
import pl.jarkos.backend.file.CsvReader;
import pl.jarkos.backend.stock.Indicators;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static pl.jarkos.backend.config.AppConfig.ROI_DATA_REPOSITORY_CSV;

@Service
public class RoiIndicatorsService {

    private CsvReader csvReader;

    public RoiIndicatorsService(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public Map<String, BigDecimal> fetchCurrentRoiIndicatorsMap() {
        Map<String, BigDecimal> indicatorsMap = new HashMap<>();
        Field[] allFields = Indicators.class.getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isPublic(field.getModifiers()) && field.getType().equals(BigDecimal.class)) {
                BigDecimal indicator;
                try {
                    indicator = (BigDecimal) field.get(BigDecimal.class);
                    indicatorsMap.put(field.getName(), indicator);
                } catch (IllegalAccessException ignored) {
                    // DO NOTHING, GO AHEAD
                }
            }
        }
        return indicatorsMap;
    }

    public List<String> fetchRoiIndicatorsNames() {
        Map<String, BigDecimal> indicatorsMap = fetchCurrentRoiIndicatorsMap();
        return new ArrayList<>(indicatorsMap.keySet());
    }

    public LinkedHashMap<String, Map<String, String>> fetchAllIndicatorCodeAndValuesMap(Map<String, BigDecimal> currentIndicatorsMap) {
        ArrayList<String[]> allLinesOfSavedRoi = getAllLinesOfSavedRoi();
        LinkedHashMap<String, Map<String, String>> allIndicatorsValuesMap = new LinkedHashMap<>();
        currentIndicatorsMap.forEach((k, v) -> {
            List<String[]> results = allLinesOfSavedRoi.stream().filter(l -> l.length > 1 && l[1].equals(k) && isGreaterThanZero(l[2])).collect(Collectors.toList());
            Map<String, String> dateValuesMap = results.stream().collect(Collectors.toMap(result -> result[0], result -> result[2]));
            allIndicatorsValuesMap.put(k, dateValuesMap);
        });
        return allIndicatorsValuesMap;
    }

    public LinkedHashMap<Long, Map<String, String>> getAllTimestampsAndIndicatorsValuesMap() {
        ArrayList<String[]> allLinesOfSavedRoi = getAllLinesOfSavedRoi();
        LinkedHashMap<Long, Map<String, String>> allDatesAndIndicatorsValuesMap = new LinkedHashMap<>();
        Map<String, BigDecimal> currentIndicatorsMap = fetchCurrentRoiIndicatorsMap();
        List<String> uniqueDates = allLinesOfSavedRoi.stream().map(result -> result[0]).distinct().sorted().collect(Collectors.toList());
        LinkedHashMap<String, Map<String, String>> indicatorValuesMap = fetchAllIndicatorCodeAndValuesMap(currentIndicatorsMap);
        uniqueDates.forEach(uniqueDate -> indicatorValuesMap.forEach((k, v) -> {
            if (v.containsKey(uniqueDate)) {
                Long date = Long.valueOf(uniqueDate);
                if (allDatesAndIndicatorsValuesMap.containsKey(date)) {
                    allDatesAndIndicatorsValuesMap.get(date).put(k, v.get(uniqueDate));
                } else {
                    allDatesAndIndicatorsValuesMap.put(date, new HashMap<String, String>() {{
                        put(k, v.get(uniqueDate));
                    }});
                }
            }
        }));
        return filterNotConsistentData(allDatesAndIndicatorsValuesMap);
    }

    private LinkedHashMap<Long, Map<String, String>> filterNotConsistentData(Map<Long, Map<String, String>> allDatesAndIndicatorsValuesMap) {
        int maxValuesSize = allDatesAndIndicatorsValuesMap.values().stream().max(Comparator.comparingInt(Map::size)).get().size();
        return allDatesAndIndicatorsValuesMap.entrySet().stream().filter(dateMapEntry -> dateMapEntry.getValue().size() == maxValuesSize)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1,
                        LinkedHashMap::new));
    }

    public ArrayList<String[]> getAllLinesOfSavedRoi() {
        return csvReader.getLines(ROI_DATA_REPOSITORY_CSV);
    }

    private static boolean isGreaterThanZero(String s) {
        Double value = Double.valueOf(s);
        return value.compareTo(0.0) > 0;
    }

}
