package bd.cityv1.forgotpassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String newPassword,
        @NotBlank(message = "Confirm password is required") String confirmPassword
) {}