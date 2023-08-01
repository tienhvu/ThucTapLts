package com.example.demo.Repository;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.KieuThanhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KieuThanhVienRepository extends JpaRepository<KieuThanhVien, Integer> {
    KieuThanhVien findByTenKieu(String tenKieu);
    Page<KieuThanhVien> findByTenKieuContainingIgnoreCase(String keyword, Pageable pageable);
}
