package rs.raf.rafcloud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.requests.LoginRequest;
import rs.raf.rafcloud.responses.LoginResponse;
import rs.raf.rafcloud.services.UserService;
import rs.raf.rafcloud.utils.JwtUtil;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception   e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        User user = userService.findByUserName(loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername(), user.getRoles(), user.getUserId())));
    }

}
