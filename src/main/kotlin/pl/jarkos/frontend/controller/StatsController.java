package pl.jarkos.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jarkos.backend.stock.service.RoiIndicatorsService;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class StatsController {

    private RoiIndicatorsService roiIndicatorsService;

    public StatsController(RoiIndicatorsService roiIndicatorsService) {
        this.roiIndicatorsService = roiIndicatorsService;
    }

    @RequestMapping("/stats")
    public String stats(Model model) {
        LinkedHashMap<Long, Map<String, String>> allDatesAndIndicatorsValuesMap = roiIndicatorsService.getAllTimestampsAndIndicatorsValuesMap();
        model.addAttribute("indicators", allDatesAndIndicatorsValuesMap);
        model.addAttribute("codes", roiIndicatorsService.fetchRoiIndicatorsNames());
        return "stats";
    }

}
