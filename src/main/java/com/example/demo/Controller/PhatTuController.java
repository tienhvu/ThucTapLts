package com.example.demo.Controller;

import com.example.demo.Entity.ERole;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Entity.Roles;
import com.example.demo.Jwt.JwtTokenProvider;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.ChuaService;
import com.example.demo.Service.PhatTuService;
import com.example.demo.Service.RoleService;
import com.example.demo.payload.request.ChangePasswordRequest;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignUpRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/phat_tu")
public class PhatTuController {
    @Autowired
    private PhatTuService phatTuService;
    @Autowired
    private PhatTuRepository phatTuRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ChuaService chuaService;

    @PostMapping("/sign_up")
    public ResponseEntity<?> dangKy(@RequestBody SignUpRequest signUpRequest) {
        if(phatTuService.existsByUserName(signUpRequest.getUserName())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already! "));
        }
        if(phatTuService.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        if(phatTuService.existsBySoDienThoai(signUpRequest.getSoDienThoai())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already"));
        }
        PhatTu phatTu = new PhatTu();
        phatTu.setUserName(signUpRequest.getUserName());
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        phatTu.setPassword(encodedPassword);
        phatTu.setEmail(signUpRequest.getEmail());
        phatTu.setAnhChup(signUpRequest.getAnhChup());
        phatTu.setSoDienThoai(signUpRequest.getSoDienThoai());
        phatTu.setGioiTinh(signUpRequest.getGioiTinh());
        phatTu.setNgaySinh(signUpRequest.getNgaySinh());
        phatTu.setTen(signUpRequest.getTen());
        phatTu.setHo(signUpRequest.getHo());
        phatTu.setTenDem(signUpRequest.getTenDem());
        phatTu.setPhapDanh(signUpRequest.getPhapDanh());
        phatTu.setNgayCapNhat(signUpRequest.getNgayCapNhat());
        phatTu.setNgayHoanTuc(signUpRequest.getNgayHoanTuc());
        phatTu.setDaThoatTuc(signUpRequest.getDaThoatTuc());
        phatTu.setNgayXuatGia(signUpRequest.getNgayXuatGia());
        phatTu.setChua(signUpRequest.getChua());
        phatTu.setKieuThanhVien(signUpRequest.getKieuThanhVien());
        phatTu.setIsActive(true);
        Set<String> strRoles = signUpRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if(strRoles==null){
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else{
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                        break;
                    case "moderator":
                        Roles modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                        break;
                    case "user":
                        Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                        break;
                }
            });
        }
        phatTu.setListRoles(listRoles);
        // Gọi service để thực hiện thêm phật tử vào cơ sở dữ liệu
        ResponseEntity<?> response = phatTuService.them(phatTu);

        // Kiểm tra kết quả trả về từ service
        if (response.getStatusCode() == HttpStatus.OK) {
            // Nếu thành công, trả về thông báo thành công cho client
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } else {
            // Nếu có lỗi, trả về thông báo lỗi từ service cho client
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }

    }

    @PutMapping("/sua")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> suaPhatTu(@RequestBody PhatTu phatTu) {

        // Gọi phương thức sua() từ service để cập nhật thông tin phật tử
        ResponseEntity<?> responseEntity = phatTuService.sua(phatTu);
        return responseEntity;
    }


    @PostMapping("/sign_in")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest) {
        // Kiểm tra trạng thái isActive của phật tử
        PhatTu phatTu = phatTuService.findByUserName(loginRequest.getUserName());
        if (phatTu != null && !phatTu.getIsActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản này không hoạt động"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwt = tokenProvider.generateToken(customUserDetails);
            List<String> listRoles = customUserDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, customUserDetails.getUsername(), customUserDetails.getEmail(),
                    listRoles));
        } catch (AuthenticationException ex) {
            // Xử lý khi xảy ra lỗi đăng nhập (tài khoản hoặc mật khẩu không chính xác)
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản hoặc mật khẩu không chính xác"));
        }
    }



    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng chưa đăng nhập");
        }

        String username = authentication.getName();
        CustomUserDetails currentUser = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không chính xác");
        }

        String newPassword = passwordEncoder.encode(request.getNewPassword());
        currentUser.setPassword(newPassword);
        PhatTu phatTu = phatTuService.findByUserName(username);
        phatTu.setPassword(newPassword);

        phatTuRepository.save(phatTu);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }


    @GetMapping("/phan_trang")
    public ResponseEntity<Page<PhatTu>> locDuLieuPhanTrang(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userName").ascending());
        Page<PhatTu> phatTuPage = phatTuService.getAll(pageable);

        return new ResponseEntity<>(phatTuPage, HttpStatus.OK);
    }

    @GetMapping("/tim_kiem/phan_trang")
    public ResponseEntity<Page<PhatTu>> timKiem(@RequestParam("keyword") String keyword,
                                                @RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("userName").ascending());

        // Gọi phương thức tìm kiếm phật tử từ phatTuService
        Page<PhatTu> phatTuPage = phatTuService.searchByUsernameOrEmailAndIsActive(keyword, keyword, pageable);

        return new ResponseEntity<>(phatTuPage, HttpStatus.OK);
    }


    @DeleteMapping("/xoa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> xoaPhatTu(@RequestParam int id){
        ResponseEntity<?> response = phatTuService.xoa(id);
        return response;
    }
}
