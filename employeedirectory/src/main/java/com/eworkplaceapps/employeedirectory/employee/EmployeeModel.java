//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;
import android.text.TextUtils;

import com.eworkplaceapps.employeedirectory.department.Department;
import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.address.AddressDataService;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.note.Note;
import com.eworkplaceapps.platform.note.NoteDataService;
import com.eworkplaceapps.platform.tenant.TenantUser;
import com.eworkplaceapps.platform.tenant.TenantUserDataService;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ViewEmployee model merge the employee detail information in a model, which is useful to display employee information in employee UI view.
 * It contains the set of employee information.
 * It merge the following employee information in a model:
 * 1) Employee basic information like name, nickname, jobtitle etc.
 * 2) It contains team list, for which teams employee is the member.
 * 3) Employee emargenacy contact information in a bunch like Name, Address, Coommunication information.
 * 4) Employee current status.
 */
public class EmployeeModel implements Serializable {

    // Initialize the new instance of ViewEmployee.
    public EmployeeModel() {
    }

    private Employee employee;
    private String reportToName = "", picture = "", locationName = "", departmentName = "", IANATimeZoneId = "";
    private String statusText = "";
    private boolean followUp = false, favorite = false;
    private EmployeeStatusEnum employeeStatus;
    private Address addressInfo;
    private List<Communication> communicationList = new ArrayList<Communication>();
    private List<EditEmployeeTeam> teamNameList;
    private List<EmployeeEmergencyContact> employeeEmergencyContactDetail;
    private Note note;
    private String statusColor = "";

    public String getIANATimeZoneId() {
        return IANATimeZoneId;
    }

