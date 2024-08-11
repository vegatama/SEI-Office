package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.SlipGajiEntryData;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface SlipGajiEntryDataRepository extends CrudRepository<SlipGajiEntryData, Long> {
    // find by its template, by joining the SlipGajiFieldTemplate entity and the SlipGajiTemplate entity
    List<SlipGajiEntryData> findAllBySlipFieldIsIn(Collection<Long> slipField);
    // delete by its template, by joining the SlipGajiFieldTemplate entity and the SlipGajiTemplate entity

    void deleteAllBySlipFieldIsIn(Collection<Long> slipField);

    SlipGajiEntryData findFirstByEmployeeAndSlipField(String employee, Long slipField);

    List<SlipGajiEntryData> findAllByEmployeeAndSlipFieldIsIn(String employee, Collection<Long> slipField);
}
