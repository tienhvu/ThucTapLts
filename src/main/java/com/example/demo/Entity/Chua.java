package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chuas")
public class Chua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChuaId")
    private int id;

    @OneToMany(mappedBy = "chua")
    @JsonIgnoreProperties(value = "chua")
    Set<PhatTu> phatTus;


    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "NgayThanhLap")
    private LocalDate ngayThanhLap;

    @Column(name = "TenChua")
    private String tenChua;

    @Column(name = "TruTri")
    @Max(11)
    private Integer truTri;


}
