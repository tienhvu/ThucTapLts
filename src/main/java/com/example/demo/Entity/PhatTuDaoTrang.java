package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhatTuDaoTrang")
public class PhatTuDaoTrang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "daoTrangId", foreignKey = @ForeignKey(name = "fk_phattudaotrang_daotrang"))
    @JsonIgnoreProperties(value = "phatTuDaoTrangs")
    DaoTrang daoTrang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phatTuId", foreignKey = @ForeignKey(name = "fk_phattudaotrang_phattu"))
    @JsonIgnoreProperties(value = "phatTuDaoTrangs")
    PhatTu phatTu;
    @Column(name = "DaThamGia")
    private Boolean daThamGia;

    @Column(name = "LyDoKhongThamGia")
    private String lyDoKhongThamGia;


}
