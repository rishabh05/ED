//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: <Shrey Sharma>
// Original DATE: <4/16/2015>
//===============================================================================
package com.eworkplaceapps.platform.security;

import android.util.Log;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;

import java.util.UUID;

/**
 * It is base access class, It implement all required methods to check entity access permission.
 * It Check login user permission for given entity and operation.
 * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
 */

public class EntityAccess {

    private static final String TAG = "EntityAccess";

    public enum OwnerType {
        USER(0),
        MANAGER(1),
        ADMINISTRATOR(2),
        SYSTEM(3),
        PSEUDO_USER(4);
        private int id;

        OwnerType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }

    public enum ShareType {
        PRIVATE(0),
        SHARE_WITH_ALL_USERS(1),
        SHARE_WITH_ADMINS(2),
        SHARE_WITH_ALL_GROUP_MEMBERS(3),
        SHARE_WITH_ALL_GROUP_MANAGERS(4),
        SHARE_WITH_LISTED_USERS(5);
        private int id;

        ShareType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Defines constants for common possible operation of an entity.
    public enum CheckOperationPermission {
        VIEW(0),
        // Enum corresponding to add operation.
        ADD(1),
        // Enum corresponding to  update operation.
        UPDATE(2),
        // Enum corresponding to delete operation.
        DELETE(3);
        private int id;

        CheckOperationPermission(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    UUID ownerId = UUID.randomUUID();
    OwnerType ownerType = OwnerType.USER;

    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        Log.d(TAG, "This method must be overridden");
        return false;
    }

    /**
     * GET access vector of an entity for all operation.
     * Returns permission bit array against defined set of entity operation.
     * Permission bits are corresponding to entity operation enum value.
     * Its true if logged-in user has permission to perform required operation
     * Ex.
     * Lets assume on entity operation set 'ADD' enum value is 2.
     * returned array contains 'ADD' operation permission bit at index position 2.
     *
     * @param entityId
     * @param userId
     * @param tenantId
     * @return
     */
    public boolean[] accessList(UUID entityId, UUID userId, UUID tenantId) throws EwpException {
        Log.d(TAG, "This method must be overridden");
        return new boolean[]{};
    }

    /**
     * @return
     */
    public boolean isOwner() {
        UUID userId = EwpSession.getSharedInstance().getUserId();
        boolean b = this.ownerId.equals(userId);  // == for Guids
        return b;
    }


    /**
     * This method is used to check permission on employee for given operation.
     *
     * @param operation
     * @param errorModule
     * @return
     */
    public boolean checkPermissionOnOperation(CheckOperationPermission operation, EnumsForExceptions.ErrorModule errorModule) {
        boolean hasPermission = checkAccess(operation, EwpSession.getSharedInstance().getUserId()
                , EwpSession.getSharedInstance().getTenantId());
        return hasPermission;
    }

    /**
     * Its a must overridable method.
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on an entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     *
     * @param operation
     * @param userId
     * @param tenantId
     * @return
     */
    public boolean checkAccess(CheckOperationPermission operation, UUID userId, UUID tenantId) {
        return false;
    }

}