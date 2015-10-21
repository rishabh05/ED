//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.UUID;

public class UnPreparedNotificationQueue extends BaseEntity {
    private static final String UN_PREPARED_NOTIFICATION_QUEUE_ENTITY_NAME = "PFUnPreparedNotificationQueue";

    public UnPreparedNotificationQueue() {
        super(UN_PREPARED_NOTIFICATION_QUEUE_ENTITY_NAME);
    }

    /**
     * Create UnPreparedNotificationQueue object and return created object.
     *
     * @return UnPreparedNotificationQueue
     */
    public static UnPreparedNotificationQueue createEntity() {
        return new UnPreparedNotificationQueue();
    }

    // Notification delivery type.
    //It stores the predefined enum value.
    private int deliveryType = 0;
    // Notification delivery sub type.
    private int deliverySubType = 0;
    // Its a notification delivery time.
    private Date deliveryTime=new Date();
    // Gets or sets the sender identifier.
    // Sender unique identifier.
    private UUID senderId = Utils.emptyUUID();
    /// The recipient type.
    private int recipientType = 0;
    /// The recipient id in case of recipient type is internal user.
    private UUID recipientId = Utils.emptyUUID();

    /// The external recipient email in case of recipient type is external email.
    private String externalRecipientEmail = "";
    /// The recipient id in case of recipient type is pseudo user.
    private int pseudoUserCode = 0;
    // Notification type.
    private int notificationType = 0;
    private int notificationProcessStatus;
    private String otherInformation;
    private int sourceType;
    private UUID sourceId;
    private UUID tenantId;
    private String applicationId = "";

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

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        setPropertyChanged(deliveryTime, this.deliveryTime);
        this.deliveryTime = deliveryTime;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        setPropertyChanged(senderId, this.senderId);
        this.senderId = senderId;
    }

    public int getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(int recipientType) {
        setPropertyChanged(recipientType, this.recipientType);
        this.recipientType = recipientType;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        setPropertyChanged(recipientId, this.recipientId);
        this.recipientId = recipientId;
    }

    public String getExternalRecipientEmail() {
        return externalRecipientEmail;
    }

    public void setExternalRecipientEmail(String externalRecipientEmail) {
        setPropertyChanged(externalRecipientEmail, this.externalRecipientEmail);
        this.externalRecipientEmail = externalRecipientEmail;
    }

    public int getPseudoUserCode() {
        return pseudoUserCode;
    }

    public void setPseudoUserCode(int pseudoUserCode) {
        setPropertyChanged(pseudoUserCode, this.pseudoUserCode);
        this.pseudoUserCode = pseudoUserCode;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        setPropertyChanged(notificationType, this.notificationType);
        this.notificationType = notificationType;
    }

    public int getNotificationProcessStatus() {
        return notificationProcessStatus;
    }

    public void setNotificationProcessStatus(int notificationProcessStatus) {
        setPropertyChanged(notificationProcessStatus, this.notificationProcessStatus);
        this.notificationProcessStatus = notificationProcessStatus;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        setPropertyChanged(otherInformation, this.otherInformation);
        this.otherInformation = otherInformation;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        setPropertyChanged(sourceType, this.sourceType);
        this.sourceType = sourceType;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
