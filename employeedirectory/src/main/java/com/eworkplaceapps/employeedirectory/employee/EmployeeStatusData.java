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
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.LinkType;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.EmployeeDayStatusEnum.ALL_DAY_TODAY;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeDayStatusEnum.ALL_DAY_TOMORROW;

/**
 *
 */
public class EmployeeStatusData extends BaseData<EmployeeStatus> {
    @Override
    public EmployeeStatus createEntity() {
        return new EmployeeStatus();
    }

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_1 = "yyyy-MM-dd";

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return EmployeeStatus
     */
    @Override
    public EmployeeStatus getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(es.EmployeeStatusId) = LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the EmployeeStatus Entity from database.
     * /// Return Collection of EmployeeStatus Entity.
     *
     * @return List<EmployeeStatus>
     */
    @Override
    public List<EmployeeStatus> getList() throws EwpException {
        return executeSqlAndGetEntityList(getSQL());
    }


    /**
     * Get EmployeeStatus Entity that matches the id and return result as! a ResultSet.
     *
     * @param id
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL() + " where EmployeeStatusId = '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all EmployeeStatus Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        return executeSqlAndGetResultSet(getSQL());
    }

    /**
     * Delete EmployeeStatus entity.
     *
     * @param entity
     */
    @Override
    public void delete(EmployeeStatus entity) throws EwpException {
        super.deleteRows("EDEmployeeStatus", "LOWER(EmployeeStatusId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(EmployeeStatus entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EmployeeStatusId", entity.getEntityId().toString());
        values.put("Description", entity.getDescription());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("PeriodStartDate", Utils.dateAsStringWithoutUTC(entity.getStartDate()));
        values.put("PeriodEndDate", Utils.dateAsStringWithoutUTC(entity.getEndDate()));
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("StatusConfigId", entity.getEmpStatusId().toString());
        values.put("SystemPeriod", entity.isSystemPeriod() ? "1" : "0");
        values.put("AllDay", entity.isAllDays());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("TenantId", entity.getTenantId().toString());
        long id = super.insert("EDEmployeeStatus", values);
        return id;
    }

