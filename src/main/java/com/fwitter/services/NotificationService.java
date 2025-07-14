//package com.fwitter.services; import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import com.fwitter.dto.MessageDTO;
//import com.fwitter.models.ApplicationUser;
//import com.fwitter.models.Message;
//import com.fwitter.models.Notification;
//import com.fwitter.models.NotificationType;
//import com.fwitter.models.Post;
//import com.fwitter.repositories.NotificationRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationService{
//
//    private final NotificationRepository notificationRepository;
//    private final UserService userService;
//    private final SimpMessagingTemplate template;
//
//    private final static List<NotificationType> NON_NEW_POST_NOTIFICATION_TYPES = List.of(
//            NotificationType.FOLLOW,
//            NotificationType.LIKE,
//            NotificationType.REPLY,
//            NotificationType.REPOST,
//            NotificationType.BOOKMARK,
//            NotificationType.MESSAGE,
//            NotificationType.MENTION
//        );
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository, UserService userService, SimpMessagingTemplate template){
//        this.notificationRepository = notificationRepository;
//        this.userService = userService;
//        this.template = template;
//    }
//
//    public List<Notification> getAllNotificationsForUser(Integer userId){
//        ApplicationUser user = userService.getUserById(userId);
//        return notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(user, NON_NEW_POST_NOTIFICATION_TYPES);
//    }
//
//    public void createAndSendPostNotifications(Post post){
//        ApplicationUser author = userService.getUserById(post.getAuthor().getUserId());
//        Set<ApplicationUser> followers = userService.retrieveFollowersList(author.getUsername());
//
//        List<Notification> notifications = followers.stream()
//            .map(follower -> {
//                return new Notification(NotificationType.NEW_POST, LocalDateTime.now(), true, follower, author, post, null);
//            }).collect(Collectors.toList());
//
//        notifications = notificationRepository.saveAll(notifications);
//
//        //Send each notification to the correct recipient
//        notifications.forEach(notification -> template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification));
//    }
//
//    public void createAndSendNotification(NotificationType type, ApplicationUser recipient, ApplicationUser actionUser, Post post, Post reply){
//        Notification notification = new Notification(type, LocalDateTime.now(), false, recipient, actionUser, post, reply);
//        notification = notificationRepository.save(notification);
//
//        template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification);
//    }
//
//    public void createAndSendFollowNotification(ApplicationUser recipient, ApplicationUser actionUser){
//        Notification notification = new Notification(NotificationType.FOLLOW, LocalDateTime.now(), false, recipient, actionUser);
//        notification = notificationRepository.save(notification);
//        template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification);
//    }
//
//    public void createAndSendMessageNotifications(List<ApplicationUser> recipients, ApplicationUser actionUser, Message message){
//        List<Notification> notifications = recipients.stream()
//            .map(user -> {
//                return new Notification(NotificationType.MESSAGE, LocalDateTime.now(), false, user, actionUser, message);
//            })
//            .toList();
//
//            notifications = notificationRepository.saveAll(notifications);
//
//            notifications.forEach(notification -> template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification));
//
//            MessageDTO messageDTO = new MessageDTO(
//                message.getMessageId(),
//                message.getMessageType(),
//                message.getConversation().getConversationId(),
//                message.getSentAt(),
//                message.getSentBy(),
//                message.getSeenBy(),
//                message.getMessageImage(),
//                message.getMessageText()
//            );
//
//            recipients.forEach(user -> template.convertAndSendToUser(user.getUsername(), "/messages", messageDTO));
//    }
//
//    public List<Notification> fetchUsersNotifications(Integer userId){
//        ApplicationUser user = userService.getUserById(userId);
//        return notificationRepository.getByRecipientAndAcknowledgedFalse(user);
//    }
//
//    public void acknowledgeNotifications(List<Notification> notifications){
//        List<Notification> acknowledgedNotifications = notifications.stream()
//            .map(notification -> {
//                notification.setAcknowledged(true);
//                return notification;
//            }).toList();
//
//        notificationRepository.saveAll(acknowledgedNotifications);
//    }
//
//    public List<Notification> readMessageNotifications(List<Message> messages, ApplicationUser recipient){
//        List<Notification> notifications = notificationRepository.findByMessageIn(messages);
//        List<Notification> filteredNotifications = notifications.stream()
//            .filter(noti -> noti.getRecipient().getUserId() == recipient.getUserId())
//            .filter(noti -> !noti.isAcknowledged())
//            .toList();
//
//
//        this.acknowledgeNotifications(filteredNotifications);
//
//        return notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(recipient, List.of(NotificationType.MESSAGE));
//    }
//
//}



//import java.time.LocalDateTime;
//import java.util.List;
//
//import com.fwitter.dto.MessageDTO;
//import com.fwitter.models.ApplicationUser;
//import com.fwitter.models.Message;
//import com.fwitter.models.Notification;
//import com.fwitter.models.NotificationType;
//import com.fwitter.repositories.NotificationRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private final UserService userService;
//    private final SimpMessagingTemplate template;
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository, UserService userService, SimpMessagingTemplate template) {
//        this.notificationRepository = notificationRepository;
//        this.userService = userService;
//        this.template = template;
//    }
//
//    /**
//     * Fetch all unread message notifications for a user.
//     */
//    public List<Notification> fetchUsersNotifications(Integer userId) {
//        ApplicationUser user = userService.getUserById(userId);
//        return notificationRepository.findByRecipientAndAcknowledgedFalse(user);
//    }
//
//    /**
//     * Create and send message notifications to recipients when a new message is received.
//     */
//    public void createAndSendMessageNotifications(List<ApplicationUser> recipients, ApplicationUser sender, Message message) {
//        List<Notification> notifications = recipients.stream()
//                .map(user -> new Notification(NotificationType.MESSAGE, LocalDateTime.now(), false, user, sender, message))
//                .toList();
//
//        notifications = notificationRepository.saveAll(notifications);
//
//        // Send WebSocket notification for each recipient
//        notifications.forEach(notification ->
//                template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification)
//        );
//
//        // Send WebSocket message payload
//        MessageDTO messageDTO = new MessageDTO(
//                message.getMessageId(),
//                message.getMessageType(),
//                message.getConversation().getConversationId(),
//                message.getSentAt(),
//                message.getSentBy(),
//                message.getSeenBy(),
//                message.getMessageImage(),
//                message.getMessageText()
//        );
//
//        recipients.forEach(user ->
//                template.convertAndSendToUser(user.getUsername(), "/messages", messageDTO)
//        );
//    }
//
//    /**
//     * Mark notifications as acknowledged (read).
//     */
//    public void acknowledgeNotifications(List<Notification> notifications) {
//        notifications.forEach(notification -> notification.setAcknowledged(true));
//        notificationRepository.saveAll(notifications);
//    }
//
//    /**
//     * Mark message notifications as read for a recipient.
//     */
//    public List<Notification> readMessageNotifications(List<Message> messages, ApplicationUser recipient) {
//        List<Notification> notifications = notificationRepository.findByMessageIn(messages);
//
//        List<Notification> filteredNotifications = notifications.stream()
//                .filter(noti -> noti.getRecipient().getUserId().equals(recipient.getUserId()) && !noti.isAcknowledged())
//                .toList();
//
//        this.acknowledgeNotifications(filteredNotifications);
//
//        return notificationRepository.findByRecipientOrderByNotificationTimestampDesc(recipient);
//    }
//}


//import com.fwitter.models.*;
//import com.fwitter.repositories.NotificationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public  class NotificationService{
//
//    private final NotificationRepository notificationRepository;
//    private final UserService userService;
//    private final SimpMessagingTemplate template;
//    private final static List<NotificationType> NON_NEW_POST_NOTIFICATION_TYPES = List.of(
//            NotificationType.FOLLOW,
//            NotificationType.NEW_POST,
//            NotificationType.LIKE,
//            NotificationType.COMMENT,
//            NotificationType.MESSAGE,
//            NotificationType.BOOKMARK
//    );
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository, UserService userService, SimpMessagingTemplate template) {
//        this.notificationRepository = notificationRepository;
//        this.userService = userService;
//        this.template = template;
//    }
//
////    public List<Notification> getAllNotificationsForUser(Integer userId){
////        ApplicationUser user = userService.getUserById(userId);
////        return notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(user,NON_NEW_POST_NOTIFICATION_TYPES);
////    }
//
//    public List<Notification> getAllNotificationsForUser(Integer userId) {
//        System.out.println("🔹 Fetching notifications for user ID: " + userId);
//
//        ApplicationUser user = userService.getUserById(userId);
//        if (user == null) {
//            System.out.println("❌ User not found!");
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//
//        List<Notification> notifications = notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(user, NON_NEW_POST_NOTIFICATION_TYPES);
//        System.out.println("✅ Found notifications: " + notifications.size());
//        return notifications;
//    }
//
//
//
//
//    public void createAndSendPostNotifications(Post post) {
//        ApplicationUser author = userService.getUserById(post.getAuthor().getUserId());
//
//        Set<ApplicationUser> followers = userService.retrieveFollowersList(author.getUsername());
//
//        List<Notification> notifications = followers.stream()
//                .map(follower -> new Notification(NotificationType.NEW_POST, LocalDateTime.now(), false, follower, author, post,null))
//                .collect(Collectors.toList());
//
//        notifications  = notificationRepository.saveAll(notifications);
//
//        notifications.forEach(notification ->template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications" ,notification));
//
//    }
//
//
//    public void createAndSendNotification(NotificationType type, ApplicationUser recipient, ApplicationUser actionUser, Post post, Comment comment){
//        Notification notification = new Notification(type,LocalDateTime.now(),false,recipient,actionUser,post,comment);
//        notification = notificationRepository.save(notification);
//
//
//        template.convertAndSendToUser(notification.getRecipient().getUsername(),"/notifications", notification );;
//    }
//
//    public void createAndSendFollowNotification(ApplicationUser recipient,ApplicationUser actionUser){
//        Notification notification = new Notification(NotificationType.FOLLOW,LocalDateTime.now(),false,recipient,actionUser);
//        notification = notificationRepository.save(notification);
//
//        template.convertAndSendToUser(notification.getRecipient().getUsername(),"/notifications",notification );
//    }
//
//
//    public List<Notification> fetchUsersNotifications(Integer userId){
//        ApplicationUser user = userService.getUserById(userId);
//        return notificationRepository.getByRecipientAndAcknowledgedFalse(user);
//    }
//
//    public void acknowledgeNotifications(List<Notification> notifications){
//        List<Notification> acknowledgedNotifications = notifications.stream()
//                .map(notification -> {
//                    notification.setAcknowledged(true);
//                    return notification;
//                }).toList();
//        notificationRepository.saveAll(acknowledgedNotifications);
//    }
//
//
//
//
//
//}

package com.fwitter.services;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fwitter.dto.MessageDTO;
import com.fwitter.models.*;

import com.fwitter.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService{

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final SimpMessagingTemplate template;

    private final static List<NotificationType> NON_NEW_POST_NOTIFICATION_TYPES = List.of(
            NotificationType.FOLLOW,
            NotificationType.LIKE,
            NotificationType.COMMENT,
//            NotificationType.REPOST,
            NotificationType.BOOKMARK,
            NotificationType.MESSAGE,
            NotificationType.MENTION
    );

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userService, SimpMessagingTemplate template){
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.template = template;
    }

    public List<Notification> getAllNotificationsForUser(Integer userId){
        ApplicationUser user = userService.getUserById(userId);
        return notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(user, NON_NEW_POST_NOTIFICATION_TYPES);
    }

    public void createAndSendPostNotifications(Post post){
        ApplicationUser author = userService.getUserById(post.getAuthor().getUserId());
        Set<ApplicationUser> followers = userService.retrieveFollowersList(author.getUsername());

        List<Notification> notifications = followers.stream()
                .map(follower -> {
                    return new Notification(NotificationType.NEW_POST, LocalDateTime.now(), true, follower, author, post, null);
                }).collect(Collectors.toList());

        notifications = notificationRepository.saveAll(notifications);

        //Send each notification to the correct recipient
        notifications.forEach(notification -> template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification));
    }

    public void createAndSendNotification(NotificationType type, ApplicationUser recipient, ApplicationUser actionUser, Post post, Comment comment){
        Notification notification = new Notification(type, LocalDateTime.now(), false, recipient, actionUser, post, comment);
        notification = notificationRepository.save(notification);

        template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification);
    }

