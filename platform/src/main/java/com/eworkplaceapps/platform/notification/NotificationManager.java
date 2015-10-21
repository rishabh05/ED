//===============================================================================
// Copyright(c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 25/05/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachment;
import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachmentDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;

import java.util.List;

/**
 * Notification manager will work in-between  of Notification observer and Notification dispatcher.
 * It work in following steps:
 * 1) It will pick the NotificationQueue list from database.
 * 2) Loop through NotificationQueue list and send one-by-one to LocalNotification dispatcher.
 * 3) Then make entry in NotificationLog and delete the item from NotificationQueue table.
 */
public class NotificationManager {

    private LocalNotificationDispatcher localNotificationService;
    private NotificationQueueDataService notificationQueueService;
    private DeliveredNotificationLogDataService deliveredNotificationLogService;
    private NotificationAttachmentDataService notificationAttachmentService;

    public NotificationManager() {
        localNotificationService = new LocalNotificationDispatcher();
        notificationQueueService = new NotificationQueueDataService();
        deliveredNotificationLogService = new DeliveredNotificationLogDataService();
        notificationAttachmentService = new NotificationAttachmentDataService();
    }

    /**
     * It will process the NotificationQueue list and will send to dispatcher for local notification.
     *
     * @throws EwpException
     */
    public void execute() throws EwpException {
        List<NotificationQueue> notificationQueueList = notificationQueueService.getNotificationQueueFromCurrentDeliveryTime();
        if (notificationQueueList != null) {
            for (int i = 0; i < notificationQueueList.size(); i++) {
                processNotificationQueue(notificationQueueList.get(i));
            }
        }
    }

    /**
     * It will dispatch notification for local notification and log the notification then delete NotificationQueue.
     *
     * @param notificationQueue
     * @throws EwpException
     */
    private void processNotificationQueue(NotificationQueue notificationQueue) throws EwpException {
        // Dispatch notification.
        List<NotificationAttachment> notificationAttachments = notificationAttachmentService.getListByNotificationQueueId(notificationQueue.getEntityId());
        localNotificationService.dispatchNotification(notificationQueue, notificationAttachments);
        // Log Dispatched notification in delivered log.
        DeliveredNotificationLog log = new DeliveredNotificationLog();
        log.setMessage1((notificationQueue.getMessage1() == null || "".equals(notificationQueue.getMessage1())) ? "" : notificationQueue.getMessage1());
        log.setMessage2((notificationQueue.getMessage2() == null || "".equals(notificationQueue.getMessage2())) ? "" : notificationQueue.getMessage2());
        log.setSenderId(notificationQueue.getSenderId());
        log.setTenantId(notificationQueue.getTenantId());
        log.setDeliveryTime(notificationQueue.getDeliveryTime());
        log.setDeliveryType(notificationQueue.getDeliveryType());
        // It might change.
        log.setDeliveryDestinations(EwpSession.getSharedInstance().getUserId().toString());
        deliveredNotificationLogService.add(log);
        // Delete Notification from notification queue table after dispatch.
        notificationQueueService.delete(notificationQueue);
    }
}
