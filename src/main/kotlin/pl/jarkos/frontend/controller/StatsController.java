package pl.jarkos.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jarkos.backend.stock.service.RoiIndicatorsService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatsController {

    private RoiIndicatorsService roiIndicatorsService;

    public StatsController(RoiIndicatorsService roiIndicatorsService) {
        this.roiIndicatorsService = roiIndicatorsService;
    }

    @RequestMapping("/stats")
    public String stats(Model model) {
        List<ArrayList<String>> list = prepareGoogleDataTableForChart();
        model.addAttribute("indicators", list.subList(list.size() - 1000, list.size()));
        model.addAttribute("labels", roiIndicatorsService.fetchRoiIndicatorsNames());
        return "stats";
    }

    private List<ArrayList<String>> prepareGoogleDataTableForChart() {
        List<ArrayList<String>> list = new ArrayList<>();
        LinkedHashMap<Long, Map<String, String>> allDatesAndIndicatorsValuesMap = roiIndicatorsService.getAllTimestampsAndIndicatorsValuesMap();
        allDatesAndIndicatorsValuesMap.forEach((k, v) -> {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(k.toString());
            temp.addAll(v.values());
            list.add(temp);
        });
        return list;
    }

}
