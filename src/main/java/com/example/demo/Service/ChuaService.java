package com.example.demo.Service;

import com.example.demo.Entity.Chua;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ChuaService {
    Chua them(Chua chua);
    ResponseEntity<?> sua(Chua chuaSua);
    ResponseEntity<?> xoa(int id);
    Page<Chua> hienThiPhanTrang(Pageable pageable);
    Page<Chua> TimTheoTenChua(String keyword, Pageable pageable);

}
