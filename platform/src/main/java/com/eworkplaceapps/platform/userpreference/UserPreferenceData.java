//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.userpreference;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * UserPreferenceData
 */
public class UserPreferenceData extends BaseData<UserPreference> {

    /**
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFUserPreference ";
    }

    /**
     * GET USER_PREFERENCE entity
     *
     * @return USER_PREFERENCE
     */
    @Override
    public UserPreference createEntity() {
        return new UserPreference();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return USER_PREFERENCE
     * @throws EwpException
     */
    @Override
    public UserPreference getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where UserPreferenceId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the USER_PREFERENCE Entity from database.
     * Return Collection of USER_PREFERENCE Entity.
     *
     * @return List<USER_PREFERENCE>
     * @throws Exception
     */
    @Override
    public List<UserPreference> getList() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET USER_PREFERENCE Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return List<USER_PREFERENCE>
     * @throws Exception
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL() + " where UserPreferenceId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void update(UserPreference entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("PreferenceName1", entity.getPreferenceName1());
        values.put("PreferenceName2", entity.getPreferenceName2());
        values.put("ModifiedBy", entity.getUpdatedBy());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("DataType", entity.getDataType());
        values.put("DataValue", entity.getDataValue());
        super.update("PFUserPreference", values, "UserPreferenceId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(UserPreference entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("UserPreferenceId", entity.getEntityId().toString());
        values.put("PreferenceName1", entity.getPreferenceName1());
        values.put("PreferenceName2", entity.getPreferenceName2());
        values.put("UserId", entity.getUserId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("DataType", entity.getDataType());
        values.put("DataValue", entity.getDataValue());
        return super.insert("PFUserPreference", values);
    }

    /**
     * GET all USER_PREFERENCE Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws Exception
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE USER_PREFERENCE entity.
     *
     * @param entity USER_PREFERENCE
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void delete(UserPreference entity) throws EwpException {
        super.deleteRows("PFUserPreference", "UserPreferenceId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * @param userId        UUID
     * @param applicationId STRING
     * @param prefName1     STRING
     * @param prefName2     STRING
     * @return USER_PREFERENCE
     * @throws EwpException
     */
    public UserPreference getUserPreference(UUID userId, String applicationId, String prefName1, String prefName2) throws EwpException {
        String sql = getSQL() + " WHERE UserId='" + userId + "' AND ApplicationId='" + applicationId + "' AND PreferenceName1= '" + prefName1 + "'";
        if (prefName2 != null && !"".equals(prefName2)) {
            sql += " AND PreferenceName2= '" + prefName2 + "' ";
        }
        return executeSqlAndGetEntity(sql);
    }

    /**
     * This method is used to set property value from  database ResultSet.
     *
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void setPropertiesFromResultSet(UserPreference entity, Cursor cursor) throws EwpException {
        // Setting the property values from database result set
        String userPreferenceId = cursor.getString(cursor.getColumnIndex("UserPreferenceId"));
        if (userPreferenceId != null && !"".equals(userPreferenceId)) {
            entity.setEntityId(UUID.fromString(userPreferenceId));
        }

        String tenantId = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (tenantId != null && !"".equals(tenantId)) {
            entity.setTenantId(UUID.fromString(tenantId));
        }

        String name = cursor.getString(cursor.getColumnIndex("PreferenceName1"));
        if (name != null) {
            entity.setPreferenceName1(name);
        }

        String name2 = cursor.getString(cursor.getColumnIndex("PreferenceName2"));
        if (name2 != null) {
            entity.setPreferenceName2(name2);
        }

        String userId = cursor.getString(cursor.getColumnIndex("UserId"));
        if (userId != null && !"".equals(userId)) {
            entity.setUserId(UUID.fromString(userId));
        }

        String applicationId = cursor.getString(cursor.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
        }

        String dataType = cursor.getString(cursor.getColumnIndex("DataType"));
        if (dataType != null && !"".equals(dataType)) {
            entity.setDataType(Integer.parseInt(dataType));
        }

        String dataValue = cursor.getString(cursor.getColumnIndex("DataValue"));
        if (dataValue != null) {
            entity.setDataValue(dataValue);
        }

        Date createdAt = Utils.dateFromString(cursor.getString(cursor.getColumnIndex("CreatedDate")), true, true);
        if (createdAt != null) {
            entity.setCreatedAt(createdAt);
        }

        String createdBy = cursor.getString(cursor.getColumnIndex("CreatedBy"));
        if (createdBy != null) {
            entity.setCreatedBy(createdBy);
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            entity.setUpdatedBy(updatedBy);
        }

        Date updatedAt = Utils.dateFromString(cursor.getString(cursor.getColumnIndex("ModifiedDate")), true, true);
        if (updatedAt != null) {
            entity.setUpdatedAt(updatedAt);
        }
        entity.setDirty(false);
    }
}
