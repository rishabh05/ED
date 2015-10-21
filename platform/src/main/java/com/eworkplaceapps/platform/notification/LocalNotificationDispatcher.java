//===============================================================================
// Copyright(c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 25/05/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachment;

import java.util.List;

/**
 *
 */
public class LocalNotificationDispatcher {

    public boolean dispatchNotification(NotificationQueue notification, List<NotificationAttachment> notificationAttachmentList) {
        // Map NotificationQueue instance to iOS localnotification and send.
       /* UILocalNotification localNotification = UILocalNotification();
        localNotification.alertAction = ""
        localNotification.alertBody = notification.message1
        localNotification.fireDate = notification.deliveryTime
        localNotification.soundName = UILocalNotificationDefaultSoundName
        //localNotification.category = "invite"
        UIApplication.sharedApplication().scheduleLocalNotification(localNotification)*/
        return true;
    }
}
