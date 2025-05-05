package com.bankassurance.backend.services.imp;

import com.bankassurance.backend.models.enums.Role;
import com.bankassurance.backend.models.requests.LoginRequest;
import com.bankassurance.backend.models.requests.RegisterRequest;
import com.bankassurance.backend.models.responses.AuthResponse;
import com.bankassurance.backend.models.responses.AuthenticationResponse;
import com.bankassurance.backend.repository.entity.User;
import com.bankassurance.backend.repository.entity.Utilisateur;
import com.bankassurance.backend.repository.rep.UserRepository;
import com.bankassurance.backend.repository.rep.UtilisateurRepository;
import com.bankassurance.backend.services.userservices.AuthService;
import com.bankassurance.backend.config.JwtUtil;
import com.bankassurance.backend.token.TokenRepository;
import com.bankassurance.backend.token.TokenType;
import com.bankassurance.backend.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getNomUtilisateur(), request.getMotDePasse())
            );
        } catch (AuthenticationException e) {
            return new AuthResponse("Échec de l'authentification");
        }

        String token = jwtUtil.generateToken(request.getNomUtilisateur());
        return new AuthResponse("Connexion réussie", token);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
