package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Jamkerja;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface JamkerjaRepository extends CrudRepository<Jamkerja, Long> {
    Jamkerja findByJamKerjaId(String jamKerjaId);
    Jamkerja findFirstByTanggal(LocalDate tanggal);
}
