//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.note;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.io.Serializable;
import java.util.UUID;

/**
 * Note class contains the note.
 * Note can be remarkable, internal, system, private note.
 */
public class Note extends BaseEntity implements Serializable{
    private static final String NOTE_ENTITY_NAME = "Note";

    private UUID sourceEntityId = Utils.emptyUUID();
    private int sourceEntityType = 0;
    private String note = "";
    private int noteType = 0;
    private boolean system = false, privateNote = false;
    private UUID tenantId = Utils.emptyUUID();
    private String applicationId = "";

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(this.sourceEntityId, sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(this.sourceEntityType, sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        setPropertyChanged(this.note, note);
        this.note = note;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        setPropertyChanged(this.noteType, noteType);
        this.noteType = noteType;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        setPropertyChanged(this.system, system);
        this.system = system;
    }

    public boolean isPrivateNote() {
        return privateNote;
    }

    public void setPrivateNote(boolean privateNote) {
        setPropertyChanged(this.privateNote, privateNote);
        this.privateNote = privateNote;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    public Note() {
        super(NOTE_ENTITY_NAME);
    }

    /**
     * Create note object and return created object.
     *
     * @return
     */
    public static Note createEntity() {
        return new Note();
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Note note = (Note) entity;
        note.setEntityId(this.entityId);
        note.setTenantId(this.tenantId);
        note.setNote(this.note);
        note.setNoteType(this.noteType);
        note.setSourceEntityId(this.sourceEntityId);
        note.setSourceEntityType(this.sourceEntityType);
        note.setApplicationId(this.applicationId);
        note.setSystem(this.system);
        note.setPrivateNote(this.privateNote);
        note.lastOperationType = this.lastOperationType;
        note.setUpdatedAt(this.updatedAt);
        note.setUpdatedBy(this.updatedBy);
        note.setCreatedAt(this.createdAt);
        note.setCreatedBy(this.createdBy);
        return note;
    }
}
