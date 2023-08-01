package com.example.demo.Controller;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.ChuaService;
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
@RequestMapping(value = "/api/chua")
public class ChuaController {
    @Autowired
    private ChuaService chuaService;

    @Autowired
    private PhatTuRepository phatTuRepository;
    @PostMapping("/them")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Chua themMoi(@RequestBody Chua chua){
        return chuaService.them(chua);
    }

    @PutMapping("/sua")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> sua(@RequestBody Chua chuaSua){

        ResponseEntity<?> response = chuaService.sua(chuaSua);
        return response;
    }

    @DeleteMapping("/xoa")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> xoaChua(@RequestParam int id){
        List<PhatTu> phatTuList = phatTuRepository.findByChuaId(id);
        for(PhatTu phatTu:phatTuList){
            phatTu.setChua(null);
            phatTu.setNgayCapNhat(LocalDateTime.now());
            phatTuRepository.save(phatTu);
        }
        ResponseEntity<?> response = chuaService.xoa(id);
        return response;
    }

    @GetMapping("/phan-trang")
    public Page<Chua> phanTrang(@RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("tenChua").ascending());
        Page<Chua> chuaPage = chuaService.hienThiPhanTrang(pageable);
        return chuaService.hienThiPhanTrang(pageable);
    }

    @GetMapping("/tim-kiem")
    public Page<Chua> timKiemPhanTrang(@RequestParam("keyword") String keyword,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("tenChua").ascending());

        Page<Chua> chuaPage = chuaService.TimTheoTenChua(keyword,pageable);
        return chuaService.TimTheoTenChua(keyword,pageable);
    }

}
