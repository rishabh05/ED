//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/15/2015
//===============================================================================
package com.eworkplaceapps.platform.exception;

import android.util.Log;

import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.eworkplaceapps.platform.utils.Utils;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * It hold the structure of error. It contain all necessary information of generated error.
 */
public class EwpException extends Exception {


    public Date errorTime = new Date();

    /**
     * ERROR type indicate the type of error. It may be validation, system error.
     */
    public EnumsForExceptions.ErrorType errorType = EnumsForExceptions.ErrorType.SUCCESS;

    /**
     * ERROR code indicate the caused of error.
     */
    public int errorCode = -1;


    /**
     * Message array hold the array of messages to show.
     */
    public List<String> message = new ArrayList<String>();

    /**
     * It holds error trace
     */
    public Object stackTrace;

    /**
     * It holds error function name
     */
    public String funcName = "";

    /**
     * It holds error file name
     */
    public String fileName = "";

    /**
     * It holds error line number
     */
    public int lineNo = -1;

    /**
     * Contains the data list
     */
    public Map<EnumsForExceptions.ErrorDataType, String[]> dataList = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

    /**
     * It indicate, Which module has error.
     */
    public EnumsForExceptions.ErrorModule errorModule = EnumsForExceptions.ErrorModule.NONE;

    /**
     * It will encapsulate the inner error.
     */
    EwpException innerError = null;

    public String localizedMessage;

    public EwpException() {
        super();
    }

    public EwpException(String message) {
        super(message);
    }

    public EwpException(ExceptionBean exceptionBean) {
        this(new EwpException("Internal Server ERROR"), EnumsForExceptions.ErrorType.stringToEnum(exceptionBean.getErrorType()), exceptionBean.getMessageList(), exceptionBean.getErrorModule(), exceptionBean.getDataList(), exceptionBean.getErrorCode());
    }

    public EwpException(Throwable throwable) {
        super(throwable);
    }

    public EwpException(Throwable throwable, String message) {
        super(message, throwable);
    }

    public EwpException(Throwable throwable, EnumsForExceptions.ErrorModule errorModule) {
        super(throwable);
        this.errorModule = errorModule;
    }

