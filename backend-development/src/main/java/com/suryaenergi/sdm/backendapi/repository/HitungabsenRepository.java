package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Hitungabsen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HitungabsenRepository extends CrudRepository<Hitungabsen, Long> {
    List<Hitungabsen> findByTahunAndBulan(int year, int month);

    Hitungabsen findByTahunAndBulanAndEmployeeCode(int year, int month, String employee_code);

    Hitungabsen findByEmployeeCodeAndTahunAndBulan(String employeeCode, int year, int month);

    Hitungabsen findByViewId(String viewId);
}
