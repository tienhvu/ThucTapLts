package com.example.demo.security;

import com.example.demo.Entity.PhatTu;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private int phatTuId;
    private String userName;

    private String password;
    private String email;
    private String soDienThoai;
    private String anhChup;
    private Boolean daThoatTuc;
    private Integer gioiTinh;
    private String ho;
    private LocalDateTime ngayCapNhat;
    private LocalDate ngayHoanTuc;
    private LocalDate ngaySinh;
    private LocalDate ngayXuatGia;
    private String phapDanh;
    private String ten;
    private String tenDem;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    //Tu thong tin user chuyen sang thong tin CustomUserDetails
    public static CustomUserDetails mapUserToUserDetails(PhatTu phatTu){
        //Lay các quyen tu doi tuong user
        List<GrantedAuthority> listAuthorities = phatTu.getListRoles().stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                .collect(Collectors.toList());

        //Tra ve doi tuong CustomUserDetails
        return new CustomUserDetails(
                phatTu.getId(),
                phatTu.getUserName(),
                phatTu.getPassword(),
                phatTu.getEmail(),
                phatTu.getSoDienThoai(),
                phatTu.getAnhChup(),
                phatTu.getDaThoatTuc(),
                phatTu.getGioiTinh(),
                phatTu.getHo(),
                phatTu.getNgayCapNhat(),
                phatTu.getNgayHoanTuc(),
                phatTu.getNgaySinh(),
                phatTu.getNgayXuatGia(),
                phatTu.getPhapDanh(),
                phatTu.getTen(),
                phatTu.getTenDem(),


                listAuthorities
        );
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
