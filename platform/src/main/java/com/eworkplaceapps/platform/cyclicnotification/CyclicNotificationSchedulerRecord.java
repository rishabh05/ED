//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.UUID;

public class CyclicNotificationSchedulerRecord extends BaseEntity {

    static String CyclicNotificationSchedulerRecord_Entity_Name = "PFCyclicNotificationSchedulerRecord";

    public CyclicNotificationSchedulerRecord() {
        super(CyclicNotificationSchedulerRecord_Entity_Name);
    }

    public static CyclicNotificationSchedulerRecord createEntity() {
        return new CyclicNotificationSchedulerRecord();
    }

    private int entityType = 0;

    private UUID mainEntityId = Utils.emptyUUID();

    private Date startDate=new Date();

    private Date endDate=new Date();

    private Date lastReminderDate=new Date();

    private Date nextReminderDate=new Date();

    private UUID actionId = Utils.emptyUUID();

    private String applicationId = "";

    private UUID cyclicNotificationId = Utils.emptyUUID();

    private UUID tenantId;

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        setPropertyChanged(entityType, this.entityType);
        this.entityType = entityType;
    }

    public UUID getMainEntityId() {
        return mainEntityId;
    }

    public void setMainEntityId(UUID mainEntityId) {
        setPropertyChanged(mainEntityId, this.mainEntityId);
        this.mainEntityId = mainEntityId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        setPropertyChanged(startDate, this.startDate);
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        setPropertyChanged(endDate, this.endDate);
        this.endDate = endDate;
    }

    public Date getLastReminderDate() {
        return lastReminderDate;
    }

    public void setLastReminderDate(Date lastReminderDate) {
        setPropertyChanged(lastReminderDate, this.lastReminderDate);
        this.lastReminderDate = lastReminderDate;
    }

    public Date getNextReminderDate() {
        return nextReminderDate;
    }

    public void setNextReminderDate(Date nextReminderDate) {
        setPropertyChanged(nextReminderDate, this.nextReminderDate);
        this.nextReminderDate = nextReminderDate;
    }

    public UUID getActionId() {
        return actionId;
    }

    public void setActionId(UUID actionId) {
        setPropertyChanged(nextReminderDate, this.nextReminderDate);
        this.actionId = actionId;
    }

    public UUID getCyclicNotificationId() {
        return cyclicNotificationId;
    }

    public void setCyclicNotificationId(UUID cyclicNotificationId) {
        setPropertyChanged(cyclicNotificationId, this.cyclicNotificationId);
        this.cyclicNotificationId = cyclicNotificationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }
}
