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
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Employee can set own status using this class. Employee can set different status for a day.
 * It provide the different status to set like "Out of Office", "Sick", "Remote", "Vacation" etc.
 */
public class EmployeeStatus extends BaseEntity {

    private static final String EMPLOYEE_STATUS_ENTITY_NAME = "EmployeeStatus";

    private String description = "", statusDisplayString = "", image = "", statusPeriodDisplayString = "", employeeFullName = "", employeePicture = "", employeeStatusGroup = "";
    private int status = 0, statusPeriod = 4;
    private boolean allDays = false;
    private boolean systemPeriod = false;
    private Date startDate = new Date(), endDate = new Date();
    private UUID employeeId = Utils.emptyUUID();
    private UUID userId = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();
    private UUID empStatusId = Utils.emptyUUID();
    private String statusColor = "";

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        setPropertyChanged(this.description, description);
        this.description = description;
    }

    public UUID getEmpStatusId() {
        return empStatusId;
    }

    public void setEmpStatusId(UUID empStatusId) {
        setPropertyChanged(this.empStatusId,empStatusId);
        this.empStatusId = empStatusId;
    }

    public String getStatusDisplayString() {
        return statusDisplayString;
    }

    public void setStatusDisplayString(String statusDisplayString) {
        setPropertyChanged(this.statusDisplayString, statusDisplayString);
        this.statusDisplayString = statusDisplayString;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        setPropertyChanged(this.image, image);
        this.image = image;
    }

    public String getStatusPeriodDisplayString() {
        return statusPeriodDisplayString;
    }

    public void setStatusPeriodDisplayString(String statusPeriodDisplayString) {
        setPropertyChanged(this.statusPeriodDisplayString, statusPeriodDisplayString);
        this.statusPeriodDisplayString = statusPeriodDisplayString;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        setPropertyChanged(this.employeeFullName, employeeFullName);
        this.employeeFullName = employeeFullName;
    }

    public boolean isSystemPeriod() {
        return systemPeriod;
    }

    public void setSystemPeriod(boolean systemPeriod) {
        setPropertyChanged(this.systemPeriod, systemPeriod);
        this.systemPeriod = systemPeriod;
    }

    public String getEmployeePicture() {
        return employeePicture;
    }

    public void setEmployeePicture(String employeePicture) {
        setPropertyChanged(this.employeePicture, employeePicture);
        this.employeePicture = employeePicture;
    }

    public String getEmployeeStatusGroup() {
        return employeeStatusGroup;
    }

    public void setEmployeeStatusGroup(String employeeStatusGroup) {
        setPropertyChanged(this.employeeStatusGroup, employeeStatusGroup);
        this.employeeStatusGroup = employeeStatusGroup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        setPropertyChanged(this.status, status);
        this.status = status;
    }

    public int getStatusPeriod() {
        return statusPeriod;
    }

    public void setStatusPeriod(int statusPeriod) {
        setPropertyChanged(this.statusPeriod, statusPeriod);
        this.statusPeriod = statusPeriod;
    }

    public boolean isAllDays() {
        return allDays;
    }

    public void setAllDays(boolean allDays) {
        setPropertyChanged(this.allDays, allDays);
        this.allDays = allDays;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        setPropertyChanged(this.startDate, startDate);
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        setPropertyChanged(this.endDate, endDate);
        this.endDate = endDate;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        setPropertyChanged(this.employeeId, employeeId);
        this.employeeId = employeeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        setPropertyChanged(this.userId, userId);
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public  Tuple.Tuple2<Integer, Integer, Integer> getStatusRGBColor() {
        String  strArray []= statusColor.split(",");
        if (strArray!=null && strArray.length == 3) {
            int r = Integer.valueOf(strArray[0]);
            int g = Integer.valueOf(strArray[1]);
            int b = Integer.valueOf(strArray[2]);
            return new Tuple.Tuple2<Integer, Integer, Integer>(r,g,b);
        }
        return new Tuple.Tuple2<Integer, Integer, Integer>(0,0,0);
    }

    public EmployeeStatus() {
        super(EMPLOYEE_STATUS_ENTITY_NAME);
    }

    public static EmployeeStatus createEntity() {
        return new EmployeeStatus();
    }


    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It used to check Employee id is blank.
        if (this.employeeId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"EmployeeId"});
            message.add(AppMessage.REFERENCE_ID_REQUIRED);
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
        EmployeeStatus employeeStatus = (EmployeeStatus) entity;
        employeeStatus.setEntityId(this.entityId);
        employeeStatus.setTenantId(this.tenantId);
        employeeStatus.setStatus(this.status);
        employeeStatus.setStatusPeriod(this.statusPeriod);
        employeeStatus.setStatusColor(this.statusColor);
        employeeStatus.setDescription(this.description);
        employeeStatus.setStartDate(this.startDate);
        employeeStatus.setEndDate(this.endDate);
        employeeStatus.setEmployeeId(this.employeeId);
        employeeStatus.setUserId(this.userId);
        employeeStatus.setEmployeeFullName(this.employeeFullName);
        employeeStatus.setEmployeePicture(this.employeePicture);
        employeeStatus.setAllDays(this.allDays);
        employeeStatus.setUpdatedAt(this.updatedAt);
        employeeStatus.setUpdatedBy(this.updatedBy);
        employeeStatus.setCreatedAt(this.createdAt);
        employeeStatus.setCreatedBy(this.createdBy);
        return employeeStatus;
    }
}
