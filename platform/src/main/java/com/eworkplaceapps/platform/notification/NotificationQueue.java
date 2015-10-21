//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseEntity;

import java.util.Date;
import java.util.UUID;

public class NotificationQueue extends BaseEntity {
    private static final String NOTIFICATION_QUEUE_ENTITY_NAME = "PFNotificationQueue";

    public NotificationQueue() {
        super(NOTIFICATION_QUEUE_ENTITY_NAME);
    }


    public static NotificationQueue createEntity() {
        return new NotificationQueue();
    }

    /**
     * Notification delivery type.
     * It stores the predefined enum value.
     */
    private int deliveryType = 0;
    /**
     * Notification delivery sub type.
     */
    private int deliverySubType = 0;
    private String targetInfo;
    // FIRST part of notification send (like email subject).
    private String message1;
    // SECOND part of notification send (like email body).
    private String message2;
    // Its a notification delivery time.
    private Date deliveryTime=new Date();
    // Gets or sets the sender identifier.
    // Sender unique identifier.
    private String senderId;
    private UUID tenantId;

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        setPropertyChanged(deliveryType, this.deliveryType);
        this.deliveryType = deliveryType;
    }

    public int getDeliverySubType() {
        return deliverySubType;
    }

    public void setDeliverySubType(int deliverySubType) {
        setPropertyChanged(deliverySubType, this.deliverySubType);
        this.deliverySubType = deliverySubType;
    }

    public String getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(String targetInfo) {
        setPropertyChanged(targetInfo, this.targetInfo);
        this.targetInfo = targetInfo;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        setPropertyChanged(message1, this.message1);
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        setPropertyChanged(message2, this.message2);
        this.message2 = message2;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        setPropertyChanged(deliveryTime, this.deliveryTime);
        this.deliveryTime = deliveryTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        setPropertyChanged(senderId, this.senderId);
        this.senderId = senderId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

}
