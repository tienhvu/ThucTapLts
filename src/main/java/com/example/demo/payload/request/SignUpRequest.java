package com.example.demo.payload.request;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.KieuThanhVien;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
public class SignUpRequest {
    private String userName;
    private String password;
    private String email;
    private String soDienThoai;
    private String anhChup;
    private Boolean daThoatTuc;
    private Integer gioiTinh;
    private String ho;
    private LocalDateTime ngayCapNhat;
    private LocalDate ngayHoanTuc;
    private LocalDate ngaySinh;
    private LocalDate ngayXuatGia;
    private String phapDanh;
    private String ten;
    private String tenDem;
    private Set<String> listRoles;
    private Chua chua;
    private KieuThanhVien kieuThanhVien;

    private Boolean isActive;


}
