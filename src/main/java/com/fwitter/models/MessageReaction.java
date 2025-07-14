package com.fwitter.models;

import java.util.Objects;
import jakarta.persistence.*;


@Entity
@Table(name="message_reactions")
public class MessageReaction{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="message_reaction_id")
    private Integer messageReactionId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="message_reaction_user", referencedColumnName = "user_id")
    ApplicationUser reactionUser;

    @Column(name="reaction")
    private String reaction;

    public MessageReaction(){

    }

    public MessageReaction(ApplicationUser user, String reaction){
        this.reactionUser = user;
        this.reaction = reaction;
    }

    public Integer getMessageReactionId() {
        return messageReactionId;
    }

    public void setMessageReactionId(Integer messageReactionId) {
        this.messageReactionId = messageReactionId;
    }

    public ApplicationUser getReactionUser() {
        return reactionUser;
    }

    public void setReactionUser(ApplicationUser reactionUser) {
        this.reactionUser = reactionUser;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    @Override
    public String toString() {
        return "MessageReaction{messageReactionId=" + messageReactionId + ", reactionUser=" + reactionUser
                + ", reaction=" + reaction + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageReactionId, reactionUser, reaction);
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
        MessageReaction other = (MessageReaction) obj;
        return Objects.equals(messageReactionId, other.messageReactionId)
                && Objects.equals(reactionUser, other.reactionUser) && Objects.equals(reaction, other.reaction);
    }

}


