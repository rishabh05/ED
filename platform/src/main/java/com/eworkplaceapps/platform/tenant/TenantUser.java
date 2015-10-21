package com.eworkplaceapps.platform.tenant;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class encapsulates all data for TENANT_USER entity.
 */
public class TenantUser extends BaseEntity {

    private static String TenantUser_Entity_Name = "TENANT_USER";

    public TenantUser() {
        super(TenantUser_Entity_Name);
    }

    /**
     * Create TENANT_USER object and return created object.
     *
     * @return
     */
    public static TenantUser createEntity() {
        return new TenantUser();
    }

    /**
     * Getter/Setter property
     * TenantId property represents uniqueness for tenant.
     */
    private UUID tenantId = Utils.emptyUUID();


    private int userStatus = 0;

    // Getter/setter for login email
    private String loginEmail;
    private String picture = "";

    // Getter/setter for login Token
    private String loginToken;

    // Getter/setter for last invitation date
    // Last invitation send date.
    private Date lastInvitationDate = new Date();

    // Getter/setter for firstname
    private String firstName;

    // Getter/setter for fullname
    private String fullName;

    // Getter/setter for last name.
    private String lastName;

    // Getter/setter for mobile number.
    private String mobileNo;

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        super.setPropertyChanged(this.userStatus, userStatus);
        this.userStatus = userStatus;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        super.setPropertyChanged(this.loginEmail, loginEmail);
        this.loginEmail = loginEmail;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        super.setPropertyChanged(this.loginToken, loginToken);
        this.loginToken = loginToken;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        super.setPropertyChanged(this.picture, picture);
        this.picture = picture;
    }

    public Date getLastInvitationDate() {
        return lastInvitationDate;
    }

    public void setLastInvitationDate(Date lastInvitationDate) {
        super.setPropertyChanged(this.lastInvitationDate, lastInvitationDate);
        this.lastInvitationDate = lastInvitationDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        super.setPropertyChanged(this.firstName, firstName);
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        super.setPropertyChanged(this.fullName, fullName);
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        super.setPropertyChanged(this.lastName, lastName);
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        super.setPropertyChanged(this.mobileNo, mobileNo);
        this.mobileNo = mobileNo;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    /**
     * It validate TENANT_USER entity
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        String[] data = new String[3];

        if (this.firstName == null) {
            message.add(AppMessage.FIRST_NAME_IS_REQUIRED);
            data[0] = "FIRST Name";
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, data);
        }

        if (this.lastName == null) {
            message.add(AppMessage.LAST_NAME_REQUIRED);
            data = dicError.get(EnumsForExceptions.ErrorDataType.REQUIRED);
            data[1] = "Last Name";
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, data);
        }
        String[] data2 = new String[2];
        if (this.firstName != null && this.firstName.length() > 200) {
            data2[0] = "FIRST Name";
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, data2);
            message.add(AppMessage.LENGTH_ERROR);
        }

        if (this.lastName != null && this.lastName.length() > 200) {
            data2 = dicError.get(EnumsForExceptions.ErrorDataType.LENGTH);
            data2[1] = "Last Name";
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, data2);
            message.add(AppMessage.LENGTH_ERROR);
        }

        if (this.loginEmail == null || "".equals(this.loginEmail)) {
            message.add(AppMessage.EMAIL_REQUIRED);
            data = dicError.get(EnumsForExceptions.ErrorDataType.REQUIRED);
            data[2] = "EMAIL";
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, data);
        }

        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Exception in TENANT_USER"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    public static List<TenantUser> parseAddEmployeeData(JSONArray jsonArray) throws JSONException {
        List<TenantUser> tenantUserList = new ArrayList<TenantUser>();
        TenantUser tenantUser = null;
        EwpException ewpException;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jObj = jsonArray.getJSONObject(i);
            boolean success = jObj.getBoolean("Success");
            if (!success) {
                if (!jObj.isNull("EwpErrorJson")) {
                    JSONObject ewpExceptionObj = jObj.getJSONObject("EwpErrorJson");
                    ewpException = new EwpException();
                    int errType = jObj.getInt("ErrorType");
                    if (EnumsForExceptions.ErrorType.values().length > errType)
                        ewpException.errorType = EnumsForExceptions.ErrorType.values()[errType];
                    JSONArray jsonArray1 = ewpExceptionObj.getJSONArray("MessageList");
                    if (jsonArray1.length() > 0) {
                        ewpException.setLocalizedMessage(jsonArray1.getString(0));
                    }
                }
            } else {
                tenantUser = new TenantUser();
                JSONObject userObj = jObj.getJSONObject("User");
                tenantUser.setEntityId(UUID.fromString(userObj.getString("UserId")));
                tenantUser.setTenantId(UUID.fromString(userObj.getString("TenantId")));
                tenantUser.setFirstName(userObj.getString("FirstName"));
                tenantUser.setLastName(userObj.getString("LastName"));
                tenantUser.setFullName(userObj.getString("FullName"));
                tenantUser.setMobileNo(userObj.getString("PhoneNumber"));
                tenantUser.setUserStatus(userObj.getInt("UserStatus"));
                tenantUser.setLoginEmail(userObj.getString("LoginEmail"));
                tenantUser.setLastInvitationDate(Utils.dateFromString(userObj.getString("LastInvitationDate"), true, true));
                tenantUser.setCreatedBy(userObj.getString("CreatedBy"));
                tenantUser.setUpdatedBy(userObj.getString("ModifiedBy"));
                tenantUser.setDirty(userObj.getBoolean("IsDirty"));
                tenantUser.setCreatedAt(Utils.dateFromString(userObj.getString("CreatedDate"), true, true));
                tenantUser.setUpdatedAt(Utils.dateFromString(userObj.getString("ModifiedDate"), true, true));
                tenantUser.setLastOperationType(DatabaseOperationType.values()[userObj.getInt("OperationType")]);
                tenantUser.setPicture(userObj.getString("Picture"));
                tenantUserList.add(tenantUser);
            }
        }
        return tenantUserList;
    }

    public static Tuple.Tuple2<List<TenantUser>, EwpException, String> parseTenantUserDictJson(JSONArray jsonArray) throws JSONException {
        List<TenantUser> tenantUserList = new ArrayList<TenantUser>();
        EwpException exception = new EwpException();
        Tuple.Tuple2<List<TenantUser>, EwpException, String> tuple = new Tuple.Tuple2<List<TenantUser>, EwpException, String>(tenantUserList, exception, "");
        TenantUser tenantUser = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jObj = jsonArray.getJSONObject(i);
            boolean success = jObj.getBoolean("Success");
            if (!success) {
                if (!jObj.isNull("EwpErrorJson")) {
                    JSONObject ewpExceptionObj = jObj.getJSONObject("EwpErrorJson");
                    int errType = ewpExceptionObj.getInt("ErrorType");
                    if (EnumsForExceptions.ErrorType.values().length > errType)
                        exception.errorType = EnumsForExceptions.ErrorType.values()[errType];
                    JSONArray jsonArray1 = ewpExceptionObj.getJSONArray("MessageList");
                    String msg = "";
                    if (jsonArray1.length() > 0) {
                        msg = jsonArray1.getString(0);
                        exception.message.add(msg);
                    }
                    JSONArray jsonArray2 = ewpExceptionObj.getJSONArray("EwpErrorDataList");
                    if (jsonArray2.length() > 0) {
                        JSONObject ewpExceptionObj1 = jsonArray2.getJSONObject(0);
                        int val = ewpExceptionObj1.getInt("ErrorSubType");
                        if (EnumsForExceptions.ErrorDataType.values().length > val)
                            exception.dataList.put(EnumsForExceptions.ErrorDataType.values()[val], new String[]{msg});
                    }
                }
                continue;
            }
            tenantUser = new TenantUser();
            JSONObject userObj = jObj.getJSONObject("User");
            tenantUser.setEntityId(UUID.fromString(userObj.getString("UserId")));
            tenantUser.setTenantId(UUID.fromString(userObj.getString("TenantId")));
            tenantUser.setFirstName(userObj.getString("FirstName"));
            tenantUser.setLastName(userObj.getString("LastName"));
            tenantUser.setFullName(userObj.getString("FullName"));
            tenantUser.setMobileNo(userObj.getString("PhoneNumber"));
            tenantUser.setUserStatus(userObj.getInt("UserStatus"));
            tenantUser.setLoginEmail(userObj.getString("LoginEmail"));
//            tenantUser.setLastInvitationDate(Utils.dateFromString(userObj.getString("LastInvitationDate"), true, true));
            tenantUser.setCreatedBy(userObj.getString("CreatedBy"));
            tenantUser.setUpdatedBy(userObj.getString("ModifiedBy"));
            tenantUser.setDirty(userObj.getBoolean("IsDirty"));
            tenantUser.setCreatedAt(Utils.dateFromString(userObj.getString("CreatedDate"), true, true));
            tenantUser.setUpdatedAt(Utils.dateFromString(userObj.getString("ModifiedDate"), true, true));
            tenantUser.setLastOperationType(DatabaseOperationType.values()[userObj.getInt("OperationType")]);
            tenantUser.setPicture(userObj.getString("Picture"));
            tenantUserList.add(tenantUser);
        }
        return tuple;
    }

    private static EwpException parseJsonError(JSONObject errorDict, EwpException error, String employeeName) {
        if (errorDict != null) {
            EwpException ewpError = EwpException.parseJsonToEwpException(errorDict.toString());
            if (error != null) {
                String[] msg = error.dataList.get(EnumsForExceptions.ErrorDataType.DUPLICATE);
                if (msg != null && msg.length > 0) {
                    if (employeeName != null) {
                        msg[0] += ", " + employeeName;
                    }
                    error.dataList.put(EnumsForExceptions.ErrorDataType.DUPLICATE, msg);
                } else if (error.errorType == EnumsForExceptions.ErrorType.DUPLICATE) {
                    msg = new String[3];
                    msg[0] = employeeName;
                    ewpError.dataList.put(EnumsForExceptions.ErrorDataType.DUPLICATE, msg);
                }
                return ewpError;
            } else {
                error = ewpError;
                if (error != null) {
                    String[] msg = error.dataList.get(EnumsForExceptions.ErrorDataType.DUPLICATE);
                    if (msg != null && msg.length > 0) {
                        if (employeeName != null) {
                            msg[0] += ", " + employeeName;
                        }
                        error.dataList.put(EnumsForExceptions.ErrorDataType.DUPLICATE, msg);
                    } else if (error.errorType == EnumsForExceptions.ErrorType.DUPLICATE) {
                        msg = new String[3];
                        msg[0] = employeeName;
                        ewpError.dataList.put(EnumsForExceptions.ErrorDataType.DUPLICATE, msg);
                    }

                }
                return ewpError;
            }

        }
        return null;

    }
}