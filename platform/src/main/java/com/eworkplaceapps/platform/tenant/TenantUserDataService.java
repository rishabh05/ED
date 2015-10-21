package com.eworkplaceapps.platform.tenant;

import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.enums.UserStatusItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * It expand the BaseDataService to provide services to TENANT_USER entities.
 * It is responsible to create the tenant user for logged in tenant.
 * It allow to check logged in user is admin for current logged in tenant.
 * Created by admin on 4/20/2015.
 */
public class TenantUserDataService extends BaseDataService {

    TenantUserData dataDelegate = new TenantUserData();

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * Initializes a new instance of the TaskDataService class.
     */
    public TenantUserDataService() {
        super("TenantUserDataService");
    }

    /**
     * It validate an entity and returns result as EwpError object variable.
     * Returns success(ErrorType) for successfully validation and array for error message.
     *
     * @return
     */
    // @Override
    public Boolean validateOnAddAndUpdate(TenantUser entity) throws EwpException {
        Boolean error = entity.validate();
        if (!error) {
            throw new EwpException(new EwpException("Validation Exception On TenantUserDataService"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, null, EnumsForExceptions.ErrorModule.DATA_SERVICE, null, 0);
        }
        TenantUser tenantUser = (TenantUser) entity;
        boolean resultTuple = dataDelegate.isTenantUserExist(tenantUser.getLoginEmail(), tenantUser.getTenantId(), tenantUser.getEntityId());
        if (resultTuple) {
            List<String> message = new ArrayList<String>();
            Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
            message.add(AppMessage.DUPLICATE_NAME);
            dicError.put(EnumsForExceptions.ErrorDataType.DUPLICATE, new String[]{"EMAIL"});
            throw new EwpException(new EwpException("Validation Exception On TenantUserDataService"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        return error;
    }

    /**
     * It adds new tenantuser with admin righs, when adding new tenant.
     *
     * @return
     */
    public UUID signUpTenantUser(UUID tenantId, String password) throws EwpException {
        TenantDataService tenantService = new TenantDataService();
        BaseEntity response = tenantService.getEntity(tenantId);
        Tenant tenant = (Tenant) response;
        // ADD user with role.
        if (tenant.getPrimaryUserId() != null) {
            TenantUser tenantUser = new TenantUser();
            tenantUser.setTenantId(tenantId);
            tenantUser.setUserStatus(UserStatusItem.ACTIVE.getId());
            Boolean error = tenantUser.validate();
            if (error) {
                Object response1 = getDataClass().add(tenantUser);
                if (response1 != null) {
                    UUID newUserId = (UUID) response1;
                    tenant.setPrimaryUserId(newUserId);
                    tenant.setCreatedBy(String.valueOf(newUserId));
                    tenant.setUpdatedBy(String.valueOf(newUserId));
                    return newUserId;
                }
                return UUID.randomUUID();
            } else {
                return UUID.randomUUID();
            }
        }
        return tenant.getPrimaryUserId();
    }

    public void updateUserToken(UUID userId, String loginToken) throws EwpException {
        BaseEntity resultTuple = getEntity(userId);
        if (resultTuple != null) {
            TenantUser tUser = (TenantUser) resultTuple;
            tUser.setLoginToken(loginToken);
            dataDelegate.update(tUser);
        }
        throw new EwpException(new EwpException("Validation Exception in TenantUserDataService"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, null, EnumsForExceptions.ErrorModule.DATA_SERVICE, null, 0);
    }

    public void updateUserNameAndPicture(TenantUser tUser) throws EwpException {
        dataDelegate.updateUserNameAndPicture(tUser);
    }

    /**
     * This method is used to check permission on given operation.
     *
     * @return
     */

    public boolean checkPermissionOnOperation(BaseEntity entity, EntityAccess.CheckOperationPermission operation) {
        //EmployeeAccess access = new EmployeeAccess();
        //TODO left for implementation
        //return access.checkPermissionOnOperation(operation, EnumsForExceptions.ErrorModule.DATA_SERVICE);
        return false;
    }

    /**
     * Searches for all tenant users (of any user status) as ResultSet by login email id.
     * Returns List of all tenant users as ResultSet that matches given user login email.
     * It will be used to get users by login email and user status as ResultSet.
     *
     * @return
     */
    public Cursor getUsersByLoginEmailAndStatusAsResultSet(String loginEmail, UserStatusItem[] userStatus) throws EwpException {
        if (userStatus == null) {
            userStatus = new UserStatusItem[]{UserStatusItem.ALL};
        }
        Cursor response = dataDelegate.getAllTenantUserListAsResultSetFromLoginEmailAsResultSet(loginEmail, userStatus);
        return response;
    }

    /**
     * Initialize the login session for provided login user email and tenant id.
     * It is used to initialize login session for given login email and tenant id.
     *
     * @param userLoginEmail
     * @param tenantId
     * @param sessionId
     */
    public void setLoginSession(String userLoginEmail, UUID tenantId, String sessionId) {
    }

    /**
     * It validate user id for system admin.
     * Returns True, If user id is system admin user otherwise return false.
     * It is used to check given login email for system admin.
     *
     * @param userId
     * @return
     */
    public boolean isSystemAdminUser(UUID userId) throws EwpException {
        BaseEntity response = getEntity(userId);
        if (response == null) {
            return false;
        }
        TenantUser tenantUser = (TenantUser) response;
        return dataDelegate.isSystemAdminUser(tenantUser.getLoginEmail());
    }

    /**
     * It is used to get user admin or system admin permission.
     *
     * @return
     */
    public  Tuple.Tuple2<Boolean, Boolean, Boolean> isAccountOrSystemAdminUser(UUID userId) throws EwpException {
        return dataDelegate.isAccountOrSystemAdminUser(userId);

    }


    /**
     * It validate login email for system admin.
     * Returns True, If login email is system admin user otherwise return false.
     * It is used to check given login email for system admin.
     *
     * @param loginEmail
     * @return
     */
    public boolean isSystemAdminUser(String loginEmail) throws EwpException {
        return dataDelegate.isSystemAdminUser(loginEmail);
    }

    /**
     * It validate primary user for given user id and tenant id.
     * Returns True, If user is primary user otherwise return false.
     * It is used to check given user id and tenant id for primary user.
     *
     * @param userId
     * @param tenantId
     * @return
     */
    public boolean isPrimaryUser(UUID userId, UUID tenantId) throws EwpException {
        return dataDelegate.isPrimaryUser(userId, tenantId);
    }

    /**
     * Checks for active user by given userId.
     * Returns True, id of user is active.
     * It is used to check user is active or not by given user id.
     *
     * @param userId
     * @return
     */
    public boolean isUserActive(UUID userId) throws EwpException {
        UserStatusItem[] userStatusItems = new UserStatusItem[]{UserStatusItem.ACTIVE};
        return dataDelegate.isUserExistsByIdAndStatus(userId.toString(), userStatusItems);
    }

    /**
     * Searches for all tenant user that matches the login email and returns the user and tenant information.
     * Returns USER and tenant information as ResultSet.
     * It is used to get all tenant users list for given login email and  tenant id as ResultSet.
     *
     * @return
     */
    public Cursor getUsersByEmailAndTenantAsResultSet(String loginEmail, UUID tenantId) throws EwpException {
        Cursor response = dataDelegate.getTenantUserListByLoginEmailAndTenantIdAsResultSet(loginEmail, tenantId);
        return response;
    }

    /**
     * It is used to add TenatUser when user successfully add in online mode.
     *
     * @param tUserList tenant user list
     * @throws EwpException
     */
    public void addTenantUser(List<TenantUser> tUserList) throws EwpException {
        DatabaseOps.defaultDatabase().beginTransaction();
        for (int i = 0; i < tUserList.size(); i++) {
            tUserList.get(i).validate();
            dataDelegate.addTenantUser(tUserList.get(i));
        }
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    /**
     * TENANT user information as ResultSet with preference values that matches given user id.
     * returns TENANT user as ResultSet with preference values.
     *
     * @return
     */
    public Cursor getUserWithPreferenceAsResultSet(UUID userId) {
        return null;
    }

    /**
     * Change user password.
     * This method will be used to change user password.
     */
    public void changePassword(String loginEmail, String oldPassword, String newPassword) {
    }


    /**
     * Logout from user session.
     * Returns True, If logout else return false.
     *
     * @param sessionId
     * @return
     */
    public boolean logoutSession(String sessionId) {
        return false;
    }
}
