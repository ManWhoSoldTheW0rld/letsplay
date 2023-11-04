package gritlab.products.user;

import gritlab.products.product.ProductRepository;
import gritlab.products.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<User> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
        return page.getContent();
    }

    public User findById(String id) {

        return userRepository.findById(id).orElseThrow();
    }


    public User updateUser(String id, User updatedData) {
        User user = userRepository.findById(id).orElseThrow();

        User updatedUser = User.builder()
                .name(updatedData.getName())
                .email(updatedData.getEmail())
                .role(updatedData.getRole())
                .id(user.getId())
                .build();

        return userRepository.save(updatedUser);
    }


    public void deleteUser(String id) {

        User user = userRepository.findById(id).orElseThrow();

        productRepository.deleteAllByUserId(id);
        userRepository.delete(user);
    }
}
