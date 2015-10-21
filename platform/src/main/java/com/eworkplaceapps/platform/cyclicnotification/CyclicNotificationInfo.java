//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import com.eworkplaceapps.platform.notification.NotificationRecipient;

import java.util.List;

public class CyclicNotificationInfo {

   public CyclicNotificationInfo() { }

    public CyclicNotification getCyclicNotificationEntity() {
        return cyclicNotificationEntity;
    }

    public void setCyclicNotificationEntity(CyclicNotification cyclicNotificationEntity) {
        this.cyclicNotificationEntity = cyclicNotificationEntity;
    }

    public List<CyclicNotificationDetails> getCyclicNotificationDetailsList() {
        return  cyclicNotificationDetailsList;
    }

    public void setCyclicNotificationDetailsList(List<CyclicNotificationDetails> cyclicNotificationDetailsList) {
        this.cyclicNotificationDetailsList = cyclicNotificationDetailsList;
    }

    public CyclicNotificationLinking getCyclicNotificationLinkingEntity() {
        return cyclicNotificationLinkingEntity;
    }

    public void setCyclicNotificationLinkingEntity(CyclicNotificationLinking cyclicNotificationLinkingEntity) {
        this.cyclicNotificationLinkingEntity = cyclicNotificationLinkingEntity;
    }

    public List<NotificationRecipient> getRecipientsList() {
        return recipientsList;
    }

    public void setRecipientsList(List<NotificationRecipient> recipientsList) {
        this.recipientsList = recipientsList;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /// CYCLIC_NOTIFICATION object.
    private CyclicNotification cyclicNotificationEntity;

    /// Repetitoin Detail object
    private List<CyclicNotificationDetails> cyclicNotificationDetailsList;

    /// CYCLIC_NOTIFICATION Linking object.
    private CyclicNotificationLinking cyclicNotificationLinkingEntity;

    /// Recipient list.
    private List<NotificationRecipient> recipientsList;

    /// The application id.
    private String applicationId;
}
