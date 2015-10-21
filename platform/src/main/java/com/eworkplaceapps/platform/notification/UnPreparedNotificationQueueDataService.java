//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.List;

public class UnPreparedNotificationQueueDataService  extends BaseDataService<UnPreparedNotificationQueue> {

    UnPreparedNotificationQueueData dataDelegate  = new  UnPreparedNotificationQueueData();

    /**
     * Initializes a new instance of the UnPreparedNotificationQueueDataService class.
     */
    public UnPreparedNotificationQueueDataService() {
        super("UnPreparedNotificationQueueDataService");
    }

    public UnPreparedNotificationQueueData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    /**
     * It is used to get the current undelivered notification list from delivery type and notification type.
     * @param notificationType NotificationEnum.NotificationTypeEnum
     * @param notificationDeliverType NotificationEnum.NotificationDeliveryType
     * @return List<UnPreparedNotificationQueue>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public  List<UnPreparedNotificationQueue> getNotificationFromCurrentDateTimeDeliveryTypeAndNotificationType(NotificationEnum.NotificationTypeEnum notificationType , NotificationEnum.NotificationDeliveryType notificationDeliverType) throws EwpException {
        return dataDelegate.getNotificationFromCurrentDateTimeDeliveryTypeAndNotificationType(notificationType,  notificationDeliverType);
    }
}
