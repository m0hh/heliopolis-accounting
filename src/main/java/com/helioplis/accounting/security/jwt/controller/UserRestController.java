package com.helioplis.accounting.security.jwt.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.entity.UserRequest;
import com.helioplis.accounting.security.jwt.entity.UserResponse;
import com.helioplis.accounting.security.jwt.service.UserService;
import com.helioplis.accounting.security.jwt.util.JWTUtil;

@Controller
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil util;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody UserHelioplis user) {

        Integer id = userService.saveUser(user);
        String message= "User with id '"+id+"' saved succssfully!";
        return ResponseEntity.ok(message);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request){

        //Validate username/password with DB(required in case of Stateless Authentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token =util.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponse(token,"Token generated successfully!"));
    }

    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }
}