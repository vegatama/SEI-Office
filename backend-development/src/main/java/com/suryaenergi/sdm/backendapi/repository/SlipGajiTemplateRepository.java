package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.SlipGajiFieldTemplate;
import com.suryaenergi.sdm.backendapi.entity.SlipGajiTemplate;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiDataEntry;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiItemData;
import org.springframework.data.repository.CrudRepository;

public interface SlipGajiTemplateRepository extends CrudRepository<SlipGajiTemplate, Long> {
    SlipGajiTemplate findFirstByBulanAndTahun(Integer bulan, Integer tahun);
}
