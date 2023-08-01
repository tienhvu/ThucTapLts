package com.example.demo.Controller;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Service.DaoTrangService;
import com.example.demo.payload.request.DaoTrangRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/dao-trang")
public class DaoTrangController {
    @Autowired
    private DaoTrangService daoTrangService;
    @PostMapping("/them")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> themDaoTrang(@RequestBody DaoTrangRequest daoTrangRequest) {
        ResponseEntity<?> response = daoTrangService.them(daoTrangRequest.getDaoTrang(), daoTrangRequest.getNguoiTruTri());
        return response;
    }
    @PutMapping("/sua")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> sua(@RequestBody DaoTrang daoTrangSua){

        ResponseEntity<?> response = daoTrangService.sua(daoTrangSua);
        return response;
    }


    @DeleteMapping("/xoa")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> xoaDaoTrang(@RequestParam int id){

        ResponseEntity<?> response = daoTrangService.xoa(id);
        return response;
    }

    @GetMapping("/phan-trang")
    public Page<DaoTrang> phanTrang(@RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("noiToChuc").ascending());
        Page<DaoTrang> daoTrangPage = daoTrangService.hienThiPhanTrang(pageable);
        return daoTrangService.hienThiPhanTrang(pageable);
    }

    @GetMapping("/tim-kiem")
    public Page<DaoTrang> timKiemPhanTrang(@RequestParam("keyword") String keyword,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("noiToChuc").ascending());

        Page<DaoTrang> daoTrangPage = daoTrangService.TimTheoNoiToChuc(keyword,pageable);
        return daoTrangService.TimTheoNoiToChuc(keyword,pageable);
    }
}
