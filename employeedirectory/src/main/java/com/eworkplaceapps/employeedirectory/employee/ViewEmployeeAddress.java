//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.address.Address;

/**
 *
 */
public class ViewEmployeeAddress {
    /// Getter/setter for Address1
    private String address1 = "";


    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public static ViewEmployeeAddress setViewAddressPropertiesFromAddressEntity(Address address) {
        ViewEmployeeAddress vAddress = new ViewEmployeeAddress();
        vAddress.setAddress1(address.getAddress1());
       // vAddress.setAddress2(address.getAddress2());
        //vAddress.setAddress3(address.getAddress3());
        //vAddress.setAddress4(address.getAddress4());
        return vAddress;
    }




}
