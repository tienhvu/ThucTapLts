package com.example.demo.Controller;

import com.example.demo.Entity.EmailSender;
import com.example.demo.Entity.PhatTu;
import com.example.demo.Repository.PhatTuRepository;
import com.example.demo.Service.PhatTuService;
import com.example.demo.payload.request.ForgotPasswordRequest;
import com.example.demo.payload.request.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RestController
@RequestMapping("/api/phat_tu")
public class ForgotPasswordController {
    @Autowired
    private PhatTuService phatTuService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PhatTuRepository phatTuRepository;
    private Map<String, String> temporaryPasswords = new HashMap<>();
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        String email = request.getEmail();

        // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
        if (!phatTuService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email không tồn tại");
        }

        // Sinh mã xác thực tạm thời
        String temporaryPassword = generateTemporaryPassword();
        // Lưu mã xác thực tạm thời vào Map với key là email
        temporaryPasswords.put(email, temporaryPassword);
        // Gửi email chứa mã xác thực đến email của người dùng
        String subject = "Mã xác thực của bạn";
        String content = "Mã xác thực của bạn là: " + temporaryPassword + "\nHãy đăng nhập và đổi mật khẩu";

        EmailSender.sendEmail(email, subject, content);

        return ResponseEntity.ok("Mã xác thực đã được gửi đến email của bạn");
    }

    // Phương thức sinh mã xt tạm thời
    private String generateTemporaryPassword() {
        int length = 8; // Độ dài của mật khẩu tạm thời
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        // Kiểm tra và tìm người dùng dựa trên email
        PhatTu phatTu = phatTuService.findByEmail(request.getEmail());
        if (phatTu == null) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại");
        }

        // Kiểm tra mã xác thực tạm thời
        String temporaryPassword = temporaryPasswords.get(request.getEmail());
        if (!request.getVerificationCode().equals(temporaryPassword)) {
            return ResponseEntity.badRequest().body("Mã xác thực không chính xác");
        }

        // Đặt lại mật khẩu mới
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        phatTu.setPassword(newPassword);
        phatTuRepository.save(phatTu);

    // Xóa mã xác thực tạm thời sau khi đã đặt lại mật khẩu
        temporaryPasswords.remove(request.getEmail());

        return ResponseEntity.ok("Đặt lại mật khẩu thành công");
    }

}
