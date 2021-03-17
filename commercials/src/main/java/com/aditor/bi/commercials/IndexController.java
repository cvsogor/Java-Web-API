package com.aditor.bi.commercials;

import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.tracking.CommercialCostsUpdater;
import com.aditor.bi.commercials.domain.Commercial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CommercialRepository commercialRepository;

    @Autowired
    private CommercialCostsUpdater commercialCostsUpdater;


    @RequestMapping("/")
    public String index() {
        return "spa_index";
    }


    @RequestMapping(value = "/commercials/daily-update-sync")
    @ResponseBody
    public String commercialsDailyUpdateSynchronized() {
        List<Commercial> commercials = commercialRepository.findAllActive();
        logger.info("Starting daily-update");
        for(Commercial commercial : commercials) {
            try {
                commercialCostsUpdater.update(commercial);
            } catch (Exception exception) {
                logger.info("Something interrupted daily-update", exception);
                return "interrupted";
            }
        }

        logger.info("Finished daily-update");
        return "success";
    }
}

