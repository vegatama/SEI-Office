package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "daftar_hadir")
public class Daftarhadir implements Serializable {
    @Serial
    private static final long serialVersionUID = -3880827014549234650L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updateAt;

    private String kegiatan;
    private String subyek;
    private LocalDate tanggal;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private String pimpinan;
    private String tempat;
    private String keterangan;

    @ManyToOne
    @JoinColumn(name="ruang_meeting_id", referencedColumnName = "id")
    private RuangMeeting ruangMeeting;

    @Column(columnDefinition = "TEXT")
    private String risalah;

    @ManyToOne
    @JoinColumn(name="notulen_id", referencedColumnName = "id")
    private Employee notulen = null;

    @ManyToOne
    @JoinColumn(name="pembuat_id", referencedColumnName = "id")
    private Employee pembuat;
    private String daftarId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "daftar_hadir_id")
    private List<Pesertakegiatan> listPeserta;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "undangan_id")
    private List<Undangan> listUndangan;

    @Column(columnDefinition = "TEXT")
    private String isiUndangan;
}
