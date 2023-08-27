package com.library.auth.libraryauthapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.library.auth.libraryauthapi.models.authentication.AuthenticationRequest;
import com.library.auth.libraryauthapi.models.authentication.AuthenticationResponse;
import com.library.auth.libraryauthapi.models.profile.ProfileResponse;
import com.library.auth.libraryauthapi.models.register.RegisterRequest;
import com.library.auth.libraryauthapi.models.register.RegisterResponse;
import com.library.auth.libraryauthapi.services.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest req) {
        RegisterResponse res = authenticationService.register(req);
        if (res == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/test")
    public String test() {
        return "Hello from unsecured endpoint!";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest req) {
        AuthenticationResponse res = authenticationService.login(req);

        if (res == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        token = token.substring(7);
        String res = authenticationService.logout(token);
        return (res == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(res);
    }

    @GetMapping()
    public ResponseEntity<ProfileResponse> getProfile(@RequestHeader("Authorization") String token) {
        System.out.println("Getting profile of: " + token);
        if (token == null || token.isEmpty() || token.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        token = token.substring(7);

        ProfileResponse res = authenticationService.getProfile(token);
        return (res == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(res);
    }

    @PutMapping()
    public ResponseEntity<ProfileResponse> updateProfile(@RequestBody RegisterRequest req) {
        ProfileResponse res = authenticationService.updateProfile(req);
        return (res == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(res);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteProfile(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        token = token.substring(7);
        String res = authenticationService.deleteProfile(token);
        return (res == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(res);
    }

    @RequestMapping(method = RequestMethod.OPTIONS, path = "/validate")
    public ResponseEntity<Boolean> isValid(@RequestHeader("Authorization") String token) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        token = token.substring(7);

        Boolean res = authenticationService.isValid(token);
        return (res) ? ResponseEntity.ok(res) : ResponseEntity.badRequest().build();
    }

}
