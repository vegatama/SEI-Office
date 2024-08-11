package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Budgetnav;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BudgetnavRepository extends CrudRepository<Budgetnav, Long> {
    List<Budgetnav> findByProjectCode(String kdproyek);
}
