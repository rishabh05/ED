//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

public class NotificationRecipientData extends BaseData<NotificationRecipient> {

    /**
     * GET NOTIFICATION_RECIPIENT entity
     *
     * @return BaseEntity
     */
    public BaseEntity createEntity() {
        return NotificationRecipient.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return NOTIFICATION_RECIPIENT
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public NotificationRecipient getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationRecipient where NotificationRecipientId= '" + id.toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the NOTIFICATION_RECIPIENT Entity from database.
     * Return Collection of NOTIFICATION_RECIPIENT Entity.
     *
     * @return List<NOTIFICATION_RECIPIENT>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<NotificationRecipient> getList() throws EwpException {
        String sql = "SELECT * From PFNotificationRecipient";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET NOTIFICATION_RECIPIENT Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationRecipient where NotificationRecipientId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all NOTIFICATION_RECIPIENT Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFNotificationRecipient";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFNotificationRecipient", "NotificationRecipientId=?", new String[]{id.toString()});
    }


    @Override
    public long insertEntity(NotificationRecipient entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("NotificationRecipientId", entity.getEntityId().toString());
        values.put("SourceEntityType", entity.getSourceEntityType());
        values.put("SourceEntityId", entity.getSourceEntityId().toString());
        values.put("RecipientType", entity.getRecipientType());
        values.put("RecipientId", entity.getRecipientId().toString());
        values.put("ExternalRecipientEmail", entity.getExternalRecipientEmail());
        values.put("PseudoUserCode", entity.getPseudoUserCode());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFNotificationRecipient", values);
    }

    @Override
    public void update(NotificationRecipient entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("SourceEntityType", entity.getSourceEntityType());
        values.put("SourceEntityId", entity.getSourceEntityId().toString());
        values.put("RecipientType", entity.getRecipientType());
        values.put("RecipientId", entity.getRecipientId().toString());
        values.put("ExternalRecipientEmail", entity.getExternalRecipientEmail());
        values.put("PseudoUserCode", entity.getPseudoUserCode());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFNotificationRecipient", values, "NotificationRecipientId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for NOTIFICATION_RECIPIENT.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFNotificationRecipient ";
    }

    public List<NotificationRecipient> getNotificationRecipientDetailListByEntityTypeAndId(int notificationType, UUID notificationId) throws EwpException {
        String sql = getSQL() + " WHERE SourceEntityType='" + notificationType + "' AND SourceEntityId= '" + notificationId.toString() + "' ";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param entity    NOTIFICATION_RECIPIENT
     * @param resultSet Cursor
     * @throws EwpException
     */
    public void setPropertiesFromFMResultSet(NotificationRecipient entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String notificationRecipientId = resultSet.getString(resultSet.getColumnIndex("NotificationRecipientId"));
        if (notificationRecipientId != null && !"".equals(notificationRecipientId)) {
            entity.setEntityId(UUID.fromString(notificationRecipientId));

            String sourceNotificationType = resultSet.getString(resultSet.getColumnIndex("SourceEntityType"));
            if (sourceNotificationType != null && !"".equals(sourceNotificationType)) {
                entity.setSourceEntityType(Integer.parseInt(sourceNotificationType));
            }

            String sourceNotificationId = resultSet.getString(resultSet.getColumnIndex("SourceEntityId"));
            if (sourceNotificationId != null && !"".equals(sourceNotificationId)) {
                entity.setSourceEntityId(UUID.fromString(sourceNotificationId));
            }

            String recipientType = resultSet.getString(resultSet.getColumnIndex("RecipientType"));
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
            if (pseudoUserCode != null && !"".equals(pseudoUserCode)) {
                entity.setPseudoUserCode(Integer.parseInt(pseudoUserCode));
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
}
