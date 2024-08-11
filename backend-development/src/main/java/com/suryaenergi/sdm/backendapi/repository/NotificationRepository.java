package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    @Query(value = "SELECT * FROM notification WHERE emp_code = :empCode AND id > :after ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Notification> findByEmpCodeAndAfter(String empCode, Long after);

    @Query(value = "SELECT * FROM notification WHERE emp_code = :empCode AND id < :before ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Notification> findByEmpCodeAndBefore(String empCode, Long before);

    @Query(value = "SELECT * FROM notification WHERE emp_code = :empCode ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Notification> findByEmpCode(String empCode);

    Notification findFirstByIdAndEmpCode(Long id, String empCode);

    @Query(value = "SELECT * FROM notification WHERE emp_code = :empCode AND is_read = 0 ORDER BY id DESC LIMIT 100", nativeQuery = true)
    List<Notification> findUnreadNotifications(String empCode);

    @Query("update Notification n set n.isRead = true where n.empCode = ?1 and n.isRead = false")
    void markAllAsRead(String empCode);
}
