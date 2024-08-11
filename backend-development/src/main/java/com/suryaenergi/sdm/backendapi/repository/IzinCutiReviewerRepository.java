package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.IzinCutiReviewer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IzinCutiReviewerRepository extends CrudRepository<IzinCutiReviewer, Long> {
    IzinCutiReviewer findIzinCutiReviewerById(Long id);

    IzinCutiReviewer findFirstByTipeIzinCutiIdAndEmpCode(Long tipeIzinCutiId, String empCode);

    void deleteByTipeIzinCutiIdAndEmpCode(Long tipeIzinCutiId, String empCode);

    List<IzinCutiReviewer> findByTipeIzinCutiId(Long tipeIzinCutiId);
}
