//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.exception;

import android.util.Log;

import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.List;

/**
 * It handles DATA_SERVICE module error.
 */
public class DataServiceErrorHandler extends EwpErrorHandler {

    private static final String TAG = "DataServiceErrorHandler";
    private static DataServiceErrorHandler dataServiceHandler;

    // Create the singleton
    static {
        dataServiceHandler = new DataServiceErrorHandler();
    }

    /**
     * Returns the singleton
     *
     * @return
     */
    public static DataServiceErrorHandler defaultDataServiceErrorHandler() {
        return dataServiceHandler;
    }

    /**
     * This method is overrided in DATA_SERVICE, Because in case of .WRAP error policy we want to log the error.Iin case of other error policy we are used to call base class method.
     *
     * @return
     */
    public EwpException handleEwpError(EwpException error, ErrorPolicy errorPolicy, List<String> messages, EnumsForExceptions.ErrorModule errorModule) {

        // In case of no error, return the same.
        if (error.errorType == EnumsForExceptions.ErrorType.SUCCESS) {
            return error;
        }

        switch (errorPolicy) {
            case WRAP:
                // If original generated error and handler of error are not same then generate error as wrapper error.
                List<String> errorMsg = messages;
                if (messages.isEmpty()) {
                    errorMsg = error.message;
                }
                EwpException wrapError = new EwpException(new EwpException("DataServiceErrorHandler"), error.errorType, errorMsg, errorModule, null, error.errorCode);
                wrapError.innerError = error;
                wrapError.message = errorMsg;
                String msg = wrapError.toString();
                Log.e(TAG, msg, error);
                return wrapError;
            default:
                return super.handleEwpError(error, errorPolicy, messages, errorModule);
        }
    }

    public EwpException handleEwpError(EwpException error, ErrorPolicy errorPolicy, List<String> messages, EnumsForExceptions.ErrorModule errorModule, String funcName, String filename, int lineNo, boolean logStackTrace) {

        // In case of no error, return the same.
        if (error.errorType == EnumsForExceptions.ErrorType.SUCCESS) {
            return error;
        }

        switch (errorPolicy) {
            case WRAP:
                // If original generated error and handler of error are not same then generate error as wrapper error.
                List<String> errorMsg = messages;
                if (messages.isEmpty()) {
                    errorMsg = error.message;
                }
                EwpException wrapError = new EwpException(new EwpException("DataServiceErrorHandler"), error.errorType, errorMsg, errorModule, null, error.errorCode);
                wrapError.errorCode = -1;
                wrapError.innerError = error;
                wrapError.message = errorMsg;
                String msg = wrapError.toString();
                Log.d(TAG, msg);
                return wrapError;
            default:
                return super.handleEwpError(error, errorPolicy, messages, errorModule);
        }
    }


}
