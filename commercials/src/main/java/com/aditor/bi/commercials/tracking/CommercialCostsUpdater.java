package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.domain.Commercial;


public interface CommercialCostsUpdater {
    void update(Commercial commercial);
    void reset(Commercial commercial);
}
