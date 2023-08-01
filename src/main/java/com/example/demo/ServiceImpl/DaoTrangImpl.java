package com.example.demo.ServiceImpl;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Entity.DonDangKy;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Error.ErrorResponse;
import com.example.demo.Repository.*;
import com.example.demo.Service.DaoTrangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DaoTrangImpl implements DaoTrangService {
    @Autowired
    private DonDangKyRepository donDangKyRepository;
    @Autowired
    private DaoTrangRepository daoTrangRepository;
    @Autowired
    private KieuThanhVienRepository kieuThanhVienRepository;
    @Autowired
    private PhatTuRepository phatTuRepository;
    @Autowired
    private PhatTuDaoTrangRepository phatTuDaoTrangRepository;

    @Override
    public ResponseEntity<?> them(DaoTrang daoTrang, PhatTu nguoiTruTri) {
        // Truy vấn để lấy thông tin của người trụ trì dựa trên ID
        Optional<PhatTu> phatTuOptional = phatTuRepository.findById(nguoiTruTri.getId());

        if (phatTuOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Người trụ trì không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        PhatTu nguoiTruTriFromDB = phatTuOptional.get();

        // Kiểm tra xem người trụ trì có kiểu thành viên là "Trụ Trì" hay không
        if (nguoiTruTri.getKieuThanhVien() == null || !"Trụ Trì".equals(nguoiTruTriFromDB.getKieuThanhVien().getTenKieu())) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Người trụ trì phải có kiểu thành viên là 'Trụ trì'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Thiết lập người trụ trì cho đối tượng DaoTrang
        daoTrang.setPhatTu(nguoiTruTriFromDB);
        daoTrang.setSoThanhVien(0);

        daoTrangRepository.save(daoTrang);
        return ResponseEntity.ok(daoTrang);
    }


    @Override
    public ResponseEntity<?> sua(DaoTrang daoTrangSua) {
        Optional<DaoTrang> daoTrangOptional = daoTrangRepository.findById(daoTrangSua.getId());
        if (daoTrangOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Buổi đạo tràng không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DaoTrang daoTrang = daoTrangOptional.get();

        // Kiểm tra xem người trụ trì có kiểu thành viên là "Trụ Trì" hay không
        PhatTu nguoiTruTri = daoTrangSua.getPhatTu();
        if (nguoiTruTri != null && (nguoiTruTri.getKieuThanhVien() == null || !"Trụ Trì".equals(nguoiTruTri.getKieuThanhVien().getTenKieu()))) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Người trụ trì phải có kiểu thành viên là 'Trụ Trì'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        daoTrang.setNoiDung(daoTrangSua.getNoiDung() != null ? daoTrangSua.getNoiDung() : daoTrang.getNoiDung());
        daoTrang.setNoiToChuc(daoTrangSua.getNoiToChuc() != null ? daoTrangSua.getNoiToChuc() : daoTrangSua.getNoiToChuc());
        daoTrang.setThoiGianToChuc(daoTrangSua.getThoiGianToChuc() != null ? daoTrangSua.getThoiGianToChuc() : daoTrang.getThoiGianToChuc());


        // Kiểm tra xem có cập nhật người trụ trì hay không
        if (nguoiTruTri != null) {
            daoTrang.setPhatTu(nguoiTruTri);
        }


        daoTrangRepository.save(daoTrang);
        return ResponseEntity.ok(daoTrang);
    }

    @Override
    public ResponseEntity<?> xoa(int id) {
        Optional<DaoTrang> daoTrangOptional = daoTrangRepository.findById(id);
        if (daoTrangOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đạo tràng không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DaoTrang daoTrang = daoTrangOptional.get();

        //Khi xóa đạo tràng sẽ xóa hết tất cả đơn đăng ký và phattudaotrang có liên quan
        daoTrang.getDonDangKys().clear();
        daoTrang.getPhatTuDaoTrangs().clear();
        donDangKyRepository.deleteAll(daoTrang.getDonDangKys());
        phatTuDaoTrangRepository.deleteAll(daoTrang.getPhatTuDaoTrangs());
        daoTrangRepository.deleteById(id);
        return ResponseEntity.ok("Xóa thành công");

    }

    @Override
    public Page<DaoTrang> hienThiPhanTrang(Pageable pageable) {
        return daoTrangRepository.findAll(pageable);
    }

    @Override
    public Page<DaoTrang> TimTheoNoiToChuc(String keyword, Pageable pageable) {
        return daoTrangRepository.findByNoiToChucContainingIgnoreCase(keyword, pageable);
    }
}
