package com.example.demo.security;

import com.example.demo.Entity.PhatTu;
import com.example.demo.Repository.PhatTuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PhatTuRepository phatTuRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PhatTu phatTu = phatTuRepository.findByUserName(username);
        if(phatTu == null){
            throw new UsernameNotFoundException("User not found");
        }
        return CustomUserDetails.mapUserToUserDetails(phatTu);
    }

}
