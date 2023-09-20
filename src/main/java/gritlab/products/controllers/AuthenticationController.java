package gritlab.products.controllers;

import gritlab.products.auth.AuthenticationRequest;
import gritlab.products.auth.AuthenticationResponse;
import gritlab.products.auth.AuthenticationService;
import gritlab.products.auth.RegisterRequest;
import gritlab.products.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository repository;

  @PostMapping("/register")
  public ResponseEntity<?> register(
          @Valid @RequestBody RegisterRequest request
  ) {

    var existingUser = repository.findByEmail(request.getEmail());

    if (existingUser.isPresent()) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body("User with this email already exists");
    }

    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
}
