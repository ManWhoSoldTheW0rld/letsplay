package gritlab.products.controllers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import gritlab.products.product.Product;
import gritlab.products.product.ProductRepository;
import gritlab.products.user.User;
import gritlab.products.user.UserRepository;
import gritlab.products.user.Views;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/list")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<User>> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<User> findById(@PathVariable String id) {
        User user = userRepository.findById(id).orElseThrow();

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String id,
            @Valid @RequestBody User updatedData) {
        User user = userRepository.findById(id).orElseThrow();

        User updatedUser = User.builder()
                .name(updatedData.getName())
                .email(updatedData.getEmail())
                .role(updatedData.getRole())
                .id(user.getId())
                .build();

        userRepository.save(updatedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    ) {
        User user = userRepository.findById(id).orElseThrow();

        productRepository.deleteAllByUserId(id);
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}

