package com.fwitter.controllers;

import com.fwitter.dto.AcknowledgeNotificationsDTO;
import com.fwitter.models.Notification;
import com.fwitter.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

////
////import java.util.List;
////
////import com.fwitter.dto.AcknowledgeNotificationsDTO;
////import com.fwitter.models.Notification;
////import com.fwitter.services.NotificationService;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestBody;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RestController;
////
////@RestController
////@RequestMapping("/notifications")
////public class NotificationController{
////    private final NotificationService notificationService;
////
////    @Autowired
////    public NotificationController(NotificationService notificationService){
////        this.notificationService = notificationService;
////    }
////
////    @GetMapping("/{userId}")
////    public List<Notification> fetchUsersNotifications(@PathVariable("userId") Integer userId){
////        return notificationService.getAllNotificationsForUser(userId);
////    }
////
////    @GetMapping("/unread/{id}")
////    public List<Notification> fetchUsersUnreadNotification(@PathVariable("id") Integer id){
////        return notificationService.fetchUsersNotifications(id);
////    }
////
////    @PostMapping("/acknowledge")
////    public ResponseEntity<String> acknowledegeNotifications(@RequestBody AcknowledgeNotificationsDTO body){
////        notificationService.acknowledgeNotifications(body.getNotifications());
////        return ResponseEntity.ok("Acknowledged Notifications");
////    }
////}
//
//
//package com.fwitter.controllers;
//
//import java.util.List;
//
//import com.fwitter.dto.AcknowledgeNotificationsDTO;
//import com.fwitter.models.Notification;
//import com.fwitter.services.NotificationService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/notifications")
//public class NotificationController {
//    private final NotificationService notificationService;
//
//    @Autowired
//    public NotificationController(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    /**
//     * Fetch all unread message notifications for a user.
//     */
//    @GetMapping("/unread/{userId}")
//    public List<Notification> fetchUsersUnreadNotifications(@PathVariable("userId") Integer userId) {
//        return notificationService.fetchUsersNotifications(userId);
//    }
//
//    /**
//     * Acknowledge (mark as read) notifications.
//     */
//    @PostMapping("/acknowledge")
//    public ResponseEntity<String> acknowledgeNotifications(@RequestBody AcknowledgeNotificationsDTO body) {
//        notificationService.acknowledgeNotifications(body.getNotifications());
//        return ResponseEntity.ok("Acknowledged Notifications");
//    }
//}


@RestController
@RequestMapping(value = "/api/notifications")

public class NotificationController{

    private final NotificationService notificationService;


    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/{userId}")
    public List<Notification> fetchUsersNotifications(@PathVariable("userId") Integer userId){

        return notificationService.getAllNotificationsForUser(userId);
    }

    @GetMapping("/unread/{id}")
    public List<Notification> fetchUsersUnreadNotification(@PathVariable("id") Integer id){
        return notificationService.fetchUsersNotifications(id);
    }

    @PostMapping("/acknowledge")
    public ResponseEntity<String> acknowledgeNotifications(@RequestBody AcknowledgeNotificationsDTO body){
        notificationService.acknowledgeNotifications(body.getNotifications());
        return ResponseEntity.ok("Acknowledged Notifications");
    }
}