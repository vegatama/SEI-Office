package com.suryaenergi.sdm.backendapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.suryaenergi.sdm.backendapi.entity.LokasiAbsen;

public interface LokasiAbsenRepository extends CrudRepository<LokasiAbsen, Long>{
    LokasiAbsen findLokasiById(Long id);

    LokasiAbsen findFirstByIsDefault(boolean b);
}
