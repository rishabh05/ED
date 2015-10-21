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


public class UnPreparedNotificationQueueData extends BaseData<UnPreparedNotificationQueue> {

    /**
     * GET UnPreparedNotificationQueue entity
     *
     * @return UnPreparedNotificationQueue
     */
    public UnPreparedNotificationQueue createEntity() {
        return UnPreparedNotificationQueue.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return UnPreparedNotificationQueue
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public UnPreparedNotificationQueue getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFUnpreparedNotificationQueue where UnpreparedNotificationQueueId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }


    /**
     * GET all the UnPreparedNotificationQueue Entity from database.
     * Return Collection of UnPreparedNotificationQueue Entity.
     *
     * @return List<UnPreparedNotificationQueue>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<UnPreparedNotificationQueue> getList() throws EwpException {
        String sql = "SELECT * From PFUnpreparedNotificationQueue";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET UnPreparedNotificationQueue Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFUnpreparedNotificationQueue where UnpreparedNotificationQueueId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all UnPreparedNotificationQueue Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFUnpreparedNotificationQueue";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFUnpreparedNotificationQueue", "UnpreparedNotificationQueueId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(UnPreparedNotificationQueue entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("UnpreparedNotificationQueueId", entity.getEntityId().toString());
        values.put("RecipientType", entity.getRecipientType());
        values.put("RecipientId", entity.getRecipientId().toString());
        values.put("NotificationType", entity.getNotificationType());
        values.put("ExternalRecipientEmail", entity.getExternalRecipientEmail());
        values.put("PseudoUserCode", entity.getPseudoUserCode());
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliverySubType", entity.getDeliverySubType());
        values.put("OtherInformation", entity.getOtherInformation());
        values.put("ProcessStatus", entity.getNotificationProcessStatus());
        values.put("DeliveryTime", Utils.getUTCDateTimeAsString(entity.getDeliveryTime()));
        values.put("SenderId", entity.getSenderId().toString());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("SourceType", entity.getSourceType());
        values.put("SourceId", entity.getSourceId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFUnpreparedNotificationQueue", values);
    }

    @Override
    public void update(UnPreparedNotificationQueue entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("RecipientType", entity.getRecipientType());
        values.put("RecipientId", entity.getRecipientId().toString());
        values.put("ExternalRecipientEmail", entity.getExternalRecipientEmail());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        values.put("PseudoUserCode", entity.getPseudoUserCode());
        values.put("ProcessStatus", entity.getNotificationProcessStatus());
        values.put("OtherInformation", entity.getOtherInformation());
        values.put("DeliveryTime", Utils.getUTCDateTimeAsString(entity.getDeliveryTime()));
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliverySubType", entity.getDeliverySubType());
        values.put("SenderId", entity.getSenderId().toString());
        super.update("PFUnpreparedNotificationQueue", values, "UnpreparedNotificationQueueId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for UnPreparedNotificationQueue.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFUnpreparedNotificationQueue ";
    }

    /**
     * It is used to get the current undelivered notification list from delivery and notification type.
     *
     * @param notificationType        NotificationEnum.NotificationTypeEnum
     * @param notificationDeliverType NotificationEnum.NotificationDeliveryType
     * @return List<UnPreparedNotificationQueue>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<UnPreparedNotificationQueue> getNotificationFromCurrentDateTimeDeliveryTypeAndNotificationType(NotificationEnum.NotificationTypeEnum notificationType, NotificationEnum.NotificationDeliveryType notificationDeliverType) throws EwpException {
        String sql = getSQL() + " WHERE DATETIME(DeliveryTime) <= DATETIME('now') and NotificationType = '" + notificationType.getId() + "' ";
        sql += " and DeliveryType = '" + notificationDeliverType.getId() + "' and ProcessStatus = '1' ";

        return super.executeSqlAndGetEntityList(sql);
    }

    /**
     * @param entity    UnPreparedNotificationQueue
     * @param resultSet Cursor
     * @throws EwpException
     */
    public void setPropertiesFromResultSet(UnPreparedNotificationQueue entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String unpreparedNotificationQueueId = resultSet.getString(resultSet.getColumnIndex("UnpreparedNotificationQueueId"));
        if (unpreparedNotificationQueueId != null && !"".equals(unpreparedNotificationQueueId)) {
            entity.setEntityId(UUID.fromString(unpreparedNotificationQueueId));
        }

        String deliverySubType = resultSet.getString(resultSet.getColumnIndex("DeliverySubType"));
        if (deliverySubType != null && !"".equals(deliverySubType)) {
            entity.setDeliverySubType(Integer.parseInt(deliverySubType));
        }

        String deliveryType = resultSet.getString(resultSet.getColumnIndex("DeliveryType"));
        if (deliveryType != null && !"".equals(deliveryType)) {
            entity.setDeliveryType(Integer.parseInt(deliveryType));
        }

        String deliveryTime = resultSet.getString(resultSet.getColumnIndex("deliveryTime"));
        if (deliveryTime != null && !"".equals(deliveryTime)) {
            entity.setDeliveryTime(Utils.dateFromString(deliveryTime, true, true));
        }

        String recipientType = resultSet.getString(resultSet.getColumnIndex("recipientType"));
        if (recipientType != null && !"".equals(recipientType)) {
            entity.setRecipientType(Integer.parseInt(recipientType));
        }

        String recipientId = resultSet.getString(resultSet.getColumnIndex("RecipientId"));
        if (recipientId != null && !"".equals(recipientId)) {
            entity.setRecipientId(UUID.fromString(recipientId));
        }

        String externalRecipientEmail = resultSet.getString(resultSet.getColumnIndex("ExternalRecipientEmail"));
        if (externalRecipientEmail != null) {
            entity.setExternalRecipientEmail(externalRecipientEmail);
        }

        String pseudoUserCode = resultSet.getString(resultSet.getColumnIndex("PseudoUserCode"));
        if (pseudoUserCode != null && !"".endsWith(pseudoUserCode)) {
            entity.setPseudoUserCode(Integer.parseInt(pseudoUserCode));
        }

        String processed = resultSet.getString(resultSet.getColumnIndex("ProcessStatus"));
        if (processed != null && !"".equals(processed)) {
            entity.setNotificationProcessStatus(Integer.parseInt(processed));
        }

        String otherInformation = resultSet.getString(resultSet.getColumnIndex("otherInformation"));
        if (otherInformation != null) {
            entity.setOtherInformation(otherInformation);
        }

        String senderId = resultSet.getString(resultSet.getColumnIndex("SenderId"));
        if (senderId != null && !"".equals(senderId)) {
            entity.setSenderId(UUID.fromString(senderId));
        }

        String sourceId = resultSet.getString(resultSet.getColumnIndex("SourceId"));
        if (sourceId != null && !"".equals(sourceId)) {
            entity.setSourceId(UUID.fromString(sourceId));
        }

        String sourceType = resultSet.getString(resultSet.getColumnIndex("SourceType"));
        if (sourceType != null && !"".equals(sourceType)) {
            entity.setSourceType(Integer.parseInt(sourceType));
        }

        String applicationId = resultSet.getString(resultSet.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(UUID.fromString(createdBy).toString());
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }
}
