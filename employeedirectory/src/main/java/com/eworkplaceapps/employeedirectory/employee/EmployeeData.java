//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.LinkType;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EmployeeData extends BaseData<Employee> {
    @Override
    public Employee createEntity() {
        return Employee.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return Employee
     * @throws EwpException
     */
    @Override
    public Employee getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(emp.EmployeeId)= LOWER('" + ((UUID) id) + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return Employee
     * @throws EwpException
     */
    public Employee getEmployeeFromUserId(UUID id) throws EwpException {
        String sql = getSQL() + " where (emp.UserId)= ('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get an entity that matched the id
     *
     * @return List<Employee>
     * @throws EwpException
     */
    @Override
    public List<Employee> getList() throws EwpException {
        String sql = getSQL() + " ORDER BY LOWER(emp.FullName) ";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get Employee Entity that matches the id and return result as! a ResultSet.
     *
     * @param id
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From Employee where LOWER(EDEmployeeId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all Employee Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDEmployee";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete Employee entity.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(Employee entity) throws EwpException {
        deleteRows("Employee", "LOWER(EDEmployeeId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(Employee entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EmployeeId", entity.getEntityId().toString());
        values.put("FirstName", entity.getFirstName());
        values.put("MiddleName", entity.getMiddleName());
        values.put("LastName", entity.getLastName());
        values.put("FullName", entity.getFullName());
        values.put("NickName", entity.getNickName());
        values.put("JobTitle", entity.getJobTitle());
        values.put("ReportsTo", entity.getReportTo());
        if (entity.getBirthDay() != null)
            values.put("Birthday", Utils.dateAsStringWithoutUTC(entity.getBirthDay()));
        if (entity.getStartDate() != null)
            values.put("StartDate", Utils.dateAsStringWithoutUTC(entity.getStartDate()));
        values.put("LocationId", entity.getLocation());
        values.put("DepartmentId", entity.getDepartmentId().toString());
        values.put("LocalTimeZone", entity.getLocalTimeZone());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("UserId", entity.getTenantUserId().toString());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDEmployee", values);
    }

    @Override
    public void update(Employee entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("FirstName", entity.getFirstName());
        values.put("MiddleName", entity.getMiddleName());
        values.put("LastName", entity.getLastName());
        values.put("FullName", entity.getFullName());
        values.put("JobTitle", entity.getJobTitle());
        values.put("NickName", entity.getNickName());
        values.put("ModifiedBy", entity.getUpdatedBy());
        if (entity.getReportTo() == null || entity.getReportTo().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000") || entity.getReportTo().toString().equalsIgnoreCase("")) {
            String id = null;
            values.put("ReportsTo", id);
        } else {
            values.put("ReportsTo", entity.getReportTo());
        }
        //values.put("ReportsTo", entity.getReportTo());
        String s = "";
        if (entity.getBirthDay() != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            final String utcTime = sdf.format(entity.getBirthDay());
            s = utcTime;
        }
        values.put("Birthday", s);
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        Date startDate = null;
        if (entity.getStartDate() != null) {
            startDate = entity.getStartDate();
        }
        /*else {
            d = new Date();
        }*/
        if (startDate != null)
            values.put("StartDate", Utils.dateAsStringWithoutUTC(startDate));

        if (entity.getLocationId() == null || entity.getLocationId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
            String id = null;
            values.put("LocationId", id);
        } else {
            values.put("LocationId", entity.getLocationId().toString());
        }

        if (entity.getDepartmentId() == null || entity.getDepartmentId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
            String id = null;
            values.put("DepartmentId", id);
        } else {
            values.put("DepartmentId", entity.getDepartmentId().toString());
        }
        //values.put("LocationId", entity.getLocationId().toString());
        //values.put("DepartmentId", entity.getDepartmentId().toString());
        super.update("EDEmployee", values, "LOWER(EmployeeId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    /**
     * Method is used to set employee location as NULL where given location id is set for employees.
     *
     * @param locationId
     */
    public void updateEmployeeLocationEmpty(UUID locationId) throws EwpException {
        Date updatedAt = new Date();
        String updatedBy = EwpSession.getSharedInstance().getUserId().toString();
        ContentValues values = new ContentValues();
        values.put("ModifiedBy", updatedBy);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(updatedAt));
        String loc = null;
        values.put("LocationId", loc);
        super.update("EDEmployee", values, "(LocationId)=?", new String[]{locationId.toString()});
    }

    /**
     * Method is used to set employee location as NULL where given location id is set for employees.
     *
     * @param locationId
     * @param employeeId
     */
    public void updateEmployeeLocationEmpty(UUID locationId, UUID employeeId) throws EwpException {
        Date updatedAt = new Date();
        String updatedBy = EwpSession.getSharedInstance().getUserId().toString();
        ContentValues values = new ContentValues();
        values.put("ModifiedBy", updatedBy);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(updatedAt));
        String loc = null;
        values.put("LocationId", loc);
        super.update("EDEmployee", values, "(LocationId)=? and (EmployeeId)=?", new String[]{locationId.toString(), employeeId.toString()});
    }

    /**
     * Method is used to set employee department as NULL where given department id is set for employees.
     *
     * @param departmentId
     */
    public void updateEmployeeDepartmentEmpty(UUID departmentId) throws EwpException {
        Date updatedAt = new Date();
        String updatedBy = EwpSession.getSharedInstance().getUserId().toString();
        ContentValues values = new ContentValues();
        values.put("ModifiedBy", updatedBy);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(updatedAt));
        String dept = null;
        values.put("DepartmentId", dept);
        super.update("EDEmployee", values, "(DepartmentId)=?", new String[]{departmentId.toString()});
    }

    /**
     * Method is used to set employee department as NULL where given department id is set for employees.
     *
     * @param departmentId
     * @param employeeId
     */
    public void updateEmployeeDepartmentEmpty(UUID departmentId, UUID employeeId) throws EwpException {
        Date updatedAt = new Date();
        String updatedBy = EwpSession.getSharedInstance().getUserId().toString();
        ContentValues values = new ContentValues();
        values.put("ModifiedBy", updatedBy);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(updatedAt));
        String dept = null;
        values.put("DepartmentId", dept);
        super.update("EDEmployee", values, "(DepartmentId)=? and (EmployeeId)=?", new String[]{departmentId.toString(), employeeId.toString()});
    }

    /**
     * This method is used to update emmployee profile image.
     *
     * @param profilePicture
     * @param employeeId
     */
    public void updateProfilePicture(String profilePicture, UUID employeeId) {
        String date = Utils.stringFromDate(new Date());
        /// Generating the update Employee image SQL Statement
        String sql = "UPDATE EDEmployee SET Picture = '" + profilePicture + "', ModifiedDate = '" + date + "' WHERE LOWER(EmployeeId) =LOWER('" + employeeId.toString() + "') ";
        super.executeNonQuerySuccess(sql);
    }

    /**
     * Method is used to get user list as! ResultSet from tenantId
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getEmployeeListTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " TenantId='" + tenantId.toString() + "'  ";
        sql += SqlUtils.buildSortClause(sql, "FirstName", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * It will search employee list from fullName. If search character appear at any place of full name then return those list of employees.
     * Method will return user list as! ResultSet.
     *
     * @param tenantId
     * @param searchByChar
     * @return List<Employee>
     * @throws EwpException
     */
    public List<Employee> searchEmployee(UUID tenantId, String searchByChar) throws EwpException {
        String sql = getSQL() + " Where ";
        if (searchByChar == null || "".equals(searchByChar)) {
            sql += " LOWER(TenantId)=LOWER('" + tenantId.toString() + "') ";
        } else {
            String str = searchByChar;
            sql += " LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  And FullName Like '%" + str + "%'";
        }
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Generate sql string with minimum required fields for Employee.
     *
     * @return string sql
     */
    private String getSQL() {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        String loggedInUserId = EwpSession.getSharedInstance().getUserId().toString();
        String sql = "";
        sql += "select distinct emp.*,(link.UserEntityLinkId!='null') As Following,tUser.Picture,tUser.LoginEmail,";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText, pl.RGBColor as StatusColor ";
        sql += " from EDEmployee emp ";
        sql += "INNER join PFTenantUser as tUser on (emp.UserId) = (tUser.UserId) ";
        sql += "Left outer join PFUserentityLink As link on (emp.EmployeeId) = (link.EntityId) and ";
        sql += "(link.createdBy) = ('" + loggedInUserId + "') and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
        sql += "left outer join EDEmployeeStatus status on ";
        sql += "(status.EmployeeStatusId) in (select (EDEmployeeStatus.EmployeeStatusId) from EDEmployeeStatus ";
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and ";
        sql += "datetime(EDEmployeeStatus.PeriodEndDate)>= datetime('now') and (EDEmployeeStatus.EmployeeId) = (emp.EmployeeId) Order By DateTime(ModifiedDate) Desc limit 1) ";
        sql += " LEFT Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(status.StatusConfigId) ";
        return sql;
    }

    /**
     * Generate sql string with minimum required fields for Employee.
     *
     * @return string sql
     */
    private String getSearchEmployeeSQL() {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        String loggedInUserId = EwpSession.getSharedInstance().getUserId().toString();
        String sql = "";
        sql += "select distinct emp.rowid as _id, emp.EmployeeId,emp.FirstName,emp.LocationId,emp.DepartmentId, emp.LastName,emp.JobTitle,(link.UserEntityLinkId!='null') As Following,tUser.Picture,tUser.LoginEmail,";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor ";
        sql += " from EDEmployee emp ";
        sql += "INNER join PFTenantUser as tUser on emp.UserId = tUser.UserId ";
        sql += "Left outer join PFUserentityLink As link on emp.EmployeeId = link.EntityId and ";
        sql += "link.createdBy = '" + loggedInUserId + "' and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
        sql += "left outer join EDEmployeeStatus status on ";
        sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId from EDEmployeeStatus ";
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and ";
        sql += "datetime(EDEmployeeStatus.PeriodEndDate)>= datetime('now') and EDEmployeeStatus.EmployeeId = emp.EmployeeId Order By DateTime(ModifiedDate) Desc limit 1) ";
        sql += " LEFT Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(status.StatusConfigId) ";
        return sql;
    }

    /**
     * It is used to check user existence with same email id.
     *
     * @param email
     * @param tenantId
     * @param employeeId if employee exists
     * @return boolean
     * @throws EwpException
     */
    public boolean employeeExists(String email, UUID tenantId, UUID employeeId) throws EwpException {
        String mySql = getSQL() + " WHERE LOWER(emp.TenantId)=LOWER('" + tenantId.toString() + "') and LOWER(emp.EmployeeId)= LOWER('" + employeeId.toString() + "') ";
        boolean employeeExists = SqlUtils.recordExists(mySql, DatabaseOps.defaultDatabase());
        return employeeExists;
    }

    /**
     * It is used to get current absent employee list.
     *
     * @param tenantId
     * @return List<Employee>
     */
    public List<Employee> getCurrentDateEmployeeAbsenceList(UUID tenantId) throws EwpException {
        /// Generating the Employee SQL Statement to get the Today absent employee list.
        /// Each employee may have muliple status for a days. But It will give distinct employee list.
        String sql = "SELECT Distinct emp.*, ";
        sql += "(select  EmpStatus from EDEmployeeStatus es1 where  EmployeeId = es.EmployeeId and ";
        sql += "(datetime(es1.START_DATE) <= datetime('now') AND datetime(es1.EndDate) >= datetime('now')) ";
        sql += " ORDER BY DateTime(es1.CreatedDate) DESC limit 1) As Status ";
        sql += "FROM EDEmployeeStatus As es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) where ";
        sql += "datetime(es.START_DATE) <= datetime('now') AND   datetime(es.EndDate) >= datetime('now') ";
        sql += "And emp.TenantId='" + tenantId.toString() + "'";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param tenantId
     * @param startDate
     * @param endDate
     * @return List<Employee>
     * @throws EwpException
     */
    public List<Employee> getEmployeeListCreatedBetweenGivenDateAndTenantId(UUID tenantId, Date startDate, Date endDate) throws EwpException {
        /// Generating the Employee SQL Statement to get the Today absent employee list.
        /// Each employee may have muliple status for a days. But It will give distinct employee list.
        String sDate = Utils.stringFromDate(startDate);
        String eDate = Utils.stringFromDate(endDate);
        String sql = "SELECT emp.*, ";
        sql += "(select  EmpStatus from EDEmployeeStatus es1 where  EmployeeId = es.EmployeeId and ";
        sql += "(datetime(es1.START_DATE) <= datetime('" + sDate + ")' AND   datetime(es1.EndDate) >= datetime('" + eDate + "')) ";
        sql += " ORDER BY DateTime(es1.CreatedDate) DESC limit 1) As Status ";
        sql += "FROM EDEmployeeStatus As es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) where ";
        sql += "datetime(es.START_DATE) <= datetime('" + sDate + "') AND   datetime(es.EndDate) >= datetime('" + eDate + "') ";
        sql += "And emp.TenantId='" + tenantId.toString() + "'";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param tenantId
     * @param startDate
     * @param endDate
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeListCreatedBetweenGivenDateAndTenantIdAsResultSet(UUID tenantId, Date startDate, Date endDate) throws EwpException {
        /// Generating the Employee SQL Statement to get the Today absent employee list.
        /// Each employee may have muliple status for a days. But It will give distinct employee list.
        String sDate = Utils.getUTCDateTimeAsString(startDate);
        String eDate = Utils.getUTCDateTimeAsString(endDate);
        String sql = "  SELECT * FROM EDEmployee AS e ";
        sql += " INNER JOIN EDEmployeeStatus eds ON e.EmployeeId=eds.EmployeeId ";
        sql += " WHERE e.TenantId=='" + tenantId.toString() + "' AND datetime(eds.START_DATE)>= datetime('" + sDate + "') OR datetime(eds.EndDate) <= datetime('" + eDate + "') ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * @return List<Employee>
     * @throws EwpException
     */
    public List<Employee> getWeekEmailSubscribedEmployeeList() throws EwpException {
        String sql = " SELECT e.* FROM EDEmployee As e ";
        sql += " INNER JOIN PFCyclicNotificationLinking AS cnl ON LOWER(e.EmployeeId)=LOWER(cnl.SourceEntityId) ";
        sql += " WHERE cnl.SourceEntityType=@SourceEntityType ";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return Cursor
     */
    public Cursor getEmployeeEntityAsResultSet(Object id) throws EwpException {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        String sql = "";
        String tenantUserId = EwpSession.getSharedInstance().getUserId().toString();
        sql = "select emp.*,loc.Name As LocationName,dept.Name as DepartmentName,tUser.Picture,tUser.LoginEmail,tUser.IANATimeZoneId,";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor, ";
        sql += "empRep.FullName as ReportToName, (link.UserEntityLinkId != 'null') As Following from ";
        sql += "EDEmployee emp INNER join PFTenantUser as tUser on (emp.UserId) =(tUser.UserId) ";
        sql += "left outer join EDLocation as loc on (emp.LocationId) =(loc.LocationId) ";
        sql += "Left Join EDEmployee as empRep on LOWER(emp.ReportsTo) = LOWER(empRep.EmployeeId) ";
        sql += "left outer join EDDepartment as dept on (emp.DepartmentId) =(dept.DepartmentId) ";
        sql += "Left outer join PFUserentityLink As link on ";
        sql += "(emp.EmployeeId) = (link.EntityId) and ";
        sql += "(link.createdBy)= ('" + tenantUserId.toString() + "') ";
        sql += "and link.LinkType= '" + LinkType.FAVOURITE.getId() + "' ";
        sql += "left outer join EDEmployeeStatus status on ";
        sql += "(status.EmployeeStatusId) in (select (EDEmployeeStatus.EmployeeStatusId) from EDEmployeeStatus ";
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
        sql += "datetime('now') and (EDEmployeeStatus.EmployeeId) = (emp.EmployeeId) ";
        sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
        sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
        sql = sql + " where (emp.EmployeeId)= ('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEmployeeViewEntity(sql);
    }

    /**
     * It execute SQL query and will return an ViewEmployee if found, otherwise return nil
     *
     * @param sql
     * @return Cursor
     */
    private Cursor executeSqlAndGetEmployeeViewEntity(String sql) throws EwpException {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor cursor = db.executeQuery(sql, null);
        if (cursor == null) {
            throw new EwpException("Cursor is null");
        }
        return cursor;
    }


    /**
     * It is used to get an employee list by department.
     * For example: To get employee list as location wise.
     *
     * @param tenantId id of tenant.
     * @return Cursor for columns
     */
    public Cursor getSelectEmployeeListAsResultSet(UUID tenantId, UUID tenantUserId, String searchByChar) throws EwpException {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        /*var sql = "Select emp.*, dept.Name as DepartmentName, "
        sql += "(select  EmpStatus from EDEmployeeStatus where  EmployeeId = emp.EmployeeId and "
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) "
        sql += ") Order By DateTime(CreatedDate) Desc limit 1) As Status "
        sql += "from EDEmployee as emp "
        sql += "INNER Join EDDepartment as dept ON LOWER(emp.DepartmentId) = LOWER(dept.DepartmentId) "

        sql = sql + " where LOWER(emp.TenantId) = LOWER('\(tenantId.stringValue())')  ORDER By dept.Name, emp.FullName "*/
        String sql = "";
        sql += "select distinct emp.rowid as _id, emp.EmployeeId,emp.FirstName,emp.LocationId,emp.DepartmentId, emp.LastName,emp.JobTitle,(link.UserEntityLinkId!='null') As Following,tUser.Picture,tUser.LoginEmail,";
        sql += "0 As Status, '' as StatusText, '' as StatusColor ";
        sql += " from EDEmployee emp ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) =LOWER(tUser.UserId) ";
        sql += "Left outer join PFUserentityLink As link on LOWER(emp.EmployeeId) = LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy) = LOWER('" + tenantUserId + "') and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";

        if (searchByChar != null && !"".equals(searchByChar)) {
            sql += " Where LOWER(emp.FullName) Like LOWER('%" + searchByChar + "%') OR  LOWER(dept.Name) Like LOWER('%" + searchByChar + "%') ";
        }
        sql += "ORDER By emp.FullName COLLATE NOCASE ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    /**
     * It is used to get an employee list by department.
     * For example: To get employee list as location wise.
     *
     * @param tenantId id of tenant.
     * @return Cursor for columns
     */
    public Cursor getDepartmentEmployeeListAsResultSet(UUID tenantId, UUID tenantUserId, UUID departmentId, String searchByChar) throws EwpException {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        /*var sql = "Select emp.*, dept.Name as DepartmentName, "
        sql += "(select  EmpStatus from EDEmployeeStatus where  EmployeeId = emp.EmployeeId and "
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) "
        sql += ") Order By DateTime(CreatedDate) Desc limit 1) As Status "
        sql += "from EDEmployee as emp "
        sql += "INNER Join EDDepartment as dept ON LOWER(emp.DepartmentId) = LOWER(dept.DepartmentId) "

        sql = sql + " where LOWER(emp.TenantId) = LOWER('\(tenantId.stringValue())')  ORDER By dept.Name, emp.FullName "*/

        String sql = "";
        if (departmentId != null) {
            sql = "select distinct emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle, dept.Name as Department, dept.DepartmentId, dept.Picture as DepartmentPicture,";
            sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor, ";
            sql += "(link.UserEntityLinkId != 'null') As Following,tUser.Picture,tUser.LoginEmail from ";
            sql += "EDEmployee emp INNER join PFTenantUser as tUser on emp.UserId =tUser.UserId ";
            sql += "INNER join EDDepartment as dept on ";
            sql += "emp.DepartmentId = dept.DepartmentId ";
            sql += "Left outer join PFUserentityLink As link on emp.EmployeeId=link.EntityId and ";
            sql += "link.createdBy = '" + tenantUserId.toString() + "' and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
            sql += "left outer join EDEmployeeStatus status on ";
            sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId from EDEmployeeStatus ";
            sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
            sql += "datetime('now') and EDEmployeeStatus.EmployeeId = emp.EmployeeId ";
            sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
            sql += "where emp.DepartmentId = '" + departmentId.toString() + "' ";
        } else {
            sql += "select distinct emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle, dept.Name as Department, dept.DepartmentId, dept.Picture as DepartmentPicture,pl.PredefineCode As Status,";
            sql += "pl.Name as StatusText,pl.RGBColor as StatusColor, (link.UserEntityLinkId != 'null') As Following,tUser.Picture,tUser.LoginEmail ";
            sql += "from EDDepartment as dept  LEFT Join EDEmployee emp on dept.DepartmentId = emp.DepartmentId ";
            sql += "Left join PFTenantUser as tUser on emp.UserId =tUser.UserId ";
            sql += "Left outer join PFUserentityLink As link on emp.EmployeeId=link.EntityId and ";
            sql += "link.createdBy = '" + tenantUserId.toString() + "' ";
            sql += "And link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' left outer join EDEmployeeStatus status on status.EmployeeStatusId in ";
            sql += "(select EDEmployeeStatus.EmployeeStatusId ";
            sql += "from EDEmployeeStatus where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and ";
            sql += "datetime(EDEmployeeStatus.PeriodEndDate)>= datetime('now') and ";
            sql += "EDEmployeeStatus.EmployeeId = emp.EmployeeId  Order By DateTime(ModifiedDate) DESC limit 1) ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
        }
        if (searchByChar != null && !"".equals(searchByChar)) {
            if (departmentId != null) {
                sql += " And (emp.FullName Like '%" + searchByChar + "%' OR  dept.Name Like '%" + searchByChar + "%') ";
            } else {
                sql += " Where (emp.FullName Like '%" + searchByChar + "%' OR  dept.Name Like '%" + searchByChar + "%') ";
            }
        }
        sql += "ORDER By dept.Name COLLATE NOCASE, emp.FullName COLLATE NOCASE ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    /**
     * It is used to get an employee list team wise.
     * /// For example: To get employee list as location wise.
     * /// :param: tenantId: It is id of tenant.
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getTeamWiseEmployeeListAsResultSet(UUID tenantId, UUID tenantUserId, UUID teamId, String searchByChar) throws EwpException {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        String sql = "";
        if (teamId != null) {
            sql = "select emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,team.Name As Team, team.TeamId,(link.UserEntityLinkId != 'null') As Following,";
            sql += "tUser.Picture, team.Picture as TeamPicture, pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor ";
            sql += " from ";
            sql += "EDEmployee emp INNER join PFTenantUser as tUser on emp.UserId = tUser.UserId ";
            sql += "Inner join EDTeamMember as member on ";
            sql += "emp.EmployeeId = member.EmployeeId Inner Join EDTeam as team ";
            sql += " ON member.TeamId = team.TeamId ";
            sql += "Left outer join PFUserentityLink As link on emp.EmployeeId = link.EntityId and ";
            sql += "link.createdBy = '" + tenantUserId.toString() + "' ";
            sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
            sql += "left outer join EDEmployeeStatus status on ";
            sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId from EDEmployeeStatus ";
            sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
            sql += "datetime('now')   and EDEmployeeStatus.EmployeeId = emp.EmployeeId ";
            sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
            sql += " where team.TeamId = '" + teamId.toString() + "' ";
        } else {
            sql += "select DISTINCT emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,team.Name As Team, team.TeamId,(link.UserEntityLinkId != 'null') As Following,tUser.Picture, team.Picture as TeamPicture, pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor  from ";
            sql += "EDTeam as team LEFT Join  EDTeamMember as member ON  member.TeamId =  team.TeamId ";
            sql += "LEFT join EDEmployee emp on emp.EmployeeId= member.EmployeeId ";
            sql += "LEFT join PFTenantUser as tUser on emp.UserId = tUser.UserId  ";
            sql += "Left outer join PFUserentityLink As link on emp.EmployeeId = link.EntityId and link.createdBy = '" + tenantUserId.toString() + "' and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "'  ";
            sql += "left outer join EDEmployeeStatus status on status.EmployeeStatusId in ";
            sql += "(select EDEmployeeStatus.EmployeeStatusId from EDEmployeeStatus where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= datetime('now')   ";
            sql += "and EDEmployeeStatus.EmployeeId = emp.EmployeeId Order By DateTime(ModifiedDate) DESC limit 1)  ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId)  ";
        }
        if (searchByChar != null && !"".equals(searchByChar)) {
            if (teamId != null) {
                sql += " And (emp.FullName Like '%" + searchByChar + "%' OR  team.Name Like '%" + searchByChar + "%') ";
            } else {
                sql += " Where emp.FullName Like '%" + searchByChar + "%' OR  team.Name Like '%" + searchByChar + "%' ";
            }
        }
        sql += "ORDER By team.Name COLLATE NOCASE, emp.FullName COLLATE NOCASE ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    /**
     * It execute SQL query and will return an ViewEmployee if found, otherwise return nil
     *
     * @param sql
     * @return Cursor
     */
    private Cursor executeSqlAndGetEmployeeResultSet(String sql) throws EwpException {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor cursor = db.executeQuery(sql, null);
        if (cursor == null) {
            List<String> message = new ArrayList<>();
            message.add("Null cursor returned on executing executeSqlAndGetEmployeeResultSet() from EmployeeData");
            throw new EwpException(new EwpException(""), EnumsForExceptions.ErrorType.DATABASE_ERROR, message, EnumsForExceptions.ErrorModule.DATA, null, 0);
        }
        return cursor;
    }


    /**
     * It is used to get an employee communication favorite list.
     * /// For example: To get employee list as location wise.
     * /// :param: tenantId: It is id of tenant.
     *
     * @param tenantId
     * @param tenantUserId
     * @return Cursor
     */
    public Cursor getLocationEmployeeListAsResultSet(UUID tenantId, UUID tenantUserId, UUID locationId, String searchByChar) throws EwpException {
        String sql = "";
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        /*var sql = "Select emp.*, loc.Name as LocationName, "
        sql += "(select  EmpStatus from EDEmployeeStatus where  EmployeeId = emp.EmployeeId and "
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) "
        sql += ") Order By DateTime(CreatedDate) Desc limit 1) As Status "
        sql += "from EDEmployee as emp "
        sql += "INNER Join EDLocation as loc ON LOWER(emp.LocationId) = LOWER(loc.LocationId) "
        sql = sql + " where LOWER(emp.TenantId) = LOWER('\(tenantId.stringValue())')  ORDER By loc.Name, emp.Name " */
        if (locationId != null) {
            sql = "select distinct emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,loc.Name As Location, loc.LocationId,loc.Picture as LocationPicture,tUser.LoginEmail,";
            sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor, ";
            sql += "(link.UserEntityLinkId != 'null') As Following, tUser.Picture from ";
            sql += "EDEmployee emp INNER join PFTenantUser as tUser on emp.UserId = tUser.UserId ";
            sql += "INNER join EDLocation as loc on emp.LocationId = loc.LocationId ";
            sql += "Left outer join PFUserentityLink As link on ";
            sql += "emp.EmployeeId = link.EntityId and ";
            sql += "link.createdBy = '" + tenantUserId.toString() + "' ";
            sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
            sql += "left outer join EDEmployeeStatus status on ";
            sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId ";
            sql += "from EDEmployeeStatus ";
            sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
            sql += "datetime('now') and EDEmployeeStatus.EmployeeId = emp.EmployeeId ";
            sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
            sql += " where emp.LocationId = '" + locationId.toString() + "' ";
        } else {
            sql = "select distinct emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,loc.Name As Location, loc.LocationId,loc.Picture as LocationPicture,tUser.LoginEmail,";
            sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor, ";
            sql += "(link.UserEntityLinkId != 'null') As Following, tUser.Picture from ";
            sql += "EDLocation as loc LEFT JOIN EDEmployee emp  on loc.LocationId = emp.LocationId ";
            sql += "LEFT join PFTenantUser as tUser on emp.UserId = tUser.UserId ";
            sql += "Left outer join PFUserentityLink As link on ";
            sql += "emp.EmployeeId = link.EntityId and ";
            sql += "link.createdBy = '" + tenantUserId.toString() + "' ";
            sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
            sql += "left outer join EDEmployeeStatus status on ";
            sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId ";
            sql += "from EDEmployeeStatus ";
            sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
            sql += "datetime('now') and EDEmployeeStatus.EmployeeId = emp.EmployeeId ";
            sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
            sql += "LEFT Join EDStatusConfig as pl ON (pl.StatusConfigId) = (status.StatusConfigId) ";
        }
        if (searchByChar != null && !"".equals(searchByChar)) {
            if (locationId != null) {
                sql += " And (LOWER(emp.FullName) Like LOWER('%" + searchByChar + "%') OR  LOWER(loc.Name) Like LOWER('%" + searchByChar + "%') ";
            } else {
                sql += " Where LOWER(emp.FullName) Like LOWER(''%" + searchByChar + "%'') OR  LOWER(loc.Name) Like LOWER('%" + searchByChar + "%') ";
            }
        }
        /*sql += "left outer join EDEmployeeStatus status on "
        sql += "LOWER(status.EmployeeStatusId) in (select LOWER(EDEmployeeStatus.EmployeeStatusId) "
        sql += "from EDEmployeeStatus "
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= "
        sql += "datetime('now') and LOWER(EDEmployeeStatus.EmployeeId) = LOWER(emp.EmployeeId) "
        sql += "Order By DateTime(ModifiedDate) limit 1) "
        sql += "LEFT Join PFPicklistItem as pl ON LOWER(pl.PickListItemId) = LOWER(status.StatusConfigId) "
        if let _locationId = locationId {
            sql += " where LOWER(emp.LocationId) = LOWER('\(_locationId.stringValue())') "
        } */

        sql += "ORDER By loc.Name COLLATE NOCASE, emp.FullName COLLATE NOCASE ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    /**
     * It is used to get an employee communication favorite list.
     * For example: To get employee favorite phone number, communicationType should be phone.
     * :param: employeeId: It is id of Employee for which we want to get employee favorite list.
     * :param: communicationType: It is type of communication.
     *
     * @param communicationType
     * @return Cursor
     */
    public Cursor getEmployeeFavoriteListAsResultSet(CommunicationType communicationType, UUID tenantUserId, String searchByChar) throws EwpException {
        /// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
         /*var sql = "Select emp.*, comm.Type as CommunicationType,comm.SubType as CommunicationSubType,comm.Value as CommunicationValue, "
        sql += "(select  EmpStatus from EDEmployeeStatus where  EmployeeId = emp.EmployeeId and "
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) "
        sql += ") Order By DateTime(CreatedDate) Desc limit 1) As Status "
        sql += "from PFUserEntityLink as user "
        sql += "INNER Join PFCommunication as comm ON LOWER(comm.CommunicationId) = LOWER(user.EntityId) "
        sql += "INNER Join EDEmployee as emp ON LOWER(emp.EmployeeId) = LOWER(comm.EntityId) "

        sql = sql + " where LOWER(user.CreatedBy) = LOWER('\(employeeId.stringValue())') and comm.Type = '\(communicationType.rawValue)' and user.LinkType = '\(LinkType.Favorite.rawValue)' ORDER By emp.FullName" */
        String sql = "";
        sql = "select distinct user.UserEntityLinkId as FavoriteId, emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,(link.UserEntityLinkId != 'null') As Following,tUser.Picture,";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor, comm.CommunicationId, ";
        sql += "comm.Type as CommunicationType,comm.SubType as CommunicationSubType,comm.Value as CommunicationValue From ";
        sql += "PFCommunication as comm Inner join PFUserEntityLink as user ON user.EntityId = comm.CommunicationId ";
        sql += "INNER join EDEmployee emp on comm.EntityId = emp.EmployeeId ";
        sql += "INNER join PFTenantUser as tUser on emp.UserId = tUser.UserId ";
        sql += "Left outer join PFUserentityLink As link on emp.EmployeeId=link.EntityId and ";
        sql += "link.createdBy = '" + tenantUserId.toString() + "' and link.LinkType= 1 ";
        sql += "left outer join EDEmployeeStatus status on ";
        sql += "status.EmployeeStatusId in (select EDEmployeeStatus.EmployeeStatusId from EDEmployeeStatus ";
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
        sql += "datetime('now') and EDEmployeeStatus.EmployeeId = emp.EmployeeId ";
        sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
        sql += "LEFT Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(status.StatusConfigId) ";
        sql += " where user.CreatedBy = '" + tenantUserId.toString() + "' and comm.Type = '" + communicationType.getId() + "' and user.LinkType = '" + LinkType.FAVOURITE.getId() + "' ";
        if (searchByChar != null && !"".equals(searchByChar)) {
            sql += " And LOWER(emp.FullName) Like LOWER('%" + searchByChar + "%') ";
        }
        sql += "ORDER By user.SortOrder ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    /**
     * It will search employee list from fullName. If search character appear at any place of full name then return those list of employees.
     * Method will return user list as! ResultSet.
     *
     * @param tenantId
     * @param searchCharInName
     * @return Cursor
     * @throws EwpException
     */
    public Cursor searchEmployeeAndGetAsResultSet(UUID tenantId, String searchCharInName) throws EwpException {
        String sql = getSearchEmployeeSQL() + " Where ";
        if (searchCharInName == null || "".equals(searchCharInName)) {
            sql += " LOWER(emp.TenantId)= LOWER('" + tenantId.toString() + "') ";
        } else {
            sql += " LOWER(emp.TenantId) =LOWER('" + tenantId.toString() + "')  And LOWER(emp.FullName) Like LOWER('" + searchCharInName + "') ";
        }
        sql += "ORDER BY LOWER(emp.FullName) ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * It is used to get an followup employee list.
     * For example: To get employee followup employee
     * :param: tenantUserId: It is id of user for which we want to get employee follower list.
     *
     * @param tenantUserId
     * @param searchByChar
     * @return
     */
    public Cursor getEmployeeFollowUpListAsResultSet(UUID tenantUserId, String searchByChar) throws EwpException {
        String sql = "";
        sql = "select distinct emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,(link.UserEntityLinkId != 'null') As Following,tUser.Picture,";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor ";
        sql += "From ";
        sql += "EDEmployee emp Inner join PFUserEntityLink as user on ";
        sql += "LOWER(user.EntityId) = LOWER(emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on (emp.UserId) =(tUser.UserId) ";
        sql += "Left outer join PFUserentityLink As link on LOWER(emp.EmployeeId)=LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy) = LOWER('" + tenantUserId.toString() + "') and link.LinkType= " + LinkType.FOLLOW_UP.getId() + " ";
        sql += "left outer join EDEmployeeStatus status on ";
        sql += "(status.EmployeeStatusId) in (select (EDEmployeeStatus.EmployeeStatusId) from EDEmployeeStatus ";
        sql += "where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= ";
        sql += "datetime('now') and (EDEmployeeStatus.EmployeeId) = (emp.EmployeeId) ";
        sql += "Order By DateTime(ModifiedDate) DESC limit 1) ";
        sql += "LEFT Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(status.StatusConfigId) ";
        sql += "where LOWER(user.CreatedBy) = LOWER('" + tenantUserId.toString() + "') and user.LinkType = " + LinkType.FOLLOW_UP.getId() + " ";
        if (searchByChar != null && !"".equals(searchByChar)) {
            sql += " And LOWER(emp.FullName) Like LOWER('%" + searchByChar + "%') ";
        }
        sql += "ORDER By emp.FullName COLLATE NOCASE";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    public Cursor getMyTeamEmployeeListAsResultSet(UUID tenantUserId) throws EwpException {
/// Generating the Employee SQL Statement to get the Employee detail. It will give the Employee detail with status.
        Employee result = getEmployeeFromUserId(tenantUserId);
        String empId = Utils.emptyUUID().toString();
        if (result != null) {
            empId = result.getEntityId().toString();
        }
        String sql = "";
        sql += "select DISTINCT emp.FirstName,emp.LastName,emp.EmployeeId,emp.JobTitle,team.Name As Team, team.TeamId,(link.UserEntityLinkId != 'null') As Following,tUser.Picture, team.Picture as TeamPicture, pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor,tUser.LoginEmail from ";
        sql += "EDTeam as team LEFT Join  EDTeamMember as member ON  (member.TeamId) =  (team.TeamId) ";
        sql += "LEFT join EDEmployee emp on (emp.EmployeeId)=(member.EmployeeId) ";
        sql += "LEFT join PFTenantUser as tUser on (emp.UserId) = (tUser.UserId)  ";
        sql += "Left outer join PFUserentityLink As link on (emp.EmployeeId) = (link.EntityId) and (link.createdBy) = ('" + tenantUserId.toString() + "') and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "'  ";
        sql += "left outer join EDEmployeeStatus status on (status.EmployeeStatusId) in ";
        sql += "(select (EDEmployeeStatus.EmployeeStatusId) from EDEmployeeStatus where datetime(EDEmployeeStatus.PeriodStartDate) <= datetime('now') and datetime(EDEmployeeStatus.PeriodEndDate)>= datetime('now')   ";
        sql += "and (EDEmployeeStatus.EmployeeId) = (emp.EmployeeId) Order By DateTime(ModifiedDate) DESC limit 1)  ";
        sql += "LEFT Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(status.StatusConfigId)  ";
        sql += "Where (team.TeamId) in ";
        sql += "( SELECT LOWER(TeamId) from EDTeamMember etm INNER Join EDEmployee as e on (etm.EmployeeId) = (e.EmployeeId) INNER JOIN  ";
        sql += "PFTenantUser user On (e.UserId) = (user.UserId) And (user.UserId) =  ('" + tenantUserId.toString() + "')   ";
        sql += "UNION  ";
        sql += "SELECT (TeamId) from EDTeam Where (Manager) =  ('" + empId + "')) ";
        sql += "ORDER By team.Name COLLATE NOCASE, emp.FullName COLLATE NOCASE  ";
        return executeSqlAndGetEmployeeResultSet(sql);
    }

    @Override
    public void setPropertiesFromResultSet(Employee entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (employeeId != null && !"".equals(employeeId))
            entity.setEntityId(UUID.fromString(employeeId));
        String fullName = cursor.getString(cursor.getColumnIndex("FullName"));
        if (fullName != null)
            entity.setFullName(fullName.replaceAll("''", "'"));
        String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
        if (firstName != null)
            entity.setFirstName(firstName.replaceAll("''", "'"));
        String middleName = cursor.getString(cursor.getColumnIndex("MiddleName"));
        if (middleName != null)
            entity.setMiddleName(middleName.replaceAll("''", "'"));
        String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
        if (lastName != null)
            entity.setLastName(lastName.replaceAll("''", "'"));
        String nickName = cursor.getString(cursor.getColumnIndex("NickName"));
        if (nickName != null)
            entity.setNickName(nickName.replaceAll("''", "'"));
        int status = cursor.getInt(cursor.getColumnIndex("Status"));
        entity.setEmployeeStatus(EmployeeStatusEnum.values()[status]);
        String email = cursor.getString(cursor.getColumnIndex("LoginEmail"));
        entity.setLoginEmail(email);
        String jobTitle = cursor.getString(cursor.getColumnIndex("JobTitle"));
        entity.setJobTitle(jobTitle);
        String reportTo = cursor.getString(cursor.getColumnIndex("ReportsTo"));
        if (reportTo != null && !"".equals(reportTo)) {
            entity.setReportTo(reportTo);
        }
        String locationId = cursor.getString(cursor.getColumnIndex("LocationId"));
        if (locationId != null && !"".equals(locationId) && !"null".equalsIgnoreCase(locationId)) {
            entity.setLocationId(UUID.fromString(locationId));
        }
        String departmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
        if (departmentId != null && !"".equals(departmentId) && !"null".equalsIgnoreCase(departmentId)) {
            entity.setDepartmentId(UUID.fromString(departmentId));
        }
        String localTimeZone = cursor.getString(cursor.getColumnIndex("LocalTimeZone"));
        entity.setLocalTimeZone(localTimeZone);
        String startDate = cursor.getString(cursor.getColumnIndex("StartDate"));
        if (startDate != null && !"".equals(startDate)) {
            entity.setStartDate(Utils.dateFromString(startDate, true, true));
        }
        String following = cursor.getString(cursor.getColumnIndex("Following"));
        entity.setFollowing("1".equals(following));
        String birthDay = cursor.getString(cursor.getColumnIndex("Birthday"));
        if (birthDay != null && !"".equals(birthDay)) {
            entity.setBirthDay(Utils.dateFromString(birthDay, true, true));
        }
        String tenantUserId = cursor.getString(cursor.getColumnIndex("UserId"));
        if (tenantUserId != null && !"".equals(tenantUserId)) {
            entity.setTenantUserId(UUID.fromString(tenantUserId));
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
        String picture = cursor.getString(cursor.getColumnIndex("Picture"));
        if (picture != null && !"".equals(picture) && !"null".equalsIgnoreCase(picture)) {
            entity.setPicture(picture);
        }
        entity.setDirty(false);
    }

    /**
     * @param employeeId
     * @return
     * @throws EwpException
     */
    public Boolean isEmployeeReferenceExist(UUID employeeId) throws EwpException {
        String sql = "SELECT Manager from EDDepartment where LOWER(Manager) = LOWER('" + employeeId.toString() + "')  ";
        sql += "UNION select Manager from EDLocation where LOWER(Manager) = LOWER('" + employeeId.toString() + "')  ";
        sql += "UNION select Manager from EDTeam where LOWER(Manager) = LOWER('" + employeeId.toString() + "')  ";
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor cursor = db.executeQuery(sql, null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;

    }


    public void updateEmpPicturetoNull() {
        String sql = "update PFTenantUser set Picture = NULL";
        DatabaseOps db = DatabaseOps.defaultDatabase();
        db.executeStatements(sql);
    }

}
