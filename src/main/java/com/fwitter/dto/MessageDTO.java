package com.fwitter.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Message;
import com.fwitter.models.MessageReaction;
import com.fwitter.models.MessageType;

public class MessageDTO{

    private Integer messageId;
    private MessageType messageType;
    private Integer conversationId;
    private LocalDateTime sentAt;
    private ApplicationUser sentBy;
    private Set<ApplicationUser> seenBy;
    private String messageImage;
    private String messageText;
    private Message replyTo;
    private Set<MessageReaction> reactions;
    private Set<ApplicationUser> hiddenBy;
    public MessageDTO() {
    }
    public MessageDTO(Integer messageId, MessageType messageType, Integer conversationId, LocalDateTime sentAt,
            ApplicationUser sentBy, Set<ApplicationUser> seenBy, String messageImage, String messageText) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.conversationId = conversationId;
        this.sentAt = sentAt;
        this.sentBy = sentBy;
        this.seenBy = seenBy;
        this.messageImage = messageImage;
        this.messageText = messageText;
    }

    public MessageDTO(Message message){
        this.messageId = message.getMessageId();
        this.messageType = message.getMessageType();
        this.conversationId = message.getConversation().getConversationId();
        this.sentAt = message.getSentAt();
        this.sentBy = message.getSentBy();
        this.seenBy = message.getSeenBy();
        this.messageImage = message.getMessageImage();
        this.messageText = message.getMessageText();
        this.replyTo = message.getReplyTo();
        this.hiddenBy = message.getHiddenBy();
        this.reactions = message.getReactions();
    }

    public Integer getMessageId() {
        return messageId;
    }
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
    public MessageType getMessageType() {
        return messageType;
    }
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    public Integer getConversationId() {
        return conversationId;
    }
    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public ApplicationUser getSentBy() {
        return sentBy;
    }
    public void setSentBy(ApplicationUser sentBy) {
        this.sentBy = sentBy;
    }
    public Set<ApplicationUser> getSeenBy() {
        return seenBy;
    }
    public void setSeenBy(Set<ApplicationUser> seenBy) {
        this.seenBy = seenBy;
    }
    public String getMessageImage() {
        return messageImage;
    }
    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
 
    public Message getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }
    public Set<MessageReaction> getReactions() {
        return reactions;
    }
    public void setMessageReactions(Set<MessageReaction> reactions) {
        this.reactions = reactions;
    }
    public Set<ApplicationUser> getHiddenBy() {
        return hiddenBy;
    }
    public void setHiddenBy(Set<ApplicationUser> hiddenBy) {
        this.hiddenBy = hiddenBy;
    }
    @Override
    public String toString() {
        return "MessageDTO{messageId=" + messageId + ", messageType=" + messageType + ", conversationId="
                + conversationId + ", sentAt=" + sentAt + ", sentBy=" + sentBy + ", seenBy=" + seenBy
                + ", messageImage=" + messageImage + ", messageText=" + messageText + ", replyTo=" + replyTo
                + ", reactions=" + reactions + ", hiddenBy=" + hiddenBy + "}";
    }
}
