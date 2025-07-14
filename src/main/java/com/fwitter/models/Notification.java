package com.fwitter.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Enumerated
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "timestamp")
    private LocalDateTime notificationTimestamp;

    private boolean acknowledged;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id")
    private ApplicationUser recipient;

    @ManyToOne
    @JoinColumn(name = "action_user_id")
    private ApplicationUser actionUser;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = true)
    private Message message;

    public Notification() {
    }

    public Notification(NotificationType notificationType, LocalDateTime notificationTimestamp, boolean acknowledged,
                        ApplicationUser recipient, ApplicationUser actionUser, Post post, Comment comment) {
        this.notificationType = notificationType;
        this.notificationTimestamp = notificationTimestamp;
        this.acknowledged = acknowledged;
        this.recipient = recipient;
        this.actionUser = actionUser;
        this.post = post;
        this.comment = comment;
    }

    public Notification(NotificationType notificationType, LocalDateTime notificationTimestamp, boolean acknowledged,
                        ApplicationUser recipient, ApplicationUser actionUser) {
        this.notificationType = notificationType;
        this.notificationTimestamp = notificationTimestamp;
        this.acknowledged = acknowledged;
        this.recipient = recipient;
        this.actionUser = actionUser;
    }

    public Notification(NotificationType notificationType, LocalDateTime notificationTimestamp, boolean acknowledged, ApplicationUser recipient, ApplicationUser actionUser, Message message) {
        this.notificationType = notificationType;
        this.notificationTimestamp = notificationTimestamp;
        this.acknowledged = acknowledged;
        this.recipient = recipient;
        this.actionUser = actionUser;
        this.message = message;
    }



    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public LocalDateTime getNotificationTimestamp() {
        return notificationTimestamp;
    }

    public void setNotificationTimestamp(LocalDateTime notificationTimestamp) {
        this.notificationTimestamp = notificationTimestamp;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public ApplicationUser getRecipient() {
        return recipient;
    }

    public void setRecipient(ApplicationUser recipient) {
        this.recipient = recipient;
    }

    public ApplicationUser getActionUser() {
        return actionUser;
    }

    public void setActionUser(ApplicationUser actionUser) {
        this.actionUser = actionUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", notificationType=" + notificationType +
                ", notificationTimestamp=" + notificationTimestamp +
                ", acknowledged=" + acknowledged +
                ", recipient=" + recipient +
                ", actionUser=" + actionUser +
                ", post=" + post +
                ", comment=" + comment +
                ", message=" + message +
                '}';
    }



}






