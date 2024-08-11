package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Departemen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartemenRepository extends CrudRepository<Departemen, Long>, PagingAndSortingRepository<Departemen, Long> {
    Departemen findByDepartemenId(String departemenId);
}
