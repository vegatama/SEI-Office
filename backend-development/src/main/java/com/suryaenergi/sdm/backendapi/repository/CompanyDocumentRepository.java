package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.CompanyDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDocumentRepository extends JpaRepository<CompanyDocument, Long> {
    // paged
    Page<CompanyDocument> findAllBy(Pageable pageable);
    Page<CompanyDocument> findAllByUploaderEmployeeCode(String uploaderEmployeeCode, Pageable pageable);
    List<CompanyDocument> findAllByUploaderEmployeeCode(String uploaderEmployeeCode);
}
