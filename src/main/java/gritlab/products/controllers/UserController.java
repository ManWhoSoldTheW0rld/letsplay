package gritlab.products.controllers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import gritlab.products.product.ProductRepository;
import gritlab.products.user.UserService;
import gritlab.products.user.model.User;
import gritlab.products.user.model.Views;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/list")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<User>> findAll(Pageable pageable) {

        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<User> findById(@PathVariable String id) {

        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String id,
            @Valid @RequestBody User updatedData) {

        userService.updateUser(id, updatedData);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    ) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

