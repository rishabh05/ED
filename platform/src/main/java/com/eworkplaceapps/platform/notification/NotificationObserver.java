//===============================================================================
// Copyright(c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 25/05/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.applicationinfo.ApplicationManager;
import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachment;
import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachmentDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It will fetch the UnpreparedNotificationQueue table data from current time less then and equal to and then craete and add the NotificationQueue.
 * It work in following steps:
 * 1) Fetch UnpreparedNotificationQueue list, which is less then or equal to current date as! well as! status is Pending.
 * 2) Loop through UnpreparedNotificationQueue list and initilize NotificationQueue instance from UnpreparedNotificationQueue.
 * 3) Call application callback method to generate the message body for NotificationQueue.
 * 4) Add NotificationQueue.
 * 5) If notification queue added successfully then set UnpreparedNotificationQueue status from Pending to Processed otherwise set as! Error and update it.
 */
public class NotificationObserver {

    private NotificationQueueDataService notificationQueueService;

    public NotificationObserver() {
        this.notificationQueueService = new NotificationQueueDataService();
    }

    /**
     * @throws EwpException
     */
    public void generateNotificationQueueBodyFromUnNotificationQueue() throws EwpException {
        UnPreparedNotificationQueueDataService queueService = new UnPreparedNotificationQueueDataService();
        List<UnPreparedNotificationQueue> unPreparedNotificationQueues = queueService.getNotificationFromCurrentDateTimeDeliveryTypeAndNotificationType(NotificationEnum.NotificationTypeEnum.EVENT, NotificationEnum.NotificationDeliveryType.LOCAL_NOTIFICATION);

        if (unPreparedNotificationQueues != null) {
            NotificationQueueDataService notificationQueueService = new NotificationQueueDataService();
            NotificationQueue notificationQueue;
            UnPreparedNotificationQueue unPreNotificationQueue;
            for (int i = 0; i < unPreparedNotificationQueues.size(); i++) {
                unPreNotificationQueue = unPreparedNotificationQueues.get(i);
                boolean isAdded = createNotificationQueue(unPreNotificationQueue);
                // If NotificationQueue added successfully then set unPreNotificationQueue status as! processed.
                if (isAdded) {
                    unPreNotificationQueue.setNotificationProcessStatus(NotificationEnum.NotificationProcessStatusEnum.PROCESSED.getId());
                } else {
                    // If NotificationQueue failed in adding then set unPreNotificationQueue status as! Error.
                    unPreNotificationQueue.setNotificationProcessStatus(NotificationEnum.NotificationProcessStatusEnum.ERROR.getId());
                }
                // Update the unPreNotificationQueue status.
                queueService.update(unPreNotificationQueue);
            }
        }
    }

    /**
     * It will create the NotificationQueue instance and initialize NotificationQueue properties from UnPreparedNotificationQueue.
     * then add it into NotificationQueue table.
     *
     * @param unPreNotificationQueue
     */
    private boolean createNotificationQueue(UnPreparedNotificationQueue unPreNotificationQueue) throws EwpException {
        NotificationQueue notificationQueue = new NotificationQueue();
        notificationQueue.setSenderId(unPreNotificationQueue.getSenderId().toString());
        notificationQueue.setDeliverySubType(unPreNotificationQueue.getDeliverySubType());
        notificationQueue.setDeliveryType(unPreNotificationQueue.getDeliveryType());
        notificationQueue.setDeliveryTime(unPreNotificationQueue.getDeliveryTime());
        notificationQueue.setTenantId(unPreNotificationQueue.getTenantId());
        // Generating the local notification message body.
        NotificationMessageInfo result = generateMessageBody(unPreNotificationQueue);
        if (result != null) {
            notificationQueue.setMessage1(result.getNotificationQueue().getMessage1());
            notificationQueue.setMessage2(result.getNotificationQueue().getMessage2());
            // Add NotificationQueue
            Object resultQueueTuple = notificationQueueService.add(notificationQueue);
            // On success fully addition if NotificationQueue, Add notification attachment.
            if (resultQueueTuple != null) {
                addNotificationAttachment(result, (UUID) resultQueueTuple);
                return true;
            }
        }
        return false;
    }

    /**
     * It is used to generate the message body for notification queue object.
     * If any error occurred then return error.
     *
     * @param unPreNotificationQueue
     * @return NotificationMessageInfo
     */
    private NotificationMessageInfo generateMessageBody(UnPreparedNotificationQueue unPreNotificationQueue) {
        Map<String, Object> commandParameters = new HashMap<String, Object>();
        commandParameters.put("TenantId", unPreNotificationQueue.getTenantId());
        commandParameters.put("NotifierEntityType", unPreNotificationQueue.getSourceType());
        commandParameters.put("NotifierEntityId", unPreNotificationQueue.getEntityId());
        commandParameters.put("NotificationType", unPreNotificationQueue.getNotificationType());
        Object detail = ApplicationManager.executeOperation(ApplicationManager.AppCallbackCommand.RESOLVE_NOTIFICATION_MESSAGE_BODY, commandParameters, unPreNotificationQueue.getApplicationId());
        // It is used to handle error. In case of any error, we will get the error object.
        if (detail != null && detail instanceof NotificationMessageInfo) {
            NotificationMessageInfo notificationInfo = (NotificationMessageInfo) detail;
            // In case of message body, Will generate Notification queue
            return notificationInfo;
        }
        return null;
    }

    /**
     * Adding the notification attachment as! child data of NotificationQueue.
     *
     * @param notificationMessageInfo
     * @param notificationQueueId
     */
    private void addNotificationAttachment(NotificationMessageInfo notificationMessageInfo, UUID notificationQueueId) throws EwpException {
        NotificationAttachment attachment;
        NotificationAttachmentDataService service = new NotificationAttachmentDataService();
        List<NotificationAttachment> notificationAttachmentList = notificationMessageInfo.getNotificationAttachmentList();
        for (int i = 0; i < notificationAttachmentList.size(); i++) {
            // Assigning the notificationQueueId.
            notificationAttachmentList.get(i).setNotificationQueueId(notificationQueueId);
            // Adding the Notification attachment.
            service.add(notificationAttachmentList.get(i));
        }
    }
}
