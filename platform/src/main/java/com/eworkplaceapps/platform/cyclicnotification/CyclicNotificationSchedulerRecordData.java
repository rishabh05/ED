//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CyclicNotificationSchedulerRecordData extends BaseData<CyclicNotificationSchedulerRecord> {


    /**
     * GET CYCLIC_NOTIFICATION entity
     *
     * @return CYCLIC_NOTIFICATION_SCHEDULER_RECORD
     */
    @Override
    public CyclicNotificationSchedulerRecord createEntity() {
        return CyclicNotificationSchedulerRecord.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object type
     * @return CYCLIC_NOTIFICATION_SCHEDULER_RECORD
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public CyclicNotificationSchedulerRecord getEntity(Object id) throws EwpException {
        String sql = "SELECT * From CyclicNotificationSchedulerRecord where CyclicNotificationSchedulerRecordId= '" + ((UUID) id).toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the CYCLIC_NOTIFICATION_SCHEDULER_RECORD Entity from database.
     * Return Collection of CYCLIC_NOTIFICATION Entity.
     *
     * @return List<CYCLIC_NOTIFICATION_SCHEDULER_RECORD>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<CyclicNotificationSchedulerRecord> getList() throws EwpException {
        String sql = "SELECT * From CyclicNotificationSchedulerRecord";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET CYCLIC_NOTIFICATION_SCHEDULER_RECORD Entity that matches the id and return result as a ResultSet.
     *
     * @param id Object type
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From CyclicNotificationSchedulerRecord where CyclicNotificationSchedulerRecordId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all CYCLIC_NOTIFICATION_SCHEDULER_RECORD Entity record from database and return result as a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From CyclicNotificationSchedulerRecord";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE CYCLIC_NOTIFICATION_SCHEDULER_RECORD entity.
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("CyclicNotificationSchedulerRecord", "CyclicNotificationSchedulerRecordId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(CyclicNotificationSchedulerRecord entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("CyclicNotificationSchedulerRecordId", entity.getEntityId().toString());
        values.put("EntityId", entity.getMainEntityId().toString());
        values.put("EntityType", entity.getEntityType());
        values.put("StartDate", entity.getStartDate().toString());
        values.put("EndDate", entity.getEndDate().toString());
        values.put("LastReminderDate", entity.getLastReminderDate().toString());
        values.put("NextReminderDate", entity.getNextReminderDate().toString());
        values.put("EmbeddedAttachment", "");
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", entity.getCreatedAt().toString());
        values.put("ModifiedDate", entity.getUpdatedAt().toString());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("CyclicNotificationSchedulerRecord", values);
    }

    @Override
    public void update(CyclicNotificationSchedulerRecord entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("EntityId", entity.getMainEntityId().toString());
        values.put("EntityType", entity.getEntityType());
        values.put("StartDate", entity.getStartDate().toString());
        values.put("EndDate", entity.getEndDate().toString());
        values.put("LastReminderDate", entity.getLastReminderDate().toString());
        values.put("NextReminderDate", entity.getNextReminderDate().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", entity.getUpdatedAt().toString());
        values.put("TenantId", entity.getTenantId().toString());
        super.update("CyclicNotificationSchedulerRecord", values, "CyclicNotificationSchedulerRecordId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for CYCLIC_NOTIFICATION_SCHEDULER_RECORD.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM CyclicNotificationSchedulerRecord ";
    }

    public Cursor getScheduledRecordsWithCyclicNotificationAsResultSet(Date tickTime) throws EwpException {
        String date = Utils.getUTCDateTimeAsString(tickTime);
        String sql = " SELECT * FROM PFCyclicNotificationSchedulerRecord AS cns ";
        sql += " INNER JOIN PFCyclicNotification AS cn ON cns.CyclicNotificationId=cn.CyclicNotificationId ";
        sql += " INNER JOIN PFCyclicNotificationLinking AS cnl ON cns.CyclicNotificationId=cnl.CyclicNotificationId ";
        sql += " Where datetime(cns.NextExecutionDate) <= datetime('" + date + "') ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * @param entity    CYCLIC_NOTIFICATION_SCHEDULER_RECORD entity
     * @param resultSet Cursor obj
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(CyclicNotificationSchedulerRecord entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("CyclicNotificationSchedulerRecordId"));

        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String entityType = resultSet.getString(resultSet.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setEntityType(Integer.parseInt(entityType));
        }

        String entityId = resultSet.getString(resultSet.getColumnIndex("EntityId"));
        if (entityId != null && !"".equals(entityId)) {
            entity.setMainEntityId(UUID.fromString(entityId));
        }

        String startDate = resultSet.getString(resultSet.getColumnIndex("StartDate"));
        if (startDate != null && !"".equals(startDate)) {
            entity.setStartDate(Utils.dateFromString(startDate,true,true));
        }

        String endDate = resultSet.getString(resultSet.getColumnIndex("EndDate"));
        if (endDate != null && !"".equals(endDate)) {
            entity.setEndDate(Utils.dateFromString(endDate,true,true));
        }

        String lastReminderDate = resultSet.getString(resultSet.getColumnIndex("LastReminderDate"));
        if (lastReminderDate != null && !"".equals(lastReminderDate)) {
            entity.setLastReminderDate(Utils.dateFromString(lastReminderDate,true,true));
        }

        String nextReminderDate = resultSet.getString(resultSet.getColumnIndex("NextReminderDate"));
        if (nextReminderDate != null && !"".equals(nextReminderDate)) {
            entity.setNextReminderDate(Utils.dateFromString(nextReminderDate,true,true));
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
            entity.setCreatedAt(Utils.dateFromString(createdAt,true,true));
        }
        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(updatedBy);
        }
        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt,true,true));
        }
        entity.setDirty(false);
    }
}
