package com.example.demo.ServiceImpl;

import com.example.demo.Entity.KieuThanhVien;
import com.example.demo.Error.ErrorResponse;
import com.example.demo.Repository.KieuThanhVienRepository;
import com.example.demo.Service.KieuThanhVienService;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KieuThanhVienServiceImpl implements KieuThanhVienService {
    @Autowired
    private KieuThanhVienRepository kieuThanhVienRepository;
    @Override
    public KieuThanhVien them(KieuThanhVien kieuThanhVien) {
        return kieuThanhVienRepository.save(kieuThanhVien);
    }

    @Override
    public ResponseEntity<?> sua(KieuThanhVien kieuThanhVienSua) {
        Optional<KieuThanhVien> kieuThanhVienOptional = kieuThanhVienRepository.findById(kieuThanhVienSua.getId());
        if (kieuThanhVienOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "kiểu thành viên không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        KieuThanhVien kieuThanhVien = kieuThanhVienOptional.get();

        kieuThanhVien.setTenKieu(kieuThanhVienSua.getTenKieu()!=null ? kieuThanhVienSua.getTenKieu() : kieuThanhVien.getTenKieu());
        kieuThanhVien.setCode(kieuThanhVienSua.getCode()!=null ? kieuThanhVienSua.getCode() : kieuThanhVien.getCode());
        kieuThanhVienRepository.save(kieuThanhVien);
        return ResponseEntity.ok(kieuThanhVien);
    }

    @Override
    public ResponseEntity<?> xoa(int id) {
        Optional<KieuThanhVien> kieuThanhVienOptional = kieuThanhVienRepository.findById(id);
        if(kieuThanhVienOptional.isEmpty()){
            ErrorResponse errorResponse = new ErrorResponse (HttpStatus.BAD_REQUEST, "Kiểu thành viên không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        kieuThanhVienRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Xóa thành công"));

    }

    @Override
    public Page<KieuThanhVien> hienThiPhanTrang(Pageable pageable) {
        return kieuThanhVienRepository.findAll(pageable);
    }

    @Override
    public Page<KieuThanhVien> TimTheoTenKieu(String keyword, Pageable pageable) {
        return kieuThanhVienRepository.findByTenKieuContainingIgnoreCase(keyword, pageable);
    }

}
