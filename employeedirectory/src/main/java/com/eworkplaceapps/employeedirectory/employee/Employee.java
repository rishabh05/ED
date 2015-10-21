//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class Employee extends BaseEntity implements Serializable {


    /**
     * Enums are used to define the column level permission in FieldPermission table.
     */
    public enum EmployeeEntityFieldId {
        FIRST_NAME(1),
        LAST_NAME(2),
        NICK_NAME(3),
        START_DATE(7),
        JOB_TITLE(8),
        BIRTHDAY(9),
        REPORT_TO(12),
        MIDDLE_NAME(13),
        TIME_ZONE(14),
        DEPARTMENT(15),
        LOCATION(16);
        private int id;

        EmployeeEntityFieldId(int id) {
            this.id = id;
        }

        private int getId() {
            return id;
        }
    }

    private static final String EMPLOYEE_ENTITY_NAME = "Employee";
    private String fullName = "", firstName = "", middleName = "", lastName = "", nickName = "", reportTo = "", reportToName = "", jobTitle = "";
    private String loginEmail = "", department = "", location = "", picture = "",localTimeZone = "";
    private UUID tenantId = Utils.emptyUUID();
    private UUID tenantUserId = Utils.emptyUUID();
    private UUID departmentId = Utils.emptyUUID();
    private UUID locationId = Utils.emptyUUID();
    private EmployeeStatusEnum employeeStatus;
    private Date birthDay, startDate;
    private boolean following = false, favorites = false;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public void setFullName(String fullName) {
        setPropertyChanged(this.fullName, fullName);
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        setPropertyChanged(this.firstName, firstName);
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        setPropertyChanged(this.middleName, middleName);
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        setPropertyChanged(this.lastName, lastName);
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        setPropertyChanged(this.nickName, nickName);
        this.nickName = nickName;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        setPropertyChanged(this.reportTo, reportTo);
        this.reportTo = reportTo;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        setPropertyChanged(this.reportToName, reportToName);
        this.reportToName = reportToName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        setPropertyChanged(this.jobTitle, jobTitle);
        this.jobTitle = jobTitle;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        setPropertyChanged(this.loginEmail, loginEmail);
        this.loginEmail = loginEmail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        setPropertyChanged(this.department, department);
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        setPropertyChanged(this.location, location);
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        setPropertyChanged(this.picture, picture);
        this.picture = picture;
    }

    public String getLocalTimeZone() {
        return localTimeZone;
    }

    public void setLocalTimeZone(String localTimeZone) {
        setPropertyChanged(this.localTimeZone, localTimeZone);
        this.localTimeZone = localTimeZone;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public UUID getTenantUserId() {
        return tenantUserId;
    }

    public void setTenantUserId(UUID tenantUserId) {
        setPropertyChanged(this.tenantUserId, tenantUserId);
        this.tenantUserId = tenantUserId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        setPropertyChanged(this.departmentId, departmentId);
        this.departmentId = departmentId;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        setPropertyChanged(this.locationId, locationId);
        this.locationId = locationId;
    }

    public EmployeeStatusEnum getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(EmployeeStatusEnum employeeStatus) {
        setPropertyChanged(this.employeeStatus, employeeStatus);
        this.employeeStatus = employeeStatus;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        setPropertyChanged(this.birthDay, birthDay);
        this.birthDay = birthDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        setPropertyChanged(this.startDate, startDate);
        this.startDate = startDate;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        setPropertyChanged(this.following, following);
        this.following = following;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        setPropertyChanged(this.favorites, favorites);
        this.favorites = favorites;
    }

    public Employee() {
        super(EMPLOYEE_ENTITY_NAME);
    }

    /**
     * Create employee object and return created object.
     *
     * @return Employee
     */
    public static Employee createEntity() {
        return new Employee();
    }

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

        /// It is used to validate the first name null or empty.
        if (this.firstName == null && "".equals(this.firstName)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"FIRST_NAME"});
            message.add(AppMessage.NAME_REQUIRED);
        }

        /// It is used to validate the lastName name null or empty.
        if (this.lastName == null && "".equals(this.lastName)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"LAST_NAME"});
            message.add(AppMessage.NAME_REQUIRED);
        }

        /// It is used to validate the email null or empty.
        if (this.loginEmail == null && "".equals(this.loginEmail)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.EMAIL_REQUIRED);
        }

        /// It is used to validate the email format.
        else if (!Utils.isValidEmail(this.loginEmail)) {
            dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, new String[]{"Email"});
            message.add(AppMessage.INVALID_EMAIL);
        }

        /// It is used to check the max charecter allowed in firstName.
        if ((this.firstName.length() > 200)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"FIRST_NAME"});
            message.add(AppMessage.LENGTH_ERROR);
        }

        /// It is used to check the max charecter allowed in lastName.
        if (this.lastName.length() > 200) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"LAST_NAME"});
            message.add(AppMessage.LENGTH_ERROR);
        }

        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Error"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Employee employee = (Employee) entity;
        employee.setEntityId(this.entityId);
        employee.setTenantId(this.tenantId);
        employee.setFullName(this.fullName);
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setMiddleName(this.middleName);
        employee.setNickName(this.nickName);
        employee.setJobTitle(this.jobTitle);
        employee.setLoginEmail(this.loginEmail);
        employee.setJobTitle(this.jobTitle);
        employee.setBirthDay(this.birthDay);
        employee.setReportTo(this.reportTo);
        employee.setStartDate(this.startDate);
        employee.setEmployeeStatus(this.employeeStatus);
        employee.setLocalTimeZone(this.localTimeZone);
        employee.setLastOperationType(this.lastOperationType);
        employee.setUpdatedAt(this.updatedAt);
        employee.setUpdatedBy(this.updatedBy);
        employee.setCreatedAt(this.createdAt);
        employee.setCreatedBy(this.createdBy);
        employee.setTenantUserId(this.tenantUserId);
        return employee;
    }
}
