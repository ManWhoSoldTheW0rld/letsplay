package gritlab.products.controllers;

import gritlab.products.product.Product;
import gritlab.products.product.ProductRepository;
import gritlab.products.user.User;
import gritlab.products.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "price"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    private ResponseEntity<Void> createProduct( @Valid @RequestBody Product request,
                                               @CurrentSecurityContext(expression="authentication.name") String ownerEmail,
                                               UriComponentsBuilder ucb) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();

        var product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(owner.getId())
                .build();

        var savedProduct = productRepository.save(product);

        URI locationOfNewProduct = ucb
                .path("/api/v1/product/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody Product updatedData,
            @CurrentSecurityContext(expression="authentication.name") String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();
        Product product = productRepository.findByUserIdAndId(owner.getId(), id).orElseThrow();

        Product updatedProduct = Product.builder()
                .name(updatedData.getName())
                .description(updatedData.getDescription())
                .price(updatedData.getPrice())
                .id(product.getId())
                .userId(product.getUserId())
                .build();

        productRepository.save(updatedProduct);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteProduct(
            @PathVariable String id,
            @CurrentSecurityContext(expression="authentication.name") String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();

        try {
            Product product = productRepository.findByUserIdAndId(owner.getId(), id).orElseThrow();
            productRepository.delete(product);

        } catch (NoSuchElementException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.noContent().build();
    }
}
