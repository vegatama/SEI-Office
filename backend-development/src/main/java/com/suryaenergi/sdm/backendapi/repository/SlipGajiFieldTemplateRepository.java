package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.SlipGajiFieldTemplate;
import com.suryaenergi.sdm.backendapi.entity.SlipGajiTemplate;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiDataEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SlipGajiFieldTemplateRepository extends CrudRepository<SlipGajiFieldTemplate, Long> {
    List<SlipGajiFieldTemplate> findAllByTemplate(Long template);
}
