//===============================================================================
// Copyright(c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 25/05/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachment;

import java.util.List;

/**
 * It is used to contain the notification message body information.
 * NotificationAttachment can be more then 0 for a NotificationQueue item.
 */
public class NotificationMessageInfo {

    private NotificationQueue notificationQueue;
    private List<NotificationAttachment> notificationAttachmentList;

    public NotificationMessageInfo(NotificationQueue notificationQueue, List<NotificationAttachment> notificationAttachmentList) {
        this.notificationQueue = notificationQueue;
        this.notificationAttachmentList = notificationAttachmentList;
    }

    public NotificationQueue getNotificationQueue() {
        return notificationQueue;
    }

    public void setNotificationQueue(NotificationQueue notificationQueue) {
        this.notificationQueue = notificationQueue;
    }

    public List<NotificationAttachment> getNotificationAttachmentList() {
        return notificationAttachmentList;
    }

    public void setNotificationAttachmentList(List<NotificationAttachment> notificationAttachmentList) {
        this.notificationAttachmentList = notificationAttachmentList;
    }
}
