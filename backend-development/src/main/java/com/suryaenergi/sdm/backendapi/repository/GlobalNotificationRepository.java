package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.GlobalNotificationSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GlobalNotificationRepository extends CrudRepository<GlobalNotificationSettings, Long> {
    List<GlobalNotificationSettings> findAllByReceiveEventNotification(boolean b);
}
