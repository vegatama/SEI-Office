package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Dpbheadnav;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DpbheadRepository extends CrudRepository<Dpbheadnav, Long> {
    List<Dpbheadnav> findByProjectCode(String kdproyek);

    Dpbheadnav findByDocumentNo(String nodpb);

    Dpbheadnav findFirstByDocumentNo(String nodpb);
}
