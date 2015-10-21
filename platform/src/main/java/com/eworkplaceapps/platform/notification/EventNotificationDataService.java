//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.List;
import java.util.UUID;


public class EventNotificationDataService extends BaseDataService<EventNotification> {

    EventNotificationData dataDelegate  = new EventNotificationData();

    /**
     * Initializes a new instance of the EventNotificationDataService class.
     */
    public  EventNotificationDataService() {
        super("EVENT_NOTIFICATION");
    }

    public EventNotificationData getDataClass()  {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    public List<EventNotification> getEntityListByParentEntityTypeAndIdAndEventTypeNo(int parentEntityType,UUID parentEntityId,int eventTypeNo) throws EwpException {
        return dataDelegate.getEntityListByParentEntityTypeAndIdAndEventTypeNo(parentEntityType,  parentEntityId,  eventTypeNo);
    }

    /**
     * It is used to add EVENT_NOTIFICATION as well as NOTIFICATION_RECIPIENT.
     * @param notificationDef EVENT_NOTIFICATION
     * @param recipientList List<NOTIFICATION_RECIPIENT>
     * @return UUID
     */

    public UUID addEventNotificationAndRecipientList(EventNotification notificationDef,List<NotificationRecipient> recipientList) throws EwpException {

        DatabaseOps.defaultDatabase().beginTransaction();

        // Adds notification definition record.
        UUID eventNotificationId = (UUID)super.add(notificationDef);

       // NotificationRecipientDataService notificationRecipientDS = new NotificationRecipientDataService();

        // ADD all recipient record.
        for (int i = 0; i < recipientList.size(); i++) {
            // Ask Nitin if any change required.
            recipientList.get(i).setSourceEntityType(PFEntityType.EVENT_NOTIFICATION.getValue());
            recipientList.get(i).setSourceEntityId(eventNotificationId);
           // recipientList.get(i).lastOperationType = DatabaseOperationType.ADD;
           // var resultNotificationTuple = notificationRecipientDS.add(recipientList.get(i));

        }

        // Commit transection.
        DatabaseOps.defaultDatabase().commitTransaction();

        return eventNotificationId;
    }

    public EventNotification getEventNotificationByOwnerAndEntityTypeAndEntityIdAndTenantIdAndAppId(UUID ownerId,int entityType,UUID entityId,UUID tenantId,String appId) {

        return getEventNotificationByOwnerAndEntityTypeAndEntityIdAndTenantIdAndAppId(ownerId,  entityType,  entityId,  tenantId,  appId);
    }

}
