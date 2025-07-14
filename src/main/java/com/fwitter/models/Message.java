package com.fwitter.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer messageId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "message_type")
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "sent_by")
    private ApplicationUser sentBy;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnore
    private Conversation conversation;

    @Column(name = "message_text", length=2056)
    private String messageText;


    @Column(name = "sent_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime sentAt;

    @ManyToMany
    @JoinTable(name = "message_seen_by", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<ApplicationUser> seenBy;

    @Column(name = "message_image", nullable = true)
    private String messageImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_reply_to", referencedColumnName = "messageId")
    private Message replyTo;

    @ManyToMany
    @JoinTable(
        name="message_reactions_junction",
        joinColumns=@JoinColumn(name="reaction_id"),
        inverseJoinColumns=@JoinColumn(name="message_id")
    )
    private Set<MessageReaction> reactions;

    @ManyToMany
    @JoinTable(name = "message_hidden_by", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
    private Set<ApplicationUser> hiddenBy;

    public Message() {
        this.seenBy = new HashSet<>();
        this.sentAt = LocalDateTime.now();
        this.hiddenBy = new HashSet<>();
        this.reactions = new HashSet<>();
    }

    public Message(MessageType messageType, ApplicationUser sentBy, Conversation conversation, String messageText) {
        this();
        this.messageType = messageType;
        this.sentBy = sentBy;
        this.conversation = conversation;
        this.messageText = messageText;
    }

    public Message(MessageType messageType, ApplicationUser sentBy, Conversation conversation, String messageText,
            String messageImage) {
        this(messageType, sentBy, conversation, messageText);
        this.messageImage = messageImage;
    }

    public Message(MessageType messageType, ApplicationUser sentBy, Conversation conversation, String messageText,
            LocalDateTime sentAt, Set<ApplicationUser> seenBy, String messageImage, Message replyTo,
            Set<MessageReaction> reactions, Set<ApplicationUser> hiddenBy) {
        this.messageType = messageType;
        this.sentBy = sentBy;
        this.conversation = conversation;
        this.messageText = messageText;
        this.sentAt = sentAt;
        this.seenBy = seenBy;
        this.messageImage = messageImage;
        this.replyTo = replyTo;
        this.reactions = reactions;
        this.hiddenBy = hiddenBy;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
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

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public Set<MessageReaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<MessageReaction> reactions) {
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
        return "Message{messageId=" + messageId + ", messageType=" + messageType + ", sentBy=" + sentBy.getUserId()
                + ", conversation=" + conversation.getConversationId() + ", messageText=" + messageText + ", sentAt=" + sentAt + ", seenBy="
                + seenBy.stream().map(user -> user.getUserId()).toList() + ", messageImage=" + messageImage + ", replyTo=" + replyTo + ", reactions=" + reactions.stream().map(reaction -> reaction.getReaction()).toList()
                + ", hiddenBy=" + hiddenBy.stream().map(user -> user.getUserId()).toList() + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, messageType, sentBy, conversation, messageText, sentAt, seenBy, messageImage,
                replyTo, reactions, hiddenBy);
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
        Message other = (Message) obj;
        return Objects.equals(messageId, other.messageId) && messageType == other.messageType
                && Objects.equals(sentBy, other.sentBy) && Objects.equals(conversation, other.conversation)
                && Objects.equals(messageText, other.messageText) && Objects.equals(sentAt, other.sentAt)
                && Objects.equals(seenBy, other.seenBy) && Objects.equals(messageImage, other.messageImage)
                && Objects.equals(replyTo, other.replyTo) && Objects.equals(reactions, other.reactions)
                && Objects.equals(hiddenBy, other.hiddenBy);
    }


}


