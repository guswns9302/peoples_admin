package com.peoples.admin.peoples_admin.service;

import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.SecurityUser;
import com.peoples.admin.peoples_admin.domain.enumeration.Role;
import com.peoples.admin.peoples_admin.dto.response.AdminResponse;
import com.peoples.admin.peoples_admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return SecurityUser.of(this.findByUserId(userId));
    }

    public Admin findByUserId(String userId) {
        return adminRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
    }

    @Transactional
    public void updateLastLogin(String userId) {
        Admin admin = this.findByUserId(userId);
        admin.updateLastLogin();
    }

    public List<AdminResponse> getAllAdmin() {
        List<Admin> adminList = adminRepository.findAll(Sort.by("createdAt"));
        List<AdminResponse> resultList = new ArrayList<>();
        adminList.forEach(list -> {
            resultList.add(AdminResponse.from(list));
        });

        return resultList;
    }

    @Transactional
    public List<AdminResponse> createAdmin(Map<String, Object> param) {
        log.debug("param : {}", param);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Admin newAdmin = Admin.builder()
                .userId(param.get("adminId").toString())
                .nickname(param.get("adminNickname").toString())
                .password(passwordEncoder.encode(param.get("adminPw").toString()))
                .role(Role.ROLE_ADMIN)
                .build();
        adminRepository.save(newAdmin);
        return this.getAllAdmin();
    }
}
