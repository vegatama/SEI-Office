package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Ketidakhadiran;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface KetidakhadiranRepository extends CrudRepository<Ketidakhadiran, Long> {
    Ketidakhadiran findByEmployeeCodeAndTanggal(String employeeNo, LocalDate tmpdate);

    @Query(value = "select * from ketidakhadiran k where k.employee_code = :emp AND MONTH(k.tanggal) = :month AND YEAR(k.tanggal) = :year AND k.cause_of_absence = 'CUTI'", nativeQuery = true)
    List<Ketidakhadiran> findAllCutiByEmployeeCodeYearMonth(String emp, int year, int month);

    void deleteByEmployeeCodeAndTanggal(String empcode, LocalDate date);

    void deleteByEmployeeCodeAndTanggalAndFromTimeAndToTime(String empcode, LocalDate localDate, LocalTime localTime, LocalTime localTime1);

    @Query("delete from Ketidakhadiran k where k.idIzinCuti = :izinCutiId")
    void deleteAllByIdIzincuti(long izinCutiId);
}
