//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.applicationinfo.BaseApplicationInfo;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notification.NotificationInfo;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It contains employee application information.
 */
public class EDApplicationInfo extends BaseApplicationInfo {

    /**
     * Defines enum constants for notification event.
     */
    public enum EDEmployeeEventNotification {
        /// The 'Employee Out of Office' notification.
        /// This event occurred when any employee sets its status to 'Out of Office'.
        EMPLOYEE_OUT_OF_OFFICE(1);
        private int id = 0;

        EDEmployeeEventNotification(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /// Create the singleton
    static {
        supportedDeliveryTypes = new ArrayList<NotificationEnum.NotificationDeliveryType>();
        supportedLocalNotificationType = new ArrayList<NotificationEnum.LocalNotificationType>();
        notificationInformationList = new ArrayList<NotificationInfo>();
    }

    /// Returns the singleton. Its reaonly property.
    public static List<NotificationInfo> getNotificationInformationList() {
        return notificationInformationList;
    }

    // Begin Local Members
    private static List<NotificationInfo> notificationInformationList;
    private static final UUID APP_ID = UUID.fromString("A8920FC7-4874-4854-BCE1-4C4909C6C824");
    private static final String APP_ABBREVIATION = "ED";
    private static final String APP_NAME = "Employee Directory";
    private static final String APP_VERSION = "1.0.0";
    private static List<NotificationEnum.NotificationDeliveryType> supportedDeliveryTypes;
    private static List<NotificationEnum.LocalNotificationType> supportedLocalNotificationType;

    public static UUID getAppId() {
        return APP_ID;
    }

    public static String getAppAbbreviation() {
        return APP_ABBREVIATION;
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static String getAppVersion() {
        return APP_VERSION;
    }


    /**
     * Initializes a new instance of the EDApplicationInfo class.
     */
    public EDApplicationInfo() {
        super();
        initializeEDNotificationInformation();
    }

    private void initializeEDNotificationInformation() {
        /// Employee 'Out of Office' event notification.
        NotificationInfo outOfOfficeEventNotification = new NotificationInfo(NotificationEnum.NotificationTypeEnum.EVENT, EDEntityType.EMPLOYEE.getId(), EDEntityType.EMPLOYEE.getId(), EDEmployeeEventNotification.EMPLOYEE_OUT_OF_OFFICE.getId(), EDEntityType.EMPLOYEE.getId(), supportedDeliveryTypes, supportedLocalNotificationType);
        notificationInformationList.add(outOfOfficeEventNotification);
        /// This Week email notification.
        NotificationInfo thisWeekEmailNotification = new NotificationInfo(NotificationEnum.NotificationTypeEnum.CYCLIC, EDEntityType.EMPLOYEE.getId(), EDEntityType.EMPLOYEE.getId(), EmployeeEnums.EDCyclicNotification.WEEKLY_DIGEST.getId(), EDEntityType.NONE.getId(), supportedDeliveryTypes, supportedLocalNotificationType);
        notificationInformationList.add(thisWeekEmailNotification);
        /// Employee 'Ping' Adhoc notification.
        NotificationInfo pingAdhocNotification = new NotificationInfo(NotificationEnum.NotificationTypeEnum.AD_HOC, EDEntityType.EMPLOYEE.getId(),
                EDEntityType.EMPLOYEE.getId(), com.eworkplaceapps.platform.utils.enums.EmployeeEnums.EDAdhocNotification.PING.getId(), EDEntityType.NONE.getId(), supportedDeliveryTypes, supportedLocalNotificationType);
        notificationInformationList.add(pingAdhocNotification);
    }

    public static void setNotificationInformationList(List<NotificationInfo> notificationInformationList) {
        EDApplicationInfo.notificationInformationList = notificationInformationList;
    }

    public static List<NotificationEnum.NotificationDeliveryType> getSupportedDeliveryTypes() {
        return supportedDeliveryTypes;
    }

    public static void setSupportedDeliveryTypes(List<NotificationEnum.NotificationDeliveryType> supportedDeliveryTypes) {
        EDApplicationInfo.supportedDeliveryTypes = supportedDeliveryTypes;
    }

    public static List<NotificationEnum.LocalNotificationType> getSupportedLocalNotificationType() {
        return supportedLocalNotificationType;
    }

    public static void setSupportedLocalNotificationType(List<NotificationEnum.LocalNotificationType> supportedLocalNotificationType) {
        EDApplicationInfo.supportedLocalNotificationType = supportedLocalNotificationType;
    }
}
