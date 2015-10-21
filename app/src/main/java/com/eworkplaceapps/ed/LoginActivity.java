package com.eworkplaceapps.ed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eworkplaceapps.ed.preferences.EDPreferenceHelper;
import com.eworkplaceapps.ed.utils.Constants;
import com.eworkplaceapps.ed.utils.GPSTracker;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.platform.authentication.AuthenticationReply;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;
import com.eworkplaceapps.platform.helper.DeviceInfo;
import com.eworkplaceapps.platform.helper.DeviceInfoData;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.tenant.TenantRegister;
import com.eworkplaceapps.platform.tenant.TenantRegistrationService;

import org.apache.log4j.Logger;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.eworkplaceapps.ed.utils.Utils.EMPLOYEE_LOGIN_ID;
import static com.eworkplaceapps.ed.utils.Utils.hideProgress;

/**
 * Activity class to perform login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RequestCallback {
    private final Logger log = Logger.getLogger(LoginActivity.class);
    private EditText edtLogin;
    private EditText edtPassword;
    private Button btnLogin;
    private String userEmail = "", password = "";
    private TenantRegistrationService tenantRegistrationService;
    private DatabaseOps databaseOps;
    private ProgressDialog horizontalProgress;
    private ProgressDialog circleProgress;
    private GPSTracker gpsTracker;
    private String locale = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Utils.setStatusBarColor(this, findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorPrimaryDark), true);
        } else {
            findViewById(R.id.statusBarBackground).setVisibility(View.GONE);
        }
        progress();
        circleProgress = new ProgressDialog(this);
        circleProgress.setCancelable(false);
        circleProgress.setIndeterminate(true);
        edtLogin = (EditText) findViewById(R.id.login_id);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        TextView txtPassword = (TextView) findViewById(R.id.txtPassword);
        btnLogin.setText(getResources().getString(R.string.title_activity_login));
        btnLogin.setOnClickListener(this);
        TextView tvrelease = (TextView) findViewById(R.id.tvrelease);
        tvrelease.setTypeface(EdApplication.HELVETICA_NEUE);
        edtLogin.setTypeface(EdApplication.HELVETICA_NEUE);
        edtPassword.setTypeface(EdApplication.HELVETICA_NEUE);
        btnLogin.setTypeface(EdApplication.HELVETICA_NEUE);
        txtPassword.setTypeface(EdApplication.HELVETICA_NEUE);
        txtEmail.setTypeface(EdApplication.HELVETICA_NEUE);
        edtPassword.setHint(Utils.actionBarTitle(getResources().getString(R.string.login_password_hint)));
        edtLogin.setHint(Utils.actionBarTitle(getResources().getString(R.string.login_email_hint)));
        getSupportActionBar().setTitle(Utils.actionBarTitle(getResources().getString(R.string.title_activity_login)));
        tenantRegistrationService = new TenantRegistrationService();
        initialization();
        sync();
    }


    /**
     * Method to listen to click listener's
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                performLogin();
        }
    }

    /**
     * Perform Login action
     */
    private void performLogin() {
        userEmail = edtLogin.getText().toString();
        password = edtPassword.getText().toString();
        if (validate(userEmail.trim(), password.trim())) {
            Utils.showProgress(circleProgress, getString(R.string.authentication_message));
            gpsTracker = new GPSTracker(this);
            try{
                TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
                locale = tm.getNetworkCountryIso();
            }catch(Exception e){
                locale ="";
                e.printStackTrace();
            }
            makeAuthenticateUserCall();
        }
    }

    /**
     * method to validate input field
     *
     * @param loginId
     * @param password
     * @return
     */
    private boolean validate(String loginId, String password) {
        if (!TextUtils.isEmpty(loginId)){
            if (Utils.isValidEmail(loginId)) {
                if (!TextUtils.isEmpty(password)){
                    return true;
                } else {
                    //Alert for invalid email
                    Utils.alertDialog(this, "", Utils.actionBarTitle(getString(R.string.password_auth_message)).toString());
                    return false;
                }
            } else {
                //Alert for Password blank
                Utils.alertDialog(this, "", Utils.actionBarTitle(getString(R.string.invalid_email_message)).toString());
                return false;
            }
        } else {
            //Alert for Email blank
            Utils.alertDialog(this, "", Utils.actionBarTitle(getString(R.string.required_email_message)).toString());
            return false;
        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        if (Commons.AUTH_USER_A.equals(name)) {
            circleProgress.setMessage(Utils.actionBarTitle(getString(R.string.register_device_message)));
            log.info("************************Login Ended Success***************************");
            Log.d(this.getClass().getName(), "AuthenticateUser (onSuccess)" + object);
            authenticateSuccess(object);
        } else if (Commons.LOGIN_USER_TOKEN_A.equals(name)) {
            Log.d(this.getClass().getName(), "GetLoginUserToken (onSuccess)" + object);
        } else if (Constants.REGISTER_DEVICE_TAG.equals(name)) {
            hideProgress(circleProgress);
            horizontalProgress.setMessage(Utils.actionBarTitle(getString(R.string.db_initialize_message)));
            horizontalProgress.show();
            log.info("************************Register Device Ended Success***************************");
            Log.d(this.getClass().getName(), "RegisterDevice (onSuccess)" + object);
            //start horizontal progress from here
            registerDeviceForTenant(object);
        } else if (Constants.INIT_INTENT_APP_DATA_TAG.equals(name)) {
            horizontalProgress.cancel();
            EDPreferenceHelper.getPreferences().setStringValue(EMPLOYEE_LOGIN_ID, userEmail);
            log.info("************************Database initialization ended Success***************************");
            Log.d(this.getClass().getName(), "InitTenantAppData (onSuccess)" + object);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        } else {
            log.info("Unknown (onSuccess)" + object);
            Log.d(this.getClass().getName(), "Unknown (onSuccess)" + object);
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.hideProgress(circleProgress);
        if (Commons.AUTH_USER_A.equals(name)) {
            log.info("************************Login Ended With Failure " + object + "***************************");
            Log.d(this.getClass().getName(), "AuthenticateUser (onFailure)" + object);
            if (object instanceof String)
                Utils.alertDialog(this, "", Utils.actionBarTitle(object.toString()).toString());
        } else if (Commons.LOGIN_USER_TOKEN_A.equals(name)) {
            log.info("GetLoginUserToken (onFailure)" + name);
            Log.d(this.getClass().getName(), "GetLoginUserToken (onFailure)" + object);
        } else if ("RegisterDevice".equals(name)) {
            Log.d(this.getClass().getName(), "RegisterDevice (onFailure)" + object);
            log.info("************************Register Device Ended Failure " + object + "***************************");
            if (object instanceof String)
                Utils.alertDialog(this, "", Utils.actionBarTitle(object.toString()).toString());
        } else if ("InitTenantAppData".equals(name)) {
            Utils.hideProgress(horizontalProgress);
            log.info("************************Database initialization ended failure " + object + "***************************");
            Log.d(this.getClass().getName(), "InitTenantAppData (onFailure)" + object);
            if (object instanceof String)
                Utils.alertDialog(this, "", object.toString());
        } else {
            log.info("Unknown (onFailure)" + name);
            Log.d(this.getClass().getName(), "Unknown (onFailure)" + object);
        }
    }

    @Override
    public void updateProgress(String name, String msg, int progressCount) {
        if ("InitTenantAppData".equals(name)) {
            setProgress(msg, progressCount);
        }
    }

    /**
     * method to
     */
    private void initialization() {
        ContextHelper.setContext(LoginActivity.this);
        try {
            databaseOps = DatabaseOps.defaultDatabase();
            if (databaseOps.checkDataBase()) {
                AppConfigData appConfigData = new AppConfigData();
                AppConfig appConfig = appConfigData.getAppConfig();
                if (appConfig.getClientDeviceId() == null || "".equals(appConfig.getClientDeviceId())) {
                    appConfigData.generateMyDeviceId();
                }
            }
        } catch (EwpException e) {
            log.info("LoginActivity-> EwpException " + e);
            Log.d(this.getClass().getName(), "LoginActivity (EwpException)" + e);
        }
    }

    /**
     * method to start syncing from where left the syncing last time
     */
    private void sync() {
        try {
            DeviceInfoData deviceInfoData = new DeviceInfoData();
            DeviceInfo deviceInfo = deviceInfoData.getUserInfoAsEntity();
            if (deviceInfo != null && !deviceInfo.isInitialized()) {
                //setProgress("Initializing Tenant app data...", 20);
                //updateProgress();
                TenantRegister tenantRegister = new TenantRegister();
                tenantRegister.setLoginToken(EwpSession.getSharedInstance().getLoginToken());
                tenantRegister.setTenantId(EwpSession.getSharedInstance().getTenantId());
                tenantRegister.setUserId(EwpSession.getSharedInstance().getUserId());
                tenantRegistrationService.setSelectedTenant(tenantRegister);
                tenantRegistrationService.initializeDataForTenant(deviceInfo.getLastSyncCount(), 100000, this, true);
            }
        } catch (EwpException e) {
            log.info("LoginActivity-> EwpException sync()" + e);
            Log.d(this.getClass().getName(), "EwpException sync() " + e);
        }
    }


    /**
     * make service call for authenticating user
     */
    private void makeAuthenticateUserCall() {
        try {
            log.info("************************Login Started***************************");
            tenantRegistrationService.authenticateUser(userEmail, password, gpsTracker.getLatitude(), gpsTracker.getLongitude(),locale, this);
        } catch (EwpException e) {
            log.info("LoginActivity-> EwpException (makeAuthenticateUser)" + e);
            Log.d(this.getClass().getName(), "EwpException (makeAuthenticateUser)" + e);
        } catch (JSONException e) {
            log.info("LoginActivity-> JSONException (makeAuthenticateUser)" + e);
            Log.d(this.getClass().getName(), "JSONException (makeAuthenticateUser)" + e);
        } catch (UnsupportedEncodingException e) {
            log.info("LoginActivity-> UnsupportedEncodingException(makeAuthenticateUser) " + e);
            Log.d(this.getClass().getName(), "UnsupportedEncodingException (makeAuthenticateUser)" + e);
        }
    }

    /**
     * authenticateSuccess
     *
     * @param object
     */
    private void authenticateSuccess(Object object) {
        if (object instanceof AuthenticationReply) {
            AuthenticationReply authenticationReply = (AuthenticationReply) object;
            try {
                log.info("************************Register Device Started***************************");
                if (authenticationReply.getTenantList().size() == 1) {
                    tenantRegistrationService.registerDevice(userEmail, authenticationReply.getTenantList().get(0), this);
                } else {
                    tenantRegistrationService.getUserToken(userEmail, authenticationReply.getTenantList().get(0), this);
                }
            } catch (EwpException e) {
                log.info("LoginActivity-> EwpException (authenticateSuccess)" + e);
                Log.d(this.getClass().getName(), "EwpException (authenticateSuccess)" + e);
            }
        }
    }

    /**
     * registerDeviceForTenant
     *
     * @param object
     */
    private void registerDeviceForTenant(Object object) {
        if (object instanceof List) {
            try {
                log.info("************************Database initialization started***************************");
                tenantRegistrationService.initializeDataForTenant(this);
            } catch (EwpException e) {
                log.info("LoginActivity-> EwpException  (registerDeviceForTenant) " + e);
                Log.d(this.getClass().getName(), "EwpException (registerDeviceForTenant)" + e);
            }
        }
    }

    /**
     * method to initialize progress dialog
     */
    private void progress() {
        horizontalProgress = new ProgressDialog(this);
        horizontalProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        horizontalProgress.setIndeterminate(false);
        horizontalProgress.setCancelable(false);
        horizontalProgress.setMax(100);
    }

    /**
     * method to set progress with message
     *
     * @param message
     * @param progress
     */
    private void setProgress(String message, int progress) {
        Spanned msg = Utils.actionBarTitle(message);
        horizontalProgress.setMessage(msg);
        horizontalProgress.setProgress(progress);
    }

/*
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            horizontalProgress.setProgress(counter);
        }
    };
*/

   /* private void updateProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (counter <= 90) {
                        counter++;
                        Thread.sleep(1000);
                        handle.sendMessage(handle.obtainMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideProgress(horizontalProgress);
    }
}