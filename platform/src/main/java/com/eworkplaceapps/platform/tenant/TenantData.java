//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.tenant;

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
 * TENANT DATA class
 */
public class TenantData extends BaseData<Tenant> {

    public TenantData() {
    }

    @Override
    public Tenant createEntity() {
        return Tenant.createEntity();
    }

    /**
     * GET tenant Entity that matches the id value.
     * Returns TENANT Entity object
     *
     * @param id
     * @return Tenant
     */
    @Override
    public Tenant getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFTenant where LOWER(TenantId) = LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Returns Collection of Entity.
     *
     * @return List<Tenant>
     */
    @Override
    public List<Tenant> getList() throws EwpException {
        String mySql = "SELECT * From PFTenant";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * GET Entity record from database and return result as! a ResultSet.
     * Returns A ResultSet populated with result.
     *
     * @param id
     * @return Cursor
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String s = "SELECT * From PFTenant where LOWER(TenantId) = LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(s);
    }

    /**
     * GET all the Entity record from database and return result as! a ResultSet.
     * Returns A ResultSet populated with result.
     *
     * @return Cursor
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFTenant";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE TENANT entity.
     *
     * @param entity
     * @return
     */
    @Override
    public boolean deleteRows(Tenant entity) throws EwpException {
        return super.deleteRows("PFTenant", "LOWER(TenantId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * delete tenant by id
     *
     * @param id
     * @throws EwpException
     */
    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFTenant", "LOWER(TenantId)=?", new String[]{id.toString().toLowerCase()});
    }

    /**
     * Checks for duplicate tenant.
     * return True if TENANT business name exits
     * This method will be used to check duplicate tenant in database for given tenant business id and business name.
     *
     * @param businessName
     * @param tenantId
     * @return boolean
     */
    public boolean isDuplicateTenant(String businessName, UUID tenantId) throws EwpException {
        String sql = "SELECT * From PFTenant where Name =  '" + businessName + "'";
        return SqlUtils.recordExists(sql);
    }

    /**
     * Searches for all tenant that matches the email
     * and returns the tenant collection.
     * The user record as! a ResultSet that matches the login email.
     * This method will be used to get tenant list by login user email as! ResultSet.
     *
     * @param loginEmail
     * @return Cursor
     */
    public Cursor getTenantListByLoginUserEmailAsResultSet(String loginEmail) throws EwpException {
        String sql = "SELECT * From PFTenant where LoginEmail = ' %@' ";
        sql = sql.format(loginEmail);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Searches for tenant that matches the email
     * and returns the tenants.
     * returns TENANT list in form of ResultSet.
     * This method will be used to get ative tenant list by login user email as! ResultSet.
     *
     * @param loginEmail
     * @param activeUserStatus
     * @param sendInvtTenantStatus
     * @param activeTenantStatus
     * @return
     */
    public Cursor getActiveTenantListByLoginUserEmailAsResultSet(String loginEmail, int activeUserStatus, int sendInvtTenantStatus, int activeTenantStatus) throws EwpException {
        String sql = "SELECT distinct  t.TenantId as! TenantId, t.TenantStatus, t.Version  FROM PFTenant AS t Inner JOIN PFTenantUser as tu on LOWER(t.TenantId)=LOWER(tu.TenantId) or LOWER(tu.TenantId)=LOWER('" + UUID.randomUUID() + "')  Where tu.LoginEmail='" + loginEmail + "' and tu.UserStatus='" + activeUserStatus + "' or tu.UserStatus='" + sendInvtTenantStatus + "' AND t.TenantStatus='" + activeTenantStatus + "' ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * searches for total task number for login tenant.
     * returns Total task number.
     * This method will be used to get task number by tenant id.
     *
     * @return
     */
    public int generateTaskNumberByTenantId(String tenantId) throws EwpException {
        String sql = "SELECT TaskNumber FROM PFTenant where LOWER(TenantId)= LOWER('" + (tenantId) + "') ";
        Cursor resultSet = executeSqlAndGetResultSet(sql);
        if (resultSet != null) {
            while (resultSet.moveToFirst()) {
                String taskNumber = resultSet.getString(resultSet.getColumnIndex("TaskNumber"));
                if (taskNumber != null) {
                    return Integer.parseInt(taskNumber);
                }
            }
        }
        return 0;
    }

    /**
     * insert TenantData in database
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public long insertEntity(Tenant entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("TenantId", entity.getEntityId().toString());
        values.put("Name", entity.getName());
        values.put("TenantStatus", entity.getTenantStatus());
        values.put("PrimaryUserId", entity.getPrimaryUserId().toString());
        values.put("URL", entity.getWebsite());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("Logo", entity.getLogo());
        long id = super.insert("PFTenant", values);
        return id;
    }

    @Override
    public void update(Tenant entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Name", entity.getName());
        values.put("TenantStatus", entity.getTenantStatus());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("URL", entity.getWebsite());
        values.put("Logo", entity.getLogo());
        super.update("PFTenant", values, "LOWER(TenantId)=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * This method is used to set property value from  database ResultSet.
     */
    @Override
    public void setPropertiesFromResultSet(Tenant entity, Cursor resultSet) {
        String name = resultSet.getString(resultSet.getColumnIndex("Name"));
        if (name != null) {
            entity.setName(name);
        }

        String primaryUserId = resultSet.getString(resultSet.getColumnIndex("PrimaryUserId"));
        if (primaryUserId != null) {
            entity.setPrimaryUserId(UUID.fromString(primaryUserId));
        }

        String tenantId = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (tenantId != null) {
            entity.setEntityId(UUID.fromString(tenantId));
        }

        String status = resultSet.getString(resultSet.getColumnIndex("TenantStatus"));
        if (status != null) {
            entity.setTenantStatus(Integer.parseInt(status));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null) {
            entity.setCreatedBy(createdBy);
        }

        Date createdAt = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex("CreatedDate")),true,true);
        if (createdAt != null) {
            entity.setCreatedAt(createdAt);
        }


        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            entity.setUpdatedBy(updatedBy);
        }

        Date updatedAt = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex("ModifiedDate")),true,true);
        if (updatedAt != null) {
            entity.setUpdatedAt(updatedAt);
        }

        String logo = resultSet.getString(resultSet.getColumnIndex("Logo"));
        if (logo != null && !"".equals(logo)) {
            entity.setLogo(logo);
        }
        entity.setDirty(false);
    }
}
