//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class NotificationRecipientDataService extends BaseDataService<NotificationRecipient> {

    NotificationRecipientData dataDelegate = new NotificationRecipientData();

    /**
     * Initializes a new instance of the NotificationRecipientDataService class.
     */
    public NotificationRecipientDataService() {
        super("NOTIFICATION_RECIPIENT");
    }

    public NotificationRecipientData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    public List<NotificationRecipientDetail> getNotificationRecipientDetailListByEntityTypeAndId(int notificationType, UUID notificationId) throws EwpException {
        List<NotificationRecipientDetail> detail = new ArrayList<>();

        List<NotificationRecipient> entityList = dataDelegate.getNotificationRecipientDetailListByEntityTypeAndId(notificationType, notificationId);

        // If any error occured then return from here.
        if (entityList == null) {
            return detail;
        }
        return NotificationRecipientDetail.mapFromNotificationRecipient(entityList);
    }
}
