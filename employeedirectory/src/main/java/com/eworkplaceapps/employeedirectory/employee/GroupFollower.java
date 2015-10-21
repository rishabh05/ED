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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines information for group follower.
 */
public class GroupFollower extends BaseEntity {
    private static final String GROUP_FOLLOWER_ENTITY_NAME = "GroupFollower";

    /**
     * Create GroupFollower object and return created object.
     *
     * @return GroupFollower
     */
    public static GroupFollower createEntity() {
        return new GroupFollower();
    }

    /// Getter/setter  for description
    private UUID employeeId = Utils.emptyUUID();
    private UUID groupId = Utils.emptyUUID();
    private int followerType = 0;
    private UUID tenantId = Utils.emptyUUID();

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        setPropertyChanged(this.employeeId, employeeId);
        this.employeeId = employeeId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        setPropertyChanged(this.groupId, groupId);
        this.groupId = groupId;
    }

    public int getFollowerType() {
        return followerType;
    }

    public void setFollowerType(int followerType) {
        setPropertyChanged(this.followerType, followerType);
        this.followerType = followerType;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public GroupFollower() {
        super(GROUP_FOLLOWER_ENTITY_NAME);
    }

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

        /// It is used to validate the first name null or empty.
        if (this.employeeId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Employee"});
            message.add(AppMessage.REFERENCE_ID_REQUIRED);
        }
        /// It is used to validate the lastName name null or empty.
        if (this.groupId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Group"});
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
        GroupFollower groupFollower = (GroupFollower) entity;
        groupFollower.setEntityId(this.entityId);
        groupFollower.setFollowerType(this.followerType);
        groupFollower.setGroupId(this.groupId);
        groupFollower.setEmployeeId(this.employeeId);
        return groupFollower;
    }
}
