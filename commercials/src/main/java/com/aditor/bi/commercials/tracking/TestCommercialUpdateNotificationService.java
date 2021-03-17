package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.Commercial;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

// TODO: Move to tests, use custom application context class instead of profiles
@Profile(BuildProfiles.TEST)
@Service
public class TestCommercialUpdateNotificationService implements CommercialUpdateNotificationService {
    private Commercial lastCommercial;

    @Override
    public void notify(Commercial commercial) {
        lastCommercial = commercial;
    }

    public Commercial getLastCommercial() {
        return lastCommercial;
    }

    public void reset() {
        lastCommercial = null;
    }
}
