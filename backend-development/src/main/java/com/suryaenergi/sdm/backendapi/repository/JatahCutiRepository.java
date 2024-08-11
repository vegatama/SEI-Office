package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.pojo.JatahCutiGroup;
import com.suryaenergi.sdm.backendapi.pojo.JatahCutiSimpleGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.suryaenergi.sdm.backendapi.entity.JatahCuti;

import java.util.List;
import java.util.Optional;

public interface JatahCutiRepository extends CrudRepository<JatahCuti, Long> {

//    @Query(value = "SELECT SUM(jumlah_cuti) as jumlah FROM jatah_cuti WHERE employee_id = :employeeId AND tahun = :tahun ", nativeQuery = true, countName = "jumlah")
//    Optional<Integer> sumJatahCutiByEmployeeIdAndTahun(String employeeId, int tahun);
    // non-native query
    @Query(value = "SELECT SUM(j.jumlahCuti) as jumlah FROM JatahCuti j WHERE j.employeeId = ?1 AND j.tahun = ?2")
    Optional<Long> sumJatahCutiByEmployeeIdAndTahun(String employeeId, int tahun);

    @Query(value = "SELECT * FROM jatah_cuti WHERE employee_id = :employeeId AND tahun = :tahun AND id < :after ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<JatahCuti> findJatahCutiByEmployeeIdAndTahunAfter(String employeeId, int tahun, Long after);

    @Query(value = "SELECT * FROM jatah_cuti WHERE employee_id = :employeeId AND tahun = :tahun ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<JatahCuti> findJatahCutiByEmployeeIdAndTahunAfter(String employeeId, int tahun);

//    @Query(value = "SELECT SUM(jumlah_cuti) AS jumlah_cuti, employee_id, tahun FROM jatah_cuti GROUP BY employee_id, tahun", nativeQuery = true)
    @Query(value = "select new com.suryaenergi.sdm.backendapi.pojo.JatahCutiGroup(SUM(j.jumlahCuti), j.employeeId, j.tahun) from JatahCuti j group by j.employeeId, j.tahun")
    List<JatahCutiGroup> sumGroupByEmployeeIdAndTahun();

    @Query(value = "select new com.suryaenergi.sdm.backendapi.pojo.JatahCutiGroup(SUM(j.jumlahCuti), j.employeeId, j.tahun) from JatahCuti j where j.tahun = ?1 group by j.employeeId")
    List<JatahCutiGroup> sumGroupByEmployeeIdAndTahun(int tahun);

//    @Query(value = "SELECT SUM(jumlah_cuti) AS jumlah_cuti, tahun FROM jatah_cuti WHERE employee_id = :employee_id GROUP BY tahun", nativeQuery = true)
    @Query(value = "select new com.suryaenergi.sdm.backendapi.pojo.JatahCutiSimpleGroup(SUM(j.jumlahCuti), j.tahun) from JatahCuti j where j.employeeId = :employee_id group by j.tahun")
    List<JatahCutiSimpleGroup> sumGroupByEmployeeIdAndTahun(String employee_id);

//    @Query(value = "SELECT SUM(jumlah_cuti) AS jumlah_cuti, tahun FROM jatah_cuti WHERE employee_id = :employee_id AND tahun = :tahun", nativeQuery = true)
    @Query(value = "select new com.suryaenergi.sdm.backendapi.pojo.JatahCutiSimpleGroup(SUM(j.jumlahCuti), j.tahun) from JatahCuti j where j.employeeId = :employee_id and j.tahun = :tahun")
    JatahCutiSimpleGroup sumGroupByEmployeeIdAndTahun(String employee_id, int tahun);

}
