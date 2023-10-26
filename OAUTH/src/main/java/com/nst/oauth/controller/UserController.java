package com.nst.oauth.controller;

import com.nst.oauth.dto.UserDTO;
import com.nst.oauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


// DO NOT COMMIT USER THIS iS FOR LOCAL TESTING..............


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
