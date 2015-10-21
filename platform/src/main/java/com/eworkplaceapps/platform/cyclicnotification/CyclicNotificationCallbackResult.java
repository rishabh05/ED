//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;

import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class CyclicNotificationCallbackResult {

    public  CyclicNotificationCallbackResult() {
        otherInformationList = new HashMap<>();
        recipientList = new ArrayList<>();
    }

    private List<NotificationRecipientDetail> recipientList;

    public List<NotificationRecipientDetail> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<NotificationRecipientDetail> recipientList) {
        this.recipientList = recipientList;
    }

    public Map<String, String> getOtherInformationList() {
        return otherInformationList;
    }

    public void setOtherInformationList(Map<String, String> otherInformationList) {
        this.otherInformationList = otherInformationList;
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

    private Map<String, String> otherInformationList;

    private String applicationId = "";

    private UUID tenantId = Utils.emptyUUID();
}
