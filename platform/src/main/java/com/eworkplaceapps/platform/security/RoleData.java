//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/16/2015
//===============================================================================

package com.eworkplaceapps.platform.security;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DATA class for ROLE
 */
public class RoleData extends BaseData<Role> {

    private String getSQL() {
        String sql = " SELECT * From PFRole ";
        return sql;
    }

    // ------------------- Begin Must override methods -----------------


    /**
     * GET the  ROLE entity
     *
     * @return
     */
    public Role createEntity() {
        return Role.createEntity();
    }

    // Searches for ROLE Entity that matches given id.
    // Returns ROLE entity object.
    public Role getEntity(Object id) throws EwpException {
        // Building select statement
        String sql = getSQL() + " where RoleId = '" + ((UUID) id).toString() + "'";
        // Executes select statement and return ROLE entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET list of roles from database.
     * Returns Collection of ROLE Entity.
     *
     * @return
     */
    public List<Role> getList() throws EwpException {
        String mySql = getSQL();
        return executeSqlAndGetEntityList(mySql);
    }

    public boolean isRoleExist(String name, UUID tenantId, String applicationId) throws EwpException {
        String mySql = getSQL() + " WHERE TenantId='" + tenantId.toString() + "' and RoleName='" + name + "' and ApplicationId='" + applicationId + "'";
        return SqlUtils.recordExists(mySql);
    }

    /**
     * GET list of roles from database for tenantId.
     * Returns collection of ROLE entity.
     *
     * @param tenantId
     * @return
     */
    public List<Role> getRoleListFromTenantId(UUID tenantId) throws EwpException {
        String sql = getSQL() + " WHERE TenantId='" + tenantId.toString() + "'";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET list of roles from database
     * Returns as ResultSet for given tenantId
     *
     * @return
     */
    public Cursor getRoleListFromTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String mySql = getSQL() + " WHERE TenantId='" + tenantId.toString() + "'";
        return executeSqlAndGetResultSet(mySql);
    }

    /**
     * Searches for ROLE Entity that matches given id.
     * Returns a ResultSet for given id.
     *
     * @param id
     * @return
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL();
        sql += " where RoleId = '" + ((UUID) id).toString() + "'";
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * GET list of roles from database
     * Returns as ResultSet.
     *
     * @return
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = getSQL();
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE ROLE Entity
     *
     * @param entity
     */
    @Override
    public boolean deleteRows(Role entity) throws EwpException {
        return super.deleteRows("PFRole", "RoleId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFRole", "RoleId=?", new String[]{id.toString()});
    }


    public Role getDefaultRoleByName(String roleName) throws EwpException {
        String sql = getSQL();
        sql += " where RoleName = '" + roleName + "'"; //TODO check if null can be assigned instead of empty Guid
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public long insertEntity(Role entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("RoleId", entity.getEntityId().toString());
        values.put("RoleName", entity.getName());
        values.put("TenantId", UUID.randomUUID().toString());
        values.put("Active", entity.isActive());
        values.put("RoleKey", entity.getRoleKey());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("ApplicationId", "A8920FC7-4874-4854-BCE1-4C4909C6C824");
        long id = super.insert("PFRole", values);
        return id;
    }

    @Override
    public void update(Role entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("RoleName", entity.getName());
        values.put("Active", entity.isActive());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFRole", values, "RoleId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Setting ROLE properties.
     *
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     */
    @Override
    public void setPropertiesFromResultSet(Role entity, Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null) {
            entity.setTenantId(UUID.fromString(id));
        }

        String roleId = cursor.getString(cursor.getColumnIndex("RoleId"));
        if (roleId != null) {
            entity.setEntityId(UUID.fromString(roleId));
        }

        String name = cursor.getString(cursor.getColumnIndex("RoleName"));
        if (name != null) {
            entity.setName(name);
        }

        String roleKey = cursor.getString(cursor.getColumnIndex("RoleKey"));
        if (roleKey != null) {
            entity.setRoleKey(roleKey);
        }

        boolean active = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("ACTIVE")));
        entity.setActive(active);

        String appType = cursor.getString(cursor.getColumnIndex("ApplicationId"));
        if (appType != null) {
            entity.setApplicationId(appType);
        }
        entity.setDirty(false);
    }

}
