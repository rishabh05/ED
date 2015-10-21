//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;
import android.util.Log;

import com.eworkplaceapps.employeedirectory.employee.Employee.EmployeeEntityFieldId;
import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.address.AddressDataService;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.customfield.CustomFieldEnums;
import com.eworkplaceapps.platform.customfield.EntityCustomFieldDataService;
import com.eworkplaceapps.platform.customfield.EntityCustomFieldDefData;
import com.eworkplaceapps.platform.customfield.EntityFieldValueDetails;
import com.eworkplaceapps.platform.customfield.UDFValues;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.note.Note;
import com.eworkplaceapps.platform.note.NoteDataService;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.security.Role;
import com.eworkplaceapps.platform.security.RoleDataService;
import com.eworkplaceapps.platform.security.RoleLinking;
import com.eworkplaceapps.platform.security.RoleLinkingDataService;
import com.eworkplaceapps.platform.tenant.TenantUser;
import com.eworkplaceapps.platform.tenant.TenantUserDataService;
import com.eworkplaceapps.platform.userentitylink.PFUserEntityLink;
import com.eworkplaceapps.platform.userentitylink.PFUserEntityLinkDataService;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;
import com.eworkplaceapps.platform.utils.enums.LinkType;
import com.eworkplaceapps.platform.utils.enums.PhoneType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.Employee.EmployeeEntityFieldId.values;


/**
 * It expand the BaseDataService to provide services to Employee entities.
 * Class is used to validate employee fields. It check employee duplicacy from email.
 * Employee service class overidding the add/update/delete methods, Because we are adding tenanuser, role permission for each employee when employee add as new employee.
 * On employee deleteion, we are deleting all references.
 */
public class EmployeeDataService extends BaseDataService {

    private EmployeeData dataDelegate = new EmployeeData();

