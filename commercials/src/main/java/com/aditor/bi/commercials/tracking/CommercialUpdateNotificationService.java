package com.aditor.bi.commercials.tracking;

import com.aditor.bi.commercials.domain.Commercial;

/**
 * Created by ivan on 3/16/16.
 */
public interface CommercialUpdateNotificationService {
    void notify(Commercial commercial);
}
