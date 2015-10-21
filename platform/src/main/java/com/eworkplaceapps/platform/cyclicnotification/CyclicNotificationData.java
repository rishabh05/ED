//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;


public class CyclicNotificationData extends BaseData<CyclicNotification> {


    /**
     * GET CYCLIC_NOTIFICATION entity
     *
     * @return BaseEntity
     */

    @Override
    public BaseEntity createEntity() {
        return CyclicNotification.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id of type object
     * @return CYCLIC_NOTIFICATION
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public CyclicNotification getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFCyclicNotification where CyclicNotificationId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the CYCLIC_NOTIFICATION Entity from database.
     * Return Collection of CYCLIC_NOTIFICATION Entity.
     *
     * @return List<CYCLIC_NOTIFICATION>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<CyclicNotification> getList() throws EwpException {
        String sql = "SELECT * From PFCyclicNotification";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET CYCLIC_NOTIFICATION Entity that matches the id and return result as a ResultSet.
     *
     * @param id of type object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFCyclicNotification where CyclicNotificationId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all CYCLIC_NOTIFICATION Entity record from database and return result as a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFCyclicNotification";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE CYCLIC_NOTIFICATION entity.
     *
     * @param id of type object
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFCyclicNotification", "CyclicNotificationId=?", new String[]{id.toString()});
    }

    /**
     * Generate sql string with minimum required fields for CYCLIC_NOTIFICATION.
     *
     * @return STRING
     */

    private String getSQL() {
        return " SELECT * FROM PFCyclicNotification ";
    }

    public CyclicNotification getCyclicNotificationByUserIdAndEntityTypeAndEntityId(UUID userId, UUID entityId, UUID tenantId, int entityType) throws EwpException {
        String sql = " SELECT cn.CyclicNotificationId,cn.CyclicType,cn.CyclicSubType,cn.CyclicPattern,cn.ACTIVE,";
        sql += " cn.RemindType,cn.RemindStartTimeUnit,cn.RemindStartTimeDelta,";
        sql += " cn.FrequencyUnit,cn.FrequencyInterval,cn.RepeatStartDate,cn.RepeatEndDate,";
        sql += " cn.ActionId, cn.CreatedBy,cn.CreatedDate,cn.ModifiedBy,cn.ModifiedDate,";
        sql += " cn.TenantId,cn.ApplicationId, cn.Version,cn.Days  ";
        sql += " FROM PFCyclicNotification cn ";
        sql += " INNER JOIN PFCyclicNotificationLinking nl ON ";
        sql += " cn.CyclicNotificationId = nl.CyclicNotificationId ";
        sql += " WHERE ";
        sql += " nl.SourceEntityId = '" + entityId.toString() + "' AND ";
        sql += " nl.SourceEntityType = '" + entityType + "' AND ";
        sql += " cn.TenantId = '" + tenantId.toString() + "' AND cn.CreatedBy= '" + userId + "' ";

        return executeSqlAndGetEntity(sql);
    }

    @Override
    public long insertEntity(CyclicNotification entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("CyclicNotificationId", entity.getEntityId().toString());
        values.put("ActionId", entity.getActionId().toString());
        values.put("CyclicType", entity.getCyclicType());
        values.put("CyclicPattern", entity.getCyclicPattern());
        values.put("frequencyUnit", entity.getFrequencyUnit());
        values.put("frequencyInterval", entity.getFrequencyInterval());
        values.put("remindStartTimeUnit", entity.getRemindStartTimeUnit());
        values.put("remindStartTimeDelta", entity.getRemindStartTimeDelta());
        values.put("RemindType", entity.getRemindType());
        values.put("RepeatStartDate", entity.getRepeatStartDate().toString());
        values.put("RepeatEndDate", entity.getRepeatEndDate().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("AppCyclicNotificationTypeNo", entity.getAppCyclicNotificationTypeNo());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFCyclicNotification", values);
    }

    @Override
    public void update(CyclicNotification entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("ActionId", entity.getActionId().toString());
        values.put("CyclicType", entity.getCyclicType());
        values.put("CyclicPattern", entity.getCyclicPattern());
        values.put("frequencyUnit", entity.getFrequencyUnit());
        values.put("frequencyInterval", entity.getFrequencyInterval());
        values.put("RepeatStartDate", entity.getRepeatStartDate().toString());
        values.put("RepeatEndDate", entity.getRepeatEndDate().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFCyclicNotification", values, "CyclicNotificationId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * @param entity,resultSet CyclicNotificationEntity and cursor resultSet
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(CyclicNotification entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("EventNotificationId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String cyclicType = resultSet.getString(resultSet.getColumnIndex("CyclicType"));
        if (cyclicType != null && !"".equals(cyclicType)) {
            entity.setCyclicType(Integer.parseInt(cyclicType));
        }
        String cyclicPattern = resultSet.getString(resultSet.getColumnIndex("cyclicPattern"));
        if (cyclicPattern != null) {
            entity.setCyclicPattern(Integer.parseInt(cyclicPattern));
        }

        Boolean active = resultSet.getInt(resultSet.getColumnIndex("ACTIVE")) == 1;
        if (active != null) {
            entity.setActive(active);
        }

        String remindType = resultSet.getString(resultSet.getColumnIndex("RemindType"));
        if (remindType != null && !"".equals(remindType)) {
            entity.setRemindType(Integer.parseInt(remindType));
        }

        String remindStartTimeUnit = resultSet.getString(resultSet.getColumnIndex("RemindStartTimeUnit"));
        if (remindStartTimeUnit != null && !"".equals(remindStartTimeUnit)) {
            entity.setRemindStartTimeUnit(Integer.parseInt(remindStartTimeUnit));
        }

        String remindStartTimeDelta = resultSet.getString(resultSet.getColumnIndex("RemindStartTimeDelta"));
        if (remindStartTimeDelta != null && !"".equals(remindStartTimeDelta)) {
            entity.setRemindStartTimeDelta(Integer.parseInt(remindStartTimeDelta));
        }

        String frequencyUnit = resultSet.getString(resultSet.getColumnIndex("FrequencyUnit"));
        if (frequencyUnit != null && !"".equals(frequencyUnit)) {
            entity.setFrequencyUnit(Integer.parseInt(frequencyUnit));
        }

        String frequencyInterval = resultSet.getString(resultSet.getColumnIndex("FrequencyInterval"));
        if (frequencyInterval != null && !"".equals(frequencyInterval)) {
            entity.setFrequencyInterval(Integer.parseInt(frequencyInterval));
        }

        String repeatStartDate = resultSet.getString(resultSet.getColumnIndex("RepeatStartDate"));
        if (repeatStartDate != null && !"".equals(repeatStartDate)) {
            entity.setRepeatStartDate(Utils.dateFromString(repeatStartDate, true, true));
        }

        String repeatEndDate = resultSet.getString(resultSet.getColumnIndex("RepeatEndDate"));
        if (repeatEndDate != null && !"".equals(repeatEndDate)) {
            entity.setRepeatEndDate(Utils.dateFromString(repeatEndDate, true, true));
        }

        String actionId = resultSet.getString(resultSet.getColumnIndex("ActionId"));
        if (actionId != null && !"".equals(actionId)) {
            entity.setActionId(Integer.parseInt(actionId));
        }

        String applicationId = resultSet.getString(resultSet.getColumnIndex("ApplicationId"));
        if (applicationId != null && !"".equals(applicationId)) {
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
