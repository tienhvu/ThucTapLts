package com.example.demo.Service;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Entity.PhatTu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DaoTrangService {
    ResponseEntity<?> them(DaoTrang daoTrang, PhatTu nguoiTruTri);
    ResponseEntity<?> sua(DaoTrang daoTrangSua);
    ResponseEntity<?> xoa(int id);
    Page<DaoTrang> hienThiPhanTrang(Pageable pageable);
    Page<DaoTrang> TimTheoNoiToChuc(String keyword, Pageable pageable);
}
