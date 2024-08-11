package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.PendingSwipeRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface PendingSwipeRequestRepository extends CrudRepository<PendingSwipeRequest, Long> {
    PendingSwipeRequest findFirstByEmployeeCodeAndDateAndCheckout(String employeeCode, LocalDate date, boolean checkout);
    // FIND LAST BY EMPLOYEE CODE AND DATE AND CHECKOUT
    PendingSwipeRequest findFirstByEmployeeCodeAndDateAndCheckoutOrderByDateTimeDesc(String employeeCode, LocalDate date, boolean checkout);
    @Query(value = "SELECT * FROM pending_swipe_request WHERE employee_code = :employeeCode AND date = :date AND approved = 0", nativeQuery = true)
    PendingSwipeRequest findFirstByEmployeeCodeAndDateAndNotApproved(String employeeCode, LocalDate date);
//    @Query(value = "SELECT * FROM pending_swipe_request WHERE ASSIGNED_REVIEWER_EMPLOYEE_CODE = :employeeCode AND approved IN :statuses AND id < :id ORDER BY id DESC LIMIT 10", nativeQuery = true)
    @Query("select p from PendingSwipeRequest p where p.assignedReviewerEmployeeCode = ?1 and p.approved in ?2 and p.id < ?3 order by p.id desc limit 10")
    List<PendingSwipeRequest> findAllOnlyUnapproved(String employeeCode, Long id, List<Integer> statuses);

    @Query("select p from PendingSwipeRequest p where p.assignedReviewerEmployeeCode = ?1 and p.approved in ?2 order by p.id desc limit 10")
    List<PendingSwipeRequest> findAllOnlyUnapproved(String employeeCode, List<Integer> statuses);

    // ascending

    // without id (first page)
    @Query(value = "SELECT * FROM pending_swipe_request WHERE ASSIGNED_REVIEWER_EMPLOYEE_CODE = :employeeCode ORDER BY id ASC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllLimitedAscending(String employeeCode);

    // without id (first page)
    @Query(value = "SELECT * FROM pending_swipe_request WHERE ASSIGNED_REVIEWER_EMPLOYEE_CODE = :employeeCode AND approved = 0 ORDER BY id ASC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllOnlyUnapprovedAscending(String employeeCode);

    @Query(value = "SELECT * FROM pending_swipe_request WHERE EMPLOYEE_CODE = :employeeCode AND id < :id ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllLimitedSelf(String employeeCode, Long id);

    @Query(value = "SELECT * FROM pending_swipe_request WHERE EMPLOYEE_CODE = :employeeCode AND approved = 0 AND id < :id ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllOnlyUnapprovedSelf(String employeeCode, Long id);

    // without id (first page)
    @Query(value = "SELECT * FROM pending_swipe_request WHERE EMPLOYEE_CODE = :employeeCode ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllLimitedSelf(String employeeCode);

    // without id (first page)
    @Query(value = "SELECT * FROM pending_swipe_request WHERE EMPLOYEE_CODE = :employeeCode AND approved = 0 ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllOnlyUnapprovedSelf(String employeeCode);

    // find all by employeecode and from id
    @Query(value = "SELECT * FROM pending_swipe_request WHERE employee_code = :employeeCode AND id < :id ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<PendingSwipeRequest> findAllByEmployeeCodeAndId(String employeeCode, Long id);

    Iterable<? extends PendingSwipeRequest> findAllByAssignedReviewerEmployeeCode(String employeeCode);
}
