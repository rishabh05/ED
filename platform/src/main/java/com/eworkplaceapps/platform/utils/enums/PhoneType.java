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
public enum PhoneType {
    /// Its subtype of (Email, Phone, Social)
    /// Email
    HOME(1),
    MOBILE(2),
    WORK(3),
    OTHER(4);
    private int id;

    PhoneType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Method will give the EmailType list as display string.
     *
     * @return Map<Integer, String>
     */
    public static Map<Integer, String> getPhoneTypeListAsDictionary() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(PhoneType.HOME.getId(), "home");
        map.put(PhoneType.MOBILE.getId(), "mobile");
        map.put(PhoneType.WORK.getId(), "work");
        map.put(PhoneType.OTHER.getId(), "other");
        return map;
    }

    /**
     *
     * @return Map<String, Integer>
     */
    public static Map<String, Integer> getPhoneTypeDictionary() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("home", PhoneType.HOME.getId());
        map.put("mobile", PhoneType.MOBILE.getId());
        map.put("work", PhoneType.WORK.getId());
        map.put("other", PhoneType.OTHER.getId());
        return map;
    }
}
