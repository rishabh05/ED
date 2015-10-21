//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 04/30/2015
//===============================================================================
package com.eworkplaceapps.platform.tenant;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;
import com.eworkplaceapps.platform.utils.enums.UserStatusItem;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * TenantUserData class
 */
public class TenantUserData extends BaseData<TenantUser> {

    @Override
    public TenantUser createEntity() {
        return TenantUser.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id
     * @return
     */
    @Override
    public TenantUser getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFTenantUser where LOWER(UserId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }


    public List<TenantUser> getList() throws EwpException {
        String sql = "SELECT * From PFTenantUser";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFTenantUser where LOWER(UserId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all TENANT_USER Entity record from database and return result as! a ResultSet.
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFTenantUser";
        return executeSqlAndGetResultSet(sql);
    }


    /**
     * delete row from tenant user
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public void delete(TenantUser entity) throws EwpException {
        super.deleteRows("PFTenantUser", "LOWER(UserId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    // TODO: [Amit] Need to implement this method
    public boolean isReferenceExist(String userId) {
        return false;
    }

    /**
     * Check user is an admin user by providing login email.
     *
     * @param loginEmail
     * @return
     */
    public boolean isSystemAdminUser(String loginEmail) throws EwpException {
        String sql = " select r.RoleName from PFTenantUser as t inner join PFRolelinking as rl on LOWER(t.userid)= LOWER(rl.userid) inner join PFRole as r on LOWER(rl.roleid) = LOWER(r.roleid)  where LOWER(t.tenantid) = LOWER('" + UUID.randomUUID() + "') and t.LoginEmail = '%@' ";
        String string = sql;
        sql = string.format(loginEmail);
        Cursor response = executeSqlAndGetResultSet(sql);
        if (response != null) {
            while (response.moveToFirst()) {
                String roleName = response.getString(response.getColumnIndex("RoleName"));
                if (roleName != null) {
                    return "SystemAdmin".equals(roleName);
                }
            }
        }
        return false;
    }

    public boolean isAccountAdminUser(String loginEmail) throws EwpException {
        String sql = " select r.RoleName from PFTenantUser as t inner join PFRolelinking as rl on LOWER(t.userid)= LOWER(rl.userid) inner join PFRole as r on LOWER(rl.roleid) = LOWER(r.roleid)  where LOWER(t.tenantid) = LOWER('" + UUID.randomUUID() + "') and t.LoginEmail = '%@'";
        String string = sql;
        sql = string.format(loginEmail);
        Cursor response = executeSqlAndGetResultSet(sql);
        if (response != null) {
            while (response.moveToFirst()) {
                String roleName = response.getString(response.getColumnIndex("RoleName"));
                if (roleName != null) {
                    return "ADMIN".equals(roleName);
                }
            }
        }
        return false;
    }

    /**
     * It is used to get user admin or system admin permission.
     *
     * @return
     */
    public Tuple.Tuple2<Boolean, Boolean, Boolean> isAccountOrSystemAdminUser(UUID userId) throws EwpException {
        String sql = " select r.RoleName from PFTenantUser as t inner join PFRolelinking as rl on LOWER(t.userid)= LOWER(rl.userid) inner join PFRole as r on LOWER(rl.roleid) = LOWER(r.roleid)  where LOWER(t.UserId) = LOWER('" + userId.toString() + "')";
        Cursor resultSet = executeSqlAndGetResultSet(sql);
        Tuple.Tuple2<Boolean, Boolean, Boolean> result = new Tuple.Tuple2<Boolean, Boolean, Boolean>(false, false, false);
        if (resultSet == null) {
            return result;
        } else {
            if (resultSet.moveToFirst()) {
                //while (resultSet.moveToNext()) {
                    String roleName = resultSet.getString(resultSet.getColumnIndex("RoleName"));
                    if(roleName == null) return result;
                    return new Tuple.Tuple2<Boolean, Boolean, Boolean>("Admin".equalsIgnoreCase(roleName), "SystemAdmin".equalsIgnoreCase(roleName), false);
                //}
            }
        }
        return result;
    }

    /**
     * It is used to check user exist with and status.
     *
     * @param userId
     * @param userStatus
     * @return
     */
    public boolean isUserExistsByIdAndStatus(String userId, UserStatusItem[] userStatus) throws EwpException {
        String sql = " Select COUNT(UserId) as Count from PFTenantUser tu WHERE LOWER(tu.UserId)=LOWER('" + userId + "')";
        if (userStatus != null && !isContain(userStatus, UserStatusItem.ALL)) {
            sql += "".format(" AND TenantStatus IN('%@')", getCommaSeperatedStatusString(userStatus));
        }
        return SqlUtils.recordExists(sql);
    }

    // This method is used to get the list of tenant for loginemail as! well as! status.
    public Cursor getAllTenantUserListAsResultSetFromLoginEmailAsResultSet(String loginEmail, UserStatusItem[] userStatus) throws EwpException {
        // Getting the sql
        String sql = getSQL();
        sql = "".format(getSQL() + " WHERE LoginEmail='%@' ", loginEmail);
        if (userStatus != null && !isContain(userStatus, UserStatusItem.ALL)) {
            sql = "".format(sql + " AND t.TenantStatus IN('%@')", getCommaSeperatedStatusString(userStatus));
        }
        return executeSqlAndGetResultSet(sql);
    }

    // Check, user is primary user of tenant
    public boolean isPrimaryUser(UUID userId, UUID tenantId) throws EwpException {
        String sql = " SELECT UserId FROM PFTenantUser AS tu INNER JOIN PFTenant AS t ON LOWER(tu.UserId)=LOWER(t.PrimaryUserId) WHERE LOWER(tu.UserId)=LOWER('" + userId + "') AND LOWER(t.TenantId)=LOWER('" + tenantId + "')";
        return SqlUtils.recordExists(sql);
    }

    /**
     * Method is used to get user list as! ResultSet from loginemail
     *
     * @param loginEmail
     * @param tenantId
     * @return
     */
    public Cursor getTenantUserListByLoginEmailAndTenantIdAsResultSet(String loginEmail, UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where LOWER(tu.LoginEmail)=LOWER('" + loginEmail + "')";
        //TODO check whether tenantId is not empty instead of checking it for null
        if (tenantId != null) {
            sql += " AND LOWER(tu.TenantId)=LOWER('" + tenantId + "')";
            sql += SqlUtils.buildSortClause(sql, "tu.FullName", SortingOrder.ASC);
            return executeSqlAndGetResultSet(sql);
        }
        return null;
    }

    /**
     * Generate comma seperated string of user status values.
     *
     * @param userStatus
     * @return
     */
    private String getCommaSeperatedStatusString(UserStatusItem[] userStatus) {
        String[] strArray = new String[userStatus.length];
        for (int i = 0; i < userStatus.length; ++i) {
            strArray[i] = String.valueOf(userStatus[i]);
        }
        //TODO i think we have to make a string separated by ',' out of array
        //return joiner.join(strArray);
        return "";
    }

    /**
     * Return true if status found in array of userstatus item list.
     *
     * @param userStatusItems
     * @param userStatus
     * @return
     */
    public boolean isContain(UserStatusItem[] userStatusItems, UserStatusItem userStatus) {
        for (int i = 0; i < userStatusItems.length; ++i) {
            if (userStatusItems[i] == userStatus) {
                return true;
            }
        }
        return false;
    }

    /**
     * insert TENANT_USER entity in database
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public long insertEntity(TenantUser entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("UserId", entity.getEntityId().toString());
        values.put("FirstName", entity.getFirstName());
        values.put("LastName", entity.getLastName());
        values.put("FullName", entity.getFullName());
        values.put("PhoneNumber", entity.getMobileNo());
        values.put("UserStatus", entity.getUserStatus());
        values.put("LoginEmail", entity.getLoginEmail());
        values.put("LastInvitationDate", Utils.getUTCDateTimeAsString(entity.getLastInvitationDate()));
        values.put("TenantId", entity.getTenantId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("Picture", entity.getPicture());
        values.put("LoginToken", entity.getLoginToken());
        long id = super.insert("PFTenantUser", values);
        return id;
    }

    @Override
    public void update(TenantUser entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("FirstName", entity.getFirstName());
        values.put("LastName", entity.getLastName());
        values.put("FullName", entity.getFullName());
        values.put("PhoneNumber", entity.getMobileNo());
        values.put("UserStatus", entity.getUserStatus());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("PFTenantUser", values, "LOWER(UserId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    public void updateUserNameAndPicture(TenantUser entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("FirstName", entity.getFirstName());
        values.put("LastName", entity.getLastName());
        String picture = null;
        if (entity.getPicture() != null && !entity.getPicture().isEmpty()) {
            picture = entity.getPicture();
        }
        values.put("Picture", picture);
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("PFTenantUser", values, "LOWER(UserId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Generate sql string with minimum required fields for TENANT_USER.
     *
     * @return
     */
    private String getSQL() {
        String sql = "SELECT [UserId],tu.[FirstName],tu.[LastName],[FullName],tu.MobileNo,USER_STATUS, [LoginEmail], [LastInvitationDate], tu.TenantId,tu.CreatedBy,ModifiedBy, ModifiedDate,tu.CreatedDate,tu.Version FROM PFTenantUser tu INNER JOIN PFTenant t on LOWER(tu.TenantId)=LOWER(t.TenantId)";
        return sql;
    }

    public boolean isTenantUserExist(String email, UUID tenantId, UUID userId) throws EwpException {
        String mySql = "Select * from PFTenantUser WHERE LOWER(TenantId)=LOWER('" + tenantId + "') and LOWER(LoginEmail)=LOWER('" + email + "') and LOWER(UserId)= LOWER('" + userId + "')";
        return SqlUtils.recordExists(mySql);
    }

    public void addTenantUser(TenantUser entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("UserId", entity.getEntityId().toString());
        values.put("FirstName", entity.getFirstName());
        values.put("LastName", entity.getLastName());
        values.put("FullName", entity.getFullName());
        values.put("PhoneNumber", entity.getMobileNo());
        values.put("UserStatus", entity.getUserStatus());
        values.put("LoginEmail", entity.getLoginEmail());
        values.put("LastInvitationDate", Utils.getUTCDateTimeAsString(entity.getLastInvitationDate()));
        values.put("TenantId", entity.getTenantId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("Picture", entity.getPicture());
        values.put("LoginToken", entity.getLoginToken());
        super.insert("PFTenantUser", values);
    }

    /**
     * @param tenantUser
     * @param resultSet
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(TenantUser tenantUser, Cursor resultSet) throws EwpException {
        // Setting the property values from database result set

        String entityId = resultSet.getString(resultSet.getColumnIndex("UserId"));
        if (entityId != null) {
            tenantUser.setEntityId(UUID.fromString(entityId));
        }

        String firstName = resultSet.getString(resultSet.getColumnIndex("FirstName"));
        if (firstName != null) {
            tenantUser.setFirstName(firstName);
        }

        String lastName = resultSet.getString(resultSet.getColumnIndex("LastName"));
        if (lastName != null) {
            tenantUser.setLastName(lastName);
        }

        String fullName = resultSet.getString(resultSet.getColumnIndex("FullName"));
        if (fullName != null) {
            tenantUser.setFullName(fullName);
        }

        String tenantId = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (tenantId != null) {
            tenantUser.setTenantId(UUID.fromString(tenantId));
        }

        String mobileNo = resultSet.getString(resultSet.getColumnIndex("PhoneNumber"));
        if (mobileNo != null) {
            tenantUser.setMobileNo(mobileNo);
        }

        String userStatus = resultSet.getString(resultSet.getColumnIndex("UserStatus"));
        if (userStatus != null) {
            tenantUser.setUserStatus(Integer.parseInt(userStatus));
        }


        String loginEmail = resultSet.getString(resultSet.getColumnIndex("LoginEmail"));
        if (loginEmail != null) {
            tenantUser.setLoginEmail(loginEmail);
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null) {
            tenantUser.setCreatedBy(createdBy);
        }

        tenantUser.setPicture(resultSet.getString(resultSet.getColumnIndex("Picture")));
        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            tenantUser.setUpdatedBy(updatedBy);
        }

        Date createdDate = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex("CreatedDate")), true, true);
        if (createdDate != null) {
            tenantUser.setCreatedAt(createdDate);
        }

        Date updatedDate = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex("ModifiedDate")), true, true);
        if (updatedDate != null) {
            tenantUser.setUpdatedAt(updatedDate);
        }

        Date lastInvitationDate = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex("LastInvitationDate")), true, true);
        if (lastInvitationDate != null) {
            tenantUser.setLastInvitationDate(lastInvitationDate);
        }

        String loginToken = resultSet.getString(resultSet.getColumnIndex("LoginToken"));
        if (loginToken != null) {
            tenantUser.setLoginToken(loginToken);
        }
        tenantUser.setDirty(false);
    }
}
