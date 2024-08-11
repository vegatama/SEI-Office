package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.IzinCutiApproval;
import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface IzinCutiApprovalRepository extends CrudRepository<IzinCutiApproval, Long> {
    List<IzinCutiApproval> findAllByIzinCutiId(Long izinCutiId);

    IzinCutiApproval findFirstByIzinCutiIdAndReviewerEmpCodeAndStatusIn(Long izinCutiId, String reviewerEmpCode, Collection<IzinCutiDetailResponse.Status> status);

//    @Query(value =
//    "SELECT izin_cuti.* FROM izin_cuti_approval LEFT JOIN izin_cuti ON izin_cuti.id = izin_cuti_approval.izin_cuti_id WHERE izin_cuti_approval.emp_code = :empCode AND izin_cuti.id < :after AND izin_cuti.start_date >= start AND izin_cuti.end_date <= end GROUP BY izin_cuti.id ORDER BY izin_cuti.id DESC LIMIT 10", nativeQuery = true)
//    List<IzinCuti> findReviewedByEmpCode(String empCode, Long after, List<IzinCutiDetailResponse.Status> statuses, LocalDateTime start, LocalDateTime end);
}
