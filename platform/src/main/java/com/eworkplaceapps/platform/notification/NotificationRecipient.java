//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;

public class NotificationRecipient extends BaseEntity {

    static String NotificationRecipient_Entity_Name = "PFNotificationRecipient";

    public NotificationRecipient() {
        super(NotificationRecipient_Entity_Name);
    }

    // Create NOTIFICATION_RECIPIENT object and return created object.
    public static NotificationRecipient createEntity() {
        return new NotificationRecipient();
    }

    /// The source notification type like EVENT notification, reminder notification etc.
    private int sourceEntityType = 0;

    /// The source notification record id.
    private UUID sourceEntityId = Utils.emptyUUID();
    /// The recipient type.
    private int recipientType = 0;

    /// The recipient id in case of recipient type is internal user.
    private UUID recipientId = Utils.emptyUUID();

    /// The external recipient email in case of recipient type is external email.
    private String externalRecipientEmail = "";

    /// The recipient id in case of recipient type is pseudo user.
    private int pseudoUserCode = 0;

    private UUID tenantId;

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(sourceEntityType, this.sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(tenantId, this.tenantId);
        this.tenantId = tenantId;
    }

    public int getPseudoUserCode() {
        return pseudoUserCode;
    }

    public void setPseudoUserCode(int pseudoUserCode) {
        setPropertyChanged(pseudoUserCode, this.pseudoUserCode);
        this.pseudoUserCode = pseudoUserCode;
    }

    public String getExternalRecipientEmail() {
        return externalRecipientEmail;
    }

    public void setExternalRecipientEmail(String externalRecipientEmail) {
        setPropertyChanged(externalRecipientEmail, this.externalRecipientEmail);
        this.externalRecipientEmail = externalRecipientEmail;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        setPropertyChanged(recipientId, this.recipientId);
        this.recipientId = recipientId;
    }

    public int getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(int recipientType) {
        this.recipientType = recipientType;
    }

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(sourceEntityId, this.sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }
}
