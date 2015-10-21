//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to ENTITY_CUSTOM_FIELD entities.
 * CUSTOM fields are user define fields, those are created by user. CUSTOM fields can be inculde/exclude by a user.
 */
public class EntityCustomFieldDataService extends BaseDataService<EntityCustomFieldDef> {

    EntityCustomFieldDefData dataDelegate = new EntityCustomFieldDefData();

    /**
     * Initializes a new instance of the EntityCustomFieldDataService class.
     */

    public EntityCustomFieldDataService() {
        super("EntityCustomFieldDef");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to get custom field list as Entity from entityType.
     *
     * @param entityType int
     * @return List<CustomField>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    //TODO dataDelegate.getEntityCustomFieldListByEntityTypeAsEntityList not returning customfield list
    public List<CustomField> getEntityCustomFieldListByEntityTypeAsEntityList(int entityType, UUID tenantId) throws EwpException {
        List<CustomField> list = new ArrayList<CustomField>();
        //List<CustomField> list  =  dataDelegate.getEntityCustomFieldListByEntityTypeAsEntityList(entityType, tenantId);

        return list;  //check
    }

    // Method is used to get custom field list as ResultSet from entityType.
    public Cursor getEntityCustomFieldListByentityTypeAsResultSet(int entityType) throws EwpException {

        return dataDelegate.getEntityCustomFieldListByEntityTypeAsResultSet(entityType);
    }

    /**
     * Method is used to get viewable custom fields list with permission as Resultset.
     *
     * @param userId     UUID
     * @param tenantId   UUID
     * @param entityType int
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public Cursor getViewableEntityCustomFieldAndPermissionListByUserId(UUID userId, UUID tenantId, int entityType) throws EwpException {
        return dataDelegate.getViewableEntityCustomFieldAndPermissionListByUserId(userId, tenantId, entityType);
    }

    /**
     * It is used to get logged in user custom field list.
     *
     * @param entityType int
     * @return List<CustomField>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<CustomField> getLoggedInUserCustomFieldList(int entityType) throws EwpException {
        List<CustomField> entityList = getEntityCustomFieldListByEntityTypeAsEntityList(entityType, EwpSession.getSharedInstance().getTenantId());

        // In case of ADMIN, Return whole customfields list.
        /*if (EwpSession.getSharedInstance().isAccountAdmin || EwpSession.getSharedInstance().isSystemAdmin) {
            return (getAdminCustomFields(entityList));
        }*/

        UUID userId = EwpSession.getSharedInstance().getUserId();


        Cursor resultSet = dataDelegate.getNonViewableEntityCustomFieldListByUserId(userId, entityType);

        List<CustomField> customFieldInfoList = new ArrayList<>();

        //check
        while (resultSet.moveToNext()) {

            //    STRING id = resultSet.getString(resultSet.getColumnIndex("FieldId"));
            //   Boolean canView = resultSet.getInt(resultSet.getColumnIndex("ViewField")) == 1;
            //    Boolean canUpdate = resultSet.getInt(resultSet.getColumnIndex("UpdateField")) == 1 ;

            for (int i = 0; i < entityList.size(); i++) {

                /*if (entityList.get(i).entityId.toString().equals(id) && canView) {
                    EntityCustomFieldDef def = (EntityCustomFieldDef) entityList.get(i);

                    CustomField customField = new CustomField();
                    customField.hasUpdatePermission = canUpdate;
                    customField.hasViewPermission = canView;
                    CustomFieldEnums.FieldCode[] arrayFieldCode = CustomFieldEnums.FieldCode.values();
                    customField.fieldCode = arrayFieldCode[def.getFieldCode()];
                    CustomFieldEnums.FieldDataType[] arrayFieldDT = CustomFieldEnums.FieldDataType.values();
                    customField.dataType = arrayFieldDT[def.getFieldDataType()];
                    customField.labelName = def.customLabel;

                    // Append the custom field in list.
                    customFieldInfoList.add(customField);

                    // It is used to remove the item which is already found.
                    entityList.remove(i);

                    break;
                }*/

            }
        }

        return customFieldInfoList;
    }

    /**
     * It is used to get admin custom fields list.
     *
     * @param entityCustomFieldList List<BaseEntity>
     * @return List<CustomField>
     */
    private List<CustomField> getAdminCustomFields(List<BaseEntity> entityCustomFieldList) {

        List<CustomField> customFieldInfoList = new ArrayList<>();
        for (int i = 0; i < entityCustomFieldList.size(); i++) {
            EntityCustomFieldDef def = (EntityCustomFieldDef) entityCustomFieldList.get(i);
            CustomField customField = new CustomField();
            customField.setHasUpdatePermission(true);
            customField.setHasViewPermission(true);
            CustomFieldEnums.FieldCode[] arrayFieldCode = CustomFieldEnums.FieldCode.values();
            customField.setFieldCode(arrayFieldCode[def.getFieldCode()]);
            CustomFieldEnums.FieldDataType[] arrayFieldDT = CustomFieldEnums.FieldDataType.values();
            customField.setDataType(arrayFieldDT[def.getFieldDataType()]);
            customField.setLabelName(def.getCustomLabel());
            customFieldInfoList.add(customField);
        }
        return customFieldInfoList;
    }

    /**
     * Unused Methods
     */
    /**
     * Method is used to get non viewable custom fields list.
     *
     * @param userId     UUID
     * @param entityType int
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getNonViewableEntityCustomFieldsByUserIdAsResultSet(UUID userId, int entityType) throws EwpException {

        return dataDelegate.getNonViewableEntityCustomFieldListByUserId(userId, entityType);
    }

}
