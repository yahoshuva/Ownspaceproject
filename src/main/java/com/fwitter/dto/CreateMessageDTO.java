package com.fwitter.dto;

import java.util.Objects;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Conversation;
import com.fwitter.models.MessageType;

public class CreateMessageDTO{

    private MessageType messageType;
    private ApplicationUser sentBy;
    private Conversation conversation;
    private String text;
    private String gifUrl;

    public CreateMessageDTO() {
    }

    public CreateMessageDTO(MessageType messageType, ApplicationUser sentBy, Conversation conversation, String text,
            String gifUrl) {
        this.messageType = messageType;
        this.sentBy = sentBy;
        this.conversation = conversation;
        this.text = text;
        this.gifUrl = gifUrl;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ApplicationUser getSentBy() {
        return sentBy;
    }

    public void setSentBy(ApplicationUser sentBy) {
        this.sentBy = sentBy;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    @Override
    public String toString() {
        return "CreateMessageDTO{messageType=" + messageType + ", sentBy=" + sentBy + ", conversation=" + conversation
                + ", text=" + text + ", gifUrl=" + gifUrl + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, sentBy, conversation, text, gifUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CreateMessageDTO other = (CreateMessageDTO) obj;
        return messageType == other.messageType && Objects.equals(sentBy, other.sentBy)
                && Objects.equals(conversation, other.conversation) && Objects.equals(text, other.text)
                && Objects.equals(gifUrl, other.gifUrl);
    }
 
}
