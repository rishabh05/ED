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
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import java.util.List;
import java.util.UUID;

/**
 * It expand BaseDataService to provide services to EmployeeGroupMember entities.
 */
public class EmployeeGroupMemberDataService extends BaseDataService {

    private EmployeeGroupMemberData dataDelegate = new EmployeeGroupMemberData();

    /// Initializes a new instance of the EmployeeGroupMemberDataService class.
    EmployeeGroupMemberDataService() {
        super("EmployeeGroupMemberDataService");
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
     */
    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operation) {
        EmployeeGroupMemberAccess access = new EmployeeGroupMemberAccess();
        return access.checkPermissionOnOperation(operation, EnumsForExceptions.ErrorModule.DATA_SERVICE);
    }

    /**
     * Method is used to get employee list as ResultSet from GroupId
     *
     * @param groupId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeGroupMemberListByGroupIdAsResultSet(UUID groupId) throws EwpException {
        return dataDelegate.getEmployeeTeamMemberListByTeamIdAsResultSet(groupId);
    }

    /**
     * Method is used to get employee list as ResultSet from GroupId
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getEmployeeGroupMemberListByTenantIdAsResultSet(UUID tenantId) throws EwpException {
        return dataDelegate.getEmployeeGroupMemberListByTenantIdAsResultSet(tenantId);
    }

    /**
     * Method is used to get Active group list, where employee is member.
     *
     * @param employeeId
     * @return List<EmployeeGroupMember>
     */
    public List<EmployeeGroupMember> getActiveGroupMembersWhereEmployeeIsMember(UUID employeeId) throws EwpException {
        return dataDelegate.getActiveGroupMembersWhereEmployeeIsMember(employeeId);
    }

    /**
     * @param employeeId
     * @param teamId
     * @return EmployeeGroupMember
     * @throws EwpException
     */
    public EmployeeGroupMember getGroupMembersFromTeamAndEmployeeId(UUID employeeId, UUID teamId) throws EwpException {
        EmployeeGroupMember resultTuple = dataDelegate.getGroupMembersWhereEmployeeIsMember(employeeId, teamId);
        if (resultTuple == null) {
            throw new EwpException("Error to getGroupMembersWhereEmployeeIsMember");
        }
        return resultTuple;
    }

    /**
     * ethod is used to delete group member from employee id. It will remove employee from all group/team.
     * /// :param: employeeId: Id of employee to delete.
     *
     * @param employeeId
     * @return int
     */
    public int deleteMemberByEmployeeId(UUID employeeId) {
        return dataDelegate.deleteGroupMemberByEmployeeId(employeeId);
    }

    /**
     * Method is used to delete all group member from group or team id.
     * :param: teamId: Id of Group or team.
     *
     * @param teamId
     * @return int
     * @throws EwpException
     */
    public int deleteAllMemberFromTeamId(UUID teamId) throws EwpException {
        return dataDelegate.deleteAllMemberFromTeamId(teamId);
    }

    /**
     * Method is used to get TeamMember from employeeid and team id.
     *
     * @param employeeId
     * @param teamId
     * @return EmployeeGroupMember
     * @throws EwpException
     */
    public EmployeeGroupMember getTeamMembersFromTeamAndEmployeeId(UUID employeeId, UUID teamId) throws EwpException {
        EmployeeGroupMember resultTuple = dataDelegate.getGroupMembersWhereEmployeeIsMember(employeeId, teamId);
        return resultTuple;
    }

    /**
     * Method is used to delete group member from employee and team id.
     * :param: teamId: Id of Group or team.
     * :param: employeeId: Id of employee to delete.
     *
     * @param teamId
     * @param employeeId
     * @throws EwpException
     */
    public void deleteMemberFromTeamAndEmployeeId(UUID teamId, UUID employeeId) throws EwpException {
        EmployeeGroupMember resultTuple = getGroupMembersFromTeamAndEmployeeId(employeeId, teamId);
        if (resultTuple == null) {
            throw new EwpException("");
        }
        super.delete(resultTuple);
        ;
    }

    /**
     * Method is used to add/remove employee from a team.
     *
     * @param teamList
     * @param employeeId
     * @throws EwpException
     */
    public void addDeleteMemberFromTeam(List<EditEmployeeTeam> teamList, UUID employeeId) throws EwpException {
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).getOperationType() == DatabaseOperationType.ADD) {
                EmployeeGroupMember member = new EmployeeGroupMember();
                member.setEmployeeId(employeeId);
                member.setEmployeeGroupId(teamList.get(i).getTeamId());
                member.setTenantId(EwpSession.getSharedInstance().getTenantId());
                super.add(member);
            } else if (teamList.get(i).getOperationType() == DatabaseOperationType.DELETE) {
                deleteMemberFromTeamAndEmployeeId(teamList.get(i).getTeamId(), employeeId);
            }
        }
    }

}