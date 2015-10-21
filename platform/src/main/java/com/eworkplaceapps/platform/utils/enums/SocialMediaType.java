//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 5/19/2015.
 */
public enum SocialMediaType {
    FACEBOOK(1),
    LINKED_IN(2),
    SKYPE(3),
    TWITTER(4),
    WHATS_APP(5);
    private int id;

    SocialMediaType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Method will give the SocialMediatype list as display string.
     *
     * @return Map<Integer, String>
     */
    public static Map<Integer, String> getSocialMediaTypeListAsDictionary() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(SocialMediaType.FACEBOOK.getId(), "Facebook");
        map.put(SocialMediaType.SKYPE.getId(), "Skype");
        map.put(SocialMediaType.TWITTER.getId(), "Twitter");
        map.put(SocialMediaType.WHATS_APP.getId(), "WhatsApp");
        map.put(SocialMediaType.LINKED_IN.getId(), "LinkedIn");
        return map;
    }

    /**
     * @return Map<String, Integer>
     */
    public static Map<String, Integer> getSocialMediaTypeDictionary() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Facebook", SocialMediaType.FACEBOOK.getId());
        map.put("Skype", SocialMediaType.SKYPE.getId());
        map.put("Twitter", SocialMediaType.TWITTER.getId());
        map.put("WhatsApp", SocialMediaType.WHATS_APP.getId());
        map.put("LinkedIn", SocialMediaType.LINKED_IN.getId());
        return map;
    }
}
