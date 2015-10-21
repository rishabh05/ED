//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.note;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 *
 */
public class NoteData extends BaseData<Note> {

    @Override
    public Note createEntity() {
        return Note.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id Object
     * @return
     * @throws EwpException
     */
    @Override
    public Note getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(NoteId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the Note Entity from database.
     * Return Collection of Note Entity.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public List<Note> getList() throws EwpException {
        return executeSqlAndGetEntityList(getSQL());
    }

    /**
     * Get Note Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFNote where LOWER(NoteId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all Note Entity record from database and return result as a ResultSet.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFNote";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to delete note with respact to sourceid , sourcetype and created by user.
     *
     * @param sourceId
     * @param sourceEntityType
     * @param createdById
     */
    public void deleteNoteFromSourceEntityIdAndTypeAndCreatedBy(UUID sourceId, int sourceEntityType, UUID createdById) throws EwpException {
        super.deleteRows("PFNote", "LOWER(EntityId)=? and EntityType=? and LOWER(CreatedBy)=?", new String[]{sourceId.toString().toLowerCase(), String.valueOf(sourceEntityType), createdById.toString().toLowerCase()});
    }


    @Override
    public long insertEntity(Note entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("NoteId", entity.getEntityId().toString());
        values.put("EntityId", entity.getSourceEntityId().toString());
        values.put("EntityType", entity.getSourceEntityType());
        values.put("Content", entity.getNote());
        values.put("NoteType", entity.getNoteType());
        values.put("System", entity.isSystem());
        values.put("Private", entity.isPrivateNote());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        Date updatedAt = new Date();
        entity.setUpdatedAt(updatedAt);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(updatedAt));
        values.put("TenantId", entity.getTenantId().toString());
        long id = super.insert("PFNote", values);
        return id;
    }

    @Override
    public void update(Note entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Content", entity.getNote());
        values.put("NoteType", entity.getNoteType());
        values.put("System", entity.isSystem());
        values.put("Private", entity.isPrivateNote());
        values.put("ModifiedBy", entity.getUpdatedBy());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFNote", values, "LOWER(NoteId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * @param sourceEntityId
     * @param sourceEntityType
     * @param createdById
     * @return
     */
    public Note getNoteFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType, UUID createdById) throws EwpException {
        String sql = getSQL() + " where LOWER(EntityId)= ('" + sourceEntityId.toString().toLowerCase() + "') and EntityType= '" + sourceEntityType + "' and LOWER(CreatedBy)= ('" + createdById.toString().toLowerCase() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Delete Note entity.
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public boolean deleteRows(Note entity) throws EwpException {
        return super.deleteRows("PFNote", "LOWER(NoteId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public void setPropertiesFromResultSet(Note entity, Cursor cursor) throws EwpException {

        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String addId = cursor.getString(cursor.getColumnIndex("NoteId"));
        if (addId != null && !"".equals(addId)) {
            entity.setEntityId(UUID.fromString(addId));
        }

        String note1 = cursor.getString(cursor.getColumnIndex("Content"));
        if (note1 != null) {
            entity.setNote(note1);
        }

        String note2 = cursor.getString(cursor.getColumnIndex("NoteType"));
        if (note2 != null && !"".equals(note2)) {
            entity.setNoteType(Integer.parseInt(note2));
        }

        boolean system = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("System")));
        entity.setSystem(system);


        boolean privateNote = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Private")));
        entity.setPrivateNote(privateNote);

        String sourceEntityId = cursor.getString(cursor.getColumnIndex("EntityId"));
        if (sourceEntityId != null && !"".equals(sourceEntityId)) {
            entity.setSourceEntityId(UUID.fromString(sourceEntityId));
        }

        String entityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setSourceEntityType(Integer.parseInt(entityType));
        }

        String createdBy = cursor.getString(cursor.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(createdBy);
        }

        String createdAt = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }

    /**
     * Generate sql string with minimum required fields for Note.
     *
     * @return
     */
    private String getSQL() {
        // Generating the Note SQL Statement to get the Note detail. It will give the Note.
        String sql = "Select * from PFNote ";
        return sql;
    }
}