    @Override
    public void update(EmployeeStatus entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Description", entity.getDescription());
        values.put("EmployeeId", entity.getEmployeeId().toString());
        values.put("PeriodStartDate", Utils.dateAsStringWithoutUTC(entity.getStartDate()));
        values.put("PeriodEndDate", Utils.dateAsStringWithoutUTC(entity.getEndDate()));
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        values.put("StatusConfigId", entity.getEmpStatusId().toString());
        values.put("SystemPeriod", entity.isSystemPeriod() ? "1" : "0");
        values.put("AllDay", entity.isAllDays());
        values.put("TenantId", entity.getTenantId().toString());
        super.update("EDEmployeeStatus", values, "LOWER(EmployeeStatusId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    /**
     * Method is used to get user list as! ResultSet from loginemail
     *
     * @param tenantId
     * @return List<EmployeeStatus>
     */
    public List<EmployeeStatus> getEmployeeStatusListByTenantId(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " TenantId='" + tenantId.toString() + "'  ";
        sql += sql + " Order By FullName, date(es.StartDate)";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Generate sql string with minimum required fields for EmployeeStatus.
     *
     * @return string sql
     */
    private String getSQL() {

        String sql = " SELECT es.*, emp.FullName AS FullName, tUser.Picture AS EmployeePicture, emp.UserId As UserId, '' As GroupByStatus,  ";
        sql += "pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor ";
        sql += "FROM EDEmployeeStatus es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) ";
        sql += "INNER Join EDStatusConfig as pl ON LOWER(pl.StatusConfigId) = LOWER(es.StatusConfigId) ";

    /*
        *
             var sql = " SELECT es.*, emp.FullName AS FullName, tUser.Picture AS EmployeePicture, emp.UserId As UserId, '' As GroupByStatus, "
        sql += "pl.PredefineCode As Status, pl.Name as StatusText "
        sql += "FROM EDEmployeeStatus es INNER JOIN EDEmployee Aemp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) "
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) "
        sql += "INNER Join PFPicklistItem as pl ON LOWER(pl.StatusConfigId) = LOWER(es.StatusConfigId)"
        * */
        return sql;
    }

    /**
     * It is used to get the employee status list on given date.
     * /// employeeId is nilable, because method can return list of employee status on given date.
     *
     * @param date
     * @param employeeId
     * @return Cursor
     */
    public Cursor getEmployeeStatusListByDateAsResultSet(Date date, UUID employeeId) throws EwpException {
        String sql = getSQL();
        if (employeeId != null) {
            sql += " es.EmployeeId = '" + employeeId.toString() + "' ";
            sql += " AND (StartDate = '" + date + "' OR EndDate = '" + date + "')";
        } else {
            String sDate = Utils.stringFromDate(date);
            sql += "StartDate = '" + date + "' OR EndDate = '" + date + "'";
        }
        sql += SqlUtils.buildSortClause(sql, "FirstName", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /// class name :  EmployeeStatusData
    /// It is used to get logged in user status from current datetime.
    public Cursor getMyStatusListFromCurrentDateAsResultSet(UUID loggedinUserId, String searchValue) throws EwpException {
        String sql = getCurrentDateSql(loggedinUserId);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * It is used to get the employee status list from current datetime.
     *
     * @param tenantId
     */
    public Cursor getTodayEmployeeStatusListInGroup(UUID tenantId, String searchValue) throws EwpException {
        Tuple.Tuple2<String, String, String> today = getTime(EmployeeDayStatusEnum.ALL_DAY_TODAY);

       // Tuple.Tuple2<String, String, String> tomorrow = getTime(EmployeeDayStatusEnum.ALL_DAY_TOMORROW);
        //Tuple.Tuple2<String, String, String> laterDate = getTime(null);
        Date d = new Date();
        String date = Utils.getUTCDateTimeAsString(d);
        String localStartDate = date;//today.getT1();
        String localEndDate = date;//today.getT2();
        String loggedInUserId = EwpSession.getSharedInstance().getUserId().toString();
        String sql = "SELECT EDS.*,emp.FirstName, emp.LastName, tUser.Picture,tUser.LoginEmail,JobTitle, trim(emp.firstName) || ' ' || trim(emp.lastName) AS FullName, (link.UserEntityLinkId!='null') As Following, ";
        sql += "emp.UserId As UserId, pl.PredefineCode As Status, pl.Name as StatusText,pl.RGBColor as StatusColor ";// StatusText
        sql += "FROM EDEmployeeStatus EDS INNER JOIN EDEmployee AS emp ON LOWER(EDS.EmployeeId) = LOWER(emp.EmployeeId) ";
        sql += "INNER Join EDStatusConfig As pl ON LOWER(pl.StatusConfigId) = LOWER(EDS.StatusConfigId) ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) ";
        sql += "Left outer join PFUserentityLink As link on LOWER(emp.EmployeeId) = LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy) = LOWER('" + loggedInUserId + "') and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
        sql += "WHERE ( (datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) OR ";
        sql += "(datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) OR ";
        sql += "((datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) AND (datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "'))) OR ";
        sql += "(datetime(EDS.PeriodStartDate)<=datetime('" + localStartDate + "') AND datetime(EDS.PeriodEndDate)>=datetime('" + localEndDate + "')) )";
        if (searchValue != null && !"".equals(searchValue)) {
            sql += " And (LOWER(emp.FullName) Like LOWER('%" + searchValue + "%') OR  LOWER(pl.Name) Like LOWER('%" + searchValue + "%')) ";
        }
        sql += "ORDER BY pl.PredefineCode";
        return executeSqlAndGetResultSet(sql);
    }

    /// It is used to get the employee status list from current datetime.
    public List<EmployeeStatus> getEmployeeStatusListFromCurrentDateAsEmployeeStatusList(UUID tenantId) throws EwpException {
        String sql = getSQL();
        sql += " where (datetime(es.StartDate) >= datetime('now')) OR (datetime(es.StartDate) <= datetime('now') AND";
        sql += " Datetime(es.EndDate) >= datetime('now')) And LOWER(es.TenantId) = LOWER('" + tenantId.toString() + "') ";
        sql += " ORDER BY DATETIME(es.StartDate) ";

        ///var today = getTime(EmployeeDayStatusEnum.AllDayToday)


        //        var todaySql = " SELECT es.*, emp.FullName AS FullName, emp.Picture AS EmployeePicture, emp.UserId As UserId, 'Today' As GroupByStatus "
        //        todaySql += "FROM EDEmployeeStatus es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) "
        //        todaySql += " where (datetime(es.StartDate) >= datetime('\(today.startDate)') And "
        //        todaySql += " datetime('\(today.endDate)') >= Datetime(es.EndDate)) And LOWER(es.TenantId) = LOWER('\(tenantId.stringValue())') "
        //        todaySql += " "
        //
        //        let tomorrow = getTime(EmployeeDayStatusEnum.AllDayTomorrow)
        //        var tomorrowSql = " SELECT es.*, emp.FullName AS FullName, emp.Picture AS EmployeePicture, emp.UserId As UserId, 'Tomorrow' As GroupByStatus "
        //        tomorrowSql += "FROM EDEmployeeStatus es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) "
        //
        //        tomorrowSql += " where (datetime(es.StartDate) <= datetime('\(tomorrow.startDate)') And "
        //        tomorrowSql += " datetime('\(tomorrow.endDate)') <= Datetime(es.EndDate)) And LOWER(es.TenantId) = LOWER('\(tenantId.stringValue())') "
        //        tomorrowSql += "  "
        //
        //        let laterDate = getTime(nil)
        //        var laterSql = " SELECT es.*, emp.FullName AS FullName, emp.Picture AS EmployeePicture, emp.UserId As UserId, 'Later' As GroupByStatus "
        //        laterSql += "FROM EDEmployeeStatus es INNER JOIN EDEmployee AS emp ON LOWER(es.EmployeeId) = LOWER(emp.EmployeeId) "
        //
        //        laterSql += " where (datetime(es.StartDate) >= datetime('\(laterDate.startDate)') And "
        //        laterSql += " Datetime(es.EndDate) >= datetime('now')) And LOWER(es.TenantId) = LOWER('\(tenantId.stringValue())') "
        //        laterSql += " "
        //
        //        sql = "Select * from (" + todaySql + " UNION " + tomorrowSql + " UNION " + laterSql + ") As es1 order By es1.StartDate"

        sql = getCurrentDateSql(null);
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * It is used to get logged in user status from current datetime.
     *
     * @param loggedinUserId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getMyStatusListFromCurrentDateAsResultSet(UUID loggedinUserId) throws EwpException {
        String sql = getLoggedInUserStatusList("");
        return executeSqlAndGetResultSet(sql);
    }


    /**
     * It is used to get current date sql.
     *
     * @return sql
     * @throws EwpException
     */
    private String getCurrentDateSql(UUID loggedinUserId) throws EwpException {
        Tuple.Tuple2<String, String, String> today = getTime(ALL_DAY_TODAY);
        Tuple.Tuple2<String, String, String> tomorrow = getTime(ALL_DAY_TOMORROW);
        Tuple.Tuple2<String, String, String> laterDate = getTime(null);
        String localStartDate = today.getT1();
        String localEndDate = today.getT2();
        String localTomStartDate = tomorrow.getT1();
        String localTomEndDate = tomorrow.getT2();
        String localLaterStartDate = laterDate.getT1();
        String localLaterEndDate = laterDate.getT2();
        UUID tenantUserId = EwpSession.getSharedInstance().getUserId();
        String sql = " SELECT EDS.*,emp.FirstName, emp.LastName,emp.JobTitle, tUser.Picture, emp.UserId As UserId, 'Today' As GroupByStatus, 1 As FirstGroup, ";
        sql += "pl.PredefineCode as Status, pl.Name as StatusText,pl.StatusConfigId as StatusItemId,";
        sql += "(link.UserEntityLinkId != 'null') As Following,EDS.PeriodStartDate ";
        sql += "FROM EDEmployeeStatus EDS INNER JOIN EDEmployee AS emp ON LOWER(EDS.EmployeeId) = LOWER(emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) ";
        sql += "INNER Join PFPicklistItem As pl ON LOWER(pl.StatusConfigId) = LOWER(EDS.StatusConfigId) ";
        // Join to get following
        sql += "Left outer join PFUserentityLink As link on ";
        sql += "LOWER(EDS.EmployeeId) = LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy)= LOWER('" + tenantUserId.toString() + "') ";
        sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
        sql += "WHERE ((datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) OR ";
        sql += "(datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) OR ";
        sql += "((datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "')) AND (datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localStartDate + "') AND datetime('" + localEndDate + "'))) OR ";
        sql += "(datetime(EDS.PeriodStartDate)<=datetime('" + localStartDate + "') AND datetime(EDS.PeriodEndDate)>=datetime('" + localEndDate + "')) ) ";

        if (loggedinUserId != null) {
            sql += "AND LOWER(emp.UserId) = LOWER('" + loggedinUserId.toString() + "') ";
        }
        sql += "UNION ";
        sql += " SELECT EDS.*, emp.FirstName,emp.LastName,emp.JobTitle, tUser.Picture, emp.UserId As UserId, 'Tomorrow' As GroupByStatus, 2 As FirstGroup, ";
        sql += "pl.PredefineCode as Status, pl.Name as StatusText,pl.StatusConfigId as StatusItemId,";
        sql += "(link.UserEntityLinkId != 'null') As Following,EDS.PeriodStartDate ";
        sql += "FROM EDEmployeeStatus EDS INNER JOIN EDEmployee AS emp ON LOWER(EDS.EmployeeId) = LOWER(emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) ";
        sql += "INNER Join PFPicklistItem As pl ON LOWER(pl.StatusConfigId) = LOWER(EDS.StatusConfigId) ";
        // Join to get following
        sql += "Left outer join PFUserentityLink As link on ";
        sql += "LOWER(EDS.EmployeeId) = LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy)= LOWER('" + tenantUserId.toString() + "') ";
        sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";

        sql += "WHERE ( (datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localTomStartDate + "') AND datetime('" + localTomEndDate + "')) OR ";
        sql += "(datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localTomStartDate + "') AND datetime('" + localTomEndDate + "')) OR ";
        sql += "((datetime(EDS.PeriodStartDate) BETWEEN datetime('" + localTomStartDate + "') AND datetime('" + localTomEndDate + "')) AND (datetime(EDS.PeriodEndDate) BETWEEN datetime('" + localTomStartDate + "') AND datetime('" + localTomEndDate + "')))     ";
        sql += "OR (datetime(EDS.PeriodStartDate)<=datetime('" + localTomStartDate + "') AND datetime(EDS.PeriodEndDate)>=datetime('" + localTomEndDate + "')) ) ";

        if (loggedinUserId != null) {
            sql += "AND LOWER(emp.UserId) =LOWER('" + loggedinUserId.toString() + "') ";
        }
        sql += "UNION ";
        sql += "SELECT EDS.*, emp.FirstName,emp.LastName,emp.JobTitle, tUser.Picture, emp.UserId As UserId, 'Future' As GroupByStatus, 3 As FirstGroup,   ";
        sql += "pl.PredefineCode as Status, pl.Name as StatusText,pl.StatusConfigId as StatusItemId,";
        sql += "(link.UserEntityLinkId != 'null') As Following, EDS.PeriodStartDate ";
        sql += "FROM EDEmployeeStatus EDS INNER JOIN EDEmployee AS emp ON LOWER(EDS.EmployeeId) = LOWER(emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on LOWER(emp.UserId) = LOWER(tUser.UserId) ";
        sql += "INNER Join PFPicklistItem As pl ON LOWER(pl.StatusConfigId) = LOWER(EDS.StatusConfigId) ";
        // Join to get following
        sql += "Left outer join PFUserentityLink As link on ";
        sql += "LOWER(EDS.EmployeeId) = LOWER(link.EntityId) and ";
        sql += "LOWER(link.createdBy)= LOWER('" + tenantUserId.toString() + "') ";
        sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";
        sql += "WHERE ( (datetime(EDS.PeriodStartDate)>=datetime('" + localLaterStartDate + "') OR datetime(EDS.PeriodEndDate)>=datetime('" + localLaterStartDate + "'))) ";
        if (loggedinUserId != null) {
            sql += "AND LOWER(emp.UserId) =LOWER('" + loggedinUserId.toString() + "') ";
        }
        sql = "Select * from (" + sql + ") As es1 order By LOWER(FirstGroup),datetime(StartDate)";
        return sql;
    }

    /**
     * It is used to get UTC Time as! per diffrent status.
     *
     * @param empStatusEnum
     * @return Tuple.Tuple2<String,String,String>
     * @throws EwpException
     */
    private Tuple.Tuple2<String, String, String> getTime(EmployeeDayStatusEnum empStatusEnum) throws EwpException {
        Date date = new Date();
        String strDate = time(date, FORMAT_1);//Utils.stringFromDateWithLocalTime(date, useHourMinSec: false)!
        String s = "";
        String e = "";
        if (empStatusEnum != null) {
            switch (empStatusEnum) {
                case THIS_MORNING:
                    s = strDate + "T00:00:00";
                    e = strDate + "T11:59:00";
                    break;
                case THIS_AFTERNOON:
                    s = strDate + "T12:00:00";
                    e = strDate + "T23:59:00";
                    break;
                case ALL_DAY_TODAY:
                    s = strDate + "T00:00:00";
                    e = strDate + "T23:59:00";
                    break;
                case ALL_DAY_TOMORROW:
                    date = addDaysToDate(new Date(), 1);
                    strDate = time(date, FORMAT_1);
                    s = strDate + "T00:00:00";
                    e = strDate + "T23:59:00";
                    break;
                default:
                    /// It is used to get date of day after tomorrow.
                    date = addDaysToDate(new Date(), 2);
                    /// These are not to be use because we are working with localization.
                    strDate = time(date, FORMAT_1);
                    s = strDate + "T00:00:00";
                    e = strDate + "T23:59:00";
            }
        } else {
            date = addDaysToDate(new Date(), 2);
            strDate = time(date, FORMAT_1);
            s = strDate + "T00:00:00";
            e = strDate + "T23:59:00";
        }
        Date startDate = dateFromString(s, FORMAT);
        Date endDate = dateFromString(e, FORMAT);
        if (!"".equals(s)) {
            s = Utils.getUTCDateTimeAsString(startDate);//time(startDate, FORMAT);
            e = Utils.getUTCDateTimeAsString(endDate);////time(endDate, FORMAT);
            return new Tuple.Tuple2<String, String, String>(s, e, "");
        }
        return new Tuple.Tuple2<String, String, String>("", "", "");
    }


    private String time(Date d, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(d);
        return utcTime;
    }

    public static Date dateFromString(String StrDate, String format) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }
    public String getLoggedInUserStatusList( String searchValue) throws EwpException {
        Tuple.Tuple2<String, String, String> laterDate = getTime(EmployeeDayStatusEnum.ALL_DAY_TODAY);
        String localLaterStartDate = laterDate.getT1();
        String localLaterEndDate = laterDate.getT2();
        UUID tenantUserId = EwpSession.getSharedInstance().getUserId();
        String sql = "";
        sql += "SELECT EDS.*, trim(emp.firstName) || ' ' || trim(emp.lastName) AS FullName, tUser.Picture,tUser.LoginEmail, emp.UserId As UserId,";
        sql += "'Today' As GroupByStatus, 3 As FirstGroup, emp.firstName, emp.lastName, jobtitle,";
        sql += "sc.PredefineCode As Status, sc.Name as StatusText,sc.RGBColor as StatusColor,sc.StatusConfigId as StatusItemId,";
        sql += "(link.UserEntityLinkId != 'null') As Following ";
        sql += "FROM EDEmployeeStatus EDS INNER JOIN EDEmployee AS emp ON (EDS.EmployeeId) = (emp.EmployeeId) ";
        sql += "INNER join PFTenantUser as tUser on (emp.UserId) = (tUser.UserId) ";
        sql += " INNER Join EDStatusConfig as sc ON (sc.StatusConfigId) = (EDS.StatusConfigId) ";
        // Join to get following
        sql += "Left outer join PFUserentityLink As link on ";
        sql += "(EDS.EmployeeId) = (link.EntityId) and ";
        sql += "(link.createdBy)= ('" + tenantUserId.toString() + "') ";
        sql += "and link.LinkType= '" + LinkType.FOLLOW_UP.getId() + "' ";

        sql += "WHERE (datetime(EDS.PeriodStartDate)>=datetime('" + localLaterStartDate.toString() + "') OR datetime(EDS.PeriodEndDate)>=datetime('" + localLaterStartDate.toString() + "')) ";
        sql += "AND (emp.UserId) =('" + tenantUserId.toString() + "') ";

        if ( searchValue!=null && "".equalsIgnoreCase(searchValue)) {
            sql += " And (LOWER(emp.FullName) Like LOWER('%" + searchValue + "%') OR  LOWER(sc.Name) Like LOWER('%" + searchValue + "%')) ";
        }

        sql += "Order By datetime(EDS.PeriodStartDate) asc, datetime(EDS.ModifiedDate) desc";

        return sql;
    }
    /**
     * It is used to current status of employee from employeeid.
     *
     * @param employeeId
     * @return EmployeeStatus
     * @throws EwpException
     */
    public EmployeeStatus getEmployeeCurrentStatusFromEmployeeId(UUID employeeId) throws EwpException {
        String sql = "select  * from EDEmployeeStatus where  EmployeeId = '" + employeeId.toString() + "' and ";
        sql += "((datetime(StartDate) <= datetime('now') AND   datetime(EndDate) >= datetime('now')) ";
        sql += ") Order By DateTime(CreatedDate) Desc limit 1";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public void setPropertiesFromResultSet(EmployeeStatus entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String employeeStatusGroupMemberId = cursor.getString(cursor.getColumnIndex("EmployeeStatusId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }
        String employeeStatusId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (employeeStatusId != null && !"".equals(employeeStatusId)) {
            entity.setEmployeeId(UUID.fromString(employeeStatusId));
        }
        String userId = cursor.getString(cursor.getColumnIndex("UserId"));
        if (userId != null && !"".equals(userId)) {
            entity.setUserId(UUID.fromString(userId));
        }
        String fullName = cursor.getString(cursor.getColumnIndex("FullName"));
        entity.setEmployeeFullName(fullName);
        String logo = cursor.getString(cursor.getColumnIndex("EmployeePicture"));
        entity.setEmployeePicture(logo);
        String empStatus = cursor.getString(cursor.getColumnIndex("StatusConfigId"));
        entity.setEmpStatusId(UUID.fromString(empStatus));
        int status = cursor.getInt(cursor.getColumnIndex("Status"));
        entity.setStatus(status);
        String statusText = cursor.getString(cursor.getColumnIndex("StatusText"));
        entity.setStatusDisplayString(statusText);
        String groupStatus = cursor.getString(cursor.getColumnIndex("GroupByStatus"));
        entity.setEmployeeStatusGroup(groupStatus);

//        int subStatus = cursor.getInt(cursor.getColumnIndex("StatusPeriod"));
//        entity.setStatusPeriod(subStatus);

       // boolean allDays = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("AllDays")));
        String allday = cursor.getString(cursor.getColumnIndex("AllDay"));
        boolean allDays = ("1".equals(allday));
        entity.setAllDays(allDays);

        String systemPeriod = cursor.getString(cursor.getColumnIndex("SystemPeriod"));
        boolean isSystemPeriod = ("1".equals(systemPeriod));
        entity.setSystemPeriod(isSystemPeriod);
        // RGBColor
        String color = cursor.getString(cursor.getColumnIndex("RGBColor"));
        entity.setStatusColor(color);

        String desc = cursor.getString(cursor.getColumnIndex("Description"));
        entity.setDescription(desc);
        String startDate = cursor.getString(cursor.getColumnIndex("PeriodStartDate"));
        if (startDate != null && !"".equals(startDate)) {
            entity.setStartDate(Utils.dateFromString(startDate, true, true));
        }
        String endDate = cursor.getString(cursor.getColumnIndex("PeriodEndDate"));
        if (endDate != null && !"".equals(endDate)) {
            entity.setEndDate(Utils.dateFromString(endDate, true, true));
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

    /**
     * addDaysToDate
     *
     * @param date
     * @param numOfDays
     * @return date
     */
    public static Date addDaysToDate(Date date, int numOfDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, numOfDays);
        date = c.getTime();
        return date;
    }
}
