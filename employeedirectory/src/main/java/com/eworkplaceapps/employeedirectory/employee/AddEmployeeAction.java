//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/25/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AddEmployeeAction {

    /// Getter for emfirstNameployee firstName
    private String firstName = "";
    private String lastName = "";
    /// Getter  for  profile picture
    private String email = "";

    /// It validate employee enity.
    public boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the first name null or empty.
        if (this.firstName == null && "".equals(this.firstName)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"FirstName"});
            message.add(AppMessage.NAME_REQUIRED);
        }
        /// It is used to validate the lastName name null or empty.
        if (this.lastName == null && "".equals(this.lastName)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"LastName"});
            message.add(AppMessage.NAME_REQUIRED);
        }

        /// It is used to validate the email null or empty.
        if (this.email == null && "".equals(this.email)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.EMAIL_REQUIRED);
        } else if (!Utils.isValidEmail(this.email)) {
            dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, new String[]{"Email"});
            message.add(AppMessage.INVALID_EMAIL);
        }
        /// It is used to check the max charecter allowed in firstName.
        if ((this.firstName == null && "".equals(this.firstName)) && this.firstName.length() > 200) {
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, new String[]{"FirstName"});
            message.add(AppMessage.LENGTH_ERROR);
        }
        /// It is used to check the max charecter allowed in lastName.
        if ((this.lastName == null && "".equals(this.lastName)) && this.lastName.length() > 200) {
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, new String[]{"LastName"});
            message.add(AppMessage.LENGTH_ERROR);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("VALIDATION_ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> toDictionary() {
        Map<String, Object> dict = new HashMap<String, Object>();
        dict.put("FullName", this.firstName+" "+this.lastName);
        //dict.put("LastName", this.lastName);
        dict.put("LoginEmail", this.email);
        dict.put("SendInvitation", "true");
        return dict;
    }
    public JSONObject toInvitedEmployeeObject() throws JSONException {
        JSONObject dict = new JSONObject();
        dict.put("FullName", this.firstName+" "+this.lastName);
        //dict.put("LastName", this.lastName);
        dict.put("Email", this.email);
        dict.put("SendInvitation", "true");
        return dict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddEmployeeAction that = (AddEmployeeAction) o;

        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
