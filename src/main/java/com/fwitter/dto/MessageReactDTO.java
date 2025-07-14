package com.fwitter.dto;

import com.fwitter.models.ApplicationUser;

public class MessageReactDTO{

    private ApplicationUser user;
    private Integer messageId;
    private String reaction;
    public MessageReactDTO() {
    }
    public MessageReactDTO(ApplicationUser user, Integer messageId) {
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
    public String getReaction() {
        return reaction;
    }
    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
    @Override
    public String toString() {
        return "MessageReactDTO{user=" + user + ", messageId=" + messageId + ", reaction=" + reaction + "}";
    }
}
