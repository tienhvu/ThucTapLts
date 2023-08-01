package com.example.demo.Service;

import com.example.demo.Entity.DonDangKy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DonDangKyService {
    ResponseEntity<?> them(DonDangKy donDangKy);
    ResponseEntity<?> sua(DonDangKy donDangKySua);
    ResponseEntity<?> xoa(int id);
    Page<DonDangKy> hienThiPhanTrang(Pageable pageable);
    ResponseEntity<?> duyetDon(int id);
}
