package com.nst.ums.controller;

import com.nst.ums.dto.UserDTO;
import com.nst.ums.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity user(Authentication auth, @PathVariable String id) {
        System.out.println(auth);
        System.out.println(auth.getAuthorities());

        return ResponseEntity.ok(UserDTO.from(userRepository.findById(id).orElseThrow()));
    }

    @PostMapping("/get")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity user() {
        return ResponseEntity.ok("hiiii");
    }
}
