//===============================================================================
//Copyright(c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 25/05/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NotificationInfo {

    private List<NotificationEnum.NotificationDeliveryType> supportedDeliveryTypes;
    private List<NotificationEnum.LocalNotificationType> supportedLocalNotificationType;
    private NotificationEnum.NotificationTypeEnum notificationTypeEnumId;
    private int parentEntityType;
    private int entityType;
    private int notificationEnumId;
    private int notifiedEntity;

    /**
     * Initializes a new instance of the NotificationInfo class.
     */
    public NotificationInfo() {
        this.supportedDeliveryTypes = new ArrayList<NotificationEnum.NotificationDeliveryType>();
        this.supportedLocalNotificationType = new ArrayList<NotificationEnum.LocalNotificationType>();
        this.notificationTypeEnumId = NotificationEnum.NotificationTypeEnum.EVENT;
    }

    /**
     * Initializes a new instance of the NotificationInfo class.
     *
     * @param notificationTypeEnumId         The notification type enum identifier.
     * @param parentEntityType               Type of the parent entity.
     * @param entityType                     Type of the entity.
     * @param notificationEnumId             The notification enum identifier.
     * @param notifiedEntity                 The notified entity.
     * @param supportedDeliveryType          Type of the supported delivery.
     * @param supportedLocalNotificationType Type of the supported local notification.
     */
    public NotificationInfo(NotificationEnum.NotificationTypeEnum notificationTypeEnumId, int parentEntityType, int entityType, int notificationEnumId, int notifiedEntity, List<NotificationEnum.NotificationDeliveryType> supportedDeliveryType, List<NotificationEnum.LocalNotificationType> supportedLocalNotificationType) {
        this.notificationTypeEnumId = notificationTypeEnumId;
        this.parentEntityType = parentEntityType;
        this.entityType = entityType;
        this.notificationEnumId = notificationEnumId;
        this.notificationTypeEnumId = notificationTypeEnumId;
        this.notifiedEntity = notifiedEntity;
        this.supportedDeliveryTypes = supportedDeliveryType;
        this.supportedLocalNotificationType = supportedLocalNotificationType;
    }

    public List<NotificationEnum.NotificationDeliveryType> getSupportedDeliveryTypes() {
        return supportedDeliveryTypes;
    }

    public void setSupportedDeliveryTypes(List<NotificationEnum.NotificationDeliveryType> supportedDeliveryTypes) {
        this.supportedDeliveryTypes = supportedDeliveryTypes;
    }

    public List<NotificationEnum.LocalNotificationType> getSupportedLocalNotificationType() {
        return supportedLocalNotificationType;
    }

    public void setSupportedLocalNotificationType(List<NotificationEnum.LocalNotificationType> supportedLocalNotificationType) {
        this.supportedLocalNotificationType = supportedLocalNotificationType;
    }

    public NotificationEnum.NotificationTypeEnum getNotificationTypeEnumId() {
        return notificationTypeEnumId;
    }

    public void setNotificationTypeEnumId(NotificationEnum.NotificationTypeEnum notificationTypeEnumId) {
        this.notificationTypeEnumId = notificationTypeEnumId;
    }

    public int getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(int parentEntityType) {
        this.parentEntityType = parentEntityType;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getNotificationEnumId() {
        return notificationEnumId;
    }

    public void setNotificationEnumId(int notificationEnumId) {
        this.notificationEnumId = notificationEnumId;
    }

    public int getNotifiedEntity() {
        return notifiedEntity;
    }

    public void setNotifiedEntity(int notifiedEntity) {
        this.notifiedEntity = notifiedEntity;
    }

    /**
     * @param notificationId
     * @param notificationInfoList
     * @return NotificationInfo instance
     */
    public static NotificationInfo getNotificationInfoByNotificationId(int notificationId, List<NotificationInfo> notificationInfoList) {
        for (int i = 0; i < notificationInfoList.size(); i++) {
            if (notificationInfoList.get(i).getNotificationEnumId() == notificationId) {
                return notificationInfoList.get(i);
            }
        }
        return null;
    }
}
