//package com.ssafy.cafe.controller.rest;
//
//import org.springframework.web.bind.annotation.*;
//
//import com.ssafy.cafe.model.service.FirebasePushNotificationService;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/notification")
//public class PushNotificationController {
//
//    private final FirebasePushNotificationService notificationService;
//
//    public PushNotificationController(FirebasePushNotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    @PostMapping("/send")
//    public String sendNotification(
//            @RequestParam String targetToken,
//            @RequestParam String title,
//            @RequestParam String body) {
//        try {
//            notificationService.sendMessageTo(targetToken, title, body);
//            return "Notification sent successfully!";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Failed to send notification: " + e.getMessage();
//        }
//    }
//}
