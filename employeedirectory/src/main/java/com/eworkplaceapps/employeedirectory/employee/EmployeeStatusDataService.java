//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.notification.EventNotification;
import com.eworkplaceapps.platform.notification.EventNotificationDataService;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;
import com.eworkplaceapps.platform.notificationworkflow.NotificationWorkflow;
import com.eworkplaceapps.platform.notificationworkflow.RawNotification;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.tenant.Tenant;
import com.eworkplaceapps.platform.tenant.TenantDataService;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.EmployeeDayStatusEnum.values;


/**
 * It expand the BaseDataService to provide services to EmployeeStatus entities.
 * It is used to manage the employee status on perticular date.
 * Employee status can be different on different dates. It is used to manage the current status as well as on a perticular date.
 */
public class EmployeeStatusDataService extends BaseDataService {

    private EmployeeStatusData dataDelegate = new EmployeeStatusData();

    /**
     * Initializes a new instance of the EmployeeStatusDataService class.
     */
    public EmployeeStatusDataService() {
        super("EmployeeStatusDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * This method is used to check permission on given operation.
     *
     * @param entity
     * @param operation
     * @return boolean
     * @throws EwpException
     */
    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operation) throws EwpException {
        EmployeeStatusAccess access = new EmployeeStatusAccess();
        if (operation == EntityAccess.CheckOperationPermission.ADD || operation == EntityAccess.CheckOperationPermission.UPDATE) {
            EmployeeStatus status = (EmployeeStatus) entity;
            EmployeeDataService service = new EmployeeDataService();
            Employee resultTuple = service.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
            if (resultTuple != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * /// It is used to add employe.
     * /// Method will Add TenantUser and RoleLinking with employee.
     *
     * @param entity
     * @return Object
     */
    @Override
    public Object add(BaseEntity entity) {
        EmployeeStatus empStatus = (EmployeeStatus) entity;
        try {
            //setTime(empStatus);
            Object resultTuple = super.add(entity);
            /// Set the notification for a user on adding status.
            if (resultTuple != null) {
                empStatus.setEntityId((UUID) resultTuple);
               // sendStatusChangeNotification(empStatus, DatabaseOperationType.ADD);
            }
            return resultTuple;
        } catch (EwpException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Method will update the tenant user with the updation of employee.
     *
     * @param entity
     */
    @Override
    public void update(BaseEntity entity) {
        EmployeeStatus empStatus = (EmployeeStatus) entity;
        try {
          //  setTime(empStatus);
            super.update(entity);
//            sendStatusChangeNotification(empStatus, DatabaseOperationType.UPDATE);
        } catch (EwpException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(BaseEntity entity) throws EwpException {
        super.delete(entity);
        /// Set the notification for users on deleting status.
        EmployeeStatus empStatus = (EmployeeStatus) entity;
       // sendStatusChangeNotification(empStatus, DatabaseOperationType.DELETE);
    }

    /**
     * It is used to set start and endtime date as per user staus period.
     *
     * @param empStatus
     */
    public void setTime(EmployeeStatus empStatus) throws EwpException {
        Date date = new Date();
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        switch (values()[empStatus.getStatusPeriod()]) {
            case THIS_MORNING:
                String s = strDate + "T00:00:00.000";
                String e = strDate + "T11:59:00.000";
                empStatus.setStartDate(Utils.utcDateFromString(s));
                empStatus.setEndDate(Utils.utcDateFromString(e));
                break;
            case THIS_AFTERNOON:
                s = strDate + "T12:00:00.000";
                e = strDate + "T05:00:00.000";//"T23:59:00.000";
                empStatus.setStartDate(Utils.utcDateFromString(s));
                empStatus.setEndDate(Utils.utcDateFromString(e));
                break;
            case ALL_DAY_TODAY:
                s = strDate + "T00:00:00.000";
                e = strDate + "T23:59:00.000";
                empStatus.setStartDate(Utils.utcDateFromString(s));
                empStatus.setEndDate(Utils.utcDateFromString(e));
                break;
            case ALL_DAY_TOMORROW:
                date = EmployeeStatusData.addDaysToDate(new Date(), 1);
                strDate = Utils.stringFromDate(date);
                s = strDate + "T00:00:00.000";
                e = strDate + "T23:59:00.000";
                empStatus.setStartDate(Utils.dateFromString(s, false, false));
                empStatus.setEndDate(Utils.dateFromString(e, false, false));
                break;
            default:
                break;
        }
    }

    /**
     * It is used to get the employee list on given date.
     * /// employeeId is nullable, because method can return list of employee on given date.
     *
     * @param date
     * @param employeeId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getStatusListByDateAsResultSet(Date date, UUID employeeId) throws EwpException {
        Cursor resultTuple = dataDelegate.getEmployeeStatusListByDateAsResultSet(date, employeeId);
        if (resultTuple == null) {
            List<String> message = new ArrayList<String>();
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("Cursor is null"), EwpErrorHandler.ErrorPolicy.WRAP, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, "getStatusListByDateAsResultSet", "EmployeeStatusDataService", 0, false);
        }
        return resultTuple;
    }

    /**
     * It is used to get the employee status list from logged in tenantid.
     *
     * @return List<BaseEntity>
     */
    public List<EmployeeStatus> getEmployeeStatusListFromLoggedInTenantId() throws EwpException {
        /// It will return the list of status.
        List<EmployeeStatus> resultTuple = dataDelegate.getEmployeeStatusListByTenantId(EwpSession.getSharedInstance().getTenantId());
        return resultTuple;
    }

    /**
     * It is used to get the employee status list from current datetime.
     *
     * @return List<BaseEntity>
     */
    public List<EmployeeStatus> getEmployeeStatusListFromCurrentDateAsEmployeeStatusList() throws EwpException {
        /// It will return the list of status.
        List<EmployeeStatus> resultTuple = dataDelegate.getEmployeeStatusListFromCurrentDateAsEmployeeStatusList(EwpSession.getSharedInstance().getTenantId());
        return resultTuple;
    }

    /// Sends Employee Status Change Notification.
    private void sendStatusChangeNotification(EmployeeStatus employeeStatus, DatabaseOperationType operationType) throws EwpException {
        EmployeeDataService employeeDS = new EmployeeDataService();
        BaseEntity resultTuple = employeeDS.getEntity(employeeStatus.getEmployeeId());
        /// If any error occured then return from here.
        if (resultTuple == null) {
            List<String> message = new ArrayList<String>();
            message.add("Error in getting employee");
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("Error in getting employee"), EwpErrorHandler.ErrorPolicy.WRAP, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, "", "", 0, false);
        }

        Employee employee = (Employee) resultTuple;
        RawNotification rawNotification = new RawNotification();
        rawNotification.setApplicationId(EDApplicationInfo.getAppId().toString());
        rawNotification.setLoginTenantId(EwpSession.getSharedInstance().getTenantId());
        rawNotification.setLoginUserId(EwpSession.getSharedInstance().getUserId());
        rawNotification.setLoginUserName(employee.getFullName());
        rawNotification.setNotificationType(NotificationEnum.NotificationTypeEnum.EVENT);
        rawNotification.setNotifierEntityId(employee.getEntityId());
        rawNotification.setNotifierEntityType(EDEntityType.EMPLOYEE.getId());
        rawNotification.setNotificationEntityType(PFEntityType.EVENT_NOTIFICATION.getValue());
        /// Initialize Other Information
        Map<String, String> otherData = new HashMap<String, String>();
        otherData.put("EmployeeStatusId", employeeStatus.getEntityId().toString());
        otherData.put("EmployeeFullName", employee.getFullName());
        otherData.put("EmployeeInitials", employee.getFullName());
        otherData.put("EmployeeEmail", employee.getLoginEmail());
        ///let employeePicture = employee.getPictureAsBase64String()
        ///otherData["EmployeeImage"] = employeePicture == "" ? Utils.getDefaultEmployeePictureAsBase64String() : employeePicture
        otherData.put("EmployeeId", employee.getEntityId().toString());
        otherData.put("StatusId", String.valueOf(employeeStatus.getStatus()));
        otherData.put("StatusDescription", employeeStatus.getDescription());
        ///otherData["StatusDuration"] =  EmployeeStatusDataService.getStatusDurationString(employeeStatus)
        ///Utils.stringFromNSDate(NSDate(), inMillisec: false, useUTC: false)
        otherData.put("StatusName", employeeStatus.getStatusDisplayString());
        otherData.put("LowerStatusName", employeeStatus.getStatusDisplayString().toLowerCase());
        otherData.put("OperationType", String.valueOf(operationType));
        otherData.put("StatusOperation", String.valueOf(operationType));
        otherData.put("AllDays", employeeStatus.isAllDays() ? "true" : "false");
        otherData.put("StartDate", Utils.stringFromDate(employeeStatus.getStartDate()));
        otherData.put("EndDate", Utils.stringFromDate(employeeStatus.getEndDate()));
        otherData.put("StatusPeriod", String.valueOf(employeeStatus.getStatusPeriod()));
        BaseEntity tenantTuple = new TenantDataService().getEntity(employee.getTenantId());
        if (tenantTuple != null) {
            Tenant tenant = (Tenant) tenantTuple;
            otherData.put("TenantName", tenant.getName());
            otherData.put("TenantId", tenant.getEntityId().toString());
        } else {
            ///otherData["TenantImage"] = Utils.getDefaultTenantLogoAsBase64String()
            otherData.put("TenantName", "BM");
            otherData.put("TenantId", Utils.emptyUUID().toString());
        }
        rawNotification.setOtherData(otherData);
        getAllYourReportsFollowerListAsEventNotification(employeeStatus, rawNotification);
        getFollowerListAsEventNotification(employeeStatus, rawNotification);
    }

    private void getEmployeeFollowerList(EmployeeStatus employeeStatus, RawNotification rawNotification) {
        ///getAllYourReportsFollwerListAsEventNotification(employeeStatus, rawNotification: &rawNotification);
        ///getFollwerListAsEventNotification(employeeStatus, rawNotification: &rawNotification)
    }

    /**
     * Method is used to get today absent employee list. Method will give return the result in group by status.
     * Method is used to get employee and its short information as EmployeeQuickView list with current status.
     * It is useful to display employee short information in employeestaus UI with current status.
     *
     * @param tenantId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getTodayEmployeeStatusListInGroup(UUID tenantId,String searchChar) throws EwpException {
        Cursor cursor = dataDelegate.getTodayEmployeeStatusListInGroup(tenantId,searchChar);
        return cursor;
    }

    /**
     * Method is used to get logged in user employee list.
     * Method is used to get loggedin employee and its short information as EmployeeQuickView list as today/tomorrow/future status list.
     * It is useful to display employee short information in employeestaus UI with current status.
     *
     * @return List<EmployeeQuickView>
     */
    public List<EmployeeQuickView> getMyStatusAsQuickViewList() throws EwpException {
        Cursor cursor = dataDelegate.getMyStatusListFromCurrentDateAsResultSet(EwpSession.getSharedInstance().getUserId());
        if (cursor != null) {
            List<EmployeeQuickView> empQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, GroupBy.EMPLOYEE_STATUS, null);
            return empQuickViewList;
        }
        return null;
    }


    /**
     * It is used to get AllYourReports follower Notfier employee list when a status set for an employee.
     *
     * @param employeeStatus
     * @param rawNotification
     */
    private void getAllYourReportsFollowerListAsEventNotification(EmployeeStatus employeeStatus, RawNotification rawNotification) throws EwpException {
        GroupFollowerDataService groupFollowerDataService = new GroupFollowerDataService();
        List<UUID> employeeIdList = groupFollowerDataService.getLoginEmployeeAllYourReportsFollowerList(employeeStatus.getEntityId(), employeeStatus.getTenantId());
        EventNotificationDataService eventNotificationDS = new EventNotificationDataService();
        EDNotificationDataService edNotificationDataService = new EDNotificationDataService();
        /// Loop through all employeee list.
        for (int i = 0; i < employeeIdList.size(); i++) {
            /// Getting the eventnotification.
            /// Event notification are set at user level. User want the notifcation when his group member set his/her status.
            List<EventNotification> resultEventNotificationTuple = eventNotificationDS.getEntityListByParentEntityTypeAndIdAndEventTypeNo(EDEntityType.EMPLOYEE.getId(), employeeIdList.get(i), 1/*EDApplicationInfo.EDEmployeeEventNotification.EMPLOYEE_OUT_OF_OFFICE*/);
            /// Reinit the detail list.
            rawNotification.setNotificationRecipientDetailList(new ArrayList<NotificationRecipientDetail>());
            rawNotification.setResolvedEventNotification(new ArrayList<EventNotification>());
            if (resultEventNotificationTuple != null) {
                /// Appending the event notification.
                for (EventNotification eventNotification : resultEventNotificationTuple) {
                    rawNotification.getResolvedEventNotification().add(eventNotification);
                    rawNotification.setNotificationEntityId(eventNotification.getEntityId());
                    /// Getting list of NotficationReceipantDetail.
                    List<NotificationRecipientDetail> detail = edNotificationDataService.getResolvedEventNotificationRecipientDetail(eventNotification, employeeStatus.getTenantId(), EDEntityType.values()[rawNotification.getNotifierEntityType()], rawNotification.getNotifierEntityId());
                    /// Adding detail list
                    if (!detail.isEmpty()) {
                        rawNotification.getNotificationRecipientDetailList().addAll(detail);
                    }
                }
                /// Start workflow.
                new NotificationWorkflow().start(rawNotification);
            }
        }
    }

    /**
     * It is used to get Notifier employee list when a status set for an employee.
     *
     * @param employeeStatus
     * @param rawNotification
     * @return
     * @throws EwpException
     */
    private void getFollowerListAsEventNotification(EmployeeStatus employeeStatus, RawNotification rawNotification) throws EwpException {
        GroupFollowerDataService groupFollowerDataService = new GroupFollowerDataService();
        Cursor cursor = groupFollowerDataService.getLoginEmployeeFollowerList(employeeStatus.getEmployeeId(),
                employeeStatus.getTenantId());
        if (cursor == null) {
            return;
        }

        EventNotificationDataService eventNotificationDS = new EventNotificationDataService();
        EDNotificationDataService edNotificationDataService = new EDNotificationDataService();
        if (cursor != null) {
            /// Loop through all employeee list.
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("EmployeeId"));
                if (id == null) {
                    continue;
                }
                UUID employeeId = UUID.fromString(id);
                /// Reinit the detail list.
                rawNotification.setNotificationRecipientDetailList(new ArrayList<NotificationRecipientDetail>());
                rawNotification.setResolvedEventNotification(new ArrayList<EventNotification>());
                /// Getting the eventnotification.
                /// Event notification are set at user level. User want the notifcation when his group member set his/her status.
                List<EventNotification> resultEventNotificationTuple = eventNotificationDS.getEntityListByParentEntityTypeAndIdAndEventTypeNo(EDEntityType.EMPLOYEE.getId(), employeeId, 1/*EDApplicationInfo.EDEmployeeEventNotification.EMPLOYEE_OUT_OF_OFFICE*/);
                if (resultEventNotificationTuple != null) {
                    /// Appending the event notification.
                    for (EventNotification eventNotification : resultEventNotificationTuple) {
                        rawNotification.getResolvedEventNotification().add(eventNotification);
                        rawNotification.setNotificationEntityId(eventNotification.getEntityId());
                        /// Getting list of NotficationReceipantDetail.
                        List<NotificationRecipientDetail> detail = edNotificationDataService.getResolvedEventNotificationRecipientDetail(eventNotification, employeeStatus.getTenantId(), EDEntityType.values()[rawNotification.getNotifierEntityType()], rawNotification.getNotifierEntityId());
                        /// Adding detail list
                        if (detail.isEmpty()) {
                            rawNotification.getNotificationRecipientDetailList().addAll(detail);
                        }
                    }
                    /// Start workflow.
                    new NotificationWorkflow().start(rawNotification);
                }
            }
        }
    }

    /**
     * @param employeeId
     * @return EmployeeStatus
     * @throws EwpException
     */
    public EmployeeStatus getEmployeeCurrentStatusFromEmployeeId(UUID employeeId) throws EwpException {
        EmployeeStatus resultTuple = dataDelegate.getEmployeeCurrentStatusFromEmployeeId(employeeId);
        if (resultTuple == null) {
            List<String> message = new ArrayList<String>();
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("EmployeeStatus is null"), EwpErrorHandler.ErrorPolicy.WRAP, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, "getEmployeeCurrentStatusFromEmployeeId", "EmployeeStatusDataService", 0, false);
        }
        return resultTuple;
    }

    /// Class name: EmployeeStatusDataService
    /// Method is used to get logged in user employee list.
    /// Method is used to get loggedin employee and its short information as EmployeeQuickView list as today/tomorrow/future status list.
    /// It is useful to display employee short information in employeestaus UI with current status.
    public Cursor getMyStatusListAsResultSet(String searchValue) throws EwpException {
        Cursor cursor = dataDelegate.getMyStatusListFromCurrentDateAsResultSet(EwpSession.getSharedInstance().getUserId());
        return cursor;
    }

    /**
     * Need localization in this method.
     *
     * @param employeeStatus
     * @return String
     */
    public static String getStatusDurationString(EmployeeStatus employeeStatus) {
        String duration = "";
        EmployeeDayStatusEnum statusPeriod = values()[employeeStatus.getStatusPeriod()];
        switch (statusPeriod) {
            case ALL_DAY_TODAY:
                duration = "ED_AllDayToday"; ///"All Day Today"
                break;
            case THIS_MORNING:
                duration = "ED_ThisMorning";/// "This Morning" "ThisMorning";
                break;
            case THIS_AFTERNOON:
                duration = "ED_ThisAfternoon"; /// "This Afternoon"
                break;
            case ALL_DAY_TOMORROW:
                duration = "ED_AllDayTomorrow"; ///"All Day Tomorrow"
                break;
            case ANOTHER_TIME:
                int date = employeeStatus.getStartDate().compareTo(employeeStatus.getEndDate());
                duration = "Another Time";
                if (date == 0) {
                    if (employeeStatus.isAllDays()) {
                        String strtDate = employeeStatus.getStartDate().toLocaleString();
                        //Monday, 3/16
                        // Todo: Change it to recipient culture.
                        //duration = string.Format("{0}, {1}/{2}", System.Threading.Thread.CurrentThread.CurrentCulture.DateTimeFormat.GetDayName(employeeStatus.StartDate.DayOfWeek), employeeStatus.StartDate.Month.ToString(), employeeStatus.StartDate.Day.ToString());
                    } else {
                        //  March 16 6:00am - 10:00pm
                        //duration = string.Format("{0} {1} {2} - {3}", employeeStatus.StartDate.ToString("MMMM"), employeeStatus.StartDate.Day.ToString(), employeeStatus.StartDate.ToString("hh:mm tt"), employeeStatus.EndDate.ToString("hh:mm tt"));
                    }
                } else {
                    String strtDate = employeeStatus.getStartDate().toLocaleString();
                    // March 11 - March 19
                    //duration = string.Format("{0} {1} - {2} {3}", employeeStatus.StartDate.ToString("MMMM"), employeeStatus.StartDate.Day.ToString(), employeeStatus.EndDate.ToString("MMMM"), employeeStatus.EndDate.Day.ToString());
                }
                break;
            default:
                duration = "";
                break;
        }
        return duration;
    }
}