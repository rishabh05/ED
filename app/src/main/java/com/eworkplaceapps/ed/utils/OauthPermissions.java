//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 07 Oct 2015
//===============================================================================
package com.eworkplaceapps.ed.utils;

import com.eworkplaceapps.employeedirectory.department.DepartmentAccess;
import com.eworkplaceapps.employeedirectory.employee.EmployeeGroupAccess;
import com.eworkplaceapps.employeedirectory.location.LocationAccess;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.employeedirectory.employee.EmployeeAccess;

import org.apache.log4j.Logger;

import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.ADD_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.DELETE_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_LOCATION;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_TEAM;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_REPORTSTO;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_STARTDATE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.VIEW_EMERGENCYCONTACT;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.VIEW_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeGroupAccess.EmployeeGroupOperation.ADD_EMPLOYEE_GROUP;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeGroupAccess.EmployeeGroupOperation.DELETE_EMPLOYEE_GROUP;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeGroupAccess.EmployeeGroupOperation.UPDATE_EMPLOYEE_GROUP;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeGroupAccess.EmployeeGroupOperation.VIEW_EMPLOYEE_GROUP;
import static com.eworkplaceapps.employeedirectory.location.LocationAccess.LocationOperation.ADD_LOCATION;
import static com.eworkplaceapps.employeedirectory.location.LocationAccess.LocationOperation.DELETE_LOCATION;
import static com.eworkplaceapps.employeedirectory.location.LocationAccess.LocationOperation.UPDATE_LOCATION;
import static com.eworkplaceapps.employeedirectory.location.LocationAccess.LocationOperation.VIEW_LOCATION;
import static com.eworkplaceapps.employeedirectory.department.DepartmentAccess.DepartmentOperation.ADD_DEPARTMENT;
import static com.eworkplaceapps.employeedirectory.department.DepartmentAccess.DepartmentOperation.DELETE_DEPARTMENT;
import static com.eworkplaceapps.employeedirectory.department.DepartmentAccess.DepartmentOperation.UPDATE_DEPARTMENT;
import static com.eworkplaceapps.employeedirectory.department.DepartmentAccess.DepartmentOperation.VIEW_DEPARTMENT;

/**
 * class to get the access permissions
 */
public class OauthPermissions {
    private final static Logger log = Logger.getLogger(OauthPermissions.class);
    private static OauthPermissions permissions;
    private static boolean[] accessVector;
    private static boolean[] teamAccessVector;
    private static boolean[] locationAccessVector;
    private static boolean[] departmentAccessVector;
    private boolean isViewEmp, isAddEmp, isDeleteEmp, isUpdateEmp, isViewEmergencyContact, manageEmpDept, manageEmpLoc, manageEmpTeam, updateReportsTo, updateStartDate;
    private boolean isTeamView, isTeamAdd, isTeamDelete, isTeamUpdate;
    private boolean isDeptView, isDeptAdd, isDeptUpdate, isDeptDelete;
    private boolean isLocView, isLocAdd, isLocUpdate, isLocDelete;
    private boolean isStatusConfigView, isStatusConfigAdd, isStatusConfigUpdate, isStatusConfigDelete;


    /**
     * Create private constructor
     */
    private OauthPermissions() {

    }

