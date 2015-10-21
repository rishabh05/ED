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

/**
 * This structure logs each system or user generated notification entry in database.
 */
public class DeliveredNotificationLog extends BaseEntity {
    static String DeliveredNotificationLog_Entity_Name = "DELIVERED_NOTIFICATION_LOG";

    // Default contstructor to initialize variables (if any).
    public DeliveredNotificationLog() {
        super(DeliveredNotificationLog_Entity_Name);
    }

    // Create EVENT_NOTIFICATION object and return created object.
    public static DeliveredNotificationLog createEntity() {
        return new DeliveredNotificationLog();
    }

    // Notification delivery type.
    private int deliveryType = 0;
    // Its a common seperated list of notification recipients.
    private String deliveryDestinations = "";

    // FIRST part of notification send (like email subject).
    private String message1 = "";
    // SECOND part of notification send (like (email/sms) body).
    private String message2 = "";

    // Its a notification delivery time.
    private Date deliveryTime=new Date();

    // Its a tenant user id, who send notification (in case of ad-hoc notification).
    private String senderId = "";
    // TENANT record reference id.
    private UUID tenantId = Utils.emptyUUID();

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        setPropertyChanged(deliveryType, this.deliveryType);
        this.deliveryType = deliveryType;
    }

    public String getDeliveryDestinations() {
        return deliveryDestinations;
    }

    public void setDeliveryDestinations(String deliveryDestinations) {
        setPropertyChanged(deliveryDestinations, this.deliveryDestinations);
        this.deliveryDestinations = deliveryDestinations;
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
        setPropertyChanged(tenantId, this.tenantId);
        this.tenantId = tenantId;
    }
}
