//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.notificationworkflow;


import com.eworkplaceapps.platform.notification.EventNotification;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RawNotification {

    // Initializes a new instance of class.
    public RawNotification() {
        resolvedEventNotification = new ArrayList<>();
        otherData = new HashMap<>();
    }

    // Gets or sets the application identifier.
    // The application identifier.
    private String applicationId = "";

    // Gets or sets the login user identifier.
    // The login user identifier.
    private UUID loginUserId = Utils.emptyUUID();

    // Gets or sets the name of the login user.
    // The name of the login user.
    private String loginUserName = "";

    // Gets or sets the login tenant identifier.
    // The login tenant identifier.
    private UUID loginTenantId = Utils.emptyUUID();

    // Gets or sets the type of the notifier entity.
    // The type of the notifier entity.
    private int notifierEntityType = 0;

    // Gets or sets the notifier entity identifier.
    // The notifier entity identifier.
    private UUID notifierEntityId = Utils.emptyUUID();

    // Gets or sets the sender identifier.
    // The sender identifier.
    private UUID senderId = Utils.emptyUUID();

    // Validate that this property should be part of this class or not.
    private List<EventNotification> resolvedEventNotification;
    // Gets or sets the type of the notification.
    // The type of the notification.
    private NotificationEnum.NotificationTypeEnum notificationType = NotificationEnum.NotificationTypeEnum.EVENT;

    private UUID notificationEntityId = Utils.emptyUUID();

    private int notificationEntityType = 0;

    // Gets or sets the other data.
    // The other data.
    private Map<String, String> otherData = new HashMap<>();

    private List<NotificationRecipientDetail> notificationRecipientDetailList;

    private UUID cyclicNotificationId = Utils.emptyUUID();

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public UUID getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(UUID loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public UUID getLoginTenantId() {
        return loginTenantId;
    }

    public void setLoginTenantId(UUID loginTenantId) {
        this.loginTenantId = loginTenantId;
    }

    public int getNotifierEntityType() {
        return notifierEntityType;
    }

    public void setNotifierEntityType(int notifierEntityType) {
        this.notifierEntityType = notifierEntityType;
    }

    public UUID getNotifierEntityId() {
        return notifierEntityId;
    }

    public void setNotifierEntityId(UUID notifierEntityId) {
        this.notifierEntityId = notifierEntityId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public List<EventNotification> getResolvedEventNotification() {
        return resolvedEventNotification;
    }

    public void setResolvedEventNotification(List<EventNotification> resolvedEventNotification) {
        this.resolvedEventNotification = resolvedEventNotification;
    }

    public NotificationEnum.NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationEnum.NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
    }

    public UUID getNotificationEntityId() {
        return notificationEntityId;
    }

    public void setNotificationEntityId(UUID notificationEntityId) {
        this.notificationEntityId = notificationEntityId;
    }

    public int getNotificationEntityType() {
        return notificationEntityType;
    }

    public void setNotificationEntityType(int notificationEntityType) {
        this.notificationEntityType = notificationEntityType;
    }

    public Map<String, String> getOtherData() {
        return otherData;
    }

    public void setOtherData(Map<String, String> otherData) {
        this.otherData = otherData;
    }

    public List<NotificationRecipientDetail> getNotificationRecipientDetailList() {
        return notificationRecipientDetailList;
    }

    public void setNotificationRecipientDetailList(List<NotificationRecipientDetail> notificationRecipientDetailList) {
        this.notificationRecipientDetailList = notificationRecipientDetailList;
    }

    public UUID getCyclicNotificationId() {
        return cyclicNotificationId;
    }

    public void setCyclicNotificationId(UUID cyclicNotificationId) {
        this.cyclicNotificationId = cyclicNotificationId;
    }
}
