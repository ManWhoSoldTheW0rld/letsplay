package gritlab.products;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gritlab.products.product.ProductRepository;
import gritlab.products.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductsApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DirtiesContext
    void shouldAddUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                 {
                                     "name" : "Admin",
                                     "email" : "admin@admin.adm",
                                     "password" : "Password1",
                                     "role" : "ADMIN"
                                 }
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }


    @Test
    void shouldReturnListOfProducts() throws Exception {
        mockMvc.perform(get("/api/v1/product/list", String.class))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DirtiesContext
    void shouldCrudProduct() throws Exception {

        String token = addUser();

        //Create product
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/product")
                        .with(csrf())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                 {
                                     "name" : "name",
                                     "description" : "description",
                                     "price" : 250.00
                                 }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn()
                .getResponse();

        String locationHeader = response.getHeader("Location");
        String[] segments = locationHeader.split("/");
        String id = segments[segments.length - 1];

        //Check if product is there
        mockMvc.perform(get(locationHeader, String.class))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$..name").value("name"))
                .andExpect(jsonPath("$..description").value("description"))
                .andExpect(jsonPath("$..price").value(250.0));
                //.andDo(print());

        //UpdateProduct
        mockMvc.perform(put("/api/v1/product/" + id)
                        .with(csrf())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                 {
                                     "name" : "new name",
                                     "description" : "new description",
                                     "price" : 350.00
                                 }
                                """))
                .andExpect(status().isNoContent());


        //Check if product is there
        mockMvc.perform(get(locationHeader, String.class))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$..name").value("new name"))
                .andExpect(jsonPath("$..description").value("new description"))
                .andExpect(jsonPath("$..price").value(350.0));

        //Delete product
        mockMvc.perform(delete("/api/v1/product/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        //Check if product not there
        mockMvc.perform(get(locationHeader, String.class))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    private String addUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                 {
                                     "name" : "Admin",
                                     "email" : "admin@admin.adm",
                                     "password" : "Password1",
                                     "role" : "ADMIN"
                                 }
                                """))
                .andReturn().getResponse();

        // Extract the access_token from the JSON response
        String responseBody = response.getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        return jsonResponse.get("access_token").asText();
    }
}
