package com.example.demo.Repository;

import com.example.demo.Entity.PhatTu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhatTuRepository extends JpaRepository<PhatTu, Integer> {

    PhatTu findByUserName(String userName);
    PhatTu findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);
    Boolean existsBySoDienThoai(String soDienThoai);

    Page<PhatTu> findAll(Pageable pageable);
    List<PhatTu> findByChuaId(int chuaId);
    List<PhatTu> findByKieuThanhVienId(int chuaId);
    // Lấy danh sách những Phật tử có trạng thái "isActive" là true
    Page<PhatTu> findByIsActiveTrue(Pageable pageable);
    Page<PhatTu> findByIsActiveTrueAndUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String userNameKeyword, String emailKeyword, Pageable pageable);


}
