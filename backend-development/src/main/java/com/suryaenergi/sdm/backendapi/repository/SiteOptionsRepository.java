package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.SiteOptions;
import org.springframework.data.repository.CrudRepository;

public interface SiteOptionsRepository extends CrudRepository<SiteOptions, Long> {
    SiteOptions findByName(String maintenanceMode);
}
