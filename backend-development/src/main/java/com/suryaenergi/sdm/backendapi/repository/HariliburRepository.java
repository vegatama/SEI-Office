package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.HariLibur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

public interface HariliburRepository extends PagingAndSortingRepository<HariLibur, Long>, CrudRepository<HariLibur, Long> {
    HariLibur findByTanggal(LocalDate tanggalCek);

    HariLibur findByHariLiburId(String hariLiburId);

    @Query(value = "SELECT * FROM hari_libur h WHERE YEAR(h.tanggal) = :tahun", nativeQuery = true)
    List<HariLibur> findAllByTahun(int tahun);
    HariLibur findFirstByTanggal(LocalDate tanggal);
}
