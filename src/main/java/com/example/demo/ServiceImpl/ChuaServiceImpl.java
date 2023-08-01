package com.example.demo.ServiceImpl;

import com.example.demo.Entity.Chua;
import com.example.demo.Error.ErrorResponse;
import com.example.demo.Repository.ChuaRepository;
import com.example.demo.Service.ChuaService;
import com.example.demo.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChuaServiceImpl implements ChuaService {
    @Autowired
    private ChuaRepository chuaRepository;

    @Override
    public Chua them(Chua chua) {
        chua.setNgayCapNhat(LocalDateTime.now());
        return chuaRepository.save(chua);
    }

    @Override
    public ResponseEntity<?> sua(Chua chuaSua) {
        Optional<Chua> chuaOptional = chuaRepository.findById(chuaSua.getId());
        if (chuaOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Chùa không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Chua chua = chuaOptional.get();
        // Cập nhật các trường chỉ khi chúng không null
        chua.setTenChua(chuaSua.getTenChua() != null ? chuaSua.getTenChua() : chua.getTenChua());
        chua.setDiaChi(chuaSua.getDiaChi() != null ? chuaSua.getDiaChi() : chua.getDiaChi());
        chua.setNgayCapNhat(LocalDateTime.now());
        chua.setNgayThanhLap(chuaSua.getNgayThanhLap() != null ? chuaSua.getNgayThanhLap() : chua.getNgayThanhLap());
        chua.setTruTri(chuaSua.getTruTri() != null ? chuaSua.getTruTri() : chua.getTruTri());

        chuaRepository.save(chua);
        return ResponseEntity.ok(chua);
    }


    @Override
    public ResponseEntity<?> xoa(int id) {
        Optional<Chua> ChuaOptional = chuaRepository.findById(id);
        if(ChuaOptional.isEmpty()){
            ErrorResponse errorResponse = new ErrorResponse (HttpStatus.BAD_REQUEST, "Chùa không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        chuaRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Xóa thành công"));
    }



    @Override
    public Page<Chua> hienThiPhanTrang(Pageable pageable) {
        return chuaRepository.findAll(pageable);
    }

    @Override
    public Page<Chua> TimTheoTenChua(String keyword, Pageable pageable) {
        return chuaRepository.findByTenChuaContainingIgnoreCase(keyword,pageable);
    }

}
