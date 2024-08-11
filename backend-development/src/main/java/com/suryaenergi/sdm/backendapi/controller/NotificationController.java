package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import com.suryaenergi.sdm.backendapi.request.GlobalNotificationUpdateRequest;
import com.suryaenergi.sdm.backendapi.response.GlobalNotificationListResponse;
import com.suryaenergi.sdm.backendapi.response.GlobalNotificationResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.NotificationListResponse;
import com.suryaenergi.sdm.backendapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/settings/all")
    public GlobalNotificationListResponse getAllSettings() {
        try {
            List<GlobalNotificationResponse> settings = notificationService.getAllSettings();
            return new GlobalNotificationListResponse("SUCCESS", settings);
        } catch (Throwable e) {
            e.printStackTrace();
            return new GlobalNotificationListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/settings/{empCode}")
    public GlobalNotificationResponse getSettings(@PathVariable String empCode) {
        try {
            GlobalNotificationResponse settings = notificationService.getSettingsByEmployee(empCode);
            return settings;
        } catch (Throwable e) {
            e.printStackTrace();
            return new GlobalNotificationResponse(e.toString(), "", "", "", false);
        }
    }

    @PostMapping("/settings/update")
    public MessageResponse updateSettings(@RequestBody GlobalNotificationUpdateRequest request) {
        try {
            notificationService.updateNotificationSettings(request);
            return new MessageResponse("SUCCESS");
        } catch (Throwable e) {
            e.printStackTrace();
            return new MessageResponse(e.toString());
        }
    }

    @DeleteMapping("/settings/{empCode}")
    public MessageResponse deleteSettings(@PathVariable String empCode) {
        try {
            notificationService.updateNotificationSettings(new GlobalNotificationUpdateRequest(empCode, false));
            return new MessageResponse("SUCCESS");
        } catch (Throwable e) {
            e.printStackTrace();
            return new MessageResponse(e.toString());
        }
    }

    @GetMapping("/list")
    public NotificationListResponse listNotifications(@RequestParam String empCode, @RequestParam(required = false) Long after) {
        try {
            List<NotificationData.NotificationDto> notifications = notificationService.getNotifications(empCode, after);
            return new NotificationListResponse("SUCCESS", notifications);
        } catch (Throwable e) {
            e.printStackTrace();
            return new NotificationListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/history")
    public NotificationListResponse historyNotifications(@RequestParam String empCode, @RequestParam(required = false) Long before) {
        try {
            List<NotificationData.NotificationDto> notifications = notificationService.getNotificationsBefore(empCode, before);
            return new NotificationListResponse("SUCCESS", notifications);
        } catch (Throwable e) {
            e.printStackTrace();
            return new NotificationListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/unread")
    public NotificationListResponse unreadNotifications(@RequestParam String empCode) {
        try {
            List<NotificationData.NotificationDto> notifications = notificationService.getUnreadNotifications(empCode);
            return new NotificationListResponse("SUCCESS", notifications);
        } catch (Throwable e) {
            e.printStackTrace();
            return new NotificationListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/read")
    public MessageResponse markAsRead(@RequestParam String empCode, @RequestParam(required = false) Long id) {
        try {
            notificationService.markAsRead(empCode, id);
            return new MessageResponse("SUCCESS");
        } catch (Throwable e) {
            e.printStackTrace();
            return new MessageResponse(e.toString());
        }
    }
}
