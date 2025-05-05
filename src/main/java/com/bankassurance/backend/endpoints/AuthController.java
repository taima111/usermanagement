package com.bankassurance.backend.endpoints;

import com.bankassurance.backend.models.requests.LoginRequest;
import com.bankassurance.backend.models.requests.RegisterRequest;
import com.bankassurance.backend.models.responses.AuthResponse;
import com.bankassurance.backend.models.responses.AuthenticationResponse;
import com.bankassurance.backend.services.userservices.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
}
