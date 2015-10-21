//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Employee entities.
 * Each employee is a part of group. This class is responsible to manage employee group information.
 */
public class EmployeeGroupDataService extends BaseDataService {

    private EmployeeGroupData dataDelegate = new EmployeeGroupData();

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * Initializes a new instance of the TaskDataService class.
     */
    public EmployeeGroupDataService() {
        super("EmployeeGroupDataService");
    }

    @Override
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        entity.validate();
        EmployeeGroup team = (EmployeeGroup) entity;
        boolean result = dataDelegate.teamExists(team.getEntityId(), team.getName(), team.getTenantId());
        if (result) {
            EwpException ewpException = new EwpException();
            ewpException.errorType = EnumsForExceptions.ErrorType.DUPLICATE;
            ewpException.setLocalizedMessage("A Team with that name already exists.");
            throw ewpException;
        }
    }

    /**
     * Method is used to get employee group list as ResultSet from tenantid
     *
     * @param tenantId
     * @return cursor
     */
    public Cursor getEmployeeGroupListByTenantIdAsResultSet(UUID tenantId) throws EwpException {
        return dataDelegate.getEmployeeGroupListByTenantIdAsResultSet(tenantId);
    }

    /**
     * Method is used to get employee group list as EmployeeTeam from tenantid and Employeeid
     *
     * @param tenantId
     * @param employeeId
     * @return List<EmployeeGroup>
     */
    public List<EmployeeGroup> getEmployeeTeamListFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        List<EmployeeGroup> resultTuple = dataDelegate.getEmployeeTeamListFromEmployeeId(tenantId, employeeId);
        return resultTuple;
    }

    /**
     * Method is used to get employee group list as EmployeeTeam from tenantid and Employeeid
     *
     * @param tenantId
     * @param employeeId
     * @return List<EmployeeGroup>
     * @throws EwpException
     */
    public List<EmployeeGroup> getEmployeeTeamListAsEmployeeTeamFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        List<EmployeeGroup> resultTuple = dataDelegate.getEmployeeTeamListFromEmployeeId(tenantId, employeeId);
        return resultTuple;
    }

    /**
     * @param tenantId
     * @param employeeId
     * @return List<String>
     * @throws EwpException
     */
    public List<String> getEmployeeTeamListAsStringFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        List<String> resultTuple = dataDelegate.getEmployeeTeamListAsStringFromEmployeeId(tenantId, employeeId);
        return resultTuple;
    }

    /**
     * @param tenantId
     * @param employeeId
     * @return List<EditEmployeeTeam>
     * @throws EwpException
     */
    public List<EditEmployeeTeam> getEmployeeTeamListAsEditEmployeeTeamFromEmployeeId(UUID tenantId, UUID employeeId) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeTeamListFromEmployeeIdAsResult(tenantId, employeeId);
        if (cursor == null) {
            new EwpException("Error to get team getEmployeeTeamListAsStringFromEmployeeId.");
        }
        List<EditEmployeeTeam> editEmployeeTeam = new ArrayList<>();
        editEmployeeTeam = EditEmployeeTeam.setPropertiesAndGetEditEmployeeTeamList(cursor);
        return editEmployeeTeam;
    }

    /**
     * This method is used to check permission on given operation.
     *
     * @param entity
     * @param operation
     * @return boolean
     */
    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operation) {
        EmployeeGroupAccess access = new EmployeeGroupAccess();
        return access.checkPermissionOnOperation(operation, EnumsForExceptions.ErrorModule.DATA_SERVICE);
    }

    public TeamModel getTeamModelFromTeamId(UUID id) throws EwpException {
        EmployeeGroup employeeGroup = dataDelegate.getEntity(id);
        TeamModel teamModel = new TeamModel();
        teamModel.setTeam(employeeGroup);
        CommunicationDataService commService = new CommunicationDataService();
        // Getting the employee contact infromation and map contact information to ViewEmployee object.
        // Employee communication contact list like Work phone, Home phone, mobile email etc.
        List<Communication> communicationList = commService.getCommunicationListFromSourceEntityIdAndType(id, EDEntityType.EMPLOYEE_GROUP.getId());
        if (communicationList != null) {
            teamModel.setCommunications(communicationList);
        }
        List<EmployeeQuickView> employeeQuickViews = EmployeeQuickView.getEmployeeQuickViewList(GroupBy.TEAM, null, id);
        teamModel.setEmployeeList(employeeQuickViews);
        return teamModel;
    }


    public List<EmployeeTeamMemberItem> getLoginEmployeeMemberList() throws EwpException {
        Cursor resultSet = dataDelegate.getEmployeeTeamMemberList(EwpSession.getSharedInstance().getTenantId(), EwpSession.getSharedInstance().getUserId());
        List<EmployeeTeamMemberItem> teamMemberList = EmployeeTeamMemberItem.setPropertiesAndGetEditEmployeeTeamMemberList(resultSet, true);
        return teamMemberList;
    }

    @Override
    public void delete(BaseEntity entity) throws EwpException {
        if (entity != null) {
            DatabaseOps.defaultDatabase().beginDeferredTransaction();
            super.delete(entity);
            EmployeeGroupMemberDataService teamMemberService = new EmployeeGroupMemberDataService();
            teamMemberService.deleteAllMemberFromTeamId(entity.getEntityId());
            CommunicationDataService commService = new CommunicationDataService();
            commService.deleteCommunicationListFromSourceEntityIdAndType(entity.getEntityId(), EDEntityType.EMPLOYEE_GROUP.getId());
            DatabaseOps.defaultDatabase().commitTransaction();
        }
    }

    /**
     * Method is used to add new Team.
     *
     * @param teamModel
     * @return Object
     * @throws EwpException
     */
    public Object addTeam(TeamModel teamModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        Object id = super.add(teamModel.getTeam());
        if (id != null) {
            teamModel.getTeam().setEntityId((UUID) id);
            String appId = EDApplicationInfo.getAppId().toString();
            CommunicationDataService commService = new CommunicationDataService();
            commService.addCommunicationList(teamModel.getCommunications(), teamModel.getTeam().getEntityId(), EDEntityType.EMPLOYEE_GROUP.getId(), appId);
            addEmployeeInTeam(teamModel.getEmployeeList(), teamModel.getTeam().getEntityId());
            DatabaseOps.defaultDatabase().commitTransaction();
        }
        return id;
    }

    /**
     * Method is used to add employees into team.
     *
     * @param employeeList employeeList: It is a list of employee to add/remove from a team. add/remove will be decide upon operation type property inside the EmployeeQuickView class.
     * @param teamId       TeamId: It is id of team.
     * @throws EwpException
     */
    private void addEmployeeInTeam(List<EmployeeQuickView> employeeList, UUID teamId) throws EwpException {
        if (employeeList != null) {
            EmployeeGroupMemberDataService teamMemberService = new EmployeeGroupMemberDataService();
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getOperationType() == DatabaseOperationType.ADD) {
                    EmployeeGroupMember emp = new EmployeeGroupMember();
                    emp.setEmployeeGroupId(teamId);
                    emp.setTenantId(EwpSession.getSharedInstance().getTenantId());
                    emp.setEmployeeId(employeeList.get(i).getEmployeeId());
                    teamMemberService.add(emp);
                }
            }
        }
    }

    /**
     * Method is used to update Team.
     *
     * @param teamModel
     */
    public void updateTeam(TeamModel teamModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        super.update(teamModel.getTeam());
        UUID appId = EDApplicationInfo.getAppId();
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(teamModel.getCommunications(), teamModel.getTeam().getEntityId(), EDEntityType.EMPLOYEE_GROUP.getId(),EDApplicationInfo.getAppId().toString());
        EmployeeDataService empService = new EmployeeDataService();
        addRemoveEmployeeFromTeam(teamModel.getEmployeeList(), teamModel.getTeam().getEntityId());
        DatabaseOps.defaultDatabase().commitTransaction();/*commitTransaction();*/

    }

    /**
     * Method is used to add/remove employee from a team.
     *
     * @param employeeList It is a list of employee to add/remove from a team. add/remove will be decide upon operation type property inside the EmployeeQuickView class.
     * @param teamId       It is id of team.
     * @throws EwpException
     */
    private void addRemoveEmployeeFromTeam(List<EmployeeQuickView> employeeList, UUID teamId) throws EwpException {
        if (employeeList != null) {
            EmployeeGroupMemberDataService teamMemberService = new EmployeeGroupMemberDataService();
            for (int i = 0; i < employeeList.size(); i++) {
                EmployeeGroupMember result = teamMemberService.getTeamMembersFromTeamAndEmployeeId(employeeList.get(i).getEmployeeId(), teamId);
                if (result == null && employeeList.get(i).getOperationType() == DatabaseOperationType.ADD) {
                    EmployeeGroupMember emp = new EmployeeGroupMember();
                    emp.setEmployeeGroupId(teamId);
                    emp.setTenantId(EwpSession.getSharedInstance().getTenantId());
                    emp.setEmployeeId(employeeList.get(i).getEmployeeId());
                    teamMemberService.add(emp);
                } else if (result != null && employeeList.get(i).getOperationType() == DatabaseOperationType.DELETE) {
                    teamMemberService.delete(result);
                }
            }
        }
    }

}