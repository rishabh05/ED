//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.exception;

import android.util.Log;

import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.List;


/**
 * EwpErrorHandler uses EwpError to indicate success or failure of a request. EwpErrorHandler uses diffrent policies to handles error.
 * <p/>
 * There are three types of policies
 * <p/>
 * [1] PASS_THROUGH: It passes through the error without any change.
 * [2] RETHROW: It rethrows an error and log error.
 * [3] WRAP: Rethrows a different error with the original error as an inner error.
 * <p/>
 * EwpErrorHandler uses EwpError and ErrorPolicy to handles error as per policies.
 * EwpError has ErrorType property that indicate success, or error, ErrorType resulted from the called method.
 * (e.g. When saving an entity and a required field value is missing, Then it raise ValidationType ERROR).
 * <p/>
 * There are four types of error:
 * SUCCESS : This means, DATA has no error.
 * VALIDATION_ERROR = It means data has some validation error. For example DATA input validation, DUPLICATE data validation.
 * DATABASE_ERROR = It handle database error, Like SQL query error and database connection error.
 * SYSTEM_ERROR = Any nil or data wrapping error etc.
 * <p/>
 */
public class EwpErrorHandler {

    public enum ErrorPolicy {

        // Specifies that the no category.
        NONE(0),

        // Specifies that the exception is PASS_THROUGH category.
        // It passes through the error without any change, sort of RETHROW.
        // It may log the exception.
        PASS_THROUGH(1),

        // Specifies that the error is RETHROW category.
        // It rethrows an error and log the message.
        RETHROW(2),

        // Specifies that the error is WRAP category.
        // Rethrows a different error with the original exception as an inner exception.
        WRAP(3);

        private int id;

        ErrorPolicy(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     *   Method is used to handle the data/service error.
     error is the original error.
     ErrorPolicy: According to policy, we want to handles the error.
     messages: ERROR message we want to raise.
     errorGeneratorModule: ERROR handler of the current module.
     */
    /**
     * Method do not log error if errortype success (it means its no error).
     * ErrorPolicy
     *
     * @return
     */
    public EwpException handleEwpError(EwpException error, ErrorPolicy errorPolicy, List<String> messages, EnumsForExceptions.ErrorModule errorModule) {

        // In case of no error, return the same.
        if (error.errorType == EnumsForExceptions.ErrorType.SUCCESS) {
            return error;
        }

        switch (errorPolicy) {
            case PASS_THROUGH:
                // Pass through will append the messages and return the same error.
                for (int i = 0; i < messages.size(); i++) {
                    error.message.add(messages.get(i));
                }
                return error;
            case RETHROW:
                Log.e("EwpErrorHandler", error.toString(), error);
                return error;
            case WRAP:
                // If original generated error and handler of error are not same then generate error as wrapper error.
                List<String> errorMsg = messages;
                if (messages.isEmpty()) {
                    errorMsg = error.message;
                }

                EwpException wrapError = new EwpException(new EwpException("EwpErrorHandler"), error.errorType, errorMsg, errorModule, null, error.errorCode);
                wrapError.innerError = error;
                wrapError.message = errorMsg;
                return wrapError;

            default:
                return error;
        }

    }


    /**
     * In case of rethrow, Use this method.
     *
     * @param error
     * @param errorPolicyEnum
     * @return
     */
    public EwpException handleEwpError(EwpException error, ErrorPolicy errorPolicyEnum) {
        return handleEwpError(error, errorPolicyEnum);

    }

    /**
     * It is used to log error.
     *
     * @param error
     */
    public void logError(EwpException error) {
        Log.e("EwpErrorHandler", error.toString(),error);
    }
}