    /**
     * Create a static method to get instance.
     */
    public static OauthPermissions getInstance(UUID entityId) {
        try {
            if (permissions == null) {
                permissions = new OauthPermissions();
            }
            // get permissions for employee
            EmployeeAccess employeeAccess = new EmployeeAccess();
            accessVector = employeeAccess.accessList(entityId, EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId());
            permissions.setIsViewEmp(accessVector[VIEW_EMPLOYEE.getId()]);
            permissions.setIsAddEmp(accessVector[ADD_EMPLOYEE.getId()]);
            permissions.setIsUpdateEmp(accessVector[UPDATE_EMPLOYEE.getId()]);
            permissions.setIsDeleteEmp(accessVector[DELETE_EMPLOYEE.getId()]);
            permissions.setManageEmpDept(accessVector[MANAGE_EMPLOYEE_DEPARTMENT.getId()]);
            permissions.setManageEmpLoc(accessVector[MANAGE_EMPLOYEE_LOCATION.getId()]);
            permissions.setManageEmpTeam(accessVector[MANAGE_EMPLOYEE_TEAM.getId()]);
            permissions.setUpdateReportsTo(accessVector[UPDATE_REPORTSTO.getId()]);
            permissions.setUpdateStartDate(accessVector[UPDATE_STARTDATE.getId()]);
            permissions.setIsViewEmergencyContact(accessVector[VIEW_EMERGENCYCONTACT.getId()]);

            // get permissions for team
            EmployeeGroupAccess employeeGroupAccess = new EmployeeGroupAccess();
            teamAccessVector = employeeGroupAccess.accessList(entityId, EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId());
            permissions.setIsTeamAdd(teamAccessVector[ADD_EMPLOYEE_GROUP.getId()]);
            permissions.setIsTeamDelete(teamAccessVector[DELETE_EMPLOYEE_GROUP.getId()]);
            permissions.setIsTeamUpdate(teamAccessVector[UPDATE_EMPLOYEE_GROUP.getId()]);
            permissions.setIsTeamView(teamAccessVector[VIEW_EMPLOYEE_GROUP.getId()]);

            // get permissions for location
            LocationAccess locationAccess = new LocationAccess();
            locationAccessVector = locationAccess.accessList(entityId, EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId());
            permissions.setIsLocAdd(locationAccessVector[ADD_LOCATION.getId()]);
            permissions.setIsLocDelete(locationAccessVector[DELETE_LOCATION.getId()]);
            permissions.setIsLocUpdate(locationAccessVector[UPDATE_LOCATION.getId()]);
            permissions.setIsLocView(locationAccessVector[VIEW_LOCATION.getId()]);

            // get permissions for department
            DepartmentAccess departmentAccess = new DepartmentAccess();
            departmentAccessVector = departmentAccess.accessList(entityId, EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId());
            permissions.setIsDeptAdd(departmentAccessVector[ADD_DEPARTMENT.getId()]);
            permissions.setIsDeptDelete(departmentAccessVector[DELETE_DEPARTMENT.getId()]);
            permissions.setIsDeptUpdate(departmentAccessVector[UPDATE_DEPARTMENT.getId()]);
            permissions.setIsDeptView(departmentAccessVector[VIEW_DEPARTMENT.getId()]);

        } catch (EwpException e) {
            log.error("OauthPermission Exception-> " + e);
        }

        return permissions;
    }

    public boolean isViewEmp() {
        return isViewEmp;
    }

    public void setIsViewEmp(boolean isViewEmp) {
        this.isViewEmp = isViewEmp;
    }

    public boolean isAddEmp() {
        return isAddEmp;
    }

    public void setIsAddEmp(boolean isAddEmp) {
        this.isAddEmp = isAddEmp;
    }

    public boolean isDeleteEmp() {
        return isDeleteEmp;
    }

    public void setIsDeleteEmp(boolean isDeleteEmp) {
        this.isDeleteEmp = isDeleteEmp;
    }

    public boolean isUpdateEmp() {
        return isUpdateEmp;
    }

    public void setIsUpdateEmp(boolean isUpdateEmp) {
        this.isUpdateEmp = isUpdateEmp;
    }

    public boolean isManageEmpDept() {
        return manageEmpDept;
    }

    public void setManageEmpDept(boolean manageEmpDept) {
        this.manageEmpDept = manageEmpDept;
    }

    public boolean isManageEmpLoc() {
        return manageEmpLoc;
    }

    public void setManageEmpLoc(boolean manageEmpLoc) {
        this.manageEmpLoc = manageEmpLoc;
    }

    public boolean isManageEmpTeam() {
        return manageEmpTeam;
    }

