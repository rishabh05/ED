//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/25/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.os.AsyncTask;

import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It is used to add new employee in online mode.
 */
public class EmployeeOnLineActionService {


    private List<AddEmployeeAction> addEmployeesList;

    private String getAlphaOne(){
        return "http://139.144.10.111/services/ed/";
    }

    private String getPMUrl() {
        return "http://ewp-dev39.eworkplace.com/alpha1/services/ed/";
    }

    private String getDevelopmentUrl() {
        return "http://172.16.1.105:84/api/ED/Employee/";
    }

    private String getFortyUrl() {
        return "http://172.16.1.105/services/ed/";
    }

    private String getFortyAlpha2Url() {
        return "http://ewp-dev40.eworkplace.com/Alpha2/DeviceServices/ed/";
    }

    private String getQAUrl(){
        return "http://ewp-dev55.eworkplace.com/services/ed/";
    }

    private String getFortyOneUrl() {
        return "http://ewp-dev41.eworkplace.com/services/ed/";
    }

    private String getAlph2PMUrl() {
        return "http://ewp-dev39.eworkplace.com/Alpha2/Services/ed/";
    }

    /**
     * addEmployee
     *
     * @param addEmployeeList
     * @param requestCallback
     * @throws EwpException
     */
    public void addEmployee(List<AddEmployeeAction> addEmployeeList, final RequestCallback requestCallback) throws EwpException, UnsupportedEncodingException {
        if (addEmployeeList == null || addEmployeeList.isEmpty()) {
            List<String> messages = new ArrayList<String>();
            messages.add("No employee exist");
            throw new EwpException(new EwpException("No employee exist"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, messages, EnumsForExceptions.ErrorModule.DATA_SERVICE, null, 0);
        }
        addEmployeesList = new ArrayList<AddEmployeeAction>();
        addEmployeesList.addAll(addEmployeeList);
        String jsonString = addEmployeesPostData(addEmployeesList).toString();
        //development
        //String url = "http://172.16.1.105:84/api/ED/Employee/AddEmployees";
        // PM
        String url = getFortyOneUrl() + "employees";

        new NetworkAsyncTask(url, "POST", "AddEmployees", jsonString).execute(requestCallback);

    }

    private JSONArray addEmployeesPostData(List<AddEmployeeAction> employeeActions) {
        JSONArray jsonArray = new JSONArray();
        for (AddEmployeeAction action : employeeActions) {
            jsonArray.put(getJobj(action));
        }
        return jsonArray;
    }

    private JSONObject getJobj(AddEmployeeAction action) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("FirstName", action.getFirstName());
            jsonObject.put("LastName", action.getLastName());
            jsonObject.put("LoginEmail", action.getEmail());
            jsonObject.put("SendInvitation", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public EmployeeModel employeeModel = null;


    public void updateEmployee(EmployeeModel employeeModel, final RequestCallback requestCallback) {
        this.employeeModel = employeeModel;
//        JSONObject jsonObject = new JSONObject();
        try {
//            EmployeeDataService service = new EmployeeDataService();
//            try {
//                Employee emp = (Employee) service.getEntity(employeeModel.getEmployee().getEntityId());
//                Employee emp2 = employeeModel.getEmployee();
//                if (emp == null) {
//                    String url = getFortyAlpha2Url() + "employees/user";
//                    new NetworkAsyncTask(url, "NONE", "UpdateEmployeeUser", null).execute(requestCallback);
//
////                    requestCallback.onSuccess("UpdateEmployeeUser", null);
//                } else if (emp.getFirstName().equals(emp2.getFirstName()) && emp.getLastName().equals(emp2.getLastName()) && emp.getPicture().equals(emp2.getPicture())) {
//                    String url = getFortyAlpha2Url() + "employees/user";
//                    new NetworkAsyncTask(url, "NONE_WITH_EMPID", "UpdateEmployeeUser", employeeModel.getEmployee().getEntityId().toString()).execute(requestCallback);
////                    requestCallback.onSuccess("UpdateEmployeeUser", employeeModel.getEmployee().getEntityId());
//                } else {
//                    jsonObject.put("UserId", employeeModel.getEmployee().getTenantUserId().toString());
//                    jsonObject.put("FirstName", employeeModel.getEmployee().getFirstName());
//                    jsonObject.put("LastName", employeeModel.getEmployee().getLastName());
//                    jsonObject.put("Picture", employeeModel.getPicture());
//                    jsonObject.put("UpdateEmployee", false);
                    String jsonString =   this.employeeModel.toJsonObject().toString();
                    // http://172.16.1.105:84/api/ED/Employee/DeleteEmployee
                    //development
//        String url = "http://172.16.1.105:84/api/ED/Employee/UpdateEmployeeUser";
                    // PM
//                    String url = getFortyAlpha2Url() + "employees/user";
                    String url = getFortyOneUrl() + "employees";

                    new NetworkAsyncTask(url, "PUT", "UpdateEmployeeUser", jsonString).execute(requestCallback);
//                }

//            } catch (EwpException e) {
//                e.printStackTrace();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeToLocal(UUID entityId, final RequestCallback requestCallback) {
        String url = getFortyOneUrl() + "employees/user";
        new NetworkAsyncTask(url, "NONE_WITH_EMPID", "UpdateEmployeeUser", entityId.toString()).execute(requestCallback);
    }

    public void deleteEmployee(UUID employeeId, final RequestCallback requestCallback) throws EwpException {
        EmployeeDataService empService = new EmployeeDataService();
        boolean referenceExist = empService.isEmployeeReferenceExist(employeeId);
        if(referenceExist){
            Map<EnumsForExceptions.ErrorDataType, String[]> dataList = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
            dataList.put(EnumsForExceptions.ErrorDataType.INVALID_EMAIL, new String[]{});
            List<String> message = new ArrayList<>();
            message.add("");
            throw new EwpException(new EwpException(""), EnumsForExceptions.ErrorType.VALIDATION_ERROR,message,
                    EnumsForExceptions.ErrorModule.DATA, dataList,0);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EntityType", EDEntityType.EMPLOYEE.getId());
            jsonObject.put("EntityId", employeeId.toString());
            jsonObject.put("ApplicationId", EwpSession.EMPLOYEE_APPLICATION_ID);
            jsonObject.put("Version", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = jsonObject.toString();
        //development
//        String url = "http://172.16.1.105:84/api/ED/Employee/DeleteEmployee";
        // PM
        String url = getFortyOneUrl() + "employees/delete";
        new NetworkAsyncTask(url, "PUT", "DeleteEmployee", jsonString).execute(requestCallback);
    }

    class NetworkAsyncTask extends AsyncTask<RequestCallback, Void, Void> {

        private String url = "";
        private String httpMethodType = "";
        private String methodName = "";
        private String jsonString = "";

        public NetworkAsyncTask(String url, String httpMethodType, String methodName, String jsonString) {
            this.url = url;
            this.httpMethodType = httpMethodType;
            this.methodName = methodName;
            this.jsonString = jsonString;
        }

        @Override
        protected Void doInBackground(RequestCallback... params) {
            switch (httpMethodType) {
                case "POST":
                    return callHttpPostMethodBackground(params);
                case "GET":
                    break;
                case "PUT":
                    callHttpPutMethodBackground(params);
                    break;
                case "NONE":
                    callHttpNoneMethodBackground(params);
                    break;
                case "NONE_WITH_EMPID":
                    callHttpNoneWithIdMethodBackground(params);
                    break;
            }
            return null;
        }

        private Void callHttpPostMethodBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPost httpPost = null;
            HttpClient httpClient = null;


            try {
                //"http://ewp-dev39.eworkplace.com:84/api/ED/Employee/AddEmployees"
                url = new URL(this.url);
                // url = new URL("http://172.16.1.105:84/api/ED/Employee/AddEmployees");
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(url.toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                httpPost.setEntity(new StringEntity(jsonString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Set up the header types needed to properly transfer JSON
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
            // Execute POST
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    JSONArray jsonArray = new JSONArray(out);
                    if (jsonArray != null) {
                        params[0].onSuccess(methodName, out);
                    }
                } else {
                    String error = "";
                    if (response.getStatusLine().getStatusCode() == 500) {
                        EwpException ex = EwpException.handleError(response, null);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                    } else {
                        EwpException ex = new EwpException();
                        ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                        error = response.getStatusLine().getReasonPhrase();
                    }
                    params[0].onFailure(methodName, error);
                }
            } catch (Exception e) {
            }
            return null;
        }

        private Void callHttpPutMethodBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPut httpPut = null;
            HttpClient httpClient = null;


            try {
                //"http://ewp-dev39.eworkplace.com:84/api/ED/Employee/AddEmployees"
                url = new URL(this.url);
                // url = new URL("http://172.16.1.105:84/api/ED/Employee/AddEmployees");
                httpClient = new DefaultHttpClient();
                httpPut = new HttpPut(url.toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                httpPut.setEntity(new StringEntity(jsonString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Set up the header types needed to properly transfer JSON
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
            // Execute POST
            try {
                response = httpClient.execute(httpPut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    params[0].onSuccess(methodName, "Employee updated successfully.");
                } else if (response.getStatusLine().getStatusCode() == 500) {
                    String error = "";
                    EwpException ex = EwpException.handleError(response, null);
                    params[0].onFailure(methodName, ex);
                } else {
                    EwpException ex = new EwpException();
                    ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                    String error = response.getStatusLine().getReasonPhrase();
                 /*   String out = Utils.convertResponseToString(response);
                    JSONObject jsonObj = new JSONObject(out);
                    JSONArray jsonArray = jsonObj.getJSONArray("MessageList");
                    String message = (String) jsonArray.get(0);*/
                    params[0].onFailure(methodName, error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private Void callHttpNoneMethodBackground(RequestCallback... params) {
            try {
                params[0].onSuccess("UpdateEmployeeUser", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private Void callHttpNoneWithIdMethodBackground(RequestCallback... params) {
            try {
                params[0].onSuccess("UpdateEmployeeUser", jsonString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
