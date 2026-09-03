package bd.cityv1.forgotpassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotEmailDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {}