//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.security;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class RolePermissionData extends BaseData<RolePermission> {

    private String getSQL() {
        String sql = " SELECT Rp.*, Rl.TenantId As TenantId From  PFRolePermission Rp INNER JOIN PFRoleLinking AS Rl ON Rp.RoleId=Rl.RoleId";
        return sql;
    }

    /**
     * GET the  ROLE_PERMISSION entity
     *
     * @return
     */
    public RolePermission createEntity() {
        return RolePermission.createEntity();
    }

    /**
     * Searches for ROLE_PERMISSION Entity that matches the id value.
     * Returns ROLE_PERMISSION an Entity object.
     *
     * @param id
     * @return
     */
    public RolePermission getEntity(Object id) throws EwpException {
        // Building select statement
        String sql = getSQL() + " where RolePermissionId = '" + ((UUID) id) + "'";
        // Execute select statement and return entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Fetch all the ROLE_PERMISSION Entity record from database.
     * Returns Collection of ROLE_PERMISSION Entity.
     *
     * @return
     */
    public List<RolePermission> getList() throws EwpException {
        String mySql = getSQL();
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * GET ROLE_PERMISSION Entity list for given tenantId.
     * Returns Collection of ROLE Entity list.
     *
     * @param tenantId
     * @return
     */
    public List<RolePermission> getRolePermissionListFromTenantId(UUID tenantId) throws EwpException {
        String mySql = getSQL() + " WHERE Rl.TenantId='" + tenantId + "'";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * GET ROLE_PERMISSION entity
     * Returns ROLE_PERMISSION as ResultSet for given tenantId
     *
     * @return
     */
    public Cursor getRolePermissionListFromTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String mySql = getSQL() + " WHERE Rl.TenantId='" + tenantId + "'";
        return executeSqlAndGetResultSet(mySql);
    }

    /**
     * GET ROLE_PERMISSION as ResultSet for given RolePermissionId.
     * Returns ROLE_PERMISSION as ResultSet.
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL();
        sql += " where RolePermissionId = '" + ((UUID) id) + "'";
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * Fetch all the ROLE_PERMISSION Entity record from database and return as ResultSet.
     *
     * @return
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = getSQL();
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE ROLE_PERMISSION Entity
     *
     * @param entity
     */
    @Override
    public void delete(RolePermission entity) throws EwpException {
        super.deleteRows("PFRolePermission", "RolePermissionId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public void update(RolePermission entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("RoleId", entity.getRoleId().toString());
        values.put("AddOp", entity.getAddOp());
        values.put("UpdateOp", entity.getUpdateOp());
        values.put("DeleteOp", entity.getDeleteOp());
        values.put("ViewOp", entity.getViewOp());
        values.put("ExtOp1", entity.getExtOp1());
        values.put("ExtOp2", entity.getExtOp2());
        values.put("ExtOp3", entity.getExtOp3());
        values.put("ExtOp4", entity.getExtOp4());
        values.put("ExtOp5", entity.getExtOp5());
        values.put("ExtOp6", entity.getExtOp6());
        values.put("ExtOp7", entity.getExtOp7());
        values.put("ExtOp8", entity.getExtOp8());
        values.put("ExtOp9", entity.getExtOp9());
        values.put("ExtOp10", entity.getExtOp10());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFRolePermission", values, "RolePermissionId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(RolePermission entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("RolePermissionId", entity.getEntityId().toString());
        values.put("RoleId", entity.getRoleId().toString());
        values.put("AddOp", entity.getAddOp());
        values.put("UpdateOp", entity.getUpdateOp());
        values.put("DeleteOp", entity.getDeleteOp());
        values.put("ViewOp", entity.getViewOp());
        values.put("ExtOp1", entity.getExtOp1());
        values.put("ExtOp2", entity.getExtOp2());
        values.put("ExtOp3", entity.getExtOp3());
        values.put("ExtOp4", entity.getExtOp4());
        values.put("ExtOp5", entity.getExtOp5());
        values.put("ExtOp6", entity.getExtOp6());
        values.put("ExtOp7", entity.getExtOp7());
        values.put("ExtOp8", entity.getExtOp8());
        values.put("ExtOp9", entity.getExtOp9());
        values.put("ExtOp10", entity.getExtOp10());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("ParentEntityType", entity.getParentEntityType());
        values.put("EntityType", entity.getRefEntityType());
        return super.insert("PFRolePermission", values);
    }

    /**
     * The method is used to get ROLE_PERMISSION  by user, tenant, application, source-id and entity type.
     *
     * @param userId
     * @param tenantId
     * @param applicationId
     * @param entityType
     * @param entityId
     * @return
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public RolePermission getRolePermissionByUserAndTenantAndApplicationAndSourceIdAndEntityType(UUID userId, UUID tenantId, String applicationId, int entityType, UUID entityId) throws EwpException {
        String sql = "SELECT * from PFRolePermission AS rp ";
        sql += " INNER JOIN PFRoleLinking AS rl ON LOWER(rp.RoleId) = LOWER(rl.RoleId) ";
        sql += " WHERE LOWER(rl.UserId) =LOWER('" + userId + "') AND LOWER(rl.TenantId)=LOWER('" + tenantId + "') and rp.EntityType='" + entityType + "'";

        if (entityId != null) {
            sql += " LOWER(rl.EntityId)=LOWER('" + entityId + "')";
        }
        RolePermission response = executeSqlAndGetEntity(sql);
        if (response != null) {
            return response;
        }
        return null;
    }

    public RolePermission getRolePermissionByLoginUserByEntityType(UUID userId, UUID tenantId, int entityType) throws EwpException {
        String sql = "SELECT * from PFRolePermission AS rp ";
        sql += " INNER JOIN PFRoleLinking AS rl ON rp.RoleId=rl.RoleId ";
        sql += " WHERE rl.UserId='" + userId + "' AND rl.TenantId='" + tenantId + "' and rl.EntityType='" + entityType + "'";
        RolePermission response = executeSqlAndGetEntity(sql);
        if (response != null) {
            return response;
        }
        return null;
    }

    /**
     * Setting ROLE_PERMISSION properties.
     *
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     */
    @Override
    public void setPropertiesFromResultSet(RolePermission entity, Cursor cursor) {


        String id = cursor.getString(cursor.getColumnIndex("RolePermissionId"));
        if (!Utils.isEmptyOrNull(id)) {
            entity.setEntityId(UUID.fromString(id));
        }

        String roleId = cursor.getString(cursor.getColumnIndex("RoleId"));
        if (!Utils.isEmptyOrNull(roleId)) {
            entity.setRoleId(UUID.fromString(roleId));
        }

//        Boolean addOp = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("AddOp")));
        boolean addOp = "1".equals(cursor.getString(cursor.getColumnIndex("AddOp")));
        entity.setAddOp(addOp);

//        Boolean updateOp = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("UpdateOp")));
        Boolean updateOp = "1".equals(cursor.getString(cursor.getColumnIndex("UpdateOp")));
        entity.setUpdateOp(updateOp);

//        Boolean deleteOp = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("DeleteOp")));
        Boolean deleteOp = "1".equals(cursor.getString(cursor.getColumnIndex("DeleteOp")));
        entity.setDeleteOp(deleteOp);

//        Boolean viewOp = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ViewOp")));
        Boolean viewOp = "1".equals(cursor.getString(cursor.getColumnIndex("ViewOp")));
        entity.setViewOp(viewOp);

//        Boolean oP1 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp1")));
        Boolean oP1 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp1")));
        entity.setExtOp1(oP1);

//        Boolean oP2 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp2")));
        Boolean oP2 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp2")));
        entity.setExtOp2(oP2);

       // Boolean oP3 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp3")));
        Boolean oP3 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp3")));
        entity.setExtOp3(oP3);

//        Boolean oP4 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp4")));
        Boolean oP4 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp4")));
        entity.setExtOp4(oP4);

//        Boolean oP5 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp5")));
        Boolean oP5 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp5")));
        entity.setExtOp5(oP5);

//        Boolean oP6 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp6")));
        Boolean oP6 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp6")));
        entity.setExtOp6(oP6);

//        Boolean oP7 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp7")));
        Boolean oP7 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp7")));
        entity.setExtOp7(oP7);

//        Boolean oP8 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp8")));
        Boolean oP8 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp8")));
        entity.setExtOp8(oP8);

//        Boolean oP9 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp9")));
        Boolean oP9 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp9")));
        entity.setExtOp9(oP9);

//        Boolean oP10 = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ExtOp10")));
        Boolean oP10 = "1".equals(cursor.getString(cursor.getColumnIndex("ExtOp10")));
        entity.setExtOp10(oP10);

        String parentEntityType = cursor.getString(cursor.getColumnIndex("ParentEntityType"));

        if (!Utils.isEmptyOrNull(parentEntityType)) {
            entity.setParentEntityType(Integer.valueOf(parentEntityType));
        }

        String refEntityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (!Utils.isEmptyOrNull(refEntityType)) {
            entity.setRefEntityType(refEntityType);
        }
        entity.setDirty(false);
    }

}
