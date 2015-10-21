//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;

public class NotificationAttachment extends BaseEntity {

    private static final String NOTIFICATION_ATTACHMENT_ENTITY_NAME = "PFNotificationAttachment";

    public NotificationAttachment() {
        super(NOTIFICATION_ATTACHMENT_ENTITY_NAME);
    }

    /**
     * Create NOTIFICATION_ATTACHMENT object and return created object.
     *
     * @return NOTIFICATION_ATTACHMENT
     */
    public static NotificationAttachment createEntity() {
        return new NotificationAttachment();
    }

    private String fileName = "";

    private String fileString = "";

    private String filePath = "";

    private Boolean embeddedAttachment = false;

    private UUID notificationQueueId = Utils.emptyUUID();

    private String applicationId = "";

    private String tenantId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        setPropertyChanged(fileName, this.fileName);
        this.fileName = fileName;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        setPropertyChanged(fileString, this.fileString);
        this.fileString = fileString;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        setPropertyChanged(filePath, this.filePath);
        this.filePath = filePath;
    }

    public Boolean getEmbeddedAttachment() {
        return embeddedAttachment;
    }

    public void setEmbeddedAttachment(Boolean embeddedAttachment) {
        setPropertyChanged(embeddedAttachment, this.embeddedAttachment);
        this.embeddedAttachment = embeddedAttachment;
    }

    public UUID getNotificationQueueId() {
        return notificationQueueId;
    }

    public void setNotificationQueueId(UUID notificationQueueId) {
        setPropertyChanged(notificationQueueId, this.notificationQueueId);
        this.notificationQueueId = notificationQueueId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }
}
