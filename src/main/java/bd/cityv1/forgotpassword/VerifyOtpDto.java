package bd.cityv1.forgotpassword;

import jakarta.validation.constraints.NotBlank;

public record VerifyOtpDto(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "OTP is required") String otp
) {}