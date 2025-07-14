package com.fwitter.dto;

import com.fwitter.models.ApplicationUser;

public class HideMessageRequestDTO{
    private ApplicationUser user;
    private Integer messageId;

    public HideMessageRequestDTO() {
    }

    public HideMessageRequestDTO(ApplicationUser user, Integer messageId) {
        this.user = user;
        this.messageId = messageId;
    }

    public ApplicationUser getUser() {
        return user;
    }
    public void setUser(ApplicationUser user) {
        this.user = user;
    }
    public Integer getMessageId() {
        return messageId;
    }
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "HideMessageRequestDTO{user=" + user + ", messageId=" + messageId + "}";
    }
}
