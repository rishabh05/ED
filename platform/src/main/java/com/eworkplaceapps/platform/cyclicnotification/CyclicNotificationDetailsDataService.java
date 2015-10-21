//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.List;
import java.util.UUID;


public class CyclicNotificationDetailsDataService extends BaseDataService<CyclicNotificationDetails> {

    CyclicNotificationDetailsData dataDelegate = new CyclicNotificationDetailsData();

    /**
     * Initializes a new instance of the CyclicNotificationDetailsDataService class.
     */
    public CyclicNotificationDetailsDataService() {
        super("CYCLIC_NOTIFICATION_DETAILS");
    }

    @Override
    public CyclicNotificationDetailsData getDataClass() {
        return dataDelegate;
    }

    /**
     * ----------------------- Begin Class Methods ---------------
     *
     * @param cyclicNotificationId  UUID
     * @return List<CYCLIC_NOTIFICATION_DETAILS>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public List<CyclicNotificationDetails> getCyclicNotificationDetailsListByCyclicNotificationId(UUID cyclicNotificationId) throws EwpException {
        return dataDelegate.getCyclicNotificationDetailsListByCyclicNotificationId(cyclicNotificationId);
    }
}
