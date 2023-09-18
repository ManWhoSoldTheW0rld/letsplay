package gritlab.products;

		import com.jayway.jsonpath.DocumentContext;
		import com.jayway.jsonpath.JsonPath;
		import gritlab.products.user.User;
		import org.junit.jupiter.api.Test;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.test.context.SpringBootTest;
		import org.springframework.boot.test.web.client.TestRestTemplate;
		import org.springframework.http.HttpStatus;
		import org.springframework.http.ResponseEntity;
		import java.net.URI;

		import static org.assertj.core.api.Assertions.assertThat;
		import org.springframework.test.web.servlet.MockMvc;
		import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
		import org.springframework.security.test.context.support.WithMockUser;

		import org.springframework.test.annotation.DirtiesContext;

		import static org.hamcrest.Matchers.containsInAnyOrder;
		import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
		import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
		import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

		import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductsApplicationTests {
	@Autowired
	TestRestTemplate restTemplate;


	@Test
	void shouldReturnAListOfProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	/*@Test
	void shouldReturnACashCardWhenDataIsSavedMoc() throws Exception {
		mockMvc.perform(get("/user/64f9c9fcfe7d5f062b455138"))
				.andExpect(jsonPath("$.name").value("name"));
	}

	@Test
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");
	}

	@Test
	@DirtiesContext
	void shouldCreateANewCashCardMoc() throws Exception {

		User newUser = new User("Julia", "juliaMoc@jul.jul", "pass", "role");
		String location = mockMvc.perform(post("/users")
						.with(csrf())
						.contentType("application/json")
						.content("""
                        {
                        "name" :"Julia",
                         "email" : "julia@jul.jul",
                          "password" : "pass", 
                          "role" : "role"
                        }
                       """))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn().getResponse().getHeader("Location");

		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Julia"));
	}

	@Test
	void shouldCreateANewUser() {
		User newUser = new User("Julia", "julia@jul.jul", "pass", "role");
		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/users", newUser, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldReturnAListOfProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	/*@Test
	void shouldNotReturnACashCardWhenUsingBadCredentials() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("BAD-USER", "abc123")
				.getForEntity("/cashcards/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

		response = restTemplate
				.withBasicAuth("sarah1", "BAD-PASSWORD")
				.getForEntity("/cashcards/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}*/
}
