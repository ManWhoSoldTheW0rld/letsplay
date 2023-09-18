package gritlab.products.controllers;

        import java.util.List;

        import gritlab.products.user.User;
        import gritlab.products.user.UserRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.domain.Sort;
        import org.springframework.http.ResponseEntity;
        import org.springframework.security.core.annotation.CurrentSecurityContext;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.beans.factory.annotation.Autowired;

        import java.net.URI;

        import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<User>> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    private ResponseEntity<Void> createUser(@RequestBody User request,
                                               @CurrentSecurityContext(expression="authentication.name") String admin,
                                               UriComponentsBuilder ucb) {

        var newUser = userRepository.save(request);

        URI locationOfNewProduct = ucb
                .path("/api/v1/user/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewProduct).build();
    }
}

