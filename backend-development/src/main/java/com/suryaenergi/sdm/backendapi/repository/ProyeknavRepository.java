package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Proyeknav;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProyeknavRepository extends CrudRepository<Proyeknav,Long>, PagingAndSortingRepository<Proyeknav, Long> {
    Proyeknav findByProjectCode(String project_code);

    List<Proyeknav> findAllByStatus(String status);

    @Query(value = "select * from proyek p where p.project_type = :tipe AND YEAR(p.tanggal_mulai) = :tahun AND status = 'Open'", nativeQuery = true)
    List<Proyeknav> findAllByProjectTypeAndTahun(String tipe, int tahun);
}
