//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/16/2015
//===============================================================================
package com.eworkplaceapps.platform.security;

import com.eworkplaceapps.platform.entity.BaseEntity;

import java.util.UUID;

/**
 * It encapsulates all data for ROLE_PERMISSION entity.
 * It contain all permissions level stuff, Like add, update, delete and view; Permissions are extandable upto 10 permissions.
 */
public class RolePermission extends BaseEntity {

    private final static String ROLE_PERMISSION_ENTITY_NAME = "ROLE_PERMISSION";

    public RolePermission() {
        super(ROLE_PERMISSION_ENTITY_NAME);
    }

    /**
     * Create ROLE_PERMISSION object and return created object.
     *
     * @return
     */
    public static RolePermission createEntity() {
        return new RolePermission();
    }

    // USER Id.
    private UUID roleId = null;
    private Boolean addOp;
    private String refEntityType;
    private Boolean updateOp;
    private Boolean deleteOp;
    private Boolean viewOp;
    private Boolean extOp1;
    private Boolean extOp2;
    private Boolean extOp3;
    private Boolean extOp4;
    private Boolean extOp5;
    private Boolean extOp6;
    private Boolean extOp7;
    private Boolean extOp8;
    private Boolean extOp9;
    private Boolean extOp10;
    private Integer parentEntityType = 0;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        super.setPropertyChanged(this.roleId, roleId);
        this.roleId = roleId;
    }

    public String getRefEntityType() {
        return refEntityType;
    }

    public void setRefEntityType(String refEntityType) {
        super.setPropertyChanged(this.refEntityType, refEntityType);
        this.refEntityType = refEntityType;
    }

    // True if permission for add operation else false.
    public void setAddOp(Boolean newVal) {
        super.setPropertyChanged(this.addOp, newVal);
        this.addOp = newVal;
    }


    // True, if permission for add operation else false.
    public void setUpdateOp(Boolean newVal) {
        super.setPropertyChanged(this.updateOp, newVal);
        this.updateOp = newVal;
    }


    // True, if permission for DELETE operation else false.
    public void setDeleteOp(Boolean newVal) {
        super.setPropertyChanged(this.deleteOp, newVal);
        this.deleteOp = newVal;
    }

    public Boolean getExtOp10() {
        return extOp10;
    }

    public Boolean getAddOp() {
        return addOp;
    }

    public Boolean getUpdateOp() {
        return updateOp;
    }

    public Boolean getDeleteOp() {
        return deleteOp;
    }

    public Boolean getViewOp() {
        return viewOp;
    }

    public Boolean getExtOp1() {
        return extOp1;
    }

    public Boolean getExtOp2() {
        return extOp2;
    }

    public Boolean getExtOp3() {
        return extOp3;
    }

    public Boolean getExtOp4() {
        return extOp4;
    }

    public Boolean getExtOp5() {
        return extOp5;
    }

    public Boolean getExtOp6() {
        return extOp6;
    }

    public Boolean getExtOp7() {
        return extOp7;
    }

    public Boolean getExtOp8() {
        return extOp8;
    }

    public Boolean getExtOp9() {
        return extOp9;
    }

    // True, if permission for VIEW operation else false.
    public void setViewOp(Boolean newVal) {
        super.setPropertyChanged(this.viewOp, newVal);
        this.viewOp = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp1(Boolean newVal) {
        super.setPropertyChanged(this.extOp1, newVal);
        this.extOp1 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp2(Boolean newVal) {
        super.setPropertyChanged(this.extOp2, newVal);
        this.extOp2 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp3(Boolean newVal) {
        super.setPropertyChanged(this.extOp3, newVal);
        this.extOp3 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp4(Boolean newVal) {
        super.setPropertyChanged(this.extOp4, newVal);
        this.extOp4 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp5(Boolean newVal) {
        super.setPropertyChanged(this.extOp5, newVal);
        this.extOp5 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp6(Boolean newVal) {
        super.setPropertyChanged(this.extOp6, newVal);
        this.extOp6 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp7(Boolean newVal) {
        super.setPropertyChanged(this.extOp7, newVal);
        this.extOp7 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp8(Boolean newVal) {
        super.setPropertyChanged(this.extOp8, newVal);
        this.extOp8 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp9(Boolean newVal) {
        super.setPropertyChanged(this.extOp9, newVal);
        this.extOp9 = newVal;
    }


    // True, if permission for CUSTOM operation else false.
    public void setExtOp10(Boolean newVal) {
        super.setPropertyChanged(this.extOp10, newVal);
        this.extOp10 = newVal;
    }

    public Integer getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(Integer parentEntityType) {
        super.setPropertyChanged(this.parentEntityType, parentEntityType);
        this.parentEntityType = parentEntityType;
    }

}