    public EwpException(Throwable throwable, EnumsForExceptions.ErrorType errorType, List<String> errorMessages, EnumsForExceptions.ErrorModule errorModule, Map<EnumsForExceptions.ErrorDataType, String[]> data, int errorCode) {
        super(throwable);
        this.errorType = errorType;
        this.message = errorMessages;
        this.errorModule = errorModule;
        this.dataList = data;
        this.errorCode = errorCode;
    }


    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    public static EwpException parseJsonToEwpException(String jsonString) {
        JSONArray jsonArray = null;
        EwpException ewpException = new EwpException();
        if (!"".equals(jsonString)) {
            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                int errorType = jsonObj.getInt("ErrorType");
                EnumsForExceptions.ErrorType error =  EnumsForExceptions.ErrorType.values()[errorType];
                ewpException.errorType =error;
                jsonArray = jsonObj.getJSONArray("MessageList");
                if (jsonArray.length() > 0) {
                    ewpException.message.add(jsonArray.getString(0));
                }
                JSONArray jsonArray2 = jsonObj.getJSONArray("EwpErrorDataList");
                if (jsonArray2.length() > 0) {
                    JSONObject ewpErrorObj = jsonArray2.getJSONObject(0);
//                    int errSubType = ewpErrorObj.getInt("ErrorSubType");
//                    String strMessage = ewpErrorObj.getString("Message");
//                   ewpException.dataList.put(EnumsForExceptions.ErrorDataType.values()[errSubType], new String[]{strMessage});
                    parseDictionaryDataListValues(jsonArray2,error);
                }
                // jsonObj.getJSONArray("EwpErrorDataList");
                ewpException.errorType = EnumsForExceptions.ErrorType.values()[errorType];

            } catch (JSONException e) {
                LogConfigurer.error("EWPException", "JSONException->" + e);
                Log.d("EWPException", "JSONException->" + e);
            }
        }
        return ewpException;
    }

    /**
     *
     * @param errorType
     * @param errorSubType
     * @return
     */
    private static EnumsForExceptions.ErrorDataType  generateErrorSubtypeFromTypeAndSubtype(EnumsForExceptions.ErrorType errorType , int errorSubType) {
        switch (errorType) {
            case VALIDATION_ERROR:
                return getValidationErrorSubType(errorSubType);
            case AUTHENTICATION_ERROR:
                if (errorSubType == 1) {
                    return EnumsForExceptions.ErrorDataType.INVALID_LOGIN_TOKEN;
                }
                else if (errorSubType == 2) {
                    return EnumsForExceptions.ErrorDataType.INVALID_EMAIL;
                }
                return EnumsForExceptions.ErrorDataType.INVALID_PASSWORD;
            default:
                return EnumsForExceptions.ErrorDataType.values()[errorSubType];

        }
    }
    /**
     *
     * @param array
     * @param errorType
     * @return
     * @throws JSONException
     */
    public static Map<EnumsForExceptions.ErrorDataType, String[]> parseDictionaryDataListValues(JSONArray array,EnumsForExceptions.ErrorType errorType) throws JSONException {
    {
        if  (array == null) {
        return null;
    }
        Map<EnumsForExceptions.ErrorDataType, String[]>  nameValueDict = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        int val;
        EnumsForExceptions.ErrorDataType errorDataType = EnumsForExceptions.ErrorDataType.NONE;
        for (int i = 0; i < array.length(); i++) {
            JSONObject jObj = array.getJSONObject(i);
            errorDataType = EnumsForExceptions.ErrorDataType.NONE;
            val = jObj.getInt("ErrorSubType");
            String strMessage = jObj.getString("Message");
            if (EnumsForExceptions.ErrorDataType.values().length > val){
               errorDataType = generateErrorSubtypeFromTypeAndSubtype(errorType,val);
                }
            if (!"".equalsIgnoreCase(strMessage) && errorDataType != EnumsForExceptions.ErrorDataType.NONE) {
                nameValueDict.put(errorDataType, new String[]{strMessage});
            }
        }
            return nameValueDict;
        }
    }
    /**
     * @param status
     * @return EwpException
     */
    public static EwpException handleError(HttpResponse response, AjaxStatus status) {
        int code = 0;
        String output = "";
        if (response != null) {
            code = response.getStatusLine().getStatusCode();
            output = Utils.convertResponseToString(response);
        } else if (status != null) {
            code = status.getCode();
            output = status.getError();
        }
        EwpException ewpException = new EwpException();
        if (code == 200) {
            ewpException.errorType = EnumsForExceptions.ErrorType.SUCCESS;
        } else if (code == 500) {
            ewpException = parseJsonToEwpException(output);
        } else {
            if (status != null) {
                ewpException.message.add(status.getMessage());
                ewpException.localizedMessage = status.getMessage();
            } else if (response != null) {
                ewpException.message.add(response.getStatusLine().getReasonPhrase());
                ewpException.localizedMessage = response.getStatusLine().getReasonPhrase();
            }
            ewpException.errorType = EnumsForExceptions.ErrorType.SYSTEM_ERROR;
        }
        return ewpException;
    }

    /**
     * @param errorSubType
     * @return EnumsForExceptions.ErrorDataType
     */
    public static EnumsForExceptions.ErrorDataType getValidationErrorSubType(int errorSubType) {
        switch (errorSubType) {
            case 1:
                return EnumsForExceptions.ErrorDataType.REQUIRED;
            case 2:
                return EnumsForExceptions.ErrorDataType.LENGTH;
            case 3:
                return EnumsForExceptions.ErrorDataType.RANGE;
            case 4:
                return EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE;
            case 5:
                return EnumsForExceptions.ErrorDataType.INVALID_ENTITY;
            case 6:
                return EnumsForExceptions.ErrorDataType.EXCEEDED_UDF_FIELD;
            case 7:
                return EnumsForExceptions.ErrorDataType.REFERENCE_EXIST;
            default:
                return EnumsForExceptions.ErrorDataType.NONE;
        }
    }

    public static EwpException handleEwpException() {
        return null;
    }

}
