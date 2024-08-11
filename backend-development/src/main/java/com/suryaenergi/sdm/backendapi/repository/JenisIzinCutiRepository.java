package com.suryaenergi.sdm.backendapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.suryaenergi.sdm.backendapi.entity.JenisIzinCuti;

public interface JenisIzinCutiRepository extends CrudRepository<JenisIzinCuti, Long>{
    
    JenisIzinCuti findIzinCutiById(Long id);
}
