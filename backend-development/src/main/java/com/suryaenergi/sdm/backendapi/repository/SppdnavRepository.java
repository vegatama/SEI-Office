package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Sppdnav;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface SppdnavRepository extends CrudRepository<Sppdnav, Long> {
    Sppdnav findByEmployeeCodeAndWaktuSPPD(String employeeNo, Date tmpdate);
}
