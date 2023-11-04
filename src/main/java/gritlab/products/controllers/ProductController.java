package gritlab.products.controllers;

import gritlab.products.product.ProductService;
import gritlab.products.product.model.Product;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    @PermitAll
    public ResponseEntity<List<Product>> findAll(Pageable pageable) {

        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/item/{id}")
    @PermitAll
    public ResponseEntity<Product> findById(@PathVariable String id) {

        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    private ResponseEntity<Void> createProduct( @Valid @RequestBody Product request,
                                               @CurrentSecurityContext(expression="authentication.name") String ownerEmail,
                                               UriComponentsBuilder ucb) {

        Product savedProduct = productService.createProduct(request, ownerEmail);

        URI locationOfNewProduct = ucb
                .path("/api/v1/product/item/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody Product updatedData,
            @CurrentSecurityContext(expression="authentication.name") String ownerEmail) {

        productService.updateProduct(id, updatedData, ownerEmail);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteProduct(
            @PathVariable String id,
            @CurrentSecurityContext(expression="authentication.name") String ownerEmail) {

        productService.deleteProduct(id, ownerEmail);
        return ResponseEntity.noContent().build();
    }
}
