package com.fwitter.dto;

import java.util.List;

import com.fwitter.models.Conversation;
import com.fwitter.models.Notification;

public class ReadMessageResponseDTO{
    private List<MessageDTO> readMessages;
    private Conversation conversation;
    private List<Notification> notifications;

    public ReadMessageResponseDTO(List<MessageDTO> readMessages, Conversation conversation, List<Notification> notifications){
        this.readMessages = readMessages;
        this.conversation = conversation;
        this.notifications = notifications;
    }

    public List<MessageDTO> getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(List<MessageDTO> readMessages) {
        this.readMessages = readMessages;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "ReadMessageResponseDTO{readMessages=" + readMessages + ", conversation=" + conversation
                + ", notifications=" + notifications + "}";
    }
}
