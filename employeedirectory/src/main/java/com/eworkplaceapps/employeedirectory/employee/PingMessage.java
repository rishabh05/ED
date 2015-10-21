//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class PingMessage extends BaseEntity {
    private static final String PING_MESSAGE_ENTITY_NAME = "PingMessage";
    private String message = "";
    private Date sentTime=new Date();
    private UUID fromId = Utils.emptyUUID();
    private UUID parentPingMessageId = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();
    private UUID toId = Utils.emptyUUID();

    public PingMessage() {
        super(PING_MESSAGE_ENTITY_NAME);
    }

    /**
     * Create PingMessage object and return created object.
     *
     * @return PingMessage
     */
    public static PingMessage createEntity() {
        return new PingMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        setPropertyChanged(this.message, message);
        this.message = message;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        setPropertyChanged(this.sentTime, sentTime);
        this.sentTime = sentTime;
    }

    public UUID getFromId() {
        return fromId;
    }

    public void setFromId(UUID fromId) {
        setPropertyChanged(this.fromId, fromId);
        this.fromId = fromId;
    }

    public UUID getParentPingMessageId() {
        return parentPingMessageId;
    }

    public void setParentPingMessageId(UUID parentPingMessageId) {
        setPropertyChanged(this.parentPingMessageId, parentPingMessageId);
        this.parentPingMessageId = parentPingMessageId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public UUID getToId() {
        return toId;
    }

    public void setToId(UUID toId) {
        setPropertyChanged(this.toId, toId);
        this.toId = toId;
    }

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the first name null or empty.
        if (this.message == null && "".equals(this.message)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Message"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        /// It is used to validate the Sender.
        if (this.fromId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Sender"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        /// It is used to validate the Receiver.
        if (this.toId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Receiver"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Error"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }
}
