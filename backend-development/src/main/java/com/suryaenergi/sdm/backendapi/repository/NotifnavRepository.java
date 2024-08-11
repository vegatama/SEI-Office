package com.suryaenergi.sdm.backendapi.repository;

import com.suryaenergi.sdm.backendapi.entity.Notifnav;
import org.springframework.data.repository.CrudRepository;

public interface NotifnavRepository extends CrudRepository<Notifnav, Long> {
    Notifnav findByUserIdAndNoDokumenAndJenisDokumen(String user, String dokumen, String jenis);
}
