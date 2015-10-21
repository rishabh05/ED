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
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class GroupFollowerData extends BaseData<GroupFollower> {
    @Override
    public GroupFollower createEntity() {
        return GroupFollower.createEntity();
    }

    @Override
    public GroupFollower getEntity(Object id) throws EwpException {
        String sql = "SELECT * From EDGroupFollower where GroupFollowerId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public List<GroupFollower> getList() throws EwpException {
        String sql = "SELECT * From EDGroupFollower";
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From EDGroupFollower where GroupFollowerId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDGroupFollower";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(GroupFollower entity) throws EwpException {
        super.deleteRows("EDGroupFollower", "GroupFollowerId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(GroupFollower entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("GroupFollowerId", UUID.randomUUID().toString());
        values.put("GroupId", entity.getGroupId().toString());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("FollowerType", entity.getFollowerType());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDGroupFollower", values);
    }

    @Override
    public void update(GroupFollower entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("GroupId", entity.getGroupId().toString());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("FollowerType", entity.getFollowerType());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("EDGroupFollower", values, "GroupFollowerId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for GroupFollower.
     *
     * @return sql
     */
    private String getSQL() {
        String sql = " SELECT egm.*, emp.egm.EmployeeId, emp.FullName ";
        sql += "FROM EDGroupFollower egm INNER JOIN EDGroup AS eg ON LOWER(egm.GroupId) = LOWER(eg.GroupId) ";
        sql += "INNER JOIN Employee emp ON LOWER(egm.EmployeeId) = LOWER(emp.EmployeeId) ";
        return sql;
    }

    public List<GroupFollower> getGroupFollowerListByEmployeeIdAndFollowerTypeAsResultSet(UUID employeeId, UUID tenantId, FollowerType followerType) throws EwpException {
        String sql = "";
        if (followerType != FollowerType.GROUP) {
            sql = " SELECT gf.FollowerType, gf.GroupId,'' as 'GroupName','1' as 'Follower' from EDGroupFollower as gf";
            sql += " WHERE  gf.TenantId = '" + tenantId.toString() + "' AND gf.EmployeeId='" + employeeId.toString() + "' ";
            sql += " AND gf.FollowerType=" + followerType;
        } else {
            sql = " SELECT gf.FollowerType,g.GroupId,g.Name as 'GroupName','Follower' = (CASE when gf.GroupFollowerId  is NULL then '0' else '1' end)";
            sql += " FROM EDGroup as g";
            sql += " LEFT JOIN EDGroupFollower as gf on gf.GroupId = g.GroupId ";
            sql += " AND gf.EmployeeId='" + employeeId.toString() + "'  AND gf.FollowerType = " + followerType + " AND gf.TenantId = '" + tenantId.toString() + "' ";
            sql += " WHERE g.TenantId='" + tenantId.toString() + "' ORDER BY g.Name";
        }
        return executeSqlAndGetEntityList(sql);
    }

    public List<UUID> getLoginEmployeeAllYourReportsFollowerList(UUID employeeId, UUID tenantId) throws EwpException {
        String sql = "";
        sql = " SELECT e.EmployeeId, e.TenantId, e.ReportsTo, e.FullName,e.Email";
        sql += " FROM EDEmployee as e ";
        sql += " WHERE e.EmployeeId = '" + employeeId.toString() + "' ";
        sql += " and e.TenantId = '" + tenantId.toString() + "' ";
        Cursor cursor = executeSqlAndGetResultSet(sql);
        UUID eId;
        UUID reportTo;
        List<UUID> employeeIdList = new ArrayList<UUID>();
        if (cursor != null) {
            if (cursor.moveToNext()) {
                /// getting the entity instance
                String id = cursor.getString(cursor.getColumnIndex("EmployeeId"));
                if (id != null && !"".equals(id)) {
                    eId = UUID.fromString(id);
                    if (!eId.toString().equals(employeeId.toString())) {
                        employeeIdList.add(eId);
                    }
                }
                id = cursor.getString(cursor.getColumnIndex("ReportsTo"));
                /// Getting ReportTo to employee. Find the recursively ReportTo user.
                if (id != null && !"".equals(id)) {
                    reportTo = UUID.fromString(id);
                    /// If id does not contain in the list.
                    if (!isContain(employeeIdList, reportTo)) {
                        employeeIdList.add(reportTo);
                        Tuple.Tuple2<String, String, String> ids = getFollwerEmployee(reportTo, tenantId);
                        id = ids.getT2();
                    }
                }
            }
        }
        return employeeIdList;
    }

    /**
     * To find the element in the list.
     *
     * @return boolean
     */
    private boolean isContain(List<UUID> employeeIdList, UUID id) {
        return employeeIdList.contains(id);
    }

    private Tuple.Tuple2<String, String, String> getFollwerEmployee(UUID employeeId, UUID tenantId) throws EwpException {
        String sql = "";
        sql = " SELECT gf.EmployeeId, gfe.TenantId, gfe.ReportsTo, gfe.FullName,gfe.Email";
        sql += " FROM EDEmployee as e ";
        sql += " INNER JOIN EDGroupFollower AS gf ON gf.EmployeeId = e.ReportsTo";
        sql += " INNER JOIN EDEmployee as gfe ON gfe.EmployeeId = gf.EmployeeId";
        sql += " WHERE e.EmployeeId = '" + employeeId.toString() + "' ";
        sql += " and gf.FollowerType = '" + FollowerType.ALL_YOUR_REPORTS + "' AND e.TenantId = '" + tenantId.toString() + "' ";
        Cursor cursor = executeSqlAndGetResultSet(sql);
        String id = "";
        String reportTo = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                /// getting the entity instance
                id = cursor.getString(cursor.getColumnIndex("EmployeeId"));
                reportTo = cursor.getString(cursor.getColumnIndex("ReportsTo"));
                return new Tuple.Tuple2<String, String, String>(id, reportTo, "");
            }
        }
        return new Tuple.Tuple2<String, String, String>("", "", "");
    }

    /**
     * @param employeeId
     * @param tenantId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getLoginEmployeeFollwerList(UUID employeeId, UUID tenantId) throws EwpException {
        String sql = "";
        sql = " SELECT gf.EmployeeId as EmployeeId, gfe.TenantId, gfe.ReportsTo, gfe.FullName,gfe.Email";
        sql += " FROM EDEmployee as e ";
        sql += " INNER JOIN EDGroupFollower AS gf ON gf.EmployeeId = e.ReportsTo";
        sql += " INNER JOIN EDEmployee as gfe ON gfe.EmployeeId = gf.EmployeeId";
        sql += " WHERE e.EmployeeId = '" + employeeId.toString() + "' ";
        sql += " and gf.FollowerType = '" + FollowerType.ALL_YOUR_REPORTS + "' AND e.TenantId = '" + tenantId.toString() + "' ";
        GroupFollower groupFollower = executeSqlAndGetEntity(sql);

        sql = " SELECT gf.EmployeeId as EmployeeId, gfe.TenantId, gfe.ReportsTo, gfe.FullName,gfe.Email";
        sql += " FROM EDEmployee as e ";
        sql += " INNER JOIN EDGroupFollower AS gf ON gf.EmployeeId = e.ReportsTo";
        sql += " INNER JOIN EDEmployee as gfe ON gfe.EmployeeId = gf.EmployeeId";
        sql += " WHERE e.EmployeeId = '" + employeeId.toString() + "' ";
        sql += " and gf.FollowerType = '" + FollowerType.YOUR_DIRECT_REPORTS + "' AND e.TenantId = '" + tenantId.toString() + "' ";
        sql += " UNION ";
        String sql1 = "";
        sql1 += " SELECT e.EmployeeId, e.TenantId, e.ReportsTo, e.FullName, e.Email  FROM EDGroupMember AS gm";
        sql1 += " INNER JOIN EDGroupFollower AS gf ON gf.GroupId=gm.GroupId AND gf.EmployeeId=gm.EmployeeId";
        sql1 += " INNER JOIN EDEmployee AS e ON e.EmployeeId=gf.EmployeeId";
        sql1 += " WHERE  gm.GroupId IN (";
        sql1 += " SELECT GroupId  FROM EDGroupMember as es WHERE es.EmployeeId = '" + employeeId.toString() + "'";
        sql1 += " AND gf.EmployeeId!='" + employeeId.toString() + "' AND gf.FollowerType='" + FollowerType.GROUP + "' ";
        sql1 += " AND e.TenantId='" + tenantId.toString() + "')";
        sql += sql1;
        ///Logger.defaultLogger.logMessage(sql)
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void setPropertiesFromResultSet(GroupFollower entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String groupFollowerId = cursor.getString(cursor.getColumnIndex("GroupFollowerId"));
        if (groupFollowerId != null && !"".equals(groupFollowerId)) {
            entity.setEntityId(UUID.fromString(groupFollowerId));
        }
        String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (employeeId != null && !"".equals(employeeId)) {
            entity.setEntityId(UUID.fromString(employeeId));
        }
        int followerType = cursor.getInt(cursor.getColumnIndex("FollowerType"));
        entity.setFollowerType(followerType);
        String groupId = cursor.getString(cursor.getColumnIndex("GroupId"));
        if (groupId != null && !"".equals(groupId)) {
            entity.setGroupId(UUID.fromString(groupId));
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
