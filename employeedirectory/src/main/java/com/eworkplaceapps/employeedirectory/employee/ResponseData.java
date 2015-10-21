//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/27/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class ResponseData {

    private boolean success = false;
    private String id = "";

    /**
     * @param object JsonObject
     * @return ResponseData
     * @throws JSONException
     */
    public static ResponseData parseJSON(JSONObject object) throws JSONException {
        String success = object.getString("Success");
        String id = object.getString("Id");
        ResponseData responseData = new ResponseData();
        responseData.setId(id);
        responseData.setSuccess("true".equals(success));
        return responseData;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
