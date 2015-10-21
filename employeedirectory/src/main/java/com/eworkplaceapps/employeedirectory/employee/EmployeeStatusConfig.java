//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 19/08/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

 public class EmployeeStatusConfig extends BaseEntity{

    private static final String  Stauts_Entity_Name = "StatusConfig";
    private String name = "",color = "",predefinedCode="";
    private UUID tenantId = Utils.emptyUUID();
    private int sortOrder = -1;


    public EmployeeStatusConfig() {
        super(Stauts_Entity_Name);
    }

    public static EmployeeStatusConfig createEntity() {
        return new EmployeeStatusConfig();
    }


    public String getPredefinedCode() {
        return predefinedCode;
    }

    public void setPredefinedCode(String predefinedColor) {
        setPropertyChanged(this.predefinedCode, predefinedCode);
        this.predefinedCode = predefinedColor;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public void setColor(String color) {
        setPropertyChanged(this.color, color);
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

     public int getSortOrder() {
         return sortOrder;
     }

     public void setSortOrder(int sortOrder) {
         setPropertyChanged(this.sortOrder, sortOrder);
         this.sortOrder = sortOrder;
     }

     public  Tuple.Tuple2<Integer, Integer, Integer> getStatusRGBColor() {
        String  strArray []= color.split(",");
        if (strArray!=null && strArray.length == 3) {
            int r = Integer.valueOf(strArray[0]);
            int g = Integer.valueOf(strArray[1]);
            int b = Integer.valueOf(strArray[2]);
            return new Tuple.Tuple2<Integer, Integer, Integer>(r,g,b);
        }
        return new Tuple.Tuple2<Integer, Integer, Integer>(0,0,0);
    }

@Override
public Boolean validate() throws EwpException {
    List<String> message = new ArrayList<>();
    Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
    // It is used to validate the name null or empty.
      if (this.name==null && "".equalsIgnoreCase(this.name)) {
        dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
        message.add(AppMessage.NAME_REQUIRED);
    }
    if (message.isEmpty()) {
        return true;
    } else {
        throw new EwpException(new EwpException("Validation Error"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
    }
}

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
            // If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EmployeeStatusConfig edStatus = (EmployeeStatusConfig) entity;
        edStatus.setEntityId(this.entityId);
        edStatus.setTenantId(this.tenantId);
        edStatus.setName(this.name);
        edStatus.setColor(this.color);
        edStatus.setSortOrder(this.sortOrder);
        edStatus.setPredefinedCode(this.predefinedCode);
        edStatus.setUpdatedAt(this.updatedAt);
        edStatus.setUpdatedBy(this.updatedBy);
        edStatus.setCreatedAt(this.createdAt);
        edStatus.setCreatedBy(this.createdBy);
        return edStatus;
    }
}
