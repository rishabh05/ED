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


public class CyclicNotification extends BaseEntity {

    public static final String CYCLIC_NOTIFICATION_ENTITY_NAME = "PFCyclicNotification";

    public CyclicNotification() {
        super(CYCLIC_NOTIFICATION_ENTITY_NAME);
    }

    /**
     * Create CYCLIC_NOTIFICATION object and return created object
     *
     * @return CYCLIC_NOTIFICATION
     */
    public static CyclicNotification createEntity() {
        return new CyclicNotification();
    }

    private Long days = 0L;

    private Integer cyclicType = 0;

    private Integer cyclicSubtype = 0;

    private Integer cyclicPattern = 0;

    private Boolean active = true;

    private Integer remindType = 0;

    private Integer remindStartTimeUnit = 0;

    private Integer remindStartTimeDelta = 0;

    private Integer frequencyUnit = 0;

    private Integer frequencyInterval = 0;

    private Date repeatStartDate = new Date();

    private Date repeatEndDate = new Date();

    private Integer actionId = 0;

    private Integer appCyclicNotificationTypeNo = 0;

    private UUID cyclicNotificationId = Utils.emptyUUID();

    private String applicationId = "";

    private UUID tenantId;


    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        setPropertyChanged(days, this.days);
        this.days = days;
    }

    public Integer getCyclicType() {
        return cyclicType;
    }

    public void setCyclicType(Integer cyclicType) {
        setPropertyChanged(cyclicType, this.cyclicType);
        this.cyclicType = cyclicType;
    }

    public Integer getCyclicSubtype() {
        return cyclicSubtype;
    }

    public void setCyclicSubtype(Integer cyclicSubtype) {
        setPropertyChanged(cyclicSubtype, this.cyclicSubtype);
        this.cyclicSubtype = cyclicSubtype;
    }

    public Integer getCyclicPattern() {
        return cyclicPattern;
    }

    public void setCyclicPattern(Integer cyclicPattern) {
        setPropertyChanged(cyclicPattern, this.cyclicPattern);
        this.cyclicPattern = cyclicPattern;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        setPropertyChanged(active, this.active);
        this.active = active;
    }

    public Integer getRemindType() {
        return remindType;
    }

    public void setRemindType(Integer remindType) {
        setPropertyChanged(remindType, this.remindType);
        this.remindType = remindType;
    }

    public Integer getRemindStartTimeUnit() {
        return remindStartTimeUnit;
    }

    public void setRemindStartTimeUnit(Integer remindStartTimeUnit) {
        setPropertyChanged(remindStartTimeUnit, this.remindStartTimeUnit);
        this.remindStartTimeUnit = remindStartTimeUnit;
    }

    public Integer getRemindStartTimeDelta() {
        return remindStartTimeDelta;
    }

    public void setRemindStartTimeDelta(Integer remindStartTimeDelta) {
        setPropertyChanged(remindStartTimeDelta, this.remindStartTimeDelta);
        this.remindStartTimeDelta = remindStartTimeDelta;
    }

    public Integer getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(Integer frequencyUnit) {
        setPropertyChanged(frequencyUnit, this.frequencyUnit);
        this.frequencyUnit = frequencyUnit;
    }

    public Integer getFrequencyInterval() {
        return frequencyInterval;
    }

    public void setFrequencyInterval(Integer frequencyInterval) {
        setPropertyChanged(frequencyInterval, this.frequencyInterval);
        this.frequencyInterval = frequencyInterval;
    }

    public Date getRepeatStartDate() {
        return repeatStartDate;
    }

    public void setRepeatStartDate(Date repeatStartDate) {
        setPropertyChanged(repeatStartDate, this.repeatStartDate);
        this.repeatStartDate = repeatStartDate;
    }

    public Date getRepeatEndDate() {
        return repeatEndDate;
    }

    public void setRepeatEndDate(Date repeatEndDate) {
        setPropertyChanged(repeatEndDate, this.repeatEndDate);
        this.repeatEndDate = repeatEndDate;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        setPropertyChanged(actionId, this.actionId);
        this.actionId = actionId;
    }

    /**
     * It will contain EDCyclicNotification enum value.
     */
    public Integer getAppCyclicNotificationTypeNo() {
        return appCyclicNotificationTypeNo;
    }

    public void setAppCyclicNotificationTypeNo(Integer appCyclicNotificationTypeNo) {
        setPropertyChanged(appCyclicNotificationTypeNo, this.appCyclicNotificationTypeNo);
        this.appCyclicNotificationTypeNo = appCyclicNotificationTypeNo;
    }

    public UUID getCyclicNotificationId() {
        return cyclicNotificationId;
    }

    public void setCyclicNotificationId(UUID cyclicNotificationId) {
        setPropertyChanged(cyclicNotificationId, this.cyclicNotificationId);
        this.cyclicNotificationId = cyclicNotificationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(applicationId, this.applicationId);
        this.applicationId = applicationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(tenantId, this.tenantId);
        this.tenantId = tenantId;
    }
}
