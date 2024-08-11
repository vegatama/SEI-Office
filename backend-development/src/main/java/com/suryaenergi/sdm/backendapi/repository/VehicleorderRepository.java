package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.VehicleOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleorderRepository extends CrudRepository<VehicleOrder, Long> {
    @Query(value = "select * from vehicle_order v where MONTH(v.waktu_berangkat) = :month AND YEAR(v.waktu_berangkat) = :year", nativeQuery = true)
    List<VehicleOrder> findAllByMonth(int year, int month);

    @Query(value = "select * from vehicle_order v where DAY(v.waktu_berangkat) = :tgl AND MONTH(v.waktu_berangkat) = :bln AND YEAR(v.waktu_berangkat) = :thn", nativeQuery = true)
    List<VehicleOrder> findAllByTanggal(int thn, int bln, int tgl);

    List<VehicleOrder> findAllByPemesan(Employee employee);

    VehicleOrder findByVehicleOrderId(String vehicleOrderId);

    List<VehicleOrder> findAllByNeedApproval(Employee pemesan);

    List<VehicleOrder> findAllByApprovalPimproAtasan(Employee pemesan);

    VehicleOrder findFirstByOrderByCreatedAtDesc();

    @Query("SELECT v FROM VehicleOrder v WHERE v.status = :status1 OR v.status = :status2")
    List<VehicleOrder> findAllByStatus(@Param("status1") String status1, @Param("status2") String status2);

    @Query(value = "select * from vehicle_order v where MONTH(v.created_at) = :month AND YEAR(v.created_at) = :year", nativeQuery = true)
    List<VehicleOrder> findAllByOrderByCreatedAtDesc(int year, int month);
}
