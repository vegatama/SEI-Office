package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Dokumen;
import org.springframework.data.repository.CrudRepository;

public interface DokumenRepository extends CrudRepository<Dokumen, Long> {
    Dokumen findByDokumenId(String dokid);
}