    public void setManageEmpTeam(boolean manageEmpTeam) {
        this.manageEmpTeam = manageEmpTeam;
    }

    public boolean isUpdateReportsTo() {
        return updateReportsTo;
    }

    public void setUpdateReportsTo(boolean updateReportsTo) {
        this.updateReportsTo = updateReportsTo;
    }

    public boolean isUpdateStartDate() {
        return updateStartDate;
    }

    public void setUpdateStartDate(boolean updateStartDate) {
        this.updateStartDate = updateStartDate;
    }

    public boolean isTeamAdd() {
        return isTeamAdd;
    }

    public void setIsTeamAdd(boolean isTeamAdd) {
        this.isTeamAdd = isTeamAdd;
    }

    public boolean isTeamView() {
        return isTeamView;
    }

    public void setIsTeamView(boolean isTeamView) {
        this.isTeamView = isTeamView;
    }

    public boolean isTeamDelete() {
        return isTeamDelete;
    }

    public void setIsTeamDelete(boolean isTeamDelete) {
        this.isTeamDelete = isTeamDelete;
    }

    public boolean isTeamUpdate() {
        return isTeamUpdate;
    }

    public void setIsTeamUpdate(boolean isTeamUpdate) {
        this.isTeamUpdate = isTeamUpdate;
    }

    public boolean isDeptView() {
        return isDeptView;
    }

    public void setIsDeptView(boolean isDeptView) {
        this.isDeptView = isDeptView;
    }

    public boolean isDeptAdd() {
        return isDeptAdd;
    }

    public void setIsDeptAdd(boolean isDeptAdd) {
        this.isDeptAdd = isDeptAdd;
    }

    public boolean isDeptUpdate() {
        return isDeptUpdate;
    }

    public void setIsDeptUpdate(boolean isDeptUpdate) {
        this.isDeptUpdate = isDeptUpdate;
    }

    public boolean isDeptDelete() {
        return isDeptDelete;
    }

    public void setIsDeptDelete(boolean isDeptDelete) {
        this.isDeptDelete = isDeptDelete;
    }

    public boolean isLocView() {
        return isLocView;
    }

    public void setIsLocView(boolean isLocView) {
        this.isLocView = isLocView;
    }

    public boolean isLocAdd() {
        return isLocAdd;
    }

    public void setIsLocAdd(boolean isLocAdd) {
        this.isLocAdd = isLocAdd;
    }

    public boolean isLocUpdate() {
        return isLocUpdate;
    }

    public void setIsLocUpdate(boolean isLocUpdate) {
        this.isLocUpdate = isLocUpdate;
    }

    public boolean isStatusConfigView() {
        return isStatusConfigView;
    }

    public void setIsStatusConfigView(boolean isStatusConfigView) {
        this.isStatusConfigView = isStatusConfigView;
    }

    public boolean isLocDelete() {
        return isLocDelete;
    }

    public void setIsLocDelete(boolean isLocDelete) {
        this.isLocDelete = isLocDelete;
    }

    public boolean isStatusConfigAdd() {
        return isStatusConfigAdd;
    }

    public void setIsStatusConfigAdd(boolean isStatusConfigAdd) {
        this.isStatusConfigAdd = isStatusConfigAdd;
    }

    public boolean isStatusConfigUpdate() {
        return isStatusConfigUpdate;
    }

    public void setIsStatusConfigUpdate(boolean isStatusConfigUpdate) {
        this.isStatusConfigUpdate = isStatusConfigUpdate;
    }

    public boolean isStatusConfigDelete() {
        return isStatusConfigDelete;
    }

    public void setIsStatusConfigDelete(boolean isStatusConfigDelete) {
        this.isStatusConfigDelete = isStatusConfigDelete;
    }

    public boolean isViewEmergencyContact() {
        return isViewEmergencyContact;
    }

    public void setIsViewEmergencyContact(boolean isViewEmergencyContact) {
        this.isViewEmergencyContact = isViewEmergencyContact;
    }
}
