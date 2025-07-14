package com.fwitter.dto;

import com.fwitter.models.Notification;

import java.util.List;
import java.util.Objects;

public class AcknowledgeNotificationsDTO{
    private List<Notification> notifications;

    public AcknowledgeNotificationsDTO() {
    }

    public AcknowledgeNotificationsDTO(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notifications);
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
        AcknowledgeNotificationsDTO other = (AcknowledgeNotificationsDTO) obj;
        return Objects.equals(notifications, other.notifications);
    }

    @Override
    public String toString() {
        return "AcknowledgeNotifcationsDTO{notifications=" + notifications + "}";
    }
  
}
