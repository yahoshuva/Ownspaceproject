package com.fwitter.repositories;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Message;
import com.fwitter.models.Notification;
import com.fwitter.models.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> getByRecipientAndAcknowledgedFalse(ApplicationUser recipient);
    List<Notification> getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(ApplicationUser user, List<NotificationType> notificationTypes);
    List<Notification> findByMessageIn(List<Message> messages);
}
