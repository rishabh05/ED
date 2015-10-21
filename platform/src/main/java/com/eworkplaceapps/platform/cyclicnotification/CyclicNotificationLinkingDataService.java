//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;

import com.eworkplaceapps.platform.entity.BaseDataService;


public class CyclicNotificationLinkingDataService extends BaseDataService<CyclicNotificationLinking> {

    CyclicNotificationLinkingData dataDelegate = new CyclicNotificationLinkingData();

    /**
     * Initializes a new instance of the CyclicNotificationLinkingDataService class.
     */
    public CyclicNotificationLinkingDataService() {
        super("CYCLIC_NOTIFICATION_LINKING");
    }

    @Override
    public CyclicNotificationLinkingData getDataClass() {
        return dataDelegate;
    }

    /**
     * ----------------------- Begin Class Methods ---------------
     */


}
