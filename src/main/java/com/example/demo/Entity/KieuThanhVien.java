package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KieuThanhVien")
public class KieuThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "kieuThanhVien")
    @JsonIgnoreProperties(value = "kieuThanhVien")
    Set<PhatTu> phatTus;
    @Column(name = "Code")
    private String code;

    @Column(name = "TenKieu")
    private String tenKieu;

}
