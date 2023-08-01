package com.example.demo.Service;

import com.example.demo.Entity.PhatTu;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface PhatTuService {
    ResponseEntity<?> sua(PhatTu phatTu);
    ResponseEntity<?> them(PhatTu phatTu);
    ResponseEntity<?> xoa(int id);


    PhatTu findByUserName(String userName);
    PhatTu findByEmail(String email);

    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    Boolean existsBySoDienThoai(String soDienThoai);
    String getPasswordByUserName(String userName);
    Page<PhatTu> getAll(Pageable pageable);
    Page<PhatTu> searchByUsernameOrEmailAndIsActive(
            String userNameKeyword, String emailKeyword, Pageable pageable);


}
