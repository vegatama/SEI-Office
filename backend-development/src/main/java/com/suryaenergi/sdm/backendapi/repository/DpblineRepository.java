package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Dpblinenav;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DpblineRepository extends CrudRepository<Dpblinenav, Long> {
    List<Dpblinenav> findAllByDocumentNo(String nodpb);
}
