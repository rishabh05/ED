//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.logging;

import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * logging configuration
 */
public class LogConfigurer {

    private static String LOG_FILE_PATH;
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 1;
    private static Logger log;

    public static void configure() throws IOException {
        final LogConfigurator logConfigurator = new LogConfigurator();

        LOG_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "Connect(R)";

        try {
            File file = new File(LOG_FILE_PATH);
            if (!file.exists()) {
                boolean val = file.mkdir();
                LOG_FILE_PATH += File.separator + "Connect_Log.txt";
                File f = new File(LOG_FILE_PATH);
                if (!f.exists()) {
                    f.createNewFile();
                }
                if (f.exists()) {
                    logConfigurator.setFileName(LOG_FILE_PATH);
                    logConfigurator.setRootLevel(Level.DEBUG);
                    logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
                    // Set log level of a specific logger
                    logConfigurator.setLevel("org.apache", Level.ERROR);
                    logConfigurator.setLevel("com.eworkplaceapps", Level.DEBUG);
                    logConfigurator.configure();
                }
            } else {
                String path = Environment.getExternalStorageDirectory() + File.separator + "Connect(R)" + File.separator + "Connect_Log.txt";
                File f = new File(path);
                if (f.exists()) {
                    logConfigurator.setFileName(path);
                    logConfigurator.setRootLevel(Level.DEBUG);
                    logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
                    // Set log level of a specific logger
                    logConfigurator.setLevel("org.apache", Level.ERROR);
                    logConfigurator.setLevel("com.eworkplaceapps", Level.DEBUG);
                    logConfigurator.configure();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e("LogConfigurer", e.getMessage());
        }
    }

    public static void initialize() {
        if (log == null) {
            log = Logger.getLogger("Connect(R)");
        }
    }

    public static void error(String tag, String Message) {
        if (log == null) {
            log = Logger.getLogger("Connect(R)");
        }
        StringBuilder sbLog = new StringBuilder();
        sbLog.append(tag);
        sbLog.append("\t");
        sbLog.append(Message);
        sbLog.append("\n");
        sbLog.append("************************************************************************************");
        sbLog.append("\n");
        log.error(sbLog.toString());
    }

    public static void info(String tag, String Message) {
        if (log == null) {
            log = Logger.getLogger("Connect(R)");
        }
        StringBuilder sbLog = new StringBuilder();
        sbLog.append(tag);
        sbLog.append("\t");
        sbLog.append(Message);
        sbLog.append("\n");
        sbLog.append("************************************************************************************");
        sbLog.append("\n");
        log.info(sbLog.toString());
    }
}
