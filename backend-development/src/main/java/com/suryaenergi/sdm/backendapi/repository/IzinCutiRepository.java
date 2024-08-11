package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.IzinCuti;
import com.suryaenergi.sdm.backendapi.pojo.IzinCutiAccumulation;
import com.suryaenergi.sdm.backendapi.pojo.JenisIzinCutiData;
import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IzinCutiRepository extends CrudRepository<IzinCuti, Long> {
    IzinCuti findIzinCutiById(Long id);

//    @Query(value = "SELECT * FROM izin_cuti WHERE emp_code = :empCode AND id > :after ORDER BY id DESC LIMIT 10", nativeQuery = true)
    @Query(value = "SELECT * FROM izin_cuti WHERE emp_code = :empCode AND id < :after AND status IN :status AND created_at >= :start AND created_at <= :end ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<IzinCuti> findByEmpCode(String empCode, Long after, List<IzinCutiDetailResponse.Status> status, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * FROM izin_cuti WHERE emp_code = :empCode AND status IN :status AND created_at >= :start AND created_at <= :end ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<IzinCuti> findByEmpCode(String empCode, List<IzinCutiDetailResponse.Status> status, LocalDateTime start, LocalDateTime end);

    // find all with status APPROVED
//    List<IzinCuti> findAllByStatusAndEmpcode(IzinCutiDetailResponse.Status status, String empCode);
    // find first with status
    @Query(value = "SELECT * FROM izin_cuti WHERE status = :status AND emp_code = :empCode AND start_date <= :now AND end_date >= :now ORDER BY id DESC LIMIT 1", nativeQuery = true)
    IzinCuti findFirstByStatusAndEmpcode(IzinCutiDetailResponse.Status status, String empCode, LocalDateTime now);

    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, izin_cuti.start_date, izin_cuti.end_date)) AS akumulasiMenit FROM izin_cuti LEFT JOIN jenis_izin_cuti ON izin_cuti.jenis_cuti = jenis_izin_cuti.id WHERE jenis_izin_cuti.cut_cuti = 1 AND izin_cuti.emp_code = :empCode AND YEAR(izin_cuti.start_date) = :year", nativeQuery = true)
    IzinCutiAccumulation accumulateCuti(String empCode, int year);

//    @Query(value = "SELECT SUM(TIMESTAMPDIFF(MINUTE, izin_cuti.start_date, izin_cuti.end_date)) AS akumulasiMenit FROM izin_cuti LEFT JOIN jenis_izin_cuti ON izin_cuti.jenis_cuti = jenis_izin_cuti.id WHERE jenis_izin_cuti.cut_cuti = 1 AND izin_cuti.emp_code = :empCode AND YEAR(izin_cuti.start_date) = :year AND jenis_izin_cuti.pengajuan = 1", nativeQuery = true)
//    IzinCutiAccumulation accumulateCutiOnlyIzin(String empCode, int year);
    @Query("select sum(TIMESTAMPDIFF(MINUTE, i.startDate, i.endDate)) from IzinCuti i left join JenisIzinCuti j on i.jenisCuti = j.id where j.cutCuti = true and i.empcode = ?1 and year(i.startDate) = ?2 and month(i.startDate) = ?3 and j.pengajuan = ?4 and i.status IN ?5")
    Long accumulateCutiOnlyIzin(String empCode, int year, int month, JenisIzinCutiData.TipePengajuan tipePengajuan, List<IzinCutiDetailResponse.Status> statuses, LocalDateTime now);

    @Query(value = "SELECT izin_cuti.* FROM izin_cuti LEFT JOIN jenis_izin_cuti ON jenis_izin_cuti.id = izin_cuti.jenis_cuti WHERE izin_cuti.emp_code = :empCode AND YEAR(izin_cuti.start_date) = :year AND izin_cuti.status = :status AND izin_cuti.start_date <= :now AND jenis_izin_cuti.cut_cuti = 1", nativeQuery = true)
    List<IzinCuti> findAllCutiAktif(String empCode, int year, IzinCutiDetailResponse.Status status, LocalDateTime now);

    @Query("SELECT i FROM IzinCuti i LEFT JOIN IzinCutiApproval a ON i.id = a.izinCutiId WHERE a.reviewerEmpCode = ?1 AND i.id < ?2 AND i.status IN ?3 AND i.createdAt >= ?4 AND i.createdAt <= ?5 ORDER BY i.id DESC LIMIT 10")
    List<IzinCuti> findAllReviewedByEmpCode(String empCode, Long after, List<IzinCutiDetailResponse.Status> statuses, LocalDateTime start, LocalDateTime end);

    @Query("SELECT i FROM IzinCuti i LEFT JOIN IzinCutiApproval a ON i.id = a.izinCutiId WHERE a.reviewerEmpCode = ?1 AND i.status IN ?2 AND i.createdAt >= ?3 AND i.createdAt <= ?4 ORDER BY i.id DESC LIMIT 10")
    List<IzinCuti> findAllReviewedByEmpCode(String empCode, List<IzinCutiDetailResponse.Status> statuses, LocalDateTime start, LocalDateTime end);

//    @Query("SELECT COUNT(i) FROM IzinCuti i WHERE i.empcode = ?1 AND i.status IN ?4" +
//            " AND ((i.startDate <= ?2 AND i.endDate >= ?2) OR (i.startDate <= ?3 AND i.endDate >= ?3) OR (i.startDate >= ?2 AND i.endDate <= ?3))")
//    // contoh kasus overlap:
//    // cuti: 1-3, ajuan: 2-4 condition: (1 <= 2 AND 3 >= 2) OR (1 <= 4 AND 3 >= 4) OR (1 >= 2 AND 3 <= 4) = TRUE (kondisi pertama terpenuhi)
//    // cuti: 2-4, ajuan 1-3 condition: (2 <= 1 AND 4 >= 1) OR (2 <= 3 AND 4 >= 3) OR (2 >= 1 AND 4 <= 3) = TRUE (kondisi kedua terpenuhi)
//    // cuti: 1-3, ajuan 1-2 condition: (1 <= 1 AND 3 >= 1) OR (1 <= 2 AND 3 >= 2) OR (1 >= 1 AND 3 <= 2) = TRUE (kondisi pertama terpenuhi)
//    // cuti: 2-4, ajuan 1-5 condition: (2 <= 1 AND 4 >= 1) OR (2 <= 5 AND 4 >= 5) OR (2 >= 1 AND 4 <= 5) = TRUE (kondisi ketiga terpenuhi)
//    // cuti: 1-2, ajuan 3-4 condition: (1 <= 3 AND 2 >= 3) OR (1 <= 4 AND 2 >= 4) OR (1 >= 3 AND 2 <= 4) = FALSE (tidak ada kondisi yang terpenuhi)
//    // cuti: 3-4, ajuan 1-2 condition: (3 <= 1 AND 4 >= 1) OR (3 <= 2 AND 4 >= 2) OR (3 >= 1 AND 4 <= 2) = FALSE (tidak ada kondisi yang terpenuhi)
//    int countOverlappingCuti(String empCode, LocalDateTime start, LocalDateTime end, List<IzinCutiDetailResponse.Status> statuses);

    List<IzinCuti> findAllByEmpcode(String empCode);

    @Query("select j.izinCuti from IzinCuti i left join JenisIzinCuti j on i.jenisCuti = j.id where i.id = ?1 order by i.id desc limit 1")
    String izinCutiJenisName(long id);
}
