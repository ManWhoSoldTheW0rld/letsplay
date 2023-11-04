package gritlab.products.product;

import gritlab.products.product.model.Product;
import gritlab.products.user.UserRepository;
import gritlab.products.user.model.Role;
import gritlab.products.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> findAll(Pageable pageable) {

        Page<Product> page = productRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "price"))
                ));
        return page.getContent();
    }

    public Product findById(String id) {

        return  productRepository.findById(id).orElseThrow();
    }

    public Product createProduct(Product request, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(owner.getId())
                .build();

        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product updatedData, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();

        Product product;
        if (owner.getRole() == Role.ADMIN) {
            product = productRepository.findById(id).orElseThrow();
        } else {
            product = productRepository.findByUserIdAndId(owner.getId(), id).orElseThrow();
        }

        Product updatedProduct = Product.builder()
                .name(updatedData.getName())
                .description(updatedData.getDescription())
                .price(updatedData.getPrice())
                .id(product.getId())
                .userId(product.getUserId())
                .build();

        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(String id, String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail).orElseThrow();

        Product product;
        if (owner.getRole() == Role.ADMIN) {
            product = productRepository.findById(id).orElseThrow();
        } else {
            product = productRepository.findByUserIdAndId(owner.getId(), id).orElseThrow();
        }
        productRepository.delete(product);
    }
}
