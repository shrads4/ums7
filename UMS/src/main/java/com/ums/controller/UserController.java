package com.ums.controller;

import com.ums.dto.UserDto;
import com.ums.entity.User;
import com.ums.response.ResponseModel;
import com.ums.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@Validated
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    UserService userService;

    /** User creation*/

    @PostMapping("/create")
    public User createUser(@Valid @RequestBody User userDetails) {
        log.info("User creation API is called!");
        return userService.createUser(userDetails);
    }

    /** Get User Details*/
    @GetMapping("/details/{userId}")
    public ResponseModel getUserDetails(@PathVariable(name = "userId", required = true) String userId) {
        log.info("Get user details called!");
        return ResponseModel.success(HttpStatus.OK, "Success", userService.getUserDetails(userId));
    }
}
