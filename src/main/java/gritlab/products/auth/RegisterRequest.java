package gritlab.products.auth;

import gritlab.products.user.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Name is required")
  @Size(max = 255, min = 2, message = "Name cannot exceed 255 characters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 64, message = "Password must be at least 8 characters long")
  private String password;

  @NotNull(message = "Role is required")
  private Role role;
}
