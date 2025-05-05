package com.bankassurance.backend.services.userservices;

import com.bankassurance.backend.models.requests.AuthenticationRequest;
import com.bankassurance.backend.models.requests.LoginRequest;
import com.bankassurance.backend.models.requests.RegisterRequest;
import com.bankassurance.backend.models.responses.AuthResponse;
import com.bankassurance.backend.models.responses.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse register(RegisterRequest request);
}
