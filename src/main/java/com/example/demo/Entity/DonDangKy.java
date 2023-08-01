package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DonDangKys")
public class DonDangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NgayGuiDon")
    private LocalDateTime ngayGuiDon;

    @Column(name = "NgayXuLy")
    private LocalDateTime ngayXuLy;

    @Column(name = "NguoiXuLy")
    private Integer nguoiXuLy;

    @Column(name = "TrangThaiDon")
    private Boolean trangThaiDon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phatTuId", foreignKey = @ForeignKey(name = "fk_dondangky_phattu"))
    @JsonIgnoreProperties(value = "donDangKys")
    private PhatTu phatTu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "daoTrangId", foreignKey = @ForeignKey(name = "fk_dondangky_daoTrang"))
    @JsonIgnoreProperties(value = "donDangKys")
    private DaoTrang daoTrang;
}
