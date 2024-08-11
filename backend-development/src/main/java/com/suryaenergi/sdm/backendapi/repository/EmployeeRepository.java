package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.controller.EmployeeController;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findByEmail(String email);

    Employee findByEmployeeId(String employeeId);

    Employee findByEmployeeCode(String employee_code);

    Employee findByFullName(String fullName);


    List<Employee> findAllByAtasanUserId(String atasanUserId);

    Employee findByNik(String nik);

    List<Employee> findAllByStatus(String karyawan_tetap);

    Page<Employee> findByStatusOrStatusOrStatusOrStatus(String s, String karyawan_tetap, String s1, String s2, Pageable paging);

    List<Employee> findAllByStatusOrStatusOrStatus(String s, String karyawan_tetap, String s1);

    List<Employee> findAllByStatusOrStatusOrStatusOrStatus(String s, String karyawan_tetap, String s1, String s2);

    List<Employee> findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja(String s, String s1, String karyawan_tetap, String s2, String s3, String s4, String s5, String s6);

    Page<Employee> findByStatusOrStatusOrStatusOrStatusOrderByFullNameAsc(String s, String karyawan_tetap, String s1, String s2, Pageable paging);

    Optional<Employee> findByEmailAndIsActive(String username, boolean b);

    Employee findFirstByUserIdNav(String user);
}
