package com.example.demo.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
@NoArgsConstructor
@Data
@Table(name = "phattus")

public class PhatTu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column(name = "username",unique = true, nullable = false)
    private String userName;
    @NotBlank
    @Column(name ="email",unique = true, nullable = false)
    @Email
    private String email;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PhatTu_Role", joinColumns = @JoinColumn(name = "PhatTuId"),
    inverseJoinColumns = @JoinColumn(name = "roleId"))
    Set<Roles> listRoles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chua_id",foreignKey = @ForeignKey(name = "fk_phattu_chua"))
    @JsonIgnoreProperties(value = "phatTus")
    private Chua chua;

    @OneToMany(mappedBy = "phatTu")
    @JsonIgnoreProperties(value = "phatTu")
    Set<DonDangKy> donDangKys;
    @OneToMany(mappedBy = "phatTu")
    @JsonIgnoreProperties(value = "phatTu")
    Set<PhatTuDaoTrang> phatTuDaoTrangs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kieuThanhVienID",foreignKey = @ForeignKey(name = "fk_phattu_kieuthanhviens"))
    @JsonIgnoreProperties(value = "phatTus")
    KieuThanhVien kieuThanhVien;



    @Column(name = "anhchup")
    private String anhChup;

    @Column(name = "dathoattuc")
    private Boolean daThoatTuc;


    @Column(name = "gioitinh")
    private Integer gioiTinh;

    @Column(name = "ho")
    private String ho;

    @Column(name = "ngaycapnhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "ngayhoantuc")
    private LocalDate ngayHoanTuc;

    @Column(name = "ngaysinh")
    private LocalDate ngaySinh;

    @Column(name = "ngayxuatgia")
    private LocalDate ngayXuatGia;


    @Column(name = "phapdanh")
    private String phapDanh;

    @Column(name= "sodienthoai",unique = true,nullable = false)
    private String soDienThoai;

    @Column(name = "ten")
    private String ten;

    @Column(name = "tendem")
    private String tenDem;
    @Column(name = "TrangThaiPhatTu")
    private Boolean isActive = true;



}
