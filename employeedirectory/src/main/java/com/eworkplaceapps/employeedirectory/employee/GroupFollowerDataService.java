//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.List;
import java.util.UUID;

public class GroupFollowerDataService extends BaseDataService {

    private GroupFollowerData dataDelegate = new GroupFollowerData();

    /**
     * Initializes a new instance of the GroupFollowerDataService class.
     */
    public GroupFollowerDataService() {
        super("GroupFollowerDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }


    /**
     * It is used to get employee follower list from employeeid.
     * /// :param: employeeId
     * /// :param: tenantId
     *
     * @param employeeId
     * @param tenantId
     * @return List<UUID>
     * @throws EwpException
     */
    public List<UUID> getLoginEmployeeAllYourReportsFollowerList(UUID employeeId, UUID tenantId) throws EwpException {
        return dataDelegate.getLoginEmployeeAllYourReportsFollowerList(employeeId, tenantId);
    }

    /**
     * /// It is used to get employee follower list from employeeid and followertype.
     * /// :param: employeeId
     * /// :param: tenantId
     * /// :followerType: follower type is an enum. It 3 type, AllyourReport, Group, YourDirectReport.
     *
     * @param employeeId
     * @param tenantId
     * @param followerType
     * @return List<GroupFollower>
     * @throws EwpException
     */
    public List<GroupFollower> getGroupFollowerListByEmployeeIdAndFollowerTypeAsDT(UUID employeeId, UUID tenantId, FollowerType followerType) throws EwpException {
        List<GroupFollower> groupFollowerList = dataDelegate.getGroupFollowerListByEmployeeIdAndFollowerTypeAsResultSet(employeeId, tenantId, followerType);
        return groupFollowerList;
    }

    /**
     * It is used to get employee follower list from employeeid and tenantid.
     * /// :param: employeeId
     * /// :param: tenantId
     *
     * @param employeeId
     * @param tenantId
     * @return Cursor
     */
    public Cursor getLoginEmployeeFollowerList(UUID employeeId, UUID tenantId) throws EwpException {
        /// groupFollowerId: groupFollowerId,
        Cursor resultTuple = dataDelegate.getLoginEmployeeFollwerList(employeeId, tenantId);
        return resultTuple;
    }

}