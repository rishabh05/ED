//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.DeviceInfo;
import com.eworkplaceapps.platform.helper.DeviceInfoData;
import com.eworkplaceapps.platform.helper.EwpSession;

import org.apache.log4j.Logger;

/**
 * Splash Screen for Application
 */
public class SplashScreenActivity extends Activity {
    private final Logger log = Logger.getLogger(SplashScreenActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Utils.setStatusBarColor(this, findViewById(R.id.statusBarBackground), getResources().getColor(R.color.colorPrimaryDark), false);
        } else {
            findViewById(R.id.statusBarBackground).setVisibility(View.GONE);
        }
        new AsyncDB().execute();
    }

    /**
     * async task for copying database from assets to app installation directory
     */
    private class AsyncDB extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DatabaseOps.defaultDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            splashJump();
        }
    }

    private void splashJump() {
        final int splashTime = 2 * 1000;
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {

                        /*
                            TODO
                            changed for Employee list testing. this code is temporary, will be revert.
                         */
//                        EwpSession.getSharedInstance().setSession(UUID.fromString("1B948F4A-67D7-4A50-A458-0CA16DAB4FAD"),
//                                UUID.fromString("F7269470-B8C9-447C-8E85-555D9ECF82B6"), "", "");
//                        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
//                        startActivity(intent);

                        DeviceInfoData deviceInfoData = new DeviceInfoData();
                        DeviceInfo deviceInfo = deviceInfoData.getUserInfoAsEntity();
                        if (deviceInfo == null || !deviceInfo.isInitialized() || !EwpSession.getSharedInstance().isUserLoggedIn()) {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (EwpException e) {
                        log.info("SplashScreenActivity-> EwpException " + e);
                        Log.d("SplashScreenActivity", "SplashScreenActivity-> EwpException " + e);
                    }
                }
            }, splashTime);
        } catch (Exception e) {
            log.info("SplashScreenActivity-> Exception " + e);
            Log.d("SplashScreenActivity", "SplashScreenActivity-> Exception " + e);
        }
    }
}
