package com.aditor.bi.commercials.tracking;


import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.persistence.impl.SelectQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Profile(BuildProfiles.PROD)
@Service
public class AppsflyerCommercialUpdateNotificationService implements CommercialUpdateNotificationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger logger = LoggerFactory.getLogger(AppsflyerCommercialUpdateNotificationService.class);

    @Override
    public void notify(Commercial commercial) {
        try {
            String wherePart = new SelectQueryBuilder()
                    .withCommercial(commercial, "C")
                    .buildWherePart();
            logger.info("Sending following query to appsflyer: " + wherePart);

            String response = restTemplate.postForObject("http://localhost:8080/appsflyer/exportToCooladata", wherePart, String.class);
            logger.info("Successfully notified with response: " + response);

        } catch (Exception e) {
            logger.error("Notification failed: ", e);
        }
    }
}