    /// Initializes a new instance of the EmployeeDataService class.
    public EmployeeDataService() {
        super("EmployeeDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }


    /**
     * It validate an entity and returns result as EwpError object variable.
     */
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        boolean error = entity.validate();
        if (!error) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().logError(new EwpException("" + error));
        }
        Employee emp = (Employee) entity;
        boolean resultTuple = dataDelegate.employeeExists(emp.getLoginEmail(), emp.getTenantId(), emp.getEntityId());
        if (!resultTuple) {
            List<String> message = new ArrayList<>();
            Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
            message.add(AppMessage.DUPLICATE_NAME);
            dicError.put(EnumsForExceptions.ErrorDataType.DUPLICATE, new String[]{"Email"});
            throw new EwpException(new EwpException("VALIDATION_ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    /**
     * This method is used to check permission on given operation.
     */
    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operationPermission) {
    /*    EmployeeAccess access = new EmployeeAccess();
        return access.checkAccess(operationPermission, EnumsForExceptions.ErrorModule.DATA_SERVICE);*/
        return true;
    }


    /**
     * @param entity
     * @return Object
     */
    @Override
    public Object add(BaseEntity entity) throws EwpException {
        Employee employee = (Employee) entity;
        /// Begin transaction
        DatabaseOps.defaultDatabase().beginTransaction();
        /// Creating the tenantUser and initializing the user properties by names and Adding the tenant user.
        TenantUserDataService service = new TenantUserDataService();
        TenantUser tUser = new TenantUser();
        tUser.setFirstName(employee.getFirstName());
        tUser.setLastName(employee.getLastName());
        tUser.setLoginEmail(employee.getLoginEmail());
        tUser.setTenantId(employee.getTenantId());
        Object id = service.add(tUser);
        if (id == null) {
            Log.d(this.getClass().getName(), "" + id);
            return id;
        }
        /// Setting TenantUserId and Adding the Employee.
        employee.setTenantUserId((UUID) id);
        Object id1 = super.add(employee);
        if (id1 == null) {
            Log.d(this.getClass().getName(), "" + id1);
            return id1;
        }
        RoleDataService roleService = new RoleDataService();
        /// Getting the default employee permission to access the employee application.
        Role resultTupleRole = null;
        resultTupleRole = roleService.getDefaultEmployeeRole();
        if (resultTupleRole == null) {
            Log.d(this.getClass().getName(), "" + resultTupleRole);
            return resultTupleRole;
        }
        RoleLinkingDataService roleLinkingService = new RoleLinkingDataService();
        /// Adding the default permission with employee to access the employee application.
        RoleLinking roleLinking = new RoleLinking();
        roleLinking.setTenantId(employee.getTenantId());
        roleLinking.setUserId(employee.getTenantUserId());
        Object resultTupleRoleLinking = roleLinkingService.add(roleLinking);
        /// Commit transaction.
        DatabaseOps.defaultDatabase().commitTransaction();
        return id1;
    }

    /**
     * Method will update the tenant user with the updation of employee.
     *
     * @param entity
     */
    public void update(BaseEntity entity) throws EwpException {
        update(entity, true);
    }

    /**
     * Method is used to set employee location as NULL where given location id is set for employees.
     *
     * @param locationId
     * @throws EwpException
     */
    public void updateEmployeeLocationEmpty(UUID locationId) throws EwpException {
        dataDelegate.updateEmployeeLocationEmpty(locationId);
    }

    /**
     * Method is used to set employee location as NULL where given location id is set for employees.
     */
    public void updateEmployeeLocationEmpty(UUID locationId, UUID employeeId) throws EwpException {
        dataDelegate.updateEmployeeLocationEmpty(locationId, employeeId);
    }

    /**
     * Method is used to set employee department as NULL where given department id is set for employees.
     *
     * @param departmentId
     * @throws EwpException
     */
    public void updateEmployeeDepartmentEmpty(UUID departmentId) throws EwpException {
        dataDelegate.updateEmployeeDepartmentEmpty(departmentId);
    }

    /**
     * Method is used to set employee location as NULL where given location id is set for employees.
     *
     * @param departmentId
     * @param employeeId
     * @throws EwpException
     */
    public void updateEmployeeDepartmentEmpty(UUID departmentId, UUID employeeId) throws EwpException {
        dataDelegate.updateEmployeeDepartmentEmpty(departmentId, employeeId);
    }

    /**
     * Method will update the tenant user with the updation of employee.
     *
     * @param entity
     * @param addTransaction
     */
    public void update(BaseEntity entity, boolean addTransaction) throws EwpException {
        Employee employee = (Employee) entity;
        if (addTransaction) {
            DatabaseOps.defaultDatabase().beginTransaction();
        }
        TenantUserDataService service = new TenantUserDataService();
        BaseEntity resultTuple = service.getEntity(employee.getTenantUserId());

        /// Setting tenantuser properties.
        TenantUser tUser = (TenantUser) resultTuple;
        tUser.setFirstName(employee.getFirstName());
        tUser.setLastName(employee.getLastName());
        tUser.setLoginEmail(employee.getLoginEmail());
        /// Updating the tenantuser
        service.update(tUser);
        super.update(employee);

        if (addTransaction) {
            /// On success, Commit transection.
            DatabaseOps.defaultDatabase().commitTransaction();
        }
    }

    /**
     * Delete an entity
     *
     * @param entity
     */
    @Override
    public void delete(BaseEntity entity) throws EwpException {
        /// Set operation mode
        entity.setLastOperationType(DatabaseOperationType.DELETE);
        /// Check login user permission on Delete entity object.

        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        Employee emp = (Employee) entity;
        /// Deleting employee entity.
        getDataClass().delete(entity);
        TenantUserDataService service = new TenantUserDataService();
        /// Delete the user from TenantUser
        service.getDataClass().delete(emp);
        /// Delete the role from rolelinking table
        RoleLinkingDataService roleLinkingService = new RoleLinkingDataService();
        RoleLinking roleLinking = RoleLinking.createEntity();
        roleLinking.setEntityId(emp.getEntityId());
        roleLinkingService.getDataClass().delete(roleLinking);
        /// Delete the user from group
        EmployeeGroupMemberDataService memberService = new EmployeeGroupMemberDataService();
        memberService.deleteMemberByEmployeeId(emp.getEntityId());
        /// On success, Commit transection.
        DatabaseOps.defaultDatabase().commitTransaction();
        removeFromCache(entity);
    }

    /**
     * Method is used to search the employee by name. If passed searched string appear at any where in employee name then return those employee list.
     *
     * @param searchCharInName
     * @return List<Employee>
     */
    public List<Employee> searchEmployee(String searchCharInName) throws EwpException {
        List<Employee> resultTuple = dataDelegate.searchEmployee(EwpSession.getSharedInstance().getTenantId(), searchCharInName);
        if (resultTuple == null) {
            throw new EwpException("resultTuple is null");
        }
        return resultTuple;
    }

    /**
     * It is used to get currently absent employee list.
     *
     * @param tenantId
     * @return List<Employee>
     */
    public List<Employee> getCurrentDateEmployeeAbsenceList(UUID tenantId) throws EwpException {
        List<Employee> resultTuple = dataDelegate.getCurrentDateEmployeeAbsenceList(EwpSession.getSharedInstance().getTenantId());
        if (resultTuple == null) {
            Log.d(this.getClass().getName(), "" + resultTuple);
        }
        return resultTuple;
    }

    /**
     * It will give the list of employee and custom fields detail with the value.
     * EntityFieldValueDetails object will give fields information like field is required, its datatype, Its value and type of field.
     * Type of field defined in 3 types of classes. 1) system 2) builtin 3) custom.
     *
     * @param entityId
     * @param entityType
     * @return List<EntityFieldValueDetails>
     */
    public List<EntityFieldValueDetails> getEmployeeFieldDetailValueList(UUID entityId, int entityType) throws EwpException {
        EntityCustomFieldDataService service = new EntityCustomFieldDataService();
        Cursor cursor = service.getViewableEntityCustomFieldAndPermissionListByUserId(EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId(), entityType);
        List<EntityFieldValueDetails> customFieldInfoList = new ArrayList<EntityFieldValueDetails>();

        BaseEntity resultTuple = getEntity(entityId);

        /// If employee entity not found or any error occured then return nil.
        if (cursor == null) {
            return null;
        }

        Employee employee = (Employee) resultTuple;
        boolean hasCustomColumn = false;
        /// Loo- through all fields.
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("EntityFieldDetailsId"));
            EntityFieldValueDetails entityFieldValueDetails;
            if (id == null) {
                continue;
            } else {
                /// create the CustomField object and initilize with EntityCustomFieldDef object.
                entityFieldValueDetails = EntityFieldValueDetails.createEntity();
                List<Object> values = new ArrayList<Object>();
                List<Object> valuesText = new ArrayList<Object>();
                entityFieldValueDetails.setValues(values);
                entityFieldValueDetails.setValuesText(valuesText);
                EntityCustomFieldDefData defData = new EntityCustomFieldDefData();
                defData.setPropertiesFromResultSet(entityFieldValueDetails, cursor);
                setSystemFieldPropertyValue(entityFieldValueDetails, employee);
            }
            hasCustomColumn = hasCustomColumn || (entityFieldValueDetails.getFieldClass() == CustomFieldEnums.FieldClass.CUSTOM.getId());
            /// Adding if field is custom or if field has value.
            if (entityFieldValueDetails.getFieldClass() == CustomFieldEnums.FieldClass.CUSTOM.getId() || entityFieldValueDetails.getValuesText().size() > 0) {
                customFieldInfoList.add(entityFieldValueDetails);
            }
        }

        /// UDFValues table save the userdefined values with respact to entityid.
        /// It is used to get the custom field values from UDFValue table. It will append the UDF(user defined field) value in customfields value object
        /// with matched fieldcode.
        if (hasCustomColumn) {
            return new UDFValues().mapFromUDFFieldValues(entityId, customFieldInfoList);
        } else {
            /// If no custom fields are there then return with included system fields.
            return customFieldInfoList;
        }
    }

    /**
     * It is used to map the column value with respact to their field code.
     * It is used to set System and Builtin field values.
     *
     * @param detail
     * @param employee
     */
    private void setSystemFieldPropertyValue(EntityFieldValueDetails detail, Employee employee) {

        if (detail.getFieldCode() == CustomFieldEnums.FieldClass.CUSTOM.getId()) {
            return;
        }
        EmployeeEntityFieldId detailId = values()[detail.getFieldCode()];
        if (detailId == null) {
            return;
        }
        switch (detailId) {
            case FIRST_NAME:
                detail.getValues().add(employee.getFirstName());
                detail.getValuesText().add(employee.getFirstName());
                break;
            case LAST_NAME:
                detail.getValues().add(employee.getLastName());
                detail.getValuesText().add(employee.getLastName());
                break;
            case NICK_NAME:
                detail.getValues().add(employee.getNickName());
                detail.getValuesText().add(employee.getNickName());
                break;
            case REPORT_TO:
                detail.getValues().add(employee.getReportTo());
                break;
            case START_DATE:
                detail.getValues().add(employee.getStartDate());
                String str = Utils.stringFromDate(employee.getStartDate());
                detail.getValuesText().add(str);
                break;
            case JOB_TITLE:
                detail.getValues().add(employee.getJobTitle());
                detail.getValuesText().add(employee.getJobTitle());
                break;
            case BIRTHDAY:
                detail.getValues().add(employee.getBirthDay());
                str = Utils.stringFromDate(employee.getBirthDay());
                detail.getValuesText().add(str);
                break;
            default:
                break;
        }
    }

    /**
     * It is used to get employee from UserId.
     *
     * @param userId
     * @return Employee
     */
    public Employee getEmployeeFromUserId(UUID userId) throws EwpException {
        Employee resultTuple = dataDelegate.getEmployeeFromUserId(userId);
        if (resultTuple == null) {
            Log.d(this.getClass().getName(), "Employee is  null at getEmployeeFromUserId");
        }
        return resultTuple;
    }

    /**
     * It is used to get employee list, Those are created in-between of a week.
     *
     * @param tenantId
     * @return List<Employee>
     */
    public List<Employee> getLastWeekCreatedEmployeeListByTenantId(UUID tenantId) throws EwpException {
        Date sDate = EmployeeStatusData.addDaysToDate(new Date(), -5);
        sDate = EmployeeStatusData.addDaysToDate(sDate, -7);
        Date eDate = EmployeeStatusData.addDaysToDate(sDate, 7);
        List<Employee> resultTuple = dataDelegate.getEmployeeListCreatedBetweenGivenDateAndTenantId(tenantId, sDate, eDate);
        if (resultTuple == null) {
            Log.d(this.getClass().getName(), "List<Employee>  is null getEmployeeListCreatedBetweenGivenDateAndTenantId");
        }
        return resultTuple;
    }

    /**
     * It is used to get employee list as result set, Those are created in-between of a week.
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getCurrentWeekEmployeeListByTenantIdAsResultSet(UUID tenantId) throws EwpException {
        Date sDate = EmployeeStatusData.addDaysToDate(new Date(), 5);
        Date eDate = EmployeeStatusData.addDaysToDate(sDate, 7);
        Cursor resultTuple = dataDelegate.getEmployeeListCreatedBetweenGivenDateAndTenantIdAsResultSet(tenantId, sDate, eDate);
        if (resultTuple != null) {
            Log.d(this.getClass().getName(), "Cursor is null from getEmployeeListCreatedBetweenGivenDateAndTenantIdAsResultSet");
        }
        return resultTuple;
    }

    /**
     * It is used to get employee list, Those are subscribed for week emial notification.
     *
     * @return List<Employee>
     */
    public List<Employee> getWeekEmailSubscribedEmployeeList() throws EwpException {
        List<Employee> resultTuple = dataDelegate.getWeekEmailSubscribedEmployeeList();
        return resultTuple;
    }


    /**
     * /// Method is used to follow/unfollow an employee.
     * /// :param: employeeid, is id of an employee to follow for loggedin user.
     * /// :param: followUp, To follow employee or not.
     *
     * @param employeeId
     * @param followUp
     */
    public void markAsFollowUp(UUID employeeId, boolean followUp) throws EwpException {
        PFUserEntityLinkDataService service = new PFUserEntityLinkDataService();
        PFUserEntityLink resultTuple = service.getUserEntityFromSourceIdSourceTypeAndLinkType(EwpSession.getSharedInstance().getTenantId(), LinkType.FOLLOW_UP.getId(), EDEntityType.EMPLOYEE.getId(), employeeId, EwpSession.getSharedInstance().getUserId());
        if (resultTuple == null) {
            Log.d(this.getClass().getName(), "getUserEntityFromSourceIdSourceTypeAndLinkType()");
        }
        if (followUp) {
            // If entity not found then mark employee as followup employee for logged in user.
            if (resultTuple == null) {
                String appId = EDApplicationInfo.getAppId().toString();
                Object result = service.addUserEntityFromSourceIdSourceTypeAndLinkType(LinkType.FOLLOW_UP.getId(), EDEntityType.EMPLOYEE.getId(), employeeId, 0, true, "", appId);
            }
        } else if (resultTuple != null) {
            service.delete(resultTuple);
        }
    }

    /**
     * Method is used to get the employee from emloyeeid as ViewEmployee model.
     * <p/>
     * ViewEmployee model will give the detail information of an empolyee.
     * <p/>
     * :param: employeeId is the id of an employee.
     * :returns: Returns the viewemployee model object and error object.
     *
     * @param employeeId
     * @return EmployeeModel
     */
  /*  public ViewEmployee getViewEmployee(UUID employeeId) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeEntityAsResultSet(employeeId);
        if (cursor == null) {
            throw new EwpException("cursor is null");
        }
        ViewEmployee viewEmployee = null;
        UUID locationId = null;
        viewEmployee = ViewEmployee.setViewEmployeeProperties(cursor);
        String locId = cursor.getString(cursor.getColumnIndex("LocationId"));
        if (locId != null && !"".equals(locId)) {
            locationId = UUID.fromString(locId);
        }
        if (viewEmployee == null) {
            return null;
        }

        // Finding employee self address.
        ViewEmployeeAddress viewEmployeeAddress = getViewAddressFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId());
        // Getting the address and map address information to ViewEmployee model object.
        viewEmployee.setAddressInfo(viewEmployeeAddress);
        if (locationId != null)
            viewEmployee.setLocationAddressInfo(new LocationDataService().getLocationViewAddressFromSourceEntityIdAndType(locationId));
        // Getting the employee contact information and map contact information to ViewEmployee object.
        // Employee communication contact list like Work phone, Home phone, mobile email etc.
        List<ViewEmployeeCommunication> resultCommTuple = getViewCommunicationListFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId());
        viewEmployee.setCommunicationList(resultCommTuple);
        // Getting the emargency contact list and map these information to ViewEmployee object.
        EmployeeContactDataService emargancyContactService = new EmployeeContactDataService();
        List<ViewEmployeeContact> emergencyContactDetailTuple = emargancyContactService.getViewEmployeeContactListFromSourceEntityIdAndType(employeeId);
        viewEmployee.setEmergencyContactList(emergencyContactDetailTuple);
        // Is logged in user following this employeeid.
        if (!EwpSession.getSharedInstance().getUserId().equals(viewEmployee.getUserId())) {
            PFUserEntityLinkDataService pfService = new PFUserEntityLinkDataService();
            viewEmployee.setFollowUp(pfService.isEmployeeFollowing(employeeId));
            viewEmployee.setFavorite(pfService.isUserEntityLinkExist(employeeId, LinkType.FAVOURITE.getId()));
        }
        EmployeeGroupDataService grpDataService = new EmployeeGroupDataService();
        // Getting the list of teams, Fow which teams user is member.
        List<String> teamNameList = grpDataService.getEmployeeTeamListAsStringFromEmployeeId(EwpSession.getSharedInstance().getTenantId(), employeeId);
        viewEmployee.setTeamNameList(teamNameList);
        // Getting logged in user not for a employee.
        NoteDataService noteService = new NoteDataService();
        Note noteTuple = noteService.getNoteFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId());
        if (noteTuple != null) {
            viewEmployee.setNote(noteTuple.getNote());
        }
        return viewEmployee;
    }*/

    /**
     * Method is used to get view communication from EntityType and EntiyId.
     * :param: sourceEntityId, It source entityId, For which we have saved the communication.
     * :param: sourceEntityType, It source entity type, For which we have saved the communication
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return List<ViewEmployeeCommunication>
     * @throws EwpException
     */
    private List<ViewEmployeeCommunication> getViewCommunicationListFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        CommunicationDataService commService = new CommunicationDataService();
        Cursor cursor = commService.getCommunicationListFromSourceEntityIdAndTypeAsResultSet(sourceEntityId, sourceEntityType);
        if (cursor != null) {
            List<ViewEmployeeCommunication> communicationList = ViewEmployeeCommunication.setPropertiesAndGetViewEmployeeCommunicationList(cursor);
            return communicationList;
        }
        return null;
    }

