package com.peoples.admin.peoples_admin.controller;

import com.peoples.admin.peoples_admin.dto.response.AdminResponse;
import com.peoples.admin.peoples_admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admins")
    public ResponseEntity<List<AdminResponse>> getAdminList(){
        return ResponseEntity.ok(adminService.getAllAdmin());
    }

    @PostMapping("/admin")
    public ResponseEntity<List<AdminResponse>> createAdmin(@RequestBody Map<String,Object> param){
        return ResponseEntity.ok(adminService.createAdmin(param));
    }

    @GetMapping("/type")
    public ResponseEntity<Map<String, Object>> getSignUpType(){
        return ResponseEntity.ok(adminService.getType());
    }

    @GetMapping("/users")
    public ResponseEntity<Map<LocalDate, Object>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }


}
