package gritlab.products.user;

import java.util.Optional;

import gritlab.products.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}