package com.peoples.admin.peoples_admin.service;

import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.SecurityUser;
import com.peoples.admin.peoples_admin.domain.Study;
import com.peoples.admin.peoples_admin.domain.User;
import com.peoples.admin.peoples_admin.domain.enumeration.Role;
import com.peoples.admin.peoples_admin.dto.response.AdminResponse;
import com.peoples.admin.peoples_admin.repository.AdminRepository;
import com.peoples.admin.peoples_admin.repository.StudyRepository;
import com.peoples.admin.peoples_admin.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    private LocalDateTime targetDate = LocalDateTime.of(2023,05,02,00,00,00);

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

    @Transactional(readOnly = true)
    public Map<String , Object> getType() {
        List<User> list = userRepository.findAllByCreatedAtAfterOrderByCreatedAt(targetDate);

        Map<String, Object> result = new HashMap<>();

        int kakaoCNT = 0;
        int naverCNT = 0;
        int emailCNT = 0;
        for(User user : list){
            if(user.isSnsKakao()){
                kakaoCNT ++;
            }
            else if(user.isSnsNaver()){
                naverCNT ++;
            }
            else{
                emailCNT ++;
            }
        }

        result.put("naver", naverCNT);
        result.put("kakao", kakaoCNT);
        result.put("email", emailCNT);

        return result;
    }

    public Map<LocalDate,Object> getUsers() {
        List<User> list = userRepository.findAllByCreatedAtAfterOrderByCreatedAt(targetDate);

        Set<LocalDate> daySet = new HashSet<>();
        for(User user : list){
            daySet.add(user.getCreatedAt().toLocalDate());
        }

        List<LocalDate> dayList = new ArrayList(daySet);
        Collections.sort(dayList);

        LinkedHashMap<LocalDate, Object> current = new LinkedHashMap<>();

        for(LocalDate ldt : dayList){
            int userCnt = 0;
            for(User user : list){
                if(ldt.isEqual(user.getCreatedAt().toLocalDate())){
                    userCnt ++;
                }
            }
            current.put(ldt, userCnt);
        }

        return current;
    }


}
