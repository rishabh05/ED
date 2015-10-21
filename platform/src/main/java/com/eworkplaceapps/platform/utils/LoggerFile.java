
//==============================================================================
// copyright 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 09/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

import android.content.Context;
import android.os.Environment;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggerFile {
    // Current state of output file
    // Full file path
    public String filePath;
    // Is file open?
    public boolean fileOpen = false;

    // Rolling file properties (Initialized in init()):
    // Use it or not
    public boolean useRollingFiles;
    // Maximum number of concurrent rolling files
    public static int numberOfRollingFiles;
    // Max size of a rolling file in bytes
    public static int maxRollingFileSize;
    public static Context mContext;
    private static Logger log;
    // The open file handle
    private static FileHandler fileHandle;
    private static Logger logger;

    public LoggerFile(Context context) {
        // Rolling file properties
        mContext = context;
        numberOfRollingFiles = 5;
        maxRollingFileSize = 1024*1024 * 1;// 1 MB
    }

    /**
     * generates the log file name for the current date/time.
     *
     * @return
     */
    private static String generateLogFileName() {
        Date formatter = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String suffix = dateFormat.format(formatter);
        String name = "app-" + suffix + ".log";
        return name;
    }

    /**
     * Gets the actual directory path for ApplicationSupportDirectory.
     *
     * @return
     */
    public static String getLogDirectoryPath() {
        String s = mContext.getExternalFilesDir(null).getAbsolutePath();
        return s;
    }

    /**
     * Builds the full log file path from its file name and log directory.
     *
     * @param fileName
     * @return
     */
    public static String getLogFilePath(String fileName) {
        String dirPath = getLogDirectoryPath();
        String filePath = dirPath + File.separator + fileName;
        return filePath;
    }

    /**
     * Method is used to get the sorted file list by LastModified date.
     *
     * @return
     */
    public String[] getLogFileNameList() {
        String folderPath = getLogDirectoryPath();
        File parentDir = new File(folderPath);
        File[] files = parentDir.listFiles();
        String fileNameList[] = {};
        if (files == null) {
            return fileNameList;
        }
        ArrayList<HashMap<String, Object>> filesAndProperties = new ArrayList<HashMap<String, Object>>();

        // Getting the log file name list.
        for (File file : files) {
            if (file.getName().endsWith(".log")) {
                HashMap<String, Object> mFile = new HashMap<String, Object>();
                mFile.put("path", file.getName());
                mFile.put("lastModDate", file.lastModified());
                filesAndProperties.add(mFile);
            }
        }

        Collections.sort(filesAndProperties, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> lhs,
                               HashMap<String, Object> rhs) {
                return String.valueOf(lhs.get("lastModDate")).compareTo(String.valueOf(rhs.get("lastModDate")));
            }
        });
        for (int i = 0; i < filesAndProperties.size(); i++) {
            // Generating the file name.
            String file = filesAndProperties.get(i).get("path").toString();
            fileNameList[i] = file;
        }
        return fileNameList;
    }

    /**
     * This method is used to delete file from the given path.
     *
     * @param fileName
     */

    public void deleteFile(String fileName) {
        String filePath = getLogDirectoryPath() + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Seacrhes the log directory for the last created log file.
     * If none, returns nil.
     *
     * @return
     */
    private static String searchCurrentLogFile() {
        String lastMatchedPath;
        String folderPath = getLogDirectoryPath();
        File parentDir = new File(folderPath);
        // Get the full file list in the log directory.
        File[] fileList = parentDir.listFiles();
        // If the path does not fully exist:
        if (fileList == null) {
            return null;
        }
        ArrayList<HashMap<String, Object>> filesAndProperties = new ArrayList<HashMap<String, Object>>();

        // Getting the log file name list.
        for (File file : fileList) {
            if (file.getName().endsWith(".log")) {
                HashMap<String, Object> mFile = new HashMap<String, Object>();
                mFile.put("path", file.getAbsolutePath());
                mFile.put("lastModDate", file.lastModified());
                filesAndProperties.add(mFile);
            }
        }
        // Sort the file list in ascending order by creationDate
        if (filesAndProperties.size() > 0) {
            Collections.sort(filesAndProperties, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> lhs,
                                   HashMap<String, Object> rhs) {
                    return String.valueOf(lhs.get("lastModDate")).compareTo(String.valueOf(rhs.get("lastModDate")));
                }
            });
            lastMatchedPath = filesAndProperties.get(filesAndProperties.size() - 1).get("path").toString();
            return lastMatchedPath;
        } else {
            return null;
        }
    }

    /**
     * Delete all log files older than numberOfRollingFiles.
     */
    private static void deleteOldRollingFiles() {

        String folderPath = getLogDirectoryPath();
        File parentDir = new File(folderPath);
        File[] fileList = parentDir.listFiles();
        if (fileList == null) {
            return;
        }
        // Filter log files
        List<File> logFileList = new ArrayList<File>();
//        File[] logFileList = new File[fileList.length];
//        int i = 0;
        // Getting the log file name list.
        for (File file : fileList) {
            if (file.getName().endsWith(".log")) {
                logFileList.add(file);
//                logFileList[i] = file;
//                i++;
            }
        }
        // If log file count < numberOfRollingFiles, exit
        if (logFileList.size() <= numberOfRollingFiles) {
            return;
        }
        //convert filelist into file array for sorting
        File[] logFileArray = new File[logFileList.size()];
        logFileArray = logFileList.toArray(logFileArray);
        // Sort the file list in descending order by creationDate
        Arrays.sort(logFileArray, new Comparator() {
            public int compare(Object o1, Object o2) {

                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }

        });
        // Now delete all log files older than the current numberOfRollingFiles
        for (int j = 5; j>= 1; j--) {
            String path = logFileArray[j].getAbsolutePath();
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }

    }

    /**
     * Creates the log directory if it does not exist.
     *
     * @param dir
     */
    private static void createLogDirectory(String dir) {
        File parentDir = new File(dir);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }
    }

    /**
     * Creates an empty log file if it does not exist.
     *
     * @param filePath
     */
    private static void createLogFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new log file. Used when none exists in the log directory.
     *
     * @return
     */
    private static String createNewLogFile() {
        // First create the log directory if it does not exist.
        String dir = getLogDirectoryPath();
        createLogDirectory(dir);

        // Generate the log file name and create it.
        String fileName = generateLogFileName();
        String filePath = getLogFilePath(fileName);
        createLogFile(filePath);

        return filePath;
    }

    /**
     * Opens the current log file. Creates a new log file if none exists.
     *
     * @return
     */
    public static String openCurrentLogFile() {
        String currentFilePath;
        // Search for the latest log file.
        currentFilePath = searchCurrentLogFile();
        // If none found, create a new log file.
        if (currentFilePath == null) {
            currentFilePath = createNewLogFile();
        }
        return currentFilePath;
    }

    /**
     * Appends the give text to the log file.
     * If the file is not open, then it is a no-op.
     *
     * @param s
     */
    public static void appendString(String s) {
        String filePath = openCurrentLogFile();
        File newFile = new File(filePath);
        if (!newFile.exists() || newFile.length() >= maxRollingFileSize) {
            createNewLogFile();
            filePath = openCurrentLogFile();
            newFile = new File(filePath);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
            writer.append(s);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Delete rolling files older than the last numberOfRollingFiles.
        deleteOldRollingFiles();
    }


}
