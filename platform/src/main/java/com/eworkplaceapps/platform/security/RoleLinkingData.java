//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/17/2015
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

/**
 * It provides database operation for ROLE_LINKING entity.
 * Build sql statement for all operation.
 * It validates ROLE_LINKING data.
 */
public class RoleLinkingData extends BaseData<RoleLinking> {
    private String getSQL() {
        final String sql = " SELECT * From PFRoleLinking ";
        return sql;
    }

    /**
     * GET the  ROLE_LINKING entity
     *
     * @return
     */
    @Override
    public RoleLinking createEntity() {
        return RoleLinking.createEntity();
    }

    /**
     * Searches for ROLE_LINKING Entity that matches the id value.
     * Returns ROLE_LINKING an Entity object.
     *
     * @param id
     * @return
     */
    @Override
    public RoleLinking getEntity(Object id) throws EwpException {
        // Building select statement
        final String sql = "SELECT * From PFRoleLinking where LOWER(RoleLinkingId) = LOWER('" + ((UUID) id).toString() + "')";
        // Execute select statement and return entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the ROLE_LINKING Entity record from database.
     * Returns Collection of ROLE_LINKING Entity.
     *
     * @return
     */
    public List<RoleLinking> getList() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET all the ROLE Entity record from database for given tenantId.
     * Returns Collection of ROLE Entity.
     *
     * @param tenantId
     * @return
     */
    public List<RoleLinking> getRoleLinkingListFromTenantId(UUID tenantId) throws EwpException {
        final String sql = getSQL() + " WHERE LOWER(TenantId)=LOWER('" + tenantId.toString() + "')";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET ROLE_LINKING entity list.
     * Returns as ResultSet for given tenantId.
     *
     * @param tenantId
     * @return
     */
    public Cursor getRoleLinkingListFromTenantIdAsResultSet(UUID tenantId) throws EwpException {
        final String sql = getSQL() + " WHERE LOWER(TenantId)=LOWER('" + tenantId.toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET ROLE_LINKING Entity record from database and return as ResultSet.
     * Returns A ResultSet populated with result.
     *
     * @param id
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL();
        sql += " where LOWER(RoleLinkingId) = LOWER('" + ((UUID) id).toString() + "')";
        return super.executeSqlAndGetResultSet(sql);
    }


    /**
     * GET all the ROLE_LINKING list
     * Returns ROLE_LINKING list as ResultSet.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        final String sql = getSQL();
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE ROLE_LINKING Entity
     *
     * @param entity
     */
    public void delete(RoleLinking entity) throws EwpException {
        super.deleteRows("PFRoleLinking", "LOWER(RoleLinkingId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(RoleLinking entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("RoleLinkingId", entity.getEntityId().toString());
        values.put("RoleId", entity.getRoleId().toString());
        values.put("UserId", entity.getUserId().toString());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("EntityId", entity.getSourceId().toString());
        values.put("EntityType", entity.getSourceType());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFRoleLinking", values);
    }

    @Override
    public void update(RoleLinking entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("RoleId", entity.getRoleId().toString());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFRoleLinking", values, "LOWER(RoleLinkingId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Setting ROLE_LINKING properties.
     *
     * @param cursor
     */
    @Override
    public void setPropertiesFromResultSet(RoleLinking entity, Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String roleId = cursor.getString(cursor.getColumnIndex("RoleId"));
        if (roleId != null && !"".equals(roleId)) {
            entity.setRoleId(UUID.fromString(roleId));
        }
        String userId = cursor.getString(cursor.getColumnIndex("UserId"));
        if (userId != null && !"".equals(userId)) {
            entity.setUserId(UUID.fromString(userId));
        }
        String refObjectId = cursor.getString(cursor.getColumnIndex("EntityId"));
        if (refObjectId != null && !"".equals(refObjectId)) {
            entity.setSourceId(UUID.fromString(refObjectId));
        }
        String sourceType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (sourceType != null && !"".equals(sourceType)) {
            entity.setSourceType(Integer.valueOf(sourceType));
        }
        String roleLinkingId = cursor.getString(cursor.getColumnIndex("RoleLinkingId"));
        if (roleLinkingId != null && !"".equals(roleLinkingId)) {
            entity.setEntityId(UUID.fromString(roleLinkingId));
        }
        entity.setDirty(false);
    }
}
