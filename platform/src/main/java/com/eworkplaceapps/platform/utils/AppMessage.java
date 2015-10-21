//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

/**
 * contain all necessary validation messages.
 */
public interface AppMessage {
    String MAX_ATTACHMENT_SIZE_REQUIRED = "";

    String MAX_ATTACHMENT_STORAGE_SIZE_REQUIRED = "";

    String TENANT_STATUS_REQUIRED = "";

    String TENANT_TYPE_REQUIRED = "";

    String TITLE_REQUIRED = "Title is required.";

    String DESCRIPTION_REQUIRED = "Task description is required.";

    String REQUIRED_FIELD = "required.";

    String NAME_REQUIRED = "Name is required.";

    String FIRST_NAME_IS_REQUIRED = "First name is required.";

    String LAST_NAME_REQUIRED = "Last name is required.";

    String EMAIL_REQUIRED = "EMAIL is required.";

    String PASSWORD_REQUIRED = "Password is required.";

    String INVALID_EMAIL = "Invalid email address.";

    String DUPLICATE_NAME = "DUPLICATE name.";

    String TASK_STATUS_REQUIRED = "Task status is required.";

    String ERROR_IN_ADDING_USER = "ERROR in adding user.";

    // ROLE entity Messages

    String ROLE_ID_REQUIRED = "RoleId is required.";
    String REFERENCE_ID_REQUIRED = "Reference id is required.";

    String ERROR_IN_SYNC_WITH_CLIENT_TO_SERVER = "ERROR occurred in sync.";

    String ERROR_IN_SYNC_WITH_REPLY_DATA = "ERROR occurred in sync reply.";

    String APPLICATION_ERROR = "ERROR occurred.";

    String INVALID_USER = "Invalid EMAIL ADDRESS";

    String LENGTH_ERROR = "STRING length should be lesser.";

    // ERROR messages for main error category type

    String SYSTEM_ERROR = "This is system error.";

    String INVALID_APP_VERSION_ERROR = "You are running an older version of Issue Tracker. In order to connect to the Issue Tracker server, please update the application.";

    String DATABASE_ERROR = "This is database error.";

    String CONCURRENCY_ERROR = "This item has been updated or deleted by another user. Please reload the item and then retry your operation.";

    String SECURITY_ERROR = "You do not have permission to perform this operation.";

    String RE_LOGIN_ERROR = "This is relogin error.";

    String INVALID_DEVICE_ID_ERROR = "This is invalid device ID error.";

    String NOT_IMPLEMENTED_ERROR = "This is not Implemented error.";

    String INVALID_DATABASE_VERSION = "This is invalid database version error.";

    String INITIALIZATION_FAILED = "Error occurred in initialization";

    String EMPLOYEE_REFERENCE_EXIST = "The employee you wish to delete is a manager. Please select a new manager, before deleting this employee.";

}
