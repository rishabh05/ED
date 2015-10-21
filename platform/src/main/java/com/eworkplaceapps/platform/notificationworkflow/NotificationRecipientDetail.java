package com.eworkplaceapps.platform.notificationworkflow;

// Represents an notification recipient expressed with their type, delivery type and other related information.

import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notification.NotificationRecipient;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotificationRecipientDetail {


    public  NotificationRecipientDetail() { }

    private UUID notificationRecipientId = Utils.emptyUUID();

    // Gets or sets the type of the recipient.
    // The type of the recipient.
    private NotificationEnum.NotificationRecipientType recipientType  = NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT;

    // The recipient id
    private UUID recipientId = Utils.emptyUUID();

    private String externalRecipientEmail = "";

    private int pseudoUserId = 0;

    // UPDATE logic to map recipient email from platform.
    private String recipientEmail = "";

    // Gets or sets the type of the recipient delivery.
    // The type of the recipient delivery.
    private NotificationEnum.NotificationDeliveryType recipientDeliveryType  = NotificationEnum.NotificationDeliveryType.LOCAL_NOTIFICATION;

    public UUID getNotificationRecipientId() {
        return notificationRecipientId;
    }

    public void setNotificationRecipientId(UUID notificationRecipientId) {
        this.notificationRecipientId = notificationRecipientId;
    }

    public NotificationEnum.NotificationRecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(NotificationEnum.NotificationRecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public String getExternalRecipientEmail() {
        return externalRecipientEmail;
    }

    public void setExternalRecipientEmail(String externalRecipientEmail) {
        this.externalRecipientEmail = externalRecipientEmail;
    }

    public int getPseudoUserId() {
        return pseudoUserId;
    }

    public void setPseudoUserId(int pseudoUserId) {
        this.pseudoUserId = pseudoUserId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public NotificationEnum.NotificationDeliveryType getRecipientDeliveryType() {
        return recipientDeliveryType;
    }

    public void setRecipientDeliveryType(NotificationEnum.NotificationDeliveryType recipientDeliveryType) {
        this.recipientDeliveryType = recipientDeliveryType;
    }

    public int getRecipientSubDeliveryType() {
        return recipientSubDeliveryType;
    }

    public void setRecipientSubDeliveryType(int recipientSubDeliveryType) {
        this.recipientSubDeliveryType = recipientSubDeliveryType;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    // Gets or sets the type of the recipient sub delivery.
    // The type of the recipient sub delivery.
    private int recipientSubDeliveryType = 0;

    // Gets or sets the delivery time.
    // The delivery time.
    private Date deliveryTime;

    // Validate about this property versus ParentEntityType, EntityType, EventId etc.
    private UUID notificationId = Utils.emptyUUID();

    // ToDo: Validate about this property versus ParentEntityType, EntityType, EventId etc.
    private int notificationType = 0;


    // Maps current instance from notification recipient.
    // The NOTIFICATION_RECIPIENT instance.
    public static List<NotificationRecipientDetail> mapFromNotificationRecipient(List<NotificationRecipient> notificationRecipientList) {
        List<NotificationRecipientDetail> recipientDetailList = new ArrayList<>();

        for (int i = 0; i < notificationRecipientList.size(); i++) {
            NotificationRecipient recipient = notificationRecipientList.get(i);

            NotificationRecipientDetail recipientDetail = new NotificationRecipientDetail();
            recipientDetail.notificationRecipientId = recipient.getEntityId();

            NotificationEnum.NotificationRecipientType[] arrayFieldCode = NotificationEnum.NotificationRecipientType.values();
            recipientDetail.recipientType  = arrayFieldCode[recipient.getRecipientType()];
            recipientDetail.recipientId = recipient.getRecipientId();
            recipientDetail.externalRecipientEmail = recipient.getExternalRecipientEmail();
            recipientDetail.pseudoUserId = recipient.getPseudoUserCode();

            recipientDetail.notificationId = recipient.getSourceEntityId();
            recipientDetail.notificationType = recipient.getSourceEntityType();

            recipientDetailList.add(recipientDetail);
        }

        return recipientDetailList;
    }
}
