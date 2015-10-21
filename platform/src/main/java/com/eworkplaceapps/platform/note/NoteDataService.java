//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.note;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Note entities.
 * It contains the note with reference to an entity or individually. Note can be remarkable note for en employee.
 */
public class NoteDataService extends BaseDataService<Note> {

    private NoteData dataDelegate = new NoteData();

    /**
     * Initializes a new instance of the NoteDataService class.
     */
    public NoteDataService() {
        super("NoteDataService");
    }

    @Override
    public BaseData<Note> getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to get note from EntityType and EntiyId
     * :param: sourceEntityid, It source entityId, For which we have saved the note
     * :param: sourceEntityType, It source entity type, For which we have saved the note
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @param createdById
     * @return
     * @throws EwpException
     */
    public Note getNoteFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType, UUID createdById) throws EwpException {
        Note response = dataDelegate.getNoteFromSourceEntityIdAndType(sourceEntityId, sourceEntityType, createdById);
        return response;
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param sourceId
     * @param sourceEntityType
     * @param createdById
     * @throws EwpException
     */
    public void deleteNoteFromSourceEntityIdAndType(UUID sourceId, int sourceEntityType, UUID createdById) throws EwpException {
        dataDelegate.deleteNoteFromSourceEntityIdAndTypeAndCreatedBy(sourceId, sourceEntityType, createdById);
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param notes
     * @param sourceId
     * @param sourceEntityType
     * @param createdById
     * @return
     */
    public Object addNoteFromSourceEntityIdAndType(String notes, UUID sourceId, int sourceEntityType, UUID createdById) throws EwpException {
        Note note = new Note();
        note.setNote(notes);
        note.setSourceEntityType(sourceEntityType);
        note.setSourceEntityId(sourceId);
        note.setCreatedBy(createdById.toString());
        return add(note);
    }

    /**
     * Add/Update note entity.
     *
     * @param entity
     * @param entityId
     * @param sourceEntityType
     * @param createdById
     * @return
     */
    public void addUpdateNote(Note entity, UUID entityId, int sourceEntityType, UUID createdById, String appId) throws EwpException {
        if (entity.getEntityId().equals(Utils.emptyUUID()) || entity.getLastOperationType() == DatabaseOperationType.ADD) {
            /// Set operation mode
            entity.setLastOperationType(DatabaseOperationType.ADD);
            entity.setSourceEntityType(sourceEntityType);
            entity.setSourceEntityId(entityId);
            entity.setApplicationId(appId);
            entity.setCreatedBy(createdById.toString());
        } else {
            /// Set operation mode
            entity.setLastOperationType(DatabaseOperationType.UPDATE);
        }
        if (entity.getLastOperationType() == DatabaseOperationType.ADD) {
            add(entity);
        } else {
            update(entity);
        }
    }
}
