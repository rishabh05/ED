//===============================================================================
// 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.department;

import android.database.Cursor;

import com.eworkplaceapps.employeedirectory.employee.EDApplicationInfo;
import com.eworkplaceapps.employeedirectory.employee.EmployeeDataService;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.employeedirectory.employee.GroupBy;
import com.eworkplaceapps.employeedirectory.location.Location;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Department entities.
 * It provides the DEPARTMENT information.
 * By using this service we can get the DEPARTMENT entity list from head of DEPARTMENT and tenant.
 */
public class DepartmentDataService extends BaseDataService<Department> {

    private DepartmentData dataDelegate = new DepartmentData();

    public DepartmentDataService() {
        super("DepartmentDataService");
    }

    @Override
    public BaseData<Department> getDataClass() {
        return dataDelegate;
    }


    /**
     * Method is used to get DEPARTMENT list as fmresultset from tenantId and HeadOfDepartmentId.
     *
     * @param tenantId
     * @param deptId
     * @return List<Department>
     */
    public List<Department> getDepartmentListFromTenantIdAndHeadOfDeptAsEntityList(UUID tenantId, UUID deptId) throws EwpException {
        List<Department> departmentList = dataDelegate.getDepartmentListFromTenantIdAndHeadOfDeptAsEntityList(tenantId, deptId);
        return departmentList;
    }

    /**
     * Method is used to get DEPARTMENT list from tenantId and HeadOfDepartmentId.
     *
     * @param tenantId
     * @param deptId
     * @return Cursor
     */
    public Cursor getDepartmentListFromTenantIdAndHeadOfDeptAsResultSet(UUID tenantId, UUID deptId) throws EwpException {
        Cursor response = dataDelegate.getDepartmentListFromTenantIdAndHeadOfDeptAsResultSet(tenantId, deptId);
        return response;
    }

    /**
     * This method is used to check permission on given operation.
     *
     * @param entity
     * @param operation
     * @return boolean
     */
    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operation) {
        DepartmentAccess access = new DepartmentAccess();
        return access.checkPermissionOnOperation(operation, EnumsForExceptions.ErrorModule.DATA_SERVICE);
    }

    /**
     * Method is used to set employee department.
     *
     * @param deptModel
     */
    public void updateDepartment(DepartmentModel deptModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        super.update(deptModel.getDepartment());
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(deptModel.getCommunications(), deptModel.getDepartment().getEntityId(), EDEntityType.DEPARTMENT.getId(), EDApplicationInfo.getAppId().toString());
        EmployeeDataService empService = new EmployeeDataService();
        empService.setEmployeeDepartment(deptModel.getEmployeeList(), deptModel.getDepartment().getEntityId());
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    public Object addDepartment(DepartmentModel deptModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        Object id = super.add(deptModel.getDepartment());

        deptModel.getDepartment().setEntityId((UUID) id);
        CommunicationDataService commService = new CommunicationDataService();
        commService.addCommunicationList(deptModel.getCommunications(), deptModel.getDepartment().getEntityId(), EDEntityType.DEPARTMENT.getId(), EwpSession.PLATFORM_APPLICATION_ID);
        EmployeeDataService empService = new EmployeeDataService();
        empService.setEmployeeDepartment(deptModel.getEmployeeList(), deptModel.getDepartment().getEntityId());
        DatabaseOps.defaultDatabase().commitTransaction();
        return id;
    }

    public DepartmentModel getDepartmentEntityAsModel(UUID id) throws EwpException {
        Department department = dataDelegate.getEntity(id);
        DepartmentModel deptModel = new DepartmentModel();
        deptModel.setDepartment(department);
        CommunicationDataService commService = new CommunicationDataService();
        // Getting the employee contact infromation and map contact information to ViewEmployee object.
        // Employee communication contact list like Work phone, Home phone, mobile email etc.
        List<Communication> communicationList = commService.getCommunicationListFromSourceEntityIdAndType(id, EDEntityType.DEPARTMENT.getId());
        if (communicationList != null) {
            deptModel.setCommunications(communicationList);
        }
        List<EmployeeQuickView> employeeQuickViews = EmployeeQuickView.getEmployeeQuickViewList(GroupBy.DEPARTMENT, null, id);
        deptModel.setEmployeeList(employeeQuickViews);
        return deptModel;
    }
    @Override
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        entity.validate();
        Department department = (Department) entity;
        boolean result = dataDelegate.departmentExists(department.getEntityId(),department.getName(),department.getTenantId());
        if (result) {
            EwpException ewpException = new EwpException();
            ewpException.errorType = EnumsForExceptions.ErrorType.DUPLICATE;
            ewpException.setLocalizedMessage("A Department with that name already exists.");
            throw ewpException;
        }
    }
    /**
     * It is overrided by Location service class.
     * If Department entity will be delete then it reference entity(communication) will also be delete.
     *
     * @param entity
     */
    @Override
    public void delete(Department entity) throws EwpException {
        if (entity != null) {
            DatabaseOps.defaultDatabase().beginDeferredTransaction();
            super.delete(entity);
            CommunicationDataService commService = new CommunicationDataService();
            commService.deleteCommunicationListFromSourceEntityIdAndType(entity.getEntityId(), EDEntityType.DEPARTMENT.getId());
            EmployeeDataService empService = new EmployeeDataService();
            empService.updateEmployeeDepartmentEmpty(entity.getEntityId());
            DatabaseOps.defaultDatabase().commitTransaction();
        }
    }
}
