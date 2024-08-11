package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.IzinCutiFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IzinCutiFileRepository extends CrudRepository<IzinCutiFile, Long> {
    List<IzinCutiFile> findAllByIzinCutiId(Long izinCutiId);
}