    public void setIANATimeZoneId(String IANATimeZoneId) {
        this.IANATimeZoneId = IANATimeZoneId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        this.reportToName = reportToName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<EditEmployeeTeam> getTeamNameList() {
        return teamNameList;
    }

    public void setTeamNameList(List<EditEmployeeTeam> teamNameList) {
        this.teamNameList = teamNameList;
    }

    public List<EmployeeEmergencyContact> getEmployeeEmergencyContactDetail() {
        return employeeEmergencyContactDetail;
    }

    public void setEmployeeEmergencyContactDetail(List<EmployeeEmergencyContact> employeeEmergencyContactDetail) {
        this.employeeEmergencyContactDetail = employeeEmergencyContactDetail;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
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

    public EmployeeStatusEnum getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(EmployeeStatusEnum employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Address getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(Address addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<Communication> getCommunicationList() {
        return communicationList;
    }

    public void setCommunicationList(List<Communication> communicationList) {
        this.communicationList = communicationList;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Tuple.Tuple2<Integer, Integer, Integer> getStatusRGBColor() {
        String strArray[] = statusColor.split(",");
        if (strArray != null && strArray.length == 3) {
            int r = Integer.valueOf(strArray[0]);
            int g = Integer.valueOf(strArray[1]);
            int b = Integer.valueOf(strArray[2]);
            return new Tuple.Tuple2<Integer, Integer, Integer>(r, g, b);
        }
        return new Tuple.Tuple2<Integer, Integer, Integer>(0, 0, 0);
    }

    public static EmployeeModel setViewEmployeeProperties(Cursor cursor) throws EwpException {
        while (cursor.moveToNext()) {
            EmployeeModel model = new EmployeeModel();
            model.setEmployee(new Employee());
            Employee employee = model.getEmployee();
            EmployeeData employeeData = new EmployeeData();
            // Set the value from FMResultSet to Employee entity.
            employeeData.setPropertiesFromResultSet(employee, cursor);
            // Other propeties
            String reportToName = cursor.getString(cursor.getColumnIndex("ReportToName"));
            model.setReportToName(reportToName);
            String color = cursor.getString(cursor.getColumnIndex("StatusColor"));
            model.setStatusColor(color);
            String locationName = cursor.getString(cursor.getColumnIndex("LocationName"));
            model.setLocationName(locationName);
            String departmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            model.setDepartmentName(departmentName);
            String IANATimeZoneId = cursor.getString(cursor.getColumnIndex("IANATimeZoneId"));
            model.setIANATimeZoneId(IANATimeZoneId);
            String picture = cursor.getString(cursor.getColumnIndex("Picture"));
            if ("null".equalsIgnoreCase(picture)) {
                picture = "";
            }
            int index = cursor.getColumnIndex("StatusText");
            if (index >= 0) {
                model.setStatusText(cursor.getString(index));

            }
            model.setPicture(picture);
            return model;
        }
        return null;
    }

    /**
     * Method is used to update the employee. It will take the employee as EmployeeModel.
     * /// EmployeeModel, contains the list of of object to update with respact to an employee.
     * /// Employee model keep the detail updated information of an empolyee.
     * /// :param: employeeid is the id of an employee.
     * /// :returns: Returns the error object if error raise to update employee.
     *
     * @param employeeModel
     */
    public static void updateEmployee(EmployeeModel employeeModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        TenantUserDataService tenantUserDataService = new TenantUserDataService();
        TenantUser tenantUser = (TenantUser) tenantUserDataService.getEntity(employeeModel.getEmployee().getTenantUserId());
        if (tenantUser != null) {
            tenantUser.setFirstName(employeeModel.getEmployee().getFirstName());
            tenantUser.setLastName(employeeModel.getEmployee().getLastName());
            tenantUser.setPicture(employeeModel.getPicture());
            tenantUserDataService.update(tenantUser);
            tenantUserDataService.updateUserNameAndPicture(tenantUser);
        }
        EmployeeDataService empService = new EmployeeDataService();
        empService.update(employeeModel.getEmployee(), false);
        if (employeeModel.getAddressInfo() != null) {
            AddressDataService addressService = new AddressDataService();
            employeeModel.getAddressInfo().setSourceEntityId(employeeModel.getEmployee().getEntityId());
            employeeModel.getAddressInfo().setSourceEntityType(EDEntityType.EMPLOYEE.getId());
            addressService.addUpdateAddress(employeeModel.getAddressInfo(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId());
        }
        // Add/Updating employee communication information. Communication information like Phone, email, mobile etc.
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(employeeModel.getCommunicationList(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(), EDApplicationInfo.getAppId().toString());
        // Mark the employee as followup employee for logged in user.
        empService.markAsFollowUp(employeeModel.getEmployee().getEntityId(), employeeModel.isFollowUp());
        if (employeeModel.getNote() != null) {
            NoteDataService noteService = new NoteDataService();
            noteService.addUpdateNote(employeeModel.getNote(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId(), EwpSession.EMPLOYEE_APPLICATION_ID);
        }
        // Add/Rmove employee from a team.
        EmployeeGroupMemberDataService service = new EmployeeGroupMemberDataService();
        service.addDeleteMemberFromTeam(employeeModel.teamNameList, employeeModel.getEmployee().getEntityId());// Add/update employee contact information. Comployee contact information like emergancy contact.
        EmployeeContactDataService employeeContactDataService = new EmployeeContactDataService();
        EmployeeEmergencyContact.updateEmployeeContactDetailFromSourceEntityIdAndType(employeeModel.getEmployeeEmergencyContactDetail(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId());
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject dict = new JSONObject();
        dict.put("EmployeeOpsDetailModel", toEmployeeObjJsonDictionary());
        if (this.communicationList != null && this.communicationList.size() > 0) {
            dict.put("ListCommunicationViewModel", Communication.getCommunicationListAsDictionary(this.communicationList, true));
        } else {
            dict.put("ListCommunicationViewModel", "NULL");
        }

        if (this.addressInfo != null) {
            dict.put("AddressViewModel", this.getAddressInfo().getAddressAsDictionary());

        } else {
            dict.put("AddressViewModel", "NULL");
        }
        if (this.employeeEmergencyContactDetail != null && this.employeeEmergencyContactDetail.size() > 0) {
            dict.put("EmergencyContactModelList", EmployeeEmergencyContact.getEmergencyContactListAsDictionary(this.employeeEmergencyContactDetail));
        } else {
            dict.put("EmergencyContactModelList", "NULL");
        }
        if (this.teamNameList != null && this.teamNameList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < this.teamNameList.size(); i++) {
                if (this.teamNameList.get(i).getOperationType() != DatabaseOperationType.DELETE) {
                    String teamId = String.valueOf(this.teamNameList.get(i).getTeamId());
                    jsonArray.put(teamId);
                }
            }
            dict.put("TeamIdList", jsonArray);
        } else {
            dict.put("TeamIdList", new JSONArray());
        }

        return dict;
    }

    private JSONObject toEmployeeObjJsonDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        Employee emp = this.employee;

        if (emp != null) {
            dict.put("EmployeeId", emp.getEntityId().toString());
        }
        if (emp.getReportTo() != null && !(emp.getReportTo().equalsIgnoreCase("00000000-0000-0000-0000-000000000000"))) {
            dict.put("ReportsTo", emp.getReportTo().toString());
        } else {
            dict.put("ReportsTo", "NULL");
        }
        dict.put("Picture", emp.getPicture());
        dict.put("UserId", emp.getTenantUserId().toString());
        dict.put("Prefix", "");
        dict.put("FirstName", emp.getFirstName());
        dict.put("LastName", emp.getLastName());
        dict.put("MiddleName", "");
        dict.put("Suffix", "");
        dict.put("FullName", emp.getFullName());
        if (emp.getNickName() == null) {
            dict.put("NickName", "");
        } else {
            dict.put("NickName", emp.getNickName());
        }
        if (emp.getJobTitle() == null) {
            dict.put("JobTitle", "");
        } else {
            dict.put("JobTitle", emp.getJobTitle());
        }
        if (emp.getBirthDay() != null) {
            String bDate = Utils.stringFromDate(emp.getBirthDay());
            if (bDate != null) {
                dict.put("BirthDay", bDate);
            } else {
                dict.put("BirthDay", "NULL");
            }
        } else {
            dict.put("BirthDay", "NULL");
        }
        if (emp.getStartDate() != null) {
            String sDate = Utils.dateAsStringWithoutUTC(emp.getStartDate());
            if (sDate != null) {
                dict.put("StartDate", sDate);
            } else {
                dict.put("StartDate", "NULL");
            }
        } else {
            dict.put("StartDate", "NULL");
        }

        if (emp.getLocationId() != null && !(emp.getLocationId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000"))) {
            dict.put("LocationId", emp.getLocationId().toString());
        } else {
            dict.put("LocationId", "NULL");
        }

        if (emp.getDepartmentId() != null && !(emp.getDepartmentId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000"))) {
            dict.put("DepartmentId", emp.getDepartmentId().toString());
        } else {
            dict.put("DepartmentId", "NULL");
        }
        dict.put("LoginEmail", emp.getLoginEmail().toString());
        if (emp.getEntityId().toString() != null && emp.getEntityId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000")) {
            dict.put("OperationType", DatabaseOperationType.ADD.getId());
        } else {
            dict.put("OperationType", DatabaseOperationType.UPDATE.getId());
        }
        if (this.note != null && !(TextUtils.isEmpty(this.note.getNote()))) {
            dict.put("Note", this.note.getNote());
        } else {
            dict.put("Note", "");
        }
        return dict;
    }
}

