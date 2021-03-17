package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.Commercial;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(BuildProfiles.TEST)
@Service
public class TestCommercialCostsUpdater implements CommercialCostsUpdater {

    private Commercial lastUpdated;
    private Commercial lastResetted;

    @Override
    public void update(Commercial commercial) {
        this.lastUpdated = commercial;
    }

    @Override
    public void reset(Commercial commercial) {
        this.lastResetted = commercial;
    }

    public void reset() {
        lastUpdated = null;
        lastResetted = null;
    }

    public Commercial getLastUpdated() {
        return lastUpdated;
    }

    public Commercial getLastResetted() {
        return lastResetted;
    }
}
