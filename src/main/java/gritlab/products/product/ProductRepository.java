package gritlab.products.product;

        import org.springframework.data.mongodb.repository.MongoRepository;
        import org.springframework.data.rest.core.annotation.RepositoryRestResource;
        import org.springframework.data.repository.PagingAndSortingRepository;

        import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends MongoRepository<Product, String>, PagingAndSortingRepository<Product, String>  {
        Optional <Product> findByUserIdAndId(String userId, String id);

        void deleteAllByUserId(String userId);
}
