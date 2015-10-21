//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

public class DeliveredNotificationLogData extends BaseData<DeliveredNotificationLog> {
    /**
     * GET DELIVERED_NOTIFICATION_LOG entity
     *
     * @return DELIVERED_NOTIFICATION_LOG
     */
    @Override
    public DeliveredNotificationLog createEntity() {
        return DeliveredNotificationLog.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object type
     * @return DELIVERED_NOTIFICATION_LOG
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public DeliveredNotificationLog getEntity(Object id) throws EwpException {
        String sql = "SELECT * From  PFDeliveredNotificationLog where  DeliveredNotificationId= '" + ((UUID) id).toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the DELIVERED_NOTIFICATION_LOG Entity from database.
     * Return Collection of DELIVERED_NOTIFICATION_LOG Entity.
     *
     * @return List<DELIVERED_NOTIFICATION_LOG>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<DeliveredNotificationLog> getList() throws EwpException {
        String sql = "SELECT * From  PFDeliveredNotificationLog";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET DELIVERED_NOTIFICATION_LOG Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object type
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From  PFDeliveredNotificationLog where  DeliveredNotificationId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all DELIVERED_NOTIFICATION_LOG Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From  PFDeliveredNotificationLog";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFDeliveredNotificationLog", "DeliveredNotificationId=?", new String[]{id.toString()});
    }

    /**
     * Generate sql string with minimum required fields for DELIVERED_NOTIFICATION_LOG.
     *
     * @return STRING
     */

    private String getSQL() {
        return " SELECT * FROM  PFDeliveredNotificationLog ";
    }

    /**
     * Searches DELIVERED_NOTIFICATION_LOG records that matches sender user id
     * and returns DELIVERED_NOTIFICATION_LOG list as! ResultSet.
     *
     * @param senderId UUID
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getDeliveredNotificationListBySenderIdAsResultSet(UUID senderId) throws EwpException {
        String sql = getSQL() + " WHERE SenderId='" + senderId.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Searches for all DELIVERED_NOTIFICATION_LOG that matches the tenant id
     * and returns the DELIVERED_NOTIFICATION_LOG list as! entity.
     *
     * @param tenantId UUID
     * @return List<DELIVERED_NOTIFICATION_LOG>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public List<DeliveredNotificationLog> getDeliveredNotificationListByTenantIdAsList(UUID tenantId) throws EwpException {
        String sql = getSQL() + " WHERE n.TenantId='" + tenantId.toString() + "' ";
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public long insertEntity(DeliveredNotificationLog entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("DeliveredNotificationId", entity.getEntityId().toString());
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliveryDestinations", entity.getDeliveryDestinations());
        values.put("Message1", entity.getMessage2());
        values.put("Message2", entity.getMessage2());
        values.put("DeliveryTime", Utils.getUTCDateTimeAsString(entity.getDeliveryTime()));
        values.put("CreatedBy", entity.getCreatedAt().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("SenderId", entity.getSenderId().toString());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFDeliveredNotificationLog", values);
    }

    @Override
    public void update(DeliveredNotificationLog entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliveryDestinations", entity.getDeliveryDestinations());
        values.put("Message1", entity.getMessage2());
        values.put("Message2", entity.getMessage2());
        values.put("DeliveryTime", Utils.getUTCDateTimeAsString(entity.getDeliveryTime()));
        values.put("CreatedBy", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFDeliveredNotificationLog", values, "DeliveredNotificationId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * @param entity    DELIVERED_NOTIFICATION_LOG
     * @param resultSet Cursor
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(DeliveredNotificationLog entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("DeliveredNotificationId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String senderId = resultSet.getString(resultSet.getColumnIndex("SenderId"));
        if (senderId != null) {
            entity.setSenderId(senderId);
        }

        String entityType = resultSet.getString(resultSet.getColumnIndex("DeliveryType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setDeliveryType(Integer.parseInt(entityType));
        }

        String deliveryDestinations = resultSet.getString(resultSet.getColumnIndex("DeliveryDestinations"));
        if (deliveryDestinations != null) {
            entity.setDeliveryDestinations(deliveryDestinations);
        }

        String message1 = resultSet.getString(resultSet.getColumnIndex("Message1"));
        if (message1 != null) {
            entity.setMessage1(message1);
        }

        String message2 = resultSet.getString(resultSet.getColumnIndex("Message2"));
        if (message2 != null) {
            entity.setMessage2(message2);
        }

        String deliveryTime = resultSet.getString(resultSet.getColumnIndex("DeliveryTime"));
        if (deliveryTime != null && !"".equals(deliveryTime)) {
            entity.setDeliveryTime(Utils.dateFromString(deliveryTime, true, true));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(createdBy);
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }
}
