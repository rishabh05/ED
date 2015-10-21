//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.applicationinfo.ApplicationManager;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.notification.EventNotification;
import com.eworkplaceapps.platform.notification.NotificationEnum.NotificationTypeEnum;
import com.eworkplaceapps.platform.notification.NotificationMessageInfo;
import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.eworkplaceapps.platform.notification.NotificationEnum.NotificationTypeEnum.values;

/**
 * This class contains initEmployeeApp() method which will execute init code,
 * like registering the app with Notification Service module etc.
 * This class may have other init-related data and methods if required.
 * AppDelegate init will call the static initEmployeeApp() method to register app.
 */
public class EDAppInit {

    /**
     * This method is call on application start.
     */
    public static void initEmployeeApp() {
        EDApplicationInfo appInfo = new EDApplicationInfo();
        ApplicationManager.register(appInfo);
    }

    /**
     * Call back function to get employee directory application data
     * /// :param: command: Application callback command to get application data
     * /// :param: parameters: Parameters  to get application data
     *
     * @param command
     * @param parameters
     * @return Object
     * @throws EwpException
     */
    public static Object getApplicationDataHandler(ApplicationManager.AppCallbackCommand command, Map<String, Object> parameters) throws EwpException {
        if (command == ApplicationManager.AppCallbackCommand.RESOLVE_NOTIFICATION_RECIPIENT) {
            return resolveNotificationRecipients(parameters);
        } else if (command == ApplicationManager.AppCallbackCommand.RESOLVE_NOTIFICATION_MESSAGE_BODY) {
            return resolveNotificationRecipients(parameters);
        }
        return null;
    }

    /**
     * It will call from getApplication call back method.
     *
     * @param parameters
     * @return List<NotificationRecipientDetail>
     */
    private static List<NotificationRecipientDetail> resolveNotificationRecipients(Map<String, Object> parameters) throws EwpException {
        int notificationTypeInt = (int) parameters.get("NotificationType");
        NotificationTypeEnum notificationTypeEnum = NotificationTypeEnum.values()[notificationTypeInt];
        List<EventNotification> eNotification = (List<EventNotification>) parameters.get("ResolvedNotificationInfo");

        switch (notificationTypeEnum) {
            case EVENT:
                int edInt = (int) parameters.get("NotifierEntityType");
                EDEntityType edEntityType = EDEntityType.values()[edInt];
                UUID tenantId = (UUID) parameters.get("TenantId");
                UUID entityId = (UUID) parameters.get("NotifierEntityId");
                /// It is used to filter Event notification from list of notification.
                return EDNotificationDataService.getEdNotificationDataService().getResolvedEventNotificationRecipientDetailList(eNotification, tenantId, edEntityType, entityId);
            case AD_HOC:
                break;
            case CYCLIC:
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * It will call from getApplication call back method
     *
     * @param parameters
     * @return NotificationMessageInfo
     */
    private static NotificationMessageInfo resolveNotificationBody(Map<String, Object> parameters) throws EwpException {
        int notificationTypeInt = (Integer) parameters.get("NotificationType");
        NotificationTypeEnum notificationTypeEnum = values()[notificationTypeInt];
        switch (notificationTypeEnum) {
            case EVENT:
                int edInt = (Integer) parameters.get("NotifierEntityType");
                EDEntityType edEntityType = EDEntityType.values()[edInt];
                UUID tenantId = (UUID) parameters.get("TenantId");
                UUID entityId = (UUID) parameters.get("NotifierEntityId");
                /// It is used to filter Event notification from list of notification.
                return EDNotificationDataService.getEdNotificationDataService().getResolvedNotificationQueueMessageBody(tenantId, edEntityType, entityId);
            case AD_HOC:
                break;
            case CYCLIC:
                break;
            default:
                break;
        }
        return null;
    }
}
