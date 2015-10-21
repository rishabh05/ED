//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/1/2015
//===============================================================================
package com.eworkplaceapps.platform.exception;

import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for Exceptions
 */
public class ExceptionBean {

    private String errorType;
    private int errorCode;
    private EnumsForExceptions.ErrorModule errorModule;
    private Map<EnumsForExceptions.ErrorDataType, String[]> dataList = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
    private List<String> messageList = new ArrayList<String>();

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Map<EnumsForExceptions.ErrorDataType, String[]> getDataList() {
        return dataList;
    }

    public void setDataList(Map<EnumsForExceptions.ErrorDataType, String[]> dataList) {
        this.dataList = dataList;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public EnumsForExceptions.ErrorModule getErrorModule() {
        return errorModule;
    }

    public void setErrorModule(EnumsForExceptions.ErrorModule errorModule) {
        this.errorModule = errorModule;
    }

    @Override
    public String toString() {
        return "ExceptionBean{" +
                "errorType='" + errorType + '\'' +
                ", errorCode=" + errorCode +
                ", errorModule=" + errorModule +
                ", dataList=" + dataList +
                ", messageList=" + messageList +
                '}';
    }

    /**
     * parse error response
     *
     * @param object
     */
    public static ExceptionBean parseErrorXml(XmlPullParser object, EnumsForExceptions.ErrorModule module, int errorCode) throws EwpException {
        try {
            ExceptionBean exceptionBean = null;
            String text = "";
            int event = object.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        if ("EwpError".equals(name)) {
                            exceptionBean = new ExceptionBean();
                            exceptionBean.setErrorCode(errorCode);
                            exceptionBean.setErrorModule(module);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        if ("ErrorType".equals(name)) {
                            exceptionBean.setErrorType(text);
                        } else if ("Message".equals(name)) {
                            exceptionBean.getMessageList().add(text);
                        } else if ("DATA".equals(name)) {
                            String key = object.getAttributeValue(null, "Key");
                            String value = object.getAttributeValue(null, "Value");
                            exceptionBean.getDataList().put(EnumsForExceptions.ErrorDataType.keyToEnum(Integer.parseInt(key)), new String[]{value});
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return exceptionBean;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }
}
