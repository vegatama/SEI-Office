package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Rekapabsen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RekapabsenRepository extends CrudRepository<Rekapabsen, Long> {
    Rekapabsen findByEmployeeCodeAndTanggal(String employeCode, LocalDate tanggalCek);

    // join table rekap_absen and employee first and find its atasan id, and then find all rekapabsen with atasan id
    // with limit date from and to
    @Query(value = "SELECT * FROM rekap_absen r WHERE r.employee_code IN (SELECT e.employee_code FROM employee e WHERE e.atasan_user_id = :atasanId) AND r.tanggal BETWEEN :from AND :to AND r.request_checkin = :requestCheckIn", nativeQuery = true)
    List<Rekapabsen> findByAtasanUserIdAndTanggalBetween(@Param("atasanId") String atasanId, @Param("from") LocalDate from, @Param("to") LocalDate to, @Param("requestCheckIn") boolean requestCheckIn);

    // join table rekap_absen and employee first and find its atasan id, and then find all rekapabsen with atasan id
    // with pagination using last id, and sort it by newest first
    @Query(value = "SELECT * FROM rekap_absen r WHERE r.employee_code IN (SELECT e.employee_code FROM employee e WHERE e.atasan_user_id = :atasanId) AND r.id < :lastId AND r.request_checkin = :requestCheckIn ORDER BY r.id DESC LIMIT :limit", nativeQuery = true)
    List<Rekapabsen> findByAtasanUserIdAndIdLessThanOrderByTanggalDesc(@Param("atasanId") String atasanId, @Param("lastId") Long lastId, @Param("limit") int limit, @Param("requestCheckIn") boolean requestCheckIn);

    // same as above, find the first page
    @Query(value = "SELECT * FROM rekap_absen r WHERE r.employee_code IN (SELECT e.employee_code FROM employee e WHERE e.atasan_user_id = :atasanId) AND r.request_checkin = :requestCheckIn ORDER BY r.id DESC LIMIT :limit", nativeQuery = true)
    List<Rekapabsen> findByAtasanUserIdOrderByTanggalDesc(@Param("atasanId") String atasanId, @Param("limit") int limit, @Param("requestCheckIn") boolean requestCheckIn);

    // find by employee code and date from and to,
    // also filter by attendanceState (String array)
    @Query(value = "SELECT * FROM rekap_absen r WHERE r.employee_code = :employeeCode AND r.tanggal BETWEEN :from AND :to AND r.attendance_state IN :attendanceState", nativeQuery = true)
    List<Rekapabsen> findByEmployeeCodeAndTanggalBetweenAndAttendanceStateIn(@Param("employeeCode") String employeeCode, @Param("from") LocalDate from, @Param("to") LocalDate to, @Param("attendanceState") List<String> attendanceState);

    // find by employee code and date from and to
    @Query(value = "SELECT * FROM rekap_absen r WHERE r.employee_code = :employeeCode AND r.tanggal BETWEEN :from AND :to", nativeQuery = true)
    List<Rekapabsen> findByEmployeeCodeAndTanggalBetween(@Param("employeeCode") String employeeCode, @Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = "select * from rekap_absen r where r.employee_code = :emp AND MONTH(r.tanggal) = :month AND YEAR(r.tanggal) = :year", nativeQuery = true)
    List<Rekapabsen> findByYearAndMonthAndEmployeeCode(@Param("year")int year, @Param("month")int month, @Param("emp")String emp);

    @Query(value = "select * from rekap_absen r where r.employee_code = :emp AND MONTH(r.tanggal) = :month AND YEAR(r.tanggal) = :year AND DAY(r.tanggal) = :date", nativeQuery = true)
    List<Rekapabsen> findByYearAndMonthAndDateAndEmployeeCode(@Param("year")int year, @Param("month")int month, @Param("date")int date, @Param("emp")String emp);

    @Query(value = "select * from rekap_absen r where r.employee_code IN :emps AND MONTH(r.tanggal) = :month AND YEAR(r.tanggal) = :year", nativeQuery = true)
    List<Rekapabsen> findByYearAndMonthAndEmployeeCodeIn(@Param("year")int year, @Param("month")int month, @Param("emps")List<String> emps);

    @Query(value = "select * from rekap_absen r where r.employee_code IN :emps AND MONTH(r.tanggal) = :month AND YEAR(r.tanggal) = :year AND DAY(r.tanggal) = :date", nativeQuery = true)
    List<Rekapabsen> findByYearAndMonthAndDateAndEmployeeCodeIn(@Param("year")int year, @Param("month")int month, @Param("date")int date, @Param("emps")List<String> emps);
}
