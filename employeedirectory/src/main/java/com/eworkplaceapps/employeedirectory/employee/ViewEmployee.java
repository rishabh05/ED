//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class ViewEmployee {

    private UUID employeeId = Utils.emptyUUID();
    private UUID userId = Utils.emptyUUID();
    private boolean admin = false;
    private String firstName = "";
    private String lastName = "";
    private String nickName = "";
    private String picture;
    private String jobTitle = "";
    private int localTimeZone = 0;
    private Date startDate=new Date();
    private Date birthDay;
    private EmployeeStatusEnum employeeStatus;
    private boolean followUp = false;
    private boolean favorite = false;
    private List<ViewEmployeeCommunication> communicationList = new ArrayList<>();
    private String departmentName = "";
    private String locationName = "";
    private String reportToName = "";
    private ViewEmployeeAddress addressInfo;
    private ViewEmployeeAddress locationAddressInfo;
    private List<String> teamNameList;
    private List<ViewEmployeeContact> emergencyContactList = new ArrayList<>();
    private String note = "";

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getLocalTimeZone() {
        return localTimeZone;
    }

    public void setLocalTimeZone(int localTimeZone) {
        this.localTimeZone = localTimeZone;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public EmployeeStatusEnum getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(EmployeeStatusEnum employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public boolean isFollowUp() {
        return followUp;
    }

    public void setFollowUp(boolean followUp) {
        this.followUp = followUp;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<ViewEmployeeCommunication> getCommunicationList() {
        return communicationList;
    }

    public void setCommunicationList(List<ViewEmployeeCommunication> communicationList) {
        this.communicationList = communicationList;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        this.reportToName = reportToName;
    }

    public ViewEmployeeAddress getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(ViewEmployeeAddress addressInfo) {
        this.addressInfo = addressInfo;
    }

    public ViewEmployeeAddress getLocationAddressInfo() {
        return locationAddressInfo;
    }

    public void setLocationAddressInfo(ViewEmployeeAddress locationAddressInfo) {
        this.locationAddressInfo = locationAddressInfo;
    }

    public List<String> getTeamNameList() {
        return teamNameList;
    }

    public void setTeamNameList(List<String> teamNameList) {
        this.teamNameList = teamNameList;
    }

    public List<ViewEmployeeContact> getEmergencyContactList() {
        return emergencyContactList;
    }

    public void setEmergencyContactList(List<ViewEmployeeContact> emergencyContactList) {
        this.emergencyContactList = emergencyContactList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static ViewEmployee setViewEmployeeProperties(Cursor cursor) throws EwpException {
        while (cursor.moveToNext()) {
            ViewEmployee viewEmployee = new ViewEmployee();
            String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
            if (employeeId != null && !"".equals(employeeId)) {
                viewEmployee.setEmployeeId(UUID.fromString(employeeId));
            }
            String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
            viewEmployee.setFirstName(firstName);
            String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
            viewEmployee.setLastName(lastName);
            String nickName = cursor.getString(cursor.getColumnIndex("NickName"));
            viewEmployee.setNickName(nickName);
            String jobTitle = cursor.getString(cursor.getColumnIndex("JobTitle"));
            viewEmployee.setJobTitle(jobTitle);
            String localTimeZone = cursor.getString(cursor.getColumnIndex("LocalTimeZone"));
            if (localTimeZone != null && !"".equals(localTimeZone)) {
                viewEmployee.setLocalTimeZone(Integer.parseInt(localTimeZone));
            }

            String startDate = cursor.getString(cursor.getColumnIndex("StartDate"));
            if (startDate != null && !"".equals(startDate)) {
                viewEmployee.setBirthDay(Utils.dateFromString(startDate, true, true));
            }

            String birthday = cursor.getString(cursor.getColumnIndex("Birthday"));
            if (birthday != null && !"".equals(birthday)) {
                viewEmployee.setBirthDay(Utils.dateFromString(birthday, true, true));
            }

            String tenantUserId = cursor.getString(cursor.getColumnIndex("UserId"));
            if (tenantUserId != null && !"".equals(tenantUserId)) {
                viewEmployee.setUserId(UUID.fromString(tenantUserId));
            }
            int status = cursor.getInt(cursor.getColumnIndex("Status"));
            viewEmployee.setEmployeeStatus(EmployeeStatusEnum.values()[status]);
            viewEmployee.setReportToName(cursor.getString(cursor.getColumnIndex("ReportToName")));
            viewEmployee.setLocationName(cursor.getString(cursor.getColumnIndex("LocationName")));
            viewEmployee.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            viewEmployee.setPicture(cursor.getString(cursor.getColumnIndex("Picture")));

            return viewEmployee;
        }
        return null;
    }

}
