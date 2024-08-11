package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.GlobalNotificationSettings;
import com.suryaenergi.sdm.backendapi.entity.Notification;
import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import com.suryaenergi.sdm.backendapi.repository.AntrianemailRepository;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.repository.GlobalNotificationRepository;
import com.suryaenergi.sdm.backendapi.repository.NotificationRepository;
import com.suryaenergi.sdm.backendapi.request.GlobalNotificationUpdateRequest;
import com.suryaenergi.sdm.backendapi.response.GlobalNotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AntrianemailRepository antrianemailRepository;

    @Autowired
    private GlobalNotificationRepository globalNotificationRepository;

    public void pushNotification(NotificationData notification, Employee employee) {
        Notification data = Notification.fromData(notification, employee.getEmployeeCode());
        notificationRepository.save(data);
//        String email = employee.getEmail();
//        if (email == null || email.isEmpty()) {
//            return;
//        }
//        Antrianemail antrianemail = new Antrianemail();
//        antrianemail.setEmail(email);
//        antrianemail.setSubject(notification.getData().getSubject());
//        antrianemail.setContent(notification.getData().getFormattedMessage());
    }

    public List<NotificationData.NotificationDto> getNotifications(String empCode, Long after) {
        if (after == null) {
            after = 0L;
        }
        List<Notification> notifications = notificationRepository.findByEmpCodeAndAfter(empCode, after);
        return notifications.stream()
                .map(NotificationData::fromEntity)
                .filter(Objects::nonNull)
                .map(NotificationData::toDto)
                .toList();
    }

    public List<NotificationData.NotificationDto> getNotificationsBefore(String empCode, Long before) {
        List<Notification> notifications = before == null ? notificationRepository.findByEmpCode(empCode) : notificationRepository.findByEmpCodeAndBefore(empCode, before);
        return notifications.stream()
                .map(NotificationData::fromEntity)
                .filter(Objects::nonNull)
                .map(NotificationData::toDto)
                .toList();
    }

    public List<NotificationData.NotificationDto> getUnreadNotifications(String empCode) {
        Employee employee = employeeRepository.findByEmployeeCode(empCode);
        if (employee == null) {
            return List.of();
        }
        List<Notification> notifications = notificationRepository.findUnreadNotifications(empCode);
        return notifications.stream()
                .map(NotificationData::fromEntity)
                .filter(Objects::nonNull)
                .map(NotificationData::toDto)
                .toList();
    }

    public void markAsRead(String empCode, Long id) {
        if (id == null) {
            // read all
            notificationRepository.markAllAsRead(empCode);
            return;
        }
        Notification notification = notificationRepository.findFirstByIdAndEmpCode(id, empCode);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            throw new RuntimeException("NOTIFICATION_NOT_FOUND");
        }
    }

    public void updateNotificationSettings(GlobalNotificationUpdateRequest request) {
        Employee employee = employeeRepository.findByEmployeeCode(request.getEmployeeCode());
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        GlobalNotificationSettings existingSettings = globalNotificationRepository.findById(employee.getId()).orElse(null);
        boolean createData = false;
        if (existingSettings == null) {
            existingSettings = new GlobalNotificationSettings();
            existingSettings.setEmployee(employee);
            existingSettings.setId(employee.getId());
            createData = true;
        }
        Boolean receiveEventNotification = request.getReceiveEventNotification();
        if (receiveEventNotification != null) {
            existingSettings.setReceiveEventNotification(receiveEventNotification);
        }
        if (createData) {
            if (existingSettings.isDefault()) {
                return;
            }
            globalNotificationRepository.save(existingSettings);
        } else {
            if (existingSettings.isDefault()) {
                globalNotificationRepository.delete(existingSettings);
            } else {
                globalNotificationRepository.save(existingSettings);
            }
        }
    }

    public GlobalNotificationResponse getSettingsByEmployee(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        GlobalNotificationSettings settings = globalNotificationRepository.findById(employee.getId()).orElse(null);
        GlobalNotificationResponse response = new GlobalNotificationResponse();
        response.setMessage("SUCCESS");
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setEmployeeName(employee.getFullName());
        response.setJobTitle(employee.getJobTitle());
        response.setReceiveEventNotification(settings != null && settings.isReceiveEventNotification());
        return response;
    }

    public List<GlobalNotificationResponse> getAllSettings() {
        Iterable<GlobalNotificationSettings> allSettings = globalNotificationRepository.findAll();
        List<GlobalNotificationResponse> responses = new ArrayList<>();
        for (GlobalNotificationSettings settings : allSettings) {
            GlobalNotificationResponse response = new GlobalNotificationResponse();
            response.setEmployeeCode(settings.getEmployee().getEmployeeCode());
            response.setEmployeeName(settings.getEmployee().getFullName());
            response.setJobTitle(settings.getEmployee().getJobTitle());
            response.setReceiveEventNotification(settings.isReceiveEventNotification());
            responses.add(response);
        }
        return responses;
    }
}
