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


public class NotificationAttachmentDataService extends BaseDataService<NotificationAttachment> {

    NotificationAttachmentData dataDelegate = new NotificationAttachmentData();

    /**
     * Initializes a new instance of the NotificationAttachmentDataService class.
     */
    public NotificationAttachmentDataService() {
        super("NOTIFICATION_ATTACHMENT");
    }

    @Override
    public NotificationAttachmentData getDataClass() {
        return dataDelegate;
    }

    /**
     * ----------------------- Begin Class Methods ---------------
     *
     * @param notificationQueueId UUID
     * @return List<NOTIFICATION_ATTACHMENT>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public List<NotificationAttachment> getListByNotificationQueueId(UUID notificationQueueId) throws EwpException {
        return dataDelegate.getListByNotificationQueueId(notificationQueueId);
    }
}
