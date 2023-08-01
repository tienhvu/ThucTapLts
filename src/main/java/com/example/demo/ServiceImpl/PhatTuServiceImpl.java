package com.example.demo.ServiceImpl;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.KieuThanhVien;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Error.ErrorResponse;
import com.example.demo.Repository.ChuaRepository;
import com.example.demo.Repository.KieuThanhVienRepository;
import com.example.demo.Repository.PhatTuDaoTrangRepository;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.PhatTuService;
import com.example.demo.payload.response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhatTuServiceImpl implements PhatTuService {
    @Autowired
    private PhatTuRepository phatTuRepository;
    @Autowired
    private KieuThanhVienRepository kieuThanhVienRepository;
    @Autowired
    private ChuaRepository chuaRepository;
    @Autowired
    private PhatTuDaoTrangRepository phatTuDaoTrangRepository;


    @Autowired
    public void PhatTuService(PhatTuRepository phatTuRepository) {
        this.phatTuRepository = phatTuRepository;

    }


    @Override
    public ResponseEntity<?> sua(PhatTu phatTu) {

        Optional<PhatTu> existingPhatTuOptional = phatTuRepository.findById(phatTu.getId());
        if (existingPhatTuOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Phật tử không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        PhatTu existingPhatTu = existingPhatTuOptional.get();

        // Cập nhật các trường chỉ định trong request vào đối tượng phật tử hiện tại
        if (phatTu.getKieuThanhVien() != null) {
            Optional<KieuThanhVien> kieuThanhVienOptional = kieuThanhVienRepository.findById(phatTu.getKieuThanhVien().getId());
            if (kieuThanhVienOptional.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Kiểu thành viên không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            KieuThanhVien kieuThanhVien = kieuThanhVienOptional.get();
            existingPhatTu.setKieuThanhVien(kieuThanhVien);
        }


        // Cập nhật các trường thông tin cá nhân
        existingPhatTu.setTen(phatTu.getTen() != null ? phatTu.getTen() : existingPhatTu.getTen());
        existingPhatTu.setHo(phatTu.getHo() != null ? phatTu.getHo() : existingPhatTu.getHo());
        existingPhatTu.setTenDem(phatTu.getTenDem() != null ? phatTu.getTenDem() : existingPhatTu.getTenDem());
        existingPhatTu.setPhapDanh(phatTu.getPhapDanh() != null ? phatTu.getPhapDanh() : existingPhatTu.getPhapDanh());
        existingPhatTu.setGioiTinh(phatTu.getGioiTinh() != null ? phatTu.getGioiTinh() : existingPhatTu.getGioiTinh());
        existingPhatTu.setAnhChup(phatTu.getAnhChup() != null ? phatTu.getAnhChup() : existingPhatTu.getAnhChup());
        existingPhatTu.setNgayCapNhat(LocalDateTime.now());
        // Lưu thông tin phật tử đã được cập nhật vào cơ sở dữ liệu
        phatTuRepository.save(existingPhatTu);
        return ResponseEntity.ok(existingPhatTu);
    }

    @Override
    public ResponseEntity<?> them(PhatTu phatTu) {
        phatTu.setNgayXuatGia(LocalDate.now());
        phatTu.setDaThoatTuc(false);
        Optional<KieuThanhVien> kieuThanhVienOptional = kieuThanhVienRepository.findById(phatTu.getKieuThanhVien().getId());
        Optional<Chua> chuaOptional = chuaRepository.findById(phatTu.getChua().getId());

        if (chuaOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Chùa không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        if (kieuThanhVienOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Kiểu thành viên không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Chua chua = chuaOptional.get();
        KieuThanhVien kieuThanhVien = kieuThanhVienOptional.get();
        phatTu.setChua(chua);
        phatTu.setKieuThanhVien(kieuThanhVien);

        PhatTu savedPhatTu = phatTuRepository.save(phatTu);
        return ResponseEntity.ok(savedPhatTu);
    }

    @Override
    public ResponseEntity<?> xoa(int id) {
        Optional<PhatTu> existingPhatTuOptional = phatTuRepository.findById(id);
        if (existingPhatTuOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Phật tử không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        PhatTu phatTu = existingPhatTuOptional.get();
        phatTu.setKieuThanhVien(null);
        phatTu.setChua(null);
        phatTu.setIsActive(false);
        phatTu.setNgayCapNhat(LocalDateTime.now());

        phatTu.getPhatTuDaoTrangs().clear();
        phatTuDaoTrangRepository.deleteAll(phatTu.getPhatTuDaoTrangs());
        phatTuRepository.save(phatTu);
        return ResponseEntity.ok(new MessageResponse("Phật tử đã được xóa thành công"));
    }


    @Override
    public PhatTu findByUserName(String userName) {
        return phatTuRepository.findByUserName(userName);
    }

    @Override
    public PhatTu findByEmail(String email) {
        return phatTuRepository.findByEmail(email);
    }


    @Override
    public Boolean existsByUserName(String userName) {
        return phatTuRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return phatTuRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsBySoDienThoai(String soDienThoai) {
        return phatTuRepository.existsBySoDienThoai(soDienThoai);
    }

    @Override
    public String getPasswordByUserName(String userName) {
        PhatTu phatTu = phatTuRepository.findByUserName(userName);
        if (phatTu != null) {
            return phatTu.getPassword();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public Page<PhatTu> getAll(Pageable pageable) {
        return  phatTuRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Page<PhatTu> searchByUsernameOrEmailAndIsActive(
            String userNameKeyword, String emailKeyword, Pageable pageable) {
        Page<PhatTu> phatTuPage = phatTuRepository.findByIsActiveTrueAndUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                userNameKeyword, emailKeyword, pageable);

        List<PhatTu> filteredPhatTuList = phatTuPage.getContent().stream()
                .filter(phatTu -> phatTu.getIsActive() != null && phatTu.getIsActive())
                .collect(Collectors.toList());

        return new PageImpl<>(filteredPhatTuList, pageable, filteredPhatTuList.size());
    }



}
