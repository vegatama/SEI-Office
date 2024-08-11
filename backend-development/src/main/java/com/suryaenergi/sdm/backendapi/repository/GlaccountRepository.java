package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Glaccountnav;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GlaccountRepository extends CrudRepository<Glaccountnav, Long> {
    Glaccountnav findByGlNo(String finalI);

    List<Glaccountnav> findByAccountType(String s);
}
