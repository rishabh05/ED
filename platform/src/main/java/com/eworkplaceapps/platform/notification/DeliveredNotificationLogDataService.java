//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.List;
import java.util.UUID;


public class DeliveredNotificationLogDataService extends BaseDataService<DeliveredNotificationLog> {

    DeliveredNotificationLogData dataDelegate = new DeliveredNotificationLogData();

    /**
     * Initializes a new instance of the EventNotificationDataService class.
     */

    public DeliveredNotificationLogDataService() {
        super("DELIVERED_NOTIFICATION_LOG");
    }

    @Override
    public DeliveredNotificationLogData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    /**
     * Searches DELIVERED_NOTIFICATION_LOG records that matches sender user id
     * and returns DELIVERED_NOTIFICATION_LOG list as ResultSet.
     *
     * @param senderId UUID
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public Cursor getDeliveredNotificationListBySenderIdAsResultSet(UUID senderId) throws EwpException {

        return dataDelegate.getDeliveredNotificationListBySenderIdAsResultSet(senderId);
    }

    /**
     * Searches for all DELIVERED_NOTIFICATION_LOG that matches the tenant id
     * and returns the DELIVERED_NOTIFICATION_LOG list as entity.
     *
     * @param tenantId UUID
     * @return List<DELIVERED_NOTIFICATION_LOG>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<DeliveredNotificationLog> getDeliveredNotificationListByTenantIdAsList(UUID tenantId) throws EwpException {

        return dataDelegate.getDeliveredNotificationListByTenantIdAsList(tenantId);
    }
}