    /**
     * Method is used to get address from EntityType and EntiyId
     * :param: sourceEntityid, It source entityId, For which we have saved the address
     * :param: sourceEntityType, It source entity type, For which we have saved the address
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return ViewEmployeeAddress
     * @throws EwpException
     */
    public ViewEmployeeAddress getViewAddressFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        AddressDataService addService = new AddressDataService();
        Address address = addService.getAddressFromSourceEntityIdAndType(sourceEntityId, sourceEntityType);
        if (address == null) {
            Log.d(this.getClass().getName(), "Error at getAddressFromSourceEntityIdAndType method in AddressDataService");
            return null;
        }
        ViewEmployeeAddress vAddress = ViewEmployeeAddress.setViewAddressPropertiesFromAddressEntity(address);
        return vAddress;
    }

    /**
     * Method is used to get the employee from emloyeeid as ViewEmployee model.
     * ViewEmployee model will give the detail information of an empolyee
     * :param: employeeid is the id of an employee.
     * :returns: Returns the viewemployee model object and error object.
     *
     * @param employeeId
     * @return EmployeeModel
     */
    public EmployeeModel getEmployee(UUID employeeId) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeEntityAsResultSet(employeeId);
        if (cursor == null) {
            throw new EwpException("cursor is null at getEmployee EmployeeDataService");
        }
        EmployeeModel viewEmployee = new EmployeeModel();
        viewEmployee = EmployeeModel.setViewEmployeeProperties(cursor);
        if (viewEmployee == null || viewEmployee.getEmployee() == null) {
            return null;
        }
        AddressDataService addService = new AddressDataService();
        // Finding employee self address.
        Address address = addService.getAddressFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId());
        if (address == null) {
            Log.d(this.getClass().getName(), "Error to find employee Address");
        }
        // Getting the address and map address information to ViewEmployee model object.
        viewEmployee.setAddressInfo(address);
        CommunicationDataService commService = new CommunicationDataService();
        // Getting the employee contact infromation and map contact information to ViewEmployee object.
        // Employee communication contact list like Work phone, Home phone, mobile email etc.
        List<Communication> communicationList = commService.getCommunicationListFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId());
        if (communicationList == null) {
            Log.d(this.getClass().getName(), "Error to find employee Communication detail.");
        }
        viewEmployee.setCommunicationList(communicationList);
        // Getting the emargency contact list and map these information to ViewEmployee object.
        EmployeeContactDataService emergencyContactService = new EmployeeContactDataService();
        List<EmployeeEmergencyContact> emergencyContact = emergencyContactService.getEmployeeContactDetailFromSourceEntityIdAndType(employeeId);
        viewEmployee.setEmployeeEmergencyContactDetail(emergencyContact);
        PFUserEntityLinkDataService pfService = new PFUserEntityLinkDataService();
        viewEmployee.setFollowUp(pfService.isEmployeeFollowing(employeeId));
        // Is logged in user following this employeeid.
        if (!EwpSession.getSharedInstance().getUserId().equals(viewEmployee.getEmployee().getTenantUserId())) {
            viewEmployee.setFavorite(pfService.isUserEntityLinkExist(employeeId, LinkType.FAVOURITE.getId()));
        }
        EmployeeGroupDataService grpDataService = new EmployeeGroupDataService();
        // Getting the list of teams, Fow which teams user is member.
        List<EditEmployeeTeam> resultTuple1 = grpDataService.getEmployeeTeamListAsEditEmployeeTeamFromEmployeeId(EwpSession.getSharedInstance().getTenantId(), employeeId);
        viewEmployee.setTeamNameList(resultTuple1);
        // Getting logged in user note for a employee.
        NoteDataService noteService = new NoteDataService();
        Note noteTuple = noteService.getNoteFromSourceEntityIdAndType(employeeId, EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId());
        if (noteTuple != null) {
            viewEmployee.setNote(noteTuple);
        }
        return viewEmployee;
    }

    /**
     * Method is used to update the employee from Employee model.
     * Employee model will give the detail updated information of an empolyee.
     * :param: employeeid is the id of an employee.
     * :returns: Returns the viewemployee model object and error object.
     *
     * @param employeeModel
     */
    public void updateEmployee(EmployeeModel employeeModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        update(employeeModel.getEmployee(), false);
        // Updating employee address information.
        AddressDataService addressService = new AddressDataService();
        employeeModel.getAddressInfo().setSourceEntityId(employeeModel.getEmployee().getEntityId());
        employeeModel.getAddressInfo().setSourceEntityType(EDEntityType.EMPLOYEE.getId());
        addressService.addUpdateAddress(employeeModel.getAddressInfo(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId());

        // Add/Updating employee communication information. Communication information like Phone, email, mobile etc.
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(employeeModel.getCommunicationList(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(),EDApplicationInfo.getAppId().toString());
        // Mark the employee as followup employee for logged in user.
        markAsFollowUp(employeeModel.getEmployee().getEntityId(), employeeModel.getEmployee().isFollowing());
        // Updateing the logged in user note for current viewable employee.
        NoteDataService noteService = new NoteDataService();
        noteService.addUpdateNote(employeeModel.getNote(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId(), EwpSession.getSharedInstance().getUserId(), EwpSession.EMPLOYEE_APPLICATION_ID);
        // Add/Rmove employee from a team.        
        EmployeeGroupMemberDataService service = new EmployeeGroupMemberDataService();
        service.addDeleteMemberFromTeam(employeeModel.getTeamNameList(), employeeModel.getEmployee().getEntityId());
        // Add/update employee contact information. Comployee contact information like emergancy contact.
        EmployeeContactDataService employeeContactDataService = new EmployeeContactDataService();
        employeeContactDataService.updateEmployeeContactDetailFromSourceEntityIdAndType(employeeModel.getEmployeeEmergencyContactDetail(), employeeModel.getEmployee().getEntityId(), EDEntityType.EMPLOYEE.getId());
        DatabaseOps.defaultDatabase().commitTransaction();
    }


    /**
     * It is used to get an employee list by department.
     * For example: To get employee list department wise as FMResultSet.
     * :param: tenantId: It is id of tenant.
     *
     * @return Cursor of rows
     */
    public Cursor getEmployeeListByDepartmentAsResultSet(UUID departmentId, String searchByChar) throws EwpException {
        Cursor cursor = dataDelegate.getDepartmentEmployeeListAsResultSet(EwpSession.getSharedInstance().getTenantId(), EwpSession.getSharedInstance().getUserId(), departmentId, searchByChar);
        return cursor;
    }

    /**
     * It is used to get an employee list by location wise.
     * /// For example: To get employee list location wise as FMResultSet.
     * /// :param: tenantId: It is id of tenant.
     *
     * @return Cursor
     */
    public Cursor getEmployeeListByLocation(UUID locationId, String searchByChar) throws EwpException {
        Cursor cursor = dataDelegate.getLocationEmployeeListAsResultSet(EwpSession.getSharedInstance().getTenantId(), EwpSession.getSharedInstance().getUserId(), locationId, searchByChar);
        return cursor;
    }

    /**
     * Method is used to get employee list by team.
     *
     * @return Cursor
     */
    public Cursor getEmployeeListByTeamAsResultSet(UUID teamId, String searchByChar) throws EwpException {
        Cursor cursor = dataDelegate.getTeamWiseEmployeeListAsResultSet(EwpSession.getSharedInstance().getTenantId(), EwpSession.getSharedInstance().getUserId(), teamId, searchByChar);
        return cursor;
    }
    /**
     * It is used to get an employee communication favorite list as Resultset.
     * For example: To get employee favorite phone number, communicationType should be phone.
     * :param: employeeId: It is id of Employee for which we want to get employee favorite list.
     * :param: communicationType: It is type of communication
     *
     * @param communicationType
     * @return Cursor
     */
    public Cursor getEmployeeFavoriteListAsResultSet(CommunicationType communicationType, String searchChar) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeFavoriteListAsResultSet(communicationType, EwpSession.getSharedInstance().getUserId(), searchChar);
        return cursor;
    }

    /**
     * /// Method is used to check employee reference exist.
     * @param employeeId
     * @return
     * @throws EwpException
     */
    public Boolean isEmployeeReferenceExist(UUID employeeId)  throws EwpException  {
        boolean referenceExist =  dataDelegate.isEmployeeReferenceExist(employeeId);
        return referenceExist;
    }
    /**
     * Method is used to search the employee by name. If passed searched string appear at any where in employee name then return those employee list.
     *
     * @param searchCharInName
     * @return Cursor
     */
    public Cursor searchEmployeeAndGetAsResultSet(String searchCharInName) throws EwpException {
        //dataDelegate.updateEmpPicturetoNull();
        Cursor cursor = dataDelegate.searchEmployeeAndGetAsResultSet(EwpSession.getSharedInstance().getTenantId(), searchCharInName);
        return cursor;
    }

    public Cursor getSelectEmployeeListAsResultSet(UUID tenantId, UUID tenantUserId, String searchByChar) throws EwpException {
        return dataDelegate.getSelectEmployeeListAsResultSet(tenantId, tenantUserId, searchByChar);
    }

    /**
     * Method is used to set employee department.
     *
     * @param employeeList
     * @param departmentId
     */
    public void setEmployeeDepartment(List<EmployeeQuickView> employeeList, UUID departmentId) throws EwpException {
        if (employeeList != null) {
            EmployeeDataService empService = new EmployeeDataService();
            for (int i = 0; i < employeeList.size(); i++) {
                BaseEntity entity = empService.getEntity(employeeList.get(i).getEmployeeId());
                if (entity != null) {
                    Employee emp = (Employee) entity;
                    // If department are not same then go to update employee department.
                    if (emp.getDepartmentId().toString().equalsIgnoreCase(departmentId.toString()) && employeeList.get(i).getOperationType() == DatabaseOperationType.DELETE) {
                        empService.updateEmployeeDepartmentEmpty(departmentId, employeeList.get(i).getEmployeeId());
                    } else if (!emp.getDepartmentId().toString().equals(departmentId.toString())&& employeeList.get(i).getOperationType() == DatabaseOperationType.ADD) {
                        emp.setDepartmentId(departmentId);
                        super.update(emp);
                    }
                }
            }
        }
    }

    /**
     * Method is used to set employee location.
     *
     * @param employeeList
     * @param locationId
     * @throws EwpException
     */
    public void setEmployeeLocation(List<EmployeeQuickView> employeeList, UUID locationId) throws EwpException {
        if (employeeList != null) {
            EmployeeDataService empService = new EmployeeDataService();
            for (int i = 0; i < employeeList.size(); i++) {
                BaseEntity entity = empService.getEntity(employeeList.get(i).getEmployeeId());
                if (entity != null) {
                    Employee emp = (Employee) entity;
                    // If location are not same then go to update employee location.
                    // if user is remove from location then set employee location as NULL or empty.
                    if (emp.getLocationId().equals(locationId) && employeeList.get(i).getOperationType() == DatabaseOperationType.DELETE) {
                        empService.updateEmployeeLocationEmpty(locationId, employeeList.get(i).getEmployeeId());
                    } else if (!emp.getLocationId().equals(locationId) && employeeList.get(i).getOperationType() == DatabaseOperationType.ADD) {
                        emp.setLocationId(locationId);
                        super.update(emp);
                    }
                }
            }
        }
    }

    /**
     * Method is used to an employee has mobile or phone exist.
     *
     * @param employeeId
     * @return boolean
     */
    public Tuple.Tuple2<Boolean, Boolean, String> isEmployeePhoneAndMobileExist(UUID employeeId) throws EwpException {
        CommunicationDataService commService = new CommunicationDataService();
        boolean resultMobile = commService.hasCommunicationInTypeAndSubtype(employeeId, CommunicationType.PHONE.getId(), PhoneType.MOBILE.getId());
        boolean resultPhone = commService.hasCommunicationInTypeAndSubtype(employeeId, CommunicationType.PHONE.getId(), 0);
        return new Tuple.Tuple2<Boolean, Boolean, String>(resultMobile, resultPhone, "");
    }

    /**
     * Method is used to get employee phone list.
     * /// for example for PhoneType can be Home, Work, Mobile, Other.
     *
     * @param employeeId employeeId, It is id of employee to get phone list.
     * @param phoneType  PhoneType, It is a type for which we want to get phone list.
     * @return List<Communication>
     * @throws EwpException
     */
    public List<Communication> getEmployeePhoneList(UUID employeeId, PhoneType phoneType) throws EwpException {
        CommunicationDataService commService = new CommunicationDataService();
        int phoneSubType = 0;
        if (phoneType != null) {
            phoneSubType = phoneType.getId();
        }
        List<Communication> resultMobile = commService.getCommunicationListFromTypeAndSubtype(employeeId, CommunicationType.PHONE.getId(), phoneSubType);
        return resultMobile;
    }

    /**
     * Method is used to mark/unmark an employee as favorite.
     *
     * @param favorite        favorite, Favorite, To mark as favorite or not.
     * @param communicationId
     * @param userId          employeeid, is id of a employee to mark as favorite.
     * @throws EwpException
     */
    public void markAsFavorite(boolean favorite, UUID communicationId, UUID userId) throws EwpException {
        PFUserEntityLinkDataService service = new PFUserEntityLinkDataService();
        PFUserEntityLink pfUserEntityLink = service.getUserEntityFromSourceIdSourceTypeAndLinkType(EwpSession.getSharedInstance().getTenantId(), LinkType.FAVOURITE.getId(), 31, communicationId, userId);
        if (favorite) {
            // If entity not found then mark employee as followup employee for logged in user.
            if (pfUserEntityLink == null) {
                String appId = EDApplicationInfo.getAppId().toString();
                service.addUserEntityFromSourceIdSourceTypeAndLinkType(LinkType.FAVOURITE.getId(), 31, communicationId, 0, true, "", appId);
                BaseEntity entity = super.getFromCache(communicationId);
                if (entity != null) {
                    super.removeFromCache(entity);
                }
            }
        } else {
            // If entity found then remove employee from followup list.
            if (pfUserEntityLink != null) {
                service.delete(pfUserEntityLink);
            }
        }
    }

    /**
     * It is used to get an employee follower list as Resultset.
     *
     * @param userId
     * @param searchByChar
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeFollowerAsResultSet(UUID userId, String searchByChar) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeFollowUpListAsResultSet(userId, searchByChar);
        return cursor;
    }

    /**
     * @param userId
     * @return Cursor
     */
    public Cursor getMyTeamEmployeeListAsResultSet(UUID userId) throws EwpException {
        Cursor cursor = dataDelegate.getMyTeamEmployeeListAsResultSet(userId);
        return cursor;
    }

}