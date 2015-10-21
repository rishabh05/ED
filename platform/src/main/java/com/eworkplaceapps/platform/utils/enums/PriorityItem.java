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
public enum PriorityItem {

    // No staus set for task
    NONE(0),
    // Specifies that  Priorty as LOW.
    LOW(1),
    // Specifies that Priorty as HIGH.
    HIGH(2),
    // Specifies that Priorty as NORMAL.
    NORMAL(3);

    private int id;

    PriorityItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // GET the Priorty as display string.
    public String ToDisplayString() {
        switch (this) {
            case LOW:
                return "LOW";
            case HIGH:
                return "HIGH";
            case NORMAL:
                return "NORMAL";
            default:
                return "NONE";
        }
    }

    // Method will give the priority list as display string.
    static String[] getPriorityItemList() {
        return new String[]{
                "LOW", "HIGH", "NORMAL"
        };
    }

    // Method will give the priority list as display string.
    public static Map<PriorityItem, String> getPriorityItemListAsDictionary() {
        Map<PriorityItem, String> map = new HashMap<PriorityItem, String>();
        map.put(PriorityItem.LOW, "LOW");
        map.put(PriorityItem.HIGH, "HIGH");
        map.put(PriorityItem.NORMAL, "NORMAL");
        return map;
    }
}
