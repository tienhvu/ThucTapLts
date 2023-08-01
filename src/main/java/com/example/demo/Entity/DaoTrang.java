package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DaoTrangs")
public class DaoTrang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "daoTrang")
    @JsonIgnoreProperties(value = "daoTrang")
    Set<PhatTuDaoTrang> phatTuDaoTrangs;

    @OneToMany(mappedBy = "daoTrang")
    @JsonIgnoreProperties(value = "daoTrang")
    Set<DonDangKy> donDangKys;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguoiTruTri", foreignKey = @ForeignKey(name = "fk_daotrang_phattu"))
    @JsonIgnoreProperties(value = "daoTrangs")
    PhatTu phatTu;
    @Column(name = "DaKetThuc")
    private Boolean daKetThuc=false;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "NoiToChuc")
    private String noiToChuc;

    @Column(name = "SoThanhVienThamGia")
    private Integer soThanhVien;

    @Column(name = "ThoiGianToChuc")
    private LocalDateTime thoiGianToChuc;


    // Constructor để thiết lập thoiGianToChuc và daKetThuc
    public DaoTrang(LocalDateTime thoiGianToChuc) {
        this.thoiGianToChuc = thoiGianToChuc;

        // Kiểm tra nếu thời gian tổ chức đã vượt qua ngày hiện tại thì đánh dấu đã kết thúc
        LocalDateTime currentDate = LocalDateTime.now();
        if (this.thoiGianToChuc.isBefore(currentDate)) {
            this.daKetThuc = true;
        } else {
            this.daKetThuc = false;
        }
    }
}