//    public void createAndSendFollowNotification(ApplicationUser recipient, ApplicationUser actionUser){
//        Notification notification = new Notification(NotificationType.FOLLOW, LocalDateTime.now(), false, recipient, actionUser);
//        notification = notificationRepository.save(notification);
//        template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification);
//    }

    public void createAndSendFollowNotification(ApplicationUser recipient, ApplicationUser actionUser) {
        if (recipient == null || actionUser == null) {
            throw new IllegalArgumentException("Recipient or action user is null in follow notification.");
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.FOLLOW);
        notification.setNotificationTimestamp(LocalDateTime.now());
        notification.setRecipient(recipient); // ✅ this was likely missing or null
        notification.setActionUser(actionUser);
        notification.setAcknowledged(false);

        notificationRepository.save(notification);

        // If you use WebSockets or real-time delivery, send it here too
        // notificationWebSocketSender.send(notification);
             template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification);

    }


    public void createAndSendMessageNotifications(List<ApplicationUser> recipients, ApplicationUser actionUser, Message message){
        List<Notification> notifications = recipients.stream()
                .map(user -> {
                    return new Notification(NotificationType.MESSAGE, LocalDateTime.now(), false, user, actionUser, message);
                })
                .collect(Collectors.toList());


        notifications = notificationRepository.saveAll(notifications);

        notifications.forEach(notification -> template.convertAndSendToUser(notification.getRecipient().getUsername(), "/notifications", notification));

        MessageDTO messageDTO = new MessageDTO(
                message.getMessageId(),
                message.getMessageType(),
                message.getConversation().getConversationId(),
                message.getSentAt(),
                message.getSentBy(),
                message.getSeenBy(),
                message.getMessageImage(),
                message.getMessageText()
        );

        recipients.forEach(user -> template.convertAndSendToUser(user.getUsername(), "/messages", messageDTO));
    }

    public List<Notification> fetchUsersNotifications(Integer userId){
        ApplicationUser user = userService.getUserById(userId);
        return notificationRepository.getByRecipientAndAcknowledgedFalse(user);
    }

    public void acknowledgeNotifications(List<Notification> notifications){
        List<Notification> acknowledgedNotifications = notifications.stream()
                .map(notification -> {
                    notification.setAcknowledged(true);
                    return notification;
                }).collect(Collectors.toList());


        notificationRepository.saveAll(acknowledgedNotifications);
    }

    public List<Notification> readMessageNotifications(List<Message> messages, ApplicationUser recipient){
        List<Notification> notifications = notificationRepository.findByMessageIn(messages);
        List<Notification> filteredNotifications = notifications.stream()
                .filter(noti -> noti.getRecipient().getUserId() == recipient.getUserId())
                .filter(noti -> !noti.isAcknowledged())
                .collect(Collectors.toList());



        this.acknowledgeNotifications(filteredNotifications);

        return notificationRepository.getByRecipientAndNotificationTypeInOrderByNotificationTimestampDesc(recipient, List.of(NotificationType.MESSAGE));
    }

}
