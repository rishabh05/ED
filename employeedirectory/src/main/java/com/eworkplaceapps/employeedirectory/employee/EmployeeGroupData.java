//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This is a data class for EmployeeGroup information.
 * Each employee may be part of group. This class is responsible to manage the each employee group information.
 */
public class EmployeeGroupData extends BaseData<EmployeeGroup> {
    @Override
    public EmployeeGroup createEntity() {
        return EmployeeGroup.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return EmployeeGroup
     * @throws EwpException
     */
    @Override
    public EmployeeGroup getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where TeamId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the EmployeeGroup Entity from database.
     * Return Collection of EmployeeGroup Entity.
     *
     * @return List<EmployeeGroup>
     * @throws EwpException
     */
    public List<EmployeeGroup> getList() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get EmployeeGroup Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return Object
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(TeamId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all EmployeeGroup Entity record from database and return result as a FMResultSet.
     *
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete EmployeeGroup entity.
     *
     * @param entity
     * @throws EwpException
     */
    public void delete(EmployeeGroup entity) throws EwpException {
        super.deleteRows("EDTeam", "LOWER(TeamId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(EmployeeGroup entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("TeamId", entity.getEntityId().toString());
        values.put("Name", entity.getName());
        values.put("Manager", entity.getManagerId().toString());
        values.put("Description", entity.getDescription());
        values.put("Picture", entity.getPicture());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDTeam", values);
    }

    @Override
    public void update(EmployeeGroup entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Name", entity.getName());
        values.put("Description", entity.getDescription());
        values.put("Manager", entity.getManagerId().toString());
        values.put("Picture", entity.getPicture());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("EDTeam", values, "LOWER(TeamId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * @param teamId
     * @param teamName
     * @param tenantId
     * @return boolean
     * @throws EwpException
     */
    public boolean teamExists(UUID teamId, String teamName, UUID tenantId) throws EwpException {
        String sql = "SELECT * From EDTeam ";
        return SqlUtils.recordExists(sql + "Where LOWER(Name) = LOWER('" + teamName + "') and LOWER(TenantId) = LOWER('" + tenantId.toString() + "') and LOWER(TeamId) <> LOWER('" + teamId.toString() + "') ");
    }

    /**
     * Method is used to get employee group list as ResultSet from tenantid
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getEmployeeGroupListByTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " AND LOWER(t.TenantId)=LOWER('" + tenantId.toString() + "')  ";
        sql += SqlUtils.buildSortClause(sql, "emp.FirstName", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Generate sql string with minimum required fields for EmployeeGroup.
     *
     * @return string sql
     */
    private String getSQL() {
        return "SELECT t.*, e.FullName as ManagerName FROM EDTeam as t LEFT JOIN  EDEmployee as e ON t.Manager = e.EmployeeId ";
    }

    /**
     * @param tenantId
     * @param employeeId
     * @return List<EmployeeGroup>
     * @throws EwpException
     */
    public List<EmployeeGroup> getEmployeeTeamListFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        String sql = " SELECT eg.* ";
        sql += "FROM EDTeam as eg INNER JOIN EDTeamMember AS egm ON LOWER(egm.TeamId) = LOWER(eg.TeamId) ";
        sql += " AND LOWER(egm.TenantId)=LOWER('" + tenantId.toString() + "') and  LOWER(egm.EmployeeId) = LOWER('" + employeeId.toString() + "') ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param tenantId
     * @param employeeId
     * @return List<String>
     * @throws EwpException
     */
    public List<String> getEmployeeTeamListAsStringFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        String sql = " SELECT eg.Name,eg.TeamId ";
        sql += "FROM EDTeam as eg INNER JOIN EDTeamMember AS egm ON LOWER(egm.TeamId) = LOWER(eg.TeamId) ";
        sql += " AND LOWER(egm.TenantId)=LOWER('" + tenantId.toString() + "') and  LOWER(egm.EmployeeId) = LOWER('" + employeeId.toString() + "') ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        Cursor cursor = executeSqlAndGetResultSet(sql);
        List<String> teamList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                teamList.add(cursor.getString(cursor.getColumnIndex("Name")));
            }
        }
        return teamList;
    }

    /**
     * @param tenantId
     * @param employeeId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeTeamListFromEmployeeIdAsResult(UUID tenantId, UUID employeeId) throws EwpException {
        String sql = " SELECT eg.Name,eg.TeamId ";
        sql += "FROM EDTeam as eg INNER JOIN EDTeamMember AS egm ON (egm.TeamId) = (eg.TeamId) ";
        sql += " AND (egm.TenantId)=('" + tenantId.toString() + "') and (egm.EmployeeId) = ('" + employeeId.toString() + "') ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to get the team member list for which team employee is the member.
     *
     * @param tenantId
     * @param employeeId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeTeamMemberList(UUID tenantId, UUID employeeId) throws EwpException {
        String sql = " SELECT EdTm.Name as TeamName, emp.*, ";
        sql += "(select  EmpStatus from EDEmployeeStatus where  EmployeeId = emp.EmployeeId and ";
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) ";
        sql += ") Order By DateTime(CreatedDate) Desc limit 1) As Status ";
        sql += "FROM EDTeam as EdTm INNER JOIN EDTeamMember AS egm ON LOWER(egm.TeamId) = LOWER(EdTm.TeamId) ";
        sql += "INNER JOIN EDEmployee AS emp ON LOWER(emp.EmployeeId) = LOWER(egm.EmployeeId) ";
        sql += "INNER JOIN EDTeamMember AS tm ON LOWER(EdTm.TeamId) = LOWER(tm.TeamId) ";
        sql += "Where egm.TenantId='" + tenantId.toString() + "' and  tm.EmployeeId = '" + employeeId.toString() + "' ORDER By EdTm.Name,emp.FullName ";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void setPropertiesFromResultSet(EmployeeGroup entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String employeeId = cursor.getString(cursor.getColumnIndex("TeamId"));
        if (employeeId != null && !"".equals(employeeId)) {
            entity.setEntityId(UUID.fromString(employeeId));
        }
        String managerId = cursor.getString(cursor.getColumnIndex("Manager"));
        if (managerId != null && !"".equals(managerId)) {
            entity.setManagerId(UUID.fromString(managerId));
        }
        String managerName = cursor.getString(cursor.getColumnIndex("ManagerName"));
        entity.setManagerName(managerName);
        String firstName = cursor.getString(cursor.getColumnIndex("Name"));
        entity.setName(firstName);
        String desc = cursor.getString(cursor.getColumnIndex("Description"));
        entity.setDescription(desc);
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
        String picture = cursor.getString(cursor.getColumnIndex("Picture"));
        if ("null".equalsIgnoreCase(picture)) {
            picture = "";
        }
        entity.setPicture(picture);
        entity.setDirty(false);
    }
}
