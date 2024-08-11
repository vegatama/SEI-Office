package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Mesinabsen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface MesinabsenRepository extends CrudRepository<Mesinabsen, Long> {
    List<Mesinabsen> findAllByPersonId(String personId);

    List<Mesinabsen> findAllByPersonIdAndIsProses(String personId, boolean b);

    List<Mesinabsen> findAllByPersonIdAndDate(String personId, LocalDate date);


    // group by both personId and date
//    @Query(value = "SELECT ID_MESIN, DATE FROM ABSENSI WHERE ID_MESIN IN :idMesinList AND DATE = :tglhariini GROUP BY ID_MESIN, DATE", nativeQuery = true)
////    @Query("select a from Mesinabsen a where a.date = :tglhariini AND a.personId IN :idMesinList GROUP BY a.personId, a.date, a.id")
//    List<Mesinabsen> findAllByPersonIdListAndDate(List<String> idMesinList, LocalDate tglhariini);
    @Query("select count(a) from Mesinabsen a where a.personId IN :personId AND a.date = :date")
    int countAllByPersonIdAndDate(List<String> personId, LocalDate date);

    List<Mesinabsen> findAllByPersonIdAndDateBetween(String personId, LocalDate date, LocalDate date2);

    boolean existsMesinabsenByPersonIdAndDate(String personId, LocalDate date);
}
