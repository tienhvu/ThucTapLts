package com.example.demo.ServiceImpl;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Entity.DonDangKy;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Error.ErrorResponse;
import com.example.demo.Repository.DaoTrangRepository;
import com.example.demo.Repository.DonDangKyRepository;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.DonDangKyService;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonDangKyServiceImpl implements DonDangKyService {
    @Autowired
    private DonDangKyRepository donDangKyRepository;
    @Autowired
    private DaoTrangRepository daoTrangRepository;
    @Autowired
    private PhatTuRepository phatTuRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<?> them(DonDangKy donDangKy) {
        Optional<PhatTu> nguoiDangNhap = phatTuRepository.findById(getCurrentUserId());
        if (nguoiDangNhap == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        donDangKy.setPhatTu(nguoiDangNhap.get());
        // Kiểm tra xem đã có đơn đăng ký nào của cùng một phật tử và cùng một buổi đạo tràng hay chưa
        DaoTrang daoTrang = donDangKy.getDaoTrang();
        if (daoTrang != null && nguoiDangNhap.get().getId() == donDangKy.getPhatTu().getId()) {
            List<DonDangKy> existingDonDangKys = donDangKyRepository.findByPhatTuAndDaoTrang(nguoiDangNhap.get(), daoTrang);
            if (!existingDonDangKys.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Phật tử đã tạo đơn đăng ký cho buổi đạo tràng này");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }

        Optional<DaoTrang> daoTrangOptional = daoTrangRepository.findById((donDangKy.getDaoTrang().getId()));
        if(daoTrangOptional.isEmpty()){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đạo tràng không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        donDangKy.setDaoTrang(daoTrangOptional.get());
        donDangKy.setNgayGuiDon(LocalDateTime.now());
        donDangKy.setTrangThaiDon(false);
        donDangKyRepository.save(donDangKy);
        return ResponseEntity.ok(donDangKy);
    }

    @Override
    public ResponseEntity<?> sua(DonDangKy donDangKySua) {
        Optional<PhatTu> nguoiDangNhap = phatTuRepository.findById(getCurrentUserId());
        if (nguoiDangNhap == null) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        Optional<DonDangKy> donDangKyOptional = donDangKyRepository.findById(donDangKySua.getId());
        if (donDangKyOptional.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đơn đăng ký không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DonDangKy donDangKy = donDangKyOptional.get();
        if (donDangKy.getTrangThaiDon()) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đơn đăng ký đã được duyệt và không thể sửa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        DaoTrang daoTrangSua = donDangKySua.getDaoTrang();
        if(daoTrangSua != null){
            Optional<DaoTrang> daoTrangOptional = daoTrangRepository.findById(daoTrangSua.getId());
            if(daoTrangOptional.isEmpty()){
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đạo tràng không tồn tại");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

        }
        // Thực hiện cập nhật thông tin của đơn đăng ký

        donDangKy.setDaoTrang(donDangKySua.getDaoTrang() != null ? daoTrangSua : donDangKy.getDaoTrang());
        donDangKy.setNgayGuiDon(LocalDateTime.now());

        donDangKyRepository.save(donDangKy);

        return ResponseEntity.ok(donDangKy);
    }


    @Override
    public ResponseEntity<?> xoa(int id) {
        Optional<DonDangKy> donDangKyOptional = donDangKyRepository.findById(id);
        if(donDangKyOptional.isEmpty()){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đơn đăng ký không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DonDangKy donDangKy = donDangKyOptional.get();
        donDangKyRepository.deleteById(id);

        return ResponseEntity.ok("Xóa thành công");
    }

    @Override
    public Page<DonDangKy> hienThiPhanTrang(Pageable pageable) {
        return donDangKyRepository.findAll(pageable);
    }
    @Override
    public ResponseEntity<?> duyetDon(int id) {
        Optional<DonDangKy> donDangKyOptional = donDangKyRepository.findById(id);
        if(donDangKyOptional.isEmpty()){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đơn đăng ký không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DonDangKy donDangKy = donDangKyOptional.get();
        // Kiểm tra trạng thái đơn xem đã được duyệt hay chưa (trạng thái đơn = false tức là chưa duyệt)
        if (!donDangKy.getTrangThaiDon()) {

            // Lấy thông tin người đang đăng nhập (người xử lý) và trạng thái đơn mới
            Optional<PhatTu> nguoiXuLy = phatTuRepository.findById(getCurrentUserId());
            boolean trangThaiDonMoi = true;
            LocalDateTime ngayXuLy = LocalDateTime.now();

            // Cập nhật thông tin cho đơn đăng ký
            donDangKy.setTrangThaiDon(trangThaiDonMoi);
            donDangKy.setNgayXuLy(ngayXuLy);
            donDangKy.setNguoiXuLy(nguoiXuLy.get().getId());

            // Lấy thông tin đạo tràng từ đơn đăng ký
            DaoTrang daoTrang = donDangKy.getDaoTrang();

            // Tăng số lượng thành viên của đạo tràng lên 1
            int soLuongThanhVienHienTai = daoTrang.getSoThanhVien();
            daoTrang.setSoThanhVien(soLuongThanhVienHienTai + 1);

            // Lưu thông tin cập nhật vào cơ sở dữ liệu
            daoTrangRepository.save(daoTrang);
            donDangKyRepository.save(donDangKy);

            // Trả về thông báo thành công hoặc các thông tin cần thiết khác
            return ResponseEntity.ok("Đã duyệt đơn thành công");
        } else {
            // Nếu đơn đã được duyệt trước đó, trả về thông báo lỗi
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Đơn đã được duyệt trước đó");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // Trả về giá trị null nếu người dùng chưa đăng nhập hoặc chưa xác thực
            return null;
        }

        String username = authentication.getName();
        CustomUserDetails currentUser = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        // Lấy thông tin người đăng nhập từ đối tượng CustomUserDetails
        int phatTuId = currentUser.getPhatTuId();
        return phatTuId;
    }
}
