package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.PONav;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PonavRepository extends CrudRepository<PONav, Long> {
    List<PONav> findAllByPrNoAndPurchaseNo(String documentNo, String itemNo);

    List<PONav> findAllByOrderByDocumentDateDesc();
}
