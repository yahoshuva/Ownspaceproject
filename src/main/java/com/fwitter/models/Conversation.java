package com.fwitter.models;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity(name="conversations")
public class Conversation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conversation_id")
    private Integer conversationId;

    @ManyToMany
    @JoinTable(
            name="conversation_user_junction",
            joinColumns = @JoinColumn(name="conversation_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    List<ApplicationUser> conversationUsers;

    @OneToMany(mappedBy="conversation")
    private List<Message> conversationMessage;

    @Column(name="converstion_name", nullable=true)
    private String conversationName;

    @Column(name="conversation_picture", nullable=true)
    private String conversationPicture;

    public Conversation(){}

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public List<ApplicationUser> getConversationUsers() {
        return conversationUsers;
    }

    public void setConversationUsers(List<ApplicationUser> conversationUsers) {
        this.conversationUsers = conversationUsers;
    }

    public List<Message> getConversationMessage() {
        return conversationMessage;
    }

    public void setConversationMessage(List<Message> conversationMessage) {
        this.conversationMessage = conversationMessage;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getConversationPicture() {
        return conversationPicture;
    }

    public void setConversationPicture(String conversationPicture) {
        this.conversationPicture = conversationPicture;
    }

    @Override
    public String toString() {
        return "Conversation{conversationId=" + conversationId + ", conversationUsers=" + conversationUsers
                + ", conversationMessage=" + conversationMessage + ", conversationName=" + conversationName
                + ", conversationPicture=" + conversationPicture + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, conversationUsers, conversationMessage, conversationName,
                conversationPicture);
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
        Conversation other = (Conversation) obj;
        return Objects.equals(conversationId, other.conversationId)
                && Objects.equals(conversationUsers, other.conversationUsers)
                && Objects.equals(conversationMessage, other.conversationMessage)
                && Objects.equals(conversationName, other.conversationName)
                && Objects.equals(conversationPicture, other.conversationPicture);
    }

}
