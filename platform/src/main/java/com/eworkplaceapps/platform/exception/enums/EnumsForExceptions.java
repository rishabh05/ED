//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/16/2015
//===============================================================================
package com.eworkplaceapps.platform.exception.enums;

/**
 * The class contains all the enums related to Exceptions used in application
 */
public class EnumsForExceptions {

    public enum ErrorType {
        SUCCESS(0),
        SYSTEM_ERROR(1),
        INVALID_VERSION(2),
        DATABASE_ERROR(3),
        AUTHENTICATION_ERROR(4),
        RE_LOGIN(5),
        SECURITY_ERROR(6),
        VALIDATION_ERROR(7),
        DUPLICATE(8),
        CONCURRENCY_ERROR(9),
        IMPORT_DATA_ERROR(10),
        INVALID_DEVICE_ID(11),
        NOT_IMPLEMENTED(12),
        INVALID_REQUEST_ARGUMENT(13);


        private int id;

        ErrorType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case SUCCESS:
                    return "SUCCESS";
                case VALIDATION_ERROR:
                    return "VALIDATION_ERROR";
                case DATABASE_ERROR:
                    return "DATABASE_ERROR";
                case CONCURRENCY_ERROR:
                    return "CONCURRENCY_ERROR";
                case INVALID_DEVICE_ID:
                    return "INVALID_DEVICE_ID";
                case SECURITY_ERROR:
                    return "SECURITY_ERROR";
                case NOT_IMPLEMENTED:
                    return "NOT_IMPLEMENTED";
                case RE_LOGIN:
                    return "RE_LOGIN";
                case DUPLICATE:
                    return "DUPLICATE";
                case AUTHENTICATION_ERROR:
                    return "AUTHENTICATION_ERROR";
                case IMPORT_DATA_ERROR:
                    return "IMPORT_DATA_ERROR";
                default:
                    return "SYSTEM_ERROR";
            }
        }

        public static EnumsForExceptions.ErrorType stringToEnum(String displayString) {
            switch (displayString) {
                case "SUCCESS":
                    return EnumsForExceptions.ErrorType.SUCCESS;
                case "VALIDATION_ERROR":
                    return EnumsForExceptions.ErrorType.VALIDATION_ERROR;
                case "DATABASE_ERROR":
                    return EnumsForExceptions.ErrorType.DATABASE_ERROR;
                case "NOT_IMPLEMENTED":
                    return EnumsForExceptions.ErrorType.NOT_IMPLEMENTED;
                case "INVALID_DEVICE_ID":
                    return EnumsForExceptions.ErrorType.INVALID_DEVICE_ID;
                case "CONCURRENCY_ERROR":
                    return EnumsForExceptions.ErrorType.CONCURRENCY_ERROR;
                case "SECURITY_ERROR":
                    return EnumsForExceptions.ErrorType.SECURITY_ERROR;
                case "RE_LOGIN":
                    return EnumsForExceptions.ErrorType.RE_LOGIN;
                case "DUPLICATE":
                    return EnumsForExceptions.ErrorType.DUPLICATE;
                case "AUTHENTICATION_ERROR":
                    return EnumsForExceptions.ErrorType.AUTHENTICATION_ERROR;
                default:
                    return EnumsForExceptions.ErrorType.SYSTEM_ERROR;

            }
        }
    }

    public enum ErrorDataType {
        NONE(0),
        DUPLICATE(1),
        DB_VERSION(2),
        APP_VERSION(3),
        LENGTH(4),
        REQUIRED(5),
        // RANGE
        RANGE(6),
        // Invalid EMAIL
        INVALID_EMAIL(7),
        // Invalid Password
        INVALID_PASSWORD(8),
        CHANGE_PASSWORD(9),
        INACTIVE_TENANT(10),
        INACTIVE_USER(11),
        FILE_NOT_FOUND(12),
        OLE_DB_CONNECTION_ERROR(13),
        INVALID_FILE_TYPE(14),
        IMPORT_PERMISSION_DENIED(15),
        INVALID_FIELD_VALUE(16),
        EXCEEDED_UDF_FIELD(17),
        SHEET_NOT_FOUND(18),
        ACCESS_DENIED(19),
        REFERENCE_EXIST(20),
        INVALID_ENTITY(21),
        INVALID_LOGIN_TOKEN(22);
        private int id;

        ErrorDataType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case DUPLICATE:
                    return "DUPLICATE";
                case DB_VERSION:
                    return "DB_VERSION";
                case APP_VERSION:
                    return "APP_VERSION";
                case LENGTH:
                    return "LENGTH";
                case RANGE:
                    return "RANGE";
                case INVALID_EMAIL:
                    return "INVALID_EMAIL";
                case INVALID_PASSWORD:
                    return "INVALID_PASSWORD";
                case CHANGE_PASSWORD:
                    return "CHANGE_PASSWORD";
                case INACTIVE_TENANT:
                    return "INACTIVE_TENANT";
                case INACTIVE_USER:
                    return "INACTIVE_USER";
                case INVALID_FIELD_VALUE:
                    return "InvalidFieldValue";
                case ACCESS_DENIED:
                    return "ACCESS_DENIED";
                default:
                    return "REQUIRED";
            }
        }

        public static EnumsForExceptions.ErrorDataType keyToEnum(int key) {
            switch (key) {
                case 1:
                    return EnumsForExceptions.ErrorDataType.DUPLICATE;
                case 2:
                    return EnumsForExceptions.ErrorDataType.DB_VERSION;
                case 3:
                    return EnumsForExceptions.ErrorDataType.APP_VERSION;
                case 4:
                    return EnumsForExceptions.ErrorDataType.LENGTH;
                case 5:
                    return EnumsForExceptions.ErrorDataType.RANGE;
                case 6:
                    return EnumsForExceptions.ErrorDataType.INVALID_EMAIL;
                case 7:
                    return EnumsForExceptions.ErrorDataType.INVALID_PASSWORD;
                case 8:
                    return EnumsForExceptions.ErrorDataType.CHANGE_PASSWORD;
                case 9:
                    return EnumsForExceptions.ErrorDataType.INACTIVE_TENANT;
                case 10:
                    return EnumsForExceptions.ErrorDataType.INACTIVE_USER;
                case 11:
                    return EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE;
                case 12:
                    return EnumsForExceptions.ErrorDataType.ACCESS_DENIED;
                default:
                    return EnumsForExceptions.ErrorDataType.REQUIRED;

            }
        }

        public static EnumsForExceptions.ErrorDataType stringToEnum(String displayString) {
            switch (displayString) {
                case "DUPLICATE":
                    return EnumsForExceptions.ErrorDataType.DUPLICATE;
                case "DB_VERSION":
                    return EnumsForExceptions.ErrorDataType.DB_VERSION;
                case "APP_VERSION":
                    return EnumsForExceptions.ErrorDataType.APP_VERSION;
                case "LENGTH":
                    return EnumsForExceptions.ErrorDataType.LENGTH;
                case "RANGE":
                    return EnumsForExceptions.ErrorDataType.RANGE;
                case "INVALID_EMAIL":
                    return EnumsForExceptions.ErrorDataType.INVALID_EMAIL;
                case "INVALID_PASSWORD":
                    return EnumsForExceptions.ErrorDataType.INVALID_PASSWORD;
                case "CHANGE_PASSWORD":
                    return EnumsForExceptions.ErrorDataType.CHANGE_PASSWORD;
                case "INACTIVE_TENANT":
                    return EnumsForExceptions.ErrorDataType.INACTIVE_TENANT;
                case "INACTIVE_USER":
                    return EnumsForExceptions.ErrorDataType.INACTIVE_USER;
                case "InvalidFieldValue":
                    return EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE;
                case "ACCESS_DENIED":
                    return EnumsForExceptions.ErrorDataType.ACCESS_DENIED;
                default:
                    return EnumsForExceptions.ErrorDataType.REQUIRED;

            }
        }
    }

    // ErrorGeneratorModule Enum:
    public enum ErrorModule {
        DATA(0),
        DATA_SERVICE(1),
        SYNC(2),
        APPLICATION(3),
        NONE(4);

        @Override
        public String toString() {
            switch (this) {
                case DATA:
                    return "DataModule";
                case DATA_SERVICE:
                    return "DataServiceModule";
                case SYNC:
                    return "SyncModule";
                case APPLICATION:
                    return "APPLICATION";
                default:
                    return "NONE";
            }
        }

        private int id;

        ErrorModule(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
