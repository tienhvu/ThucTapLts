package com.example.demo.Controller;

import com.example.demo.Entity.KieuThanhVien;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.KieuThanhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/kieu-thanh-vien")
public class KieuThanhVienController {
    @Autowired
    private KieuThanhVienService kieuThanhVienService;

    @Autowired
    private PhatTuRepository phatTuRepository;

    @PostMapping("/them")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public KieuThanhVien themMoi(@RequestBody KieuThanhVien kieuThanhVien){
        return kieuThanhVienService.them(kieuThanhVien);
    }

    @PutMapping("/sua")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> sua(@RequestBody KieuThanhVien kieuThanhVien){

        ResponseEntity<?> response = kieuThanhVienService.sua(kieuThanhVien);
        return response;
    }

    @DeleteMapping("/xoa")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> xoa(@RequestParam int id){
        List<PhatTu> phatTuList = phatTuRepository.findByKieuThanhVienId(id);
        for(PhatTu phatTu:phatTuList){
            phatTu.setKieuThanhVien(null);
            phatTu.setNgayCapNhat(LocalDateTime.now());
            phatTuRepository.save(phatTu);
        }
        ResponseEntity<?> response = kieuThanhVienService.xoa(id);
        return response;
    }

    @GetMapping("/phan-trang")
    public Page<KieuThanhVien> phanTrang(@RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("tenKieu").ascending());
        Page<KieuThanhVien> kieuThanhVienPage = kieuThanhVienService.hienThiPhanTrang(pageable);
        return kieuThanhVienService.hienThiPhanTrang(pageable);
    }

    @GetMapping("/tim-kiem")
    public Page<KieuThanhVien> timKiemPhanTrang(@RequestParam("keyword") String keyword,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("tenKieu").ascending());

        Page<KieuThanhVien> kieuThanhVienPage = kieuThanhVienService.TimTheoTenKieu(keyword,pageable);
        return kieuThanhVienService.TimTheoTenKieu(keyword,pageable);
    }
}
