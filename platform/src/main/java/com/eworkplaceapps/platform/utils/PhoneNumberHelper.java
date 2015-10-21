//===============================================================================
// copyright 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 09/15/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

import com.eworkplaceapps.platform.exception.EwpException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberHelper {
    public String canonicalNumber;
    private String defaultRegionCode = "";
    private String defaultCountyCode = "";
    private String countryCode;
    private String nationalPhoneNumber;
    private int numberOfPauses = 0;
    public String extensionNumber;
    public String phoneNumber;

    public PhoneNumberHelper(String defaultRegionCode) {
        setRegionAndCountry(defaultRegionCode);
    }

    public PhoneNumberHelper(String phoneNumber, String defaultRegionCode) {
        setRegionAndCountry(defaultRegionCode);
        // Convert to to canonical form
        parse(phoneNumber);
    }

    /**
     * Gets the phone number in following format '+19999999999,,,252'.
     *
     * @return
     */
    public String getPhoneNumber() {
        String phoneNumber = "";
        // Check that national phone number is not null or enmpty.
        if (nationalPhoneNumber != null && !nationalPhoneNumber.isEmpty()) {
            // Check that country code is not null or empty then add it in phone number.
            if (this.countryCode != null && !this.countryCode.isEmpty()) {
                phoneNumber = "+" + this.countryCode;
            }
            // Add national phone number in phone number property.
            phoneNumber = phoneNumber + nationalPhoneNumber;

            if (extensionNumber != null && !extensionNumber.isEmpty()) {
                // Add Pauses(',') in phone number.
                for (int i = 0; i < numberOfPauses; i++) {
                    phoneNumber += ",";
                }
                // Check that extension number is not null or empty theen add it in phone number.
                phoneNumber += extensionNumber;
            }

        }
        return phoneNumber;
    }
    public String getCanonicalNumber() {
        return canonicalNumber;
    }

    public String getExtensionNumber() {
        return extensionNumber;
    }

    private void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    public int getNumberOfPauses() {
        return numberOfPauses;
    }

    private void setNumberOfPauses(int numberOfPauses) {
        this.numberOfPauses = numberOfPauses;
    }

    public String getNationalPhoneNumber() {
        return nationalPhoneNumber;
    }

    private void setNationalPhoneNumber(String nationalPhoneNumber) {
        this.nationalPhoneNumber = nationalPhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    private void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Sets the region and country.
     *
     * @param defaultRegion
     */
    private void setRegionAndCountry(String defaultRegion) {
        // Initialize default region and country code.
        this.defaultRegionCode = defaultRegion;
        // Get country code from given region.
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        this.defaultCountyCode = String.valueOf(phoneNumberUtil.getCountryCodeForRegion(defaultRegion));
    }

    /// Evalutes the country code from provided region using Google API.
    /// Return country code for provided region code.
    public static String countryCodeFromRegion(String region) {
        // Get country code from given region.
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        int number = phoneNumberUtil.getCountryCodeForRegion(region);

        return String.valueOf(number);
    }

    /**
     * Method is used to convert raw number to cannonical form.
     * It will remove non numeric and special charecter from raw phone#.
     *
     * @param rawNumber
     * @return
     */
    public String normalize(String rawNumber) {
        // Remove charecters to get number in canonical form.
        String pattern = "[a-zA-Z]";

        // Get the number from first occurance of + sign.
        int range = rawNumber.indexOf("+");
        if (range != -1) {
            rawNumber = rawNumber.substring(range);
        }

        boolean plusAppear = false;
        boolean commaAppear = false;
        String number = "";
        int charLength = rawNumber.length() - 1;
        char[] chars = rawNumber.toCharArray();
        for (int i = 0; i <= charLength; i++) {
            if (chars[i] == '+') {
                if (!plusAppear)
                    number = String.valueOf(chars[i]);
                plusAppear = true;
                continue;
            }
            if (chars[i] == ',') {
                // Add comma, if comma appear in between of phone#. otherwise ignore.
                if (i == 0 || i == charLength) {
                    continue;
                }
                // Ignoer if user type the comma in contineous like 9898989898,,,,,,,,
                for (int j = i + 1; j <= charLength; j++) {
                    if (chars[j] != ',') {
                        break;
                    }
                    // if comma appear till the end then ignore comma to add.
//                    if (chars[j] == ',' && j == charLength) {
//                        commaAppear = true;
//                        continue;
//                    }
                }
                // Add comma, if comma appear in between of phone#. otherwise ignore.
//                if (commaAppear) {
//                    continue;
//                }
                // Add comma, if comma appear in between of phone#.
                number = number + chars[i];
                commaAppear = true;
                continue;
            }
            if (Character.isDigit(chars[i])) {
                number += chars[i];
            }
            //  Ignoer all special charecters.
//            if (chars[i] == '0' || chars[i] == '1' || chars[i] == '2' || chars[i] == '3' || chars[i] == '4' || chars[i] == '5' || chars[i] == '6' || chars[i] == '7' || chars[i] == '8' || chars[i] == '9') {
//                number += chars[i];
//            }


        }

        if (!number.isEmpty()) {
            return number;
        }

        return rawNumber;
    }

    /**
     * Call Google API to format in the local style of the country code.
     * For view/edit modes?
     *
     * @return
     * @throws EwpException
     */
    public String formatPhoneNumber() throws EwpException {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parseAndKeepRawInput(canonicalNumber, defaultRegionCode);
        } catch (NumberParseException e) {
            e.printStackTrace();
            phoneNumber = null;

        }

        return  phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    /**
     * Call Google API to validate in the local style of the country code.
     * WHAT DOES IT DO? Input in what form?
     *
     * @return
     */
    public boolean validatePhoneNumber() {
        //    // Initialize phone util
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        // Parse raw phone number
        Phonenumber.PhoneNumber numberProto = null;
        try {
            numberProto = phoneUtil.parseAndKeepRawInput(canonicalNumber, defaultRegionCode);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        String regionCode = phoneUtil.getRegionCodeForNumber(numberProto);
        int CountyCode = phoneUtil.getCountryCodeForRegion(regionCode);
        String regioncodefromcountry = phoneUtil.getRegionCodeForCountryCode(CountyCode);
        return phoneUtil.isValidNumber(numberProto);
    }

    /**
     * Google API to get this. Apply default region code if missing.
     *
     * @param phoneNumber
     * @return
     */
    public String getRegionCodeFromNumber(Phonenumber.PhoneNumber phoneNumber) {
        // Initialize phone util
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        // return region code
        return phoneUtil.getRegionCodeForNumber(phoneNumber);
    }

    /**
     * Google API to get this. Apply default region code if missing.
     *
     * @param countyCode
     * @return
     */
    public String getRegionCodeFromCountryCode(int countyCode) {
        // Initialize phone util
        // return country code
        return PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(countyCode);
    }

    /**
     * Parses the specified phone number and map it to properties.
     *
     * @param phoneNumber
     */
    public void parse(String phoneNumber) {
        // Normalize given phone number.
        this.canonicalNumber = normalize(phoneNumber);
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        // Parse normalized phone number and assign into properties.
        Phonenumber.PhoneNumber number = null;
        try {
            number = phoneNumberUtil.parse(this.canonicalNumber, this.defaultRegionCode);

            // Check that phone number has country code.
            if (number.getCountryCode() != 0) {
                this.countryCode = String.valueOf(number.getCountryCode());
            }
            // Check that phone number has 'National Phone' no.
            if (number.getNationalNumber() != 0) {
                this.nationalPhoneNumber = String.valueOf(number.getNationalNumber());

                // Check that phone number has extension number then map extension number and calculate number of comma in between.
                if (number.getExtension() != null && !number.getExtension().isEmpty()) {
                    this.extensionNumber = number.getExtension();

                    // Calculate number of pauses (',') between national number and extention number.
                    int startFrom = this.canonicalNumber.indexOf(this.nationalPhoneNumber) + nationalPhoneNumber.length();
                    int endTo = this.canonicalNumber.lastIndexOf(this.extensionNumber);
                    String value = this.canonicalNumber.substring(startFrom, endTo);
                    int count = 0;
                    for (int i = 0; i < value.length(); i++) {
                        if (value.charAt(i) == ',') {
                            count += 1;
                        }
                    }
                    this.numberOfPauses = count;
                }
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines whether [is international number].
     *
     * @return True if CountryCode is null/blank OR CountryCode is not equal to DefaultCountryCode.
     */
    public boolean isInternationalNumber() {
        // Return True if CountryCode is null/blank OR CountryCode is not equal to DefaultCountryCode.
        if (this.countryCode != null && this.countryCode.equalsIgnoreCase("") || !(this.countryCode.equalsIgnoreCase(this.defaultCountyCode))) {
            return true;
        } else {
            // Else return false.
            return false;
        }
    }


}