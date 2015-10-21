//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum EmailType {

    PERSONAL(1),
    WORK(2),
    OTHER(3);

    private int id;

    EmailType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     *
     * @return  Map<Integer, String>
     */
    public static Map<Integer, String> getEmailTypeListAsDictionary() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(EmailType.PERSONAL.getId(), "Personal");
        map.put(EmailType.WORK.getId(), "Work");
        map.put(EmailType.OTHER.getId(), "Other");
        return map;
    }
    /**
     *
     * @return Map<String, Integer>
     */
    public static Map<String, Integer> getEmailTypeDictionary() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("personal", EmailType.PERSONAL.getId());
        map.put("work", EmailType.WORK.getId());
        map.put("other", EmailType.OTHER.getId());
        return map;
    }
}
