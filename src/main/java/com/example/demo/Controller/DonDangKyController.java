package com.example.demo.Controller;

import com.example.demo.Entity.DonDangKy;
import com.example.demo.Service.DonDangKyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/don-dang-ky")
public class DonDangKyController {
    @Autowired
    private DonDangKyService donDangKyService;
    @PostMapping("/them")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> themDonDangKy(@RequestBody DonDangKy donDangKy) {
        ResponseEntity<?> response = donDangKyService.them(donDangKy);
        return response;
    }
    @PutMapping("/sua")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> sua(@RequestBody DonDangKy donDangKySua){

        ResponseEntity<?> response = donDangKyService.sua(donDangKySua);
        return response;
    }


    @DeleteMapping("/xoa")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> xoaDonDangKy(@RequestParam int id){

        ResponseEntity<?> response = donDangKyService.xoa(id);
        return response;
    }

    @GetMapping("/phan-trang")
    public Page<DonDangKy> phanTrang(@RequestParam int page, @RequestParam  int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DonDangKy> daoTrangPage = donDangKyService.hienThiPhanTrang(pageable);
        return donDangKyService.hienThiPhanTrang(pageable);
    }

    @PostMapping("/duyet-dang-ky")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> duyetDangKy(@RequestParam int id){
        ResponseEntity<?> response = donDangKyService.duyetDon(id);
        return response;
    }
}
