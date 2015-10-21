//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Specifies the different possible values of TASK_STATUS.
 */
public enum TaskStatusItem {

    // No staus set for task
    NONE(0),
    // Specifies that Task  status as OPEN.
    NEW(1),
    // Specifies that Task  status as OPEN.
    OPEN(2),
    // Specifies that Task  status as Close.
    ON_HOLD(3),
    COMPLETED(4),
    DIFFERED(5),
    // Specifies that Task  status as Reopen.
    IN_PROCESS(6);

    private int id;

    TaskStatusItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // GET the task staus as display string.
    public String toDisplayString() {
        switch (this) {
            case NEW:
                return "NEW";
            case OPEN:
                return "OPEN";
            case COMPLETED:
                return "COMPLETED";
            case IN_PROCESS:
                return "In Process";
            case ON_HOLD:
                return "On Hold";
            default:
                return "NONE";
        }
    }

    // Method will give task status list as display string.
    public static String[] getTaskStatusItemList() {
        return new String[]{"NEW", "OPEN", "COMPLETED", "In Process", "On Hold"};
    }

    // Method will give the task status list as key/display string.
    public static Map<TaskStatusItem, String> getTaskStatusListAsDictionary(TaskStatusItem currentTaskStatus) {
        Map<TaskStatusItem, String> map = new HashMap<TaskStatusItem, String>();
        switch (currentTaskStatus) {
            case OPEN:
                map.put(TaskStatusItem.NEW, "NEW");
                map.put(TaskStatusItem.OPEN, "OPEN");
                map.put(TaskStatusItem.COMPLETED, "COMPLETED");
                return map;
            case NEW:
                map.put(TaskStatusItem.NEW, "NEW");
                map.put(TaskStatusItem.OPEN, "OPEN");
                map.put(TaskStatusItem.COMPLETED, "COMPLETED");
                return map;
            case COMPLETED:
                map.put(TaskStatusItem.COMPLETED, "COMPLETED");
                map.put(TaskStatusItem.IN_PROCESS, "In Process");
                return map;
            default:
                map.put(TaskStatusItem.NEW, "NEW");
                map.put(TaskStatusItem.OPEN, "OPEN");
                return map;
        }
    }
}
