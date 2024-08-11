package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Daftarhadir;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.RuangMeeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface DaftarhadirRepository extends CrudRepository<Daftarhadir, Long> {
    List<Daftarhadir> findAllByPembuat(Employee employee);

    Daftarhadir findByDaftarId(String hid);

    List<Daftarhadir> findAllByRuangMeetingAndTanggal(RuangMeeting ruangMeeting, LocalDate tanggal);

    @Query("SELECT d FROM Daftarhadir d WHERE d.ruangMeeting = ?1 AND d.tanggal = ?2 AND d.waktuMulai <= ?3 AND d.waktuSelesai >= ?3 ORDER BY d.id LIMIT 1")
    Daftarhadir getCurrentMeeting(RuangMeeting ruangMeeting, LocalDate today, LocalTime now);

    @Query("select d from Daftarhadir d where d.ruangMeeting.id = ?1")
    List<Daftarhadir> findActive(long ruangMeetingId);
}
