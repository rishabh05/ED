//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.department;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class DepartmentData extends BaseData<Department> {
    @Override
    public Department createEntity() {
        return Department.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return Department
     * @throws EwpException
     */
    @Override
    public Department getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where (dept.DepartmentId)= ('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the Department Entity from database.
     * Return Collection of Department Entity.
     *
     * @return List<Department>
     * @throws EwpException
     */
    @Override
    public List<Department> getList() throws EwpException {
        String sql = getSQL() + " ORDER BY LOWER(dept.Name) ";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get Department Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From EDDepartment where LOWER(DepartmentId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all Department Entity record from database and return result as a ResultSet.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDDepartment";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete Department entity.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(Department entity) throws EwpException {
        super.deleteRows("EDDepartment", "LOWER(DepartmentId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * insert Department entity.
     *
     * @param entity
     * @return long row id
     * @throws EwpException
     */
    @Override
    public long insertEntity(Department entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("DepartmentId", entity.getEntityId().toString());
        values.put("Name", entity.getName());
        values.put("Manager", entity.getHeadOfDepartment().toString());
        values.put("Picture", entity.getPicture());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("Description", entity.getDescription());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        long id = super.insert("EDDepartment", values);
        return id;
    }

    /**
     * update Department entity.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void update(Department entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Name", entity.getName());
        values.put("Manager", entity.getHeadOfDepartment().toString());
        values.put("Picture", entity.getPicture());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("Description", entity.getDescription());
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("EDDepartment", values, "LOWER(DepartmentId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    /**
     * Method is used to get DEPARTMENT list as ResultSet from tenantId
     *
     * @param tenantId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getDepartmentListFromTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " LOWER(TenantId)=LOWER('" + tenantId.toString() + "') ";
        sql += SqlUtils.buildSortClause(sql, "dept.Name", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to get DEPARTMENT list as FMResultSet from tenantId
     *
     * @param tenantId
     * @param deptId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getDepartmentListFromTenantIdAndHeadOfDeptAsResultSet(UUID tenantId, UUID deptId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  And ";
        sql += " LOWER(dept.DepartmentId)=LOWER('" + deptId.toString() + "')  ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to get DEPARTMENT list from tenantId and HeadOfDepartmentId.
     *
     * @param tenantId
     * @param deptId
     * @return List of departments
     * @throws EwpException
     */
    public List<Department> getDepartmentListFromTenantIdAndHeadOfDeptAsEntityList(UUID tenantId, UUID deptId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += "LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  And ";
        sql += " LOWER(dept.DepartmentId)=LOWER('" + deptId.toString() + "')";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Generate sql string with minimum required fields for Department.
     *
     * @return string sql
     */
    private String getSQL() {
        String sql = "";
        sql += "Select dept.*, emp.FullName as HeadOfDepartmentName ";
        sql += "from EDDepartment As dept Left Join EDEmployee as emp ON (dept.Manager) = (emp.EmployeeId) ";
        return sql;
    }
    /**
     * @param deptId
     * @param departmentName
     * @param tenantId
     * @return boolean
     * @throws EwpException
     */
    public boolean departmentExists(UUID deptId, String departmentName, UUID tenantId) throws EwpException {
        String sql = "SELECT * From EDDepartment ";
        return SqlUtils.recordExists(sql + "Where LOWER(Name) = LOWER('" + departmentName + "') and LOWER(TenantId) = LOWER('" + tenantId.toString() + "') and LOWER(DepartmentId) <> LOWER('" + deptId.toString() + "') ");
    }
    @Override
    public void setPropertiesFromResultSet(Department entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String departmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
        if (departmentId != null && !"".equals(departmentId)) {
            entity.setEntityId(UUID.fromString(departmentId));
        }

        String name = cursor.getString(cursor.getColumnIndex("Name"));
        if (name != null && !"".equals(name)) {
            entity.setName(name.replaceAll("''", "'"));
        }

        String headOfDepartment = cursor.getString(cursor.getColumnIndex("Manager"));
        if (headOfDepartment != null && !"".equals(headOfDepartment)) {
            entity.setHeadOfDepartment(UUID.fromString(headOfDepartment));
        }

        String headOfDepartmentName = cursor.getString(cursor.getColumnIndex("HeadOfDepartmentName"));
        if (headOfDepartmentName != null && !"".equals(headOfDepartmentName)) {
            entity.setHeadOfDepartmentName(headOfDepartmentName);
        }

        String description = cursor.getString(cursor.getColumnIndex("Description"));
        if (description != null && !"".equals(description)) {
            entity.setDescription(description.replaceAll("''", "'"));
        }


        String createdBy = cursor.getString(cursor.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(createdBy);
        }

        String createdAt = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }

        String logo = cursor.getString(cursor.getColumnIndex("Picture"));
        if ("null".equalsIgnoreCase(logo)) {
            logo = "";
        }
        entity.setPicture(logo);
        entity.setDirty(false);
    }
}
