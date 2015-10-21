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


public class CyclicNotificationDetails extends BaseEntity {

    static String CyclicNotificationDetails_Entity_Name = "PFCyclicNotificationDetails";


    public CyclicNotificationDetails() {
        super(CyclicNotificationDetails_Entity_Name);
    }

    /**
     * Create CYCLIC_NOTIFICATION_DETAILS object and return created object.
     *
     * @return CYCLIC_NOTIFICATION_DETAILS
     */
    public static CyclicNotificationDetails createEntity() {
        return new CyclicNotificationDetails();
    }

    private int subscriptionDay = 0;

    private Date subscriptionDateTime=new Date();
    private UUID cyclicNotificationId = Utils.emptyUUID();

    private String applicationId = "";

    private UUID tenantId;

    public int getSubscriptionDay() {
        return subscriptionDay;
    }

    public void setSubscriptionDay(int subscriptionDay) {
        setPropertyChanged(subscriptionDay, this.subscriptionDay);
        this.subscriptionDay = subscriptionDay;
    }

    public Date getSubscriptionDateTime() {
        return subscriptionDateTime;
    }

    public void setSubscriptionDateTime(Date subscriptionDateTime) {
        setPropertyChanged(subscriptionDateTime, this.subscriptionDateTime);
        this.subscriptionDateTime = subscriptionDateTime;
    }

    public UUID getCyclicNotificationId() {
        return cyclicNotificationId;
    }

    public void setCyclicNotificationId(UUID cyclicNotificationId) {
        this.cyclicNotificationId = cyclicNotificationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
