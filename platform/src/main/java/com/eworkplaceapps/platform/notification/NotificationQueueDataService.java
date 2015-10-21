//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/28/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.List;

/**
 *
 */
public class NotificationQueueDataService extends BaseDataService<NotificationQueue> {

    NotificationQueueData dataDelegate = new NotificationQueueData();

    // Initializes a new instance of the NotificationQueueDataService class.
    public NotificationQueueDataService() {
        super("NOTIFICATION_QUEUE");
    }

    public NotificationQueueData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    // To discuss with nitin
    // It is used to get the current NOTIFICATION_QUEUE list from current delivery time.
    public List<NotificationQueue> getNotificationQueueFromCurrentDeliveryTime() throws EwpException {
        return dataDelegate.getNotificationQueueFromCurrentDeliveryTime();
    }
}
