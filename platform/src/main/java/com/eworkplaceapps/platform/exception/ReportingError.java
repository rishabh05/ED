package com.eworkplaceapps.platform.exception;

import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;

import java.util.Map;
import java.util.Set;

import static com.eworkplaceapps.platform.exception.enums.EnumsForExceptions.ErrorType;


/**
 * // This is a ReportingError class for making Error messages. Points to note about the class:
 * // This class provides error messages which we will display on UI.
 * // It implements complete control flow to make the error messages.
 * // There are two main control flows:
 * // (1) First we check that we get any error or not.
 * // (2) If we get any error then make the appropriate message
 * Created by eWorkplace on 7/28/2015.
 */
public class ReportingError {

    public String checkResponse(EwpException response) {
        String message = "";
        switch (response.errorType) {
            case SYSTEM_ERROR:
                message = AppMessage.SYSTEM_ERROR;
                break;
            case VALIDATION_ERROR:
                message = makeMessageString(response);
                break;
            case DUPLICATE:
                message = makeMessageString(response);
                break;
            case DATABASE_ERROR:
                message = AppMessage.DATABASE_ERROR;
                break;
            case CONCURRENCY_ERROR:
                message = AppMessage.CONCURRENCY_ERROR;
                break;
            case INVALID_VERSION:
                message = AppMessage.INVALID_APP_VERSION_ERROR;
                break;
            case SUCCESS:
                message = "Success";
                break;
            case SECURITY_ERROR:
                message = AppMessage.SECURITY_ERROR;
                break;
            case RE_LOGIN:
                message = AppMessage.RE_LOGIN_ERROR;
                break;
            case INVALID_DEVICE_ID:
                message = AppMessage.INVALID_DEVICE_ID_ERROR;
                break;
            case NOT_IMPLEMENTED:
                message = AppMessage.NOT_IMPLEMENTED_ERROR;
                break;
            case AUTHENTICATION_ERROR:
                message = "Authentication error";
                break;
            default:
                return "";
        }
        return message;
    }


    public String makeMessageString(EwpException response) {
        String errorMessage = "";
        Map<EnumsForExceptions.ErrorDataType, String[]> dataDict = response.dataList;
        if (dataDict == null || dataDict.size() == 0) {
            errorMessage = !response.message.isEmpty() ? response.message.get(0) : "";
        }
        if (dataDict != null) {
            Set<EnumsForExceptions.ErrorDataType> keySet = dataDict.keySet();
            String[] d;
            for (EnumsForExceptions.ErrorDataType key : keySet) {
                switch (key) {
                    case DUPLICATE:
                        d = dataDict.get(key);
                        break;
                    case DB_VERSION:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case APP_VERSION:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case LENGTH:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case REQUIRED:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        errorMessage = errorMessage + " " + "Required";
                        break;
                    case RANGE:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case INVALID_EMAIL:
                        d = dataDict.get(key);
                        errorMessage= "The employee you wish to delete is a manager. Please select a new manager before deleting this employee.";
//                        errorMessage = d[0];
                        break;
                    case INVALID_PASSWORD:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case INVALID_FIELD_VALUE:
                        d = dataDict.get(key);
                        errorMessage = d[0];
                        break;
                    case REFERENCE_EXIST:
                         d =  dataDict.get(key);
                    if (d != null && d.length > 0) {
                        errorMessage += d[0];
                    }
                    else {
                        errorMessage += response.message.get(0);
                    }
                    break;
                    default:
                        errorMessage = response.message.get(0);
                        break;
                }
            }
        }
        return errorMessage;
    }

}
