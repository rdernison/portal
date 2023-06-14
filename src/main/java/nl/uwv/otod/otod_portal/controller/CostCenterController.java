package nl.uwv.otod.otod_portal.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.CostCenter;
import nl.uwv.otod.otod_portal.service.CostCenterService;

@RestController
@RequestMapping("costCenters")
@Log4j2
public class CostCenterController {
	
	@Autowired
	private CostCenterService costCenterService;

    @GetMapping
    public List<CostCenter> costCenters(@RequestParam(value = "q", required = false) String query) {
    	log.info("Getting cost centers");
        if (StringUtils.isEmpty(query)) {
        	log.info("All cost centers");
            return costCenterService.getAll()/*.stream()
                         .limit(15)
                         //.map(this::mapToStateItem)
                         .collect(Collectors.toList())*/;
        }
        log.info("Filtering : {}", query);
        return costCenterService.getAll().stream()
                     .filter(costCenter -> costCenter.getCode()
                                           .toLowerCase()
                                           .contains(query))
                     .limit(15)
                     .collect(Collectors.toList());
    }

}
