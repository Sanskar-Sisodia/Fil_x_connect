package com.filxconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.filxconnect.dto.NotificationRequest;
import com.filxconnect.entity.Notification;
import com.filxconnect.service.NotificationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ✅ Create Notification (POST)
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationRequest request) {
        Notification notification = notificationService.createNotification(request.getUserId(), request.getMessage(), request.getPostId());
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PostMapping("/warn/{id}")
    public ResponseEntity<Notification> sendWarning(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.sendWarning(id));
    }

    // ✅ Get Notifications for a User (GET)
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    // ✅ Mark a Single Notification as Read (PUT)
    @PutMapping("/{notificationId}")
    public ResponseEntity<Notification> markAsRead(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    // ✅ Mark All Notifications for a User as Read (PUT)
    @PutMapping("/mark-all/{userId}")
    public ResponseEntity<String> markAllAsRead(@PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read.");
    }
}
