//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.notificationworkflow;


import com.eworkplaceapps.platform.applicationinfo.ApplicationManager;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notification.UnPreparedNotificationQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationWorkflow {


// It define the notification workflow by implementing the diffrent notification operations.
// It perform notification operaion in following steps.
// 1) Execute the RawNotification.
// 2) Pass RawNotification into application call back method to generate the NotificationRecipientDetail list.
// 3) Resolve the duplicate receipant detail list.
// 4) Loop through NotificationRecipientDetail and create UnPreparedNotificationQueue instance and initilize all necessary properties.
//    and save UnPreparedNotificationQueue into database.


    public List<NotificationRecipientDetail> start(RawNotification rawNotification) {
        NotificationWorkflow notificationWorkflow = new NotificationWorkflow();

        // getting the notification detail array list.
        List<NotificationRecipientDetail> detail = rawNotification.getNotificationRecipientDetailList(); //notificationWorkflow.execute(&rawNotification)

        // Remove the duplicate receipants.
        detail = notificationWorkflow.resolveDuplicateiRecipientEmail(detail);

        // Prepared the UnPreparedNotificationQueue and ADD them.
        notificationWorkflow.addInUnpreparedNotification(detail, rawNotification);

        return detail;
    }

    /// Generating notification recipient list from calling application call back method.
    private List<NotificationRecipientDetail> execute(RawNotification rawNotification) {
        List<NotificationRecipientDetail> recipientDetailList = new ArrayList<>();

        // Gets all recipient data from DB table for event id.
        Map<String, Object> commandParameters = new HashMap<>();

        commandParameters.put("ResolvedNotificationInfo", rawNotification.getResolvedEventNotification());
        commandParameters.put("TenantId", rawNotification.getLoginTenantId());

        commandParameters.put("NotifierEntityType", rawNotification.getNotifierEntityType());

        commandParameters.put("NotifierEntityId", rawNotification.getNotifierEntityId());

        commandParameters.put("NotificationType", rawNotification.getNotificationType().getId());

        // Calling application callback method to generate NotificationRecipientDetail list.

        NotificationRecipientDetail detail = (NotificationRecipientDetail) ApplicationManager.executeOperation(ApplicationManager.AppCallbackCommand.RESOLVE_NOTIFICATION_RECIPIENT, commandParameters, rawNotification.getApplicationId());

        if (detail != null) {
            recipientDetailList.add(detail);
        }

        return recipientDetailList;
    }

    // It is used to resolved dulicate receipant list.
    private List<NotificationRecipientDetail> resolveDuplicateiRecipientEmail(List<NotificationRecipientDetail> recipientDetailList) {
        List<NotificationRecipientDetail> resolvedRecipientDetail = new ArrayList<>();

        Boolean exist;
        for (int i = 0; i < recipientDetailList.size(); i++) {
            exist = false;
            for (int j = resolvedRecipientDetail.size() - 1; j >= 0; j--) {

                // If receipant found then remove break from the loop.
                if (recipientDetailList.get(i).getRecipientDeliveryType() == resolvedRecipientDetail.get(j).getRecipientDeliveryType()
                        && recipientDetailList.get(i).getRecipientSubDeliveryType() == resolvedRecipientDetail.get(j).getRecipientSubDeliveryType()
                        && recipientDetailList.get(i).getRecipientEmail().equals(resolvedRecipientDetail.get(j).getRecipientEmail())) {
                    exist = true;
                    break;
                }
            }

            // If already exist then do not addd int the list.
            if (!(exist)) {
                resolvedRecipientDetail.add(recipientDetailList.get(i));
            }

        }

        return resolvedRecipientDetail;
    }

    // It is used to prepare UnPreparedNotificationQueue and add into database.
    private void addInUnpreparedNotification(List<NotificationRecipientDetail> recipientDetailList, RawNotification rawNotification) {

        //UnPreparedNotificationQueueDataService unqDS = new UnPreparedNotificationQueueDataService();

        DatabaseOps.defaultDatabase().beginDeferredTransaction();

        for (int i = 0; i < recipientDetailList.size(); i++) {
            UnPreparedNotificationQueue unq = new UnPreparedNotificationQueue();
            NotificationRecipientDetail recipient = recipientDetailList.get(i);

            unq.setNotificationType(rawNotification.getNotificationType().getId());
            unq.setNotificationProcessStatus(NotificationEnum.NotificationProcessStatusEnum.PENDING.getId());
            unq.setDeliveryType(recipient.getRecipientType().getId());
            unq.setDeliverySubType(recipient.getRecipientSubDeliveryType());
            unq.setDeliveryTime(recipient.getDeliveryTime());
            unq.setRecipientType(recipient.getRecipientType().getId());
            unq.setRecipientId(recipient.getRecipientId());
            unq.setExternalRecipientEmail(recipient.getExternalRecipientEmail());
            unq.setPseudoUserCode(recipient.getPseudoUserId());

            // Need to discuss with Nitin. To genearte notification body, we need these 2 variables.
            unq.setSourceType(rawNotification.getNotificationEntityType());
            unq.setSourceId(rawNotification.getNotificationEntityId());

            unq.setSenderId(EwpSession.getSharedInstance().getUserId());
            unq.setTenantId(EwpSession.getSharedInstance().getTenantId());

            // Nitin: It is required to generate the notification body.
            unq.setApplicationId(rawNotification.getApplicationId());

            //TODO No class XMLHelper
             /*   unq.otherInformation = XMLHelper.toXmlWriter(rawNotification.otherData, rootElementName:
                "DataList")
                let response = unqDS.add(unq);*/

            // If error occured then log the error and rollback the transection.

        }
        DatabaseOps.defaultDatabase().commitTransaction();

    }

}

