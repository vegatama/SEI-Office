package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "slip_gaji_field_template")
@Data
public class SlipGajiFieldTemplate {
    private static final String ID = "ID";
    private static final String TEMPLATE = "TEMPLATE";
    private static final String NAMA_FIELD = "NAMA_FIELD";
    private static final String KATEGORI = "KATEGORI";
    private static final String URUTAN = "URUTAN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = TEMPLATE, referencedColumnName = "ID")
//    private SlipGajiTemplate template;
    @Column(name = TEMPLATE)
    private Long template;

    @Column(name = NAMA_FIELD)
    private String namaField;

    @Column(name = KATEGORI)
    private Kategori kategori;

    @Column(name = URUTAN)
    private Integer urutan;

    public enum Kategori {
        PEMASUKAN("Pemasukan", 1),
        PENGELUARAN("Pengeluaran", -1);

        private final String value;
        private final int multiplier;

        Kategori(String value, int multiplier) {
            this.value = value;
            this.multiplier = multiplier;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public String getValue() {
            return value;
        }
    }
}
