//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;
import com.eworkplaceapps.platform.utils.enums.UserStatusItem;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EmployeeGroupMemberData extends BaseData<EmployeeGroupMember> {
    @Override
    public EmployeeGroupMember createEntity() {
        return EmployeeGroupMember.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return EmployeeGroupMember
     * @throws EwpException
     */
    public EmployeeGroupMember getEntity(Object id) throws EwpException {
        String sql = "SELECT * From EDTeamMember where LOWER(TeamMemberId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the EmployeeGroupMember Entity from database.
     * Return Collection of EmployeeGroupMember Entity.
     *
     * @return List<EmployeeGroupMember>
     * @throws EwpException
     */
    @Override
    public List<EmployeeGroupMember> getList() throws EwpException {
        String sql = "SELECT * From EDTeamMember";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get EmployeeGroupMember Entity that matches the id and return result as a FMResultSet.
     *
     * @param id
     * @return Cursor
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From EDTeamMember where LOWER(TeamMemberId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * @return Cursor
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDTeamMember";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete EmployeeGroupMember entity.
     *
     * @param entity
     */
    @Override
    public void delete(EmployeeGroupMember entity) throws EwpException {
        super.deleteRows("EDTeamMember", "LOWER(TeamMemberId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(EmployeeGroupMember entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("TeamMemberId", entity.getEntityId().toString());
        values.put("TeamId", entity.getEmployeeGroupId().toString());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDTeamMember", values);
    }

    @Override
    public void update(EmployeeGroupMember entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("TeamId", entity.getEmployeeGroupId().toString());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("EDTeamMember", values, "LOWER(TeamMemberId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Method is used to get user list as ResultSet from loginemail
     *
     * @param tenantId
     * @return cursor
     * @throws EwpException
     */
    public Cursor getEmployeeGroupMemberListByTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " AND LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  ";
        sql += SqlUtils.buildSortClause(sql, "FirstName", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }


    /**
     * Method is used to get user list as ResultSet from loginemail
     *
     * @param groupId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeTeamMemberListByTeamIdAsResultSet(UUID groupId) throws EwpException {
        String sql = getSQL();
        sql += "LOWER(TeamId) = LOWER('" + groupId.toString() + "') ";
        sql += SqlUtils.buildSortClause(sql, "FirstName", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Generate sql string with minimum required fields for EmployeeGroupMember.
     *
     * @return string sql
     */
    private String getSQL() {
        String sql = " SELECT egm.*, emp.FirstName AS FirstName, emp.LastName AS LastName, eg.Name as GroupName ";
        sql += "FROM EDTeamMember egm INNER JOIN EDTeam AS eg ON LOWER(egm.TeamId) = LOWER(eg.TeamId) ";
        sql += "INNER JOIN EDEmployee emp ON LOWER(egm.EmployeeId) = LOWER(emp.EmployeeId) ";
        return sql;
    }

    /**
     * @param employeeId
     * @return int no. of rows deleted
     */
    public int deleteGroupMemberByEmployeeId(UUID employeeId) {
        return DatabaseOps.defaultDatabase().deleteStatement("EDTeamMember", "LOWER(TeamId)=?", new String[]{employeeId.toString().toLowerCase()});
    }

    /**
     * @param teamId
     * @param employeeId
     * @return int no. of rows deleted
     */
    public int deleteGroupMemberFromTeamAndEmployeeId(UUID teamId, UUID employeeId) {
        return DatabaseOps.defaultDatabase().deleteStatement("EDTeamMember", "LOWER(TeamId)=? And LOWER(EmployeeId)=?", new String[]{teamId.toString().toLowerCase(), employeeId.toString().toLowerCase()});
    }

    /**
     * @param teamId
     * @return int no. of rows deleted
     * @throws EwpException
     */
    public int deleteAllMemberFromTeamId(UUID teamId) throws EwpException {
        return DatabaseOps.defaultDatabase().deleteStatement("EDTeamMember", "LOWER(TeamId)=?", new String[]{teamId.toString().toLowerCase()});
    }

    /**
     * Method is used to get active employee Group entity list.
     * /// :param: employeeId: Id of employee.
     *
     * @param employeeId
     * @return List<EmployeeGroupMember>
     * @throws EwpException
     */
    public List<EmployeeGroupMember> getActiveGroupMembersWhereEmployeeIsMember(UUID employeeId) throws EwpException {
        String sql = " SELECT gm.*, e.FullName FROM EDTeamMember as gm " +
                " INNER JOIN EDTeam As g ON LOWER(gm.TeamId)=LOWER(g.TeamId) " +
                " INNER JOIN EDTeamMember AS gm1 ON LOWER(g.TeamId)=LOWER(gm1.TeamId) " +
                " INNER JOIN EDEmployee As e ON LOWER(gm.EmployeeId)=LOWER(e.EmployeeId) " +
                " INNER JOIN PFTenantUser AS tu ON LOWER(e.UserId)=LOWER(tu.UserId) " +
                " Where LOWER(gm1.EmployeeId)=LOWER('" + employeeId.toString() + "') AND LOWER(gm.EmployeeId)=LOWER('" + employeeId.toString() + "')" +
                " AND tu.UserStatus = " + UserStatusItem.ACTIVE.getId();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Method is used to get active employee Group entity list.
     * /// :param: employeeId: Id of employee.
     *
     * @param employeeId
     * @param teamId
     * @return EmployeeGroupMember
     * @throws EwpException
     */
    public EmployeeGroupMember getGroupMembersWhereEmployeeIsMember(UUID employeeId, UUID teamId) throws EwpException {
        String sql = getSQL() + " where LOWER(egm.EmployeeId) = LOWER('" + employeeId.toString() + "') And LOWER(egm.TeamId) = LOWER('" + teamId.toString() + "') ";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public void setPropertiesFromResultSet(EmployeeGroupMember entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeGroupMemberId = cursor.getString(cursor.getColumnIndex("TeamMemberId"));
        if (employeeGroupMemberId != null && !"".equals(employeeGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeGroupMemberId));
        }

        String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (employeeId != null && !"".equals(employeeId)) {
            entity.setEmployeeId(UUID.fromString(employeeId));
        }

        String employeeGroupId = cursor.getString(cursor.getColumnIndex("TeamId"));
        if (employeeGroupId != null && !"".equals(employeeGroupId)) {
            entity.setEmployeeGroupId(UUID.fromString(employeeGroupId));
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
        entity.setDirty(false);
    }
}
