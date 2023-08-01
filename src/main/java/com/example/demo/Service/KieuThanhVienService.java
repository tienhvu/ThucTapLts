package com.example.demo.Service;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.KieuThanhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface KieuThanhVienService {
    KieuThanhVien them(KieuThanhVien kieuThanhVien);
    ResponseEntity<?> sua(KieuThanhVien kieuThanhVienSua);
    ResponseEntity<?> xoa(int id);
    Page<KieuThanhVien> hienThiPhanTrang(Pageable pageable);
    Page<KieuThanhVien> TimTheoTenKieu(String keyword, Pageable pageable);


}
