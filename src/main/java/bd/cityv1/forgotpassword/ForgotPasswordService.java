package bd.cityv1.forgotpassword;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserLookupService userLookupService;
    private final OtpService otpService;
    private final OtpMailService otpMailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendOtp(ForgotEmailDto dto) {
        if (!userLookupService.emailExists(dto.email())) {
            throw new RuntimeException("No account found with this email");
        }
        String otp = otpService.generateOtp(dto.email());
        otpMailService.sendOtp(dto.email(), otp);
    }

    @Transactional
    public void verifyOtp(VerifyOtpDto dto) {
        if (!otpService.isValid(dto.email(), dto.otp())) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        otpService.markVerified(dto.email());
    }

    @Transactional
    public void resetPassword(ResetPasswordDto dto) {
        if (!otpService.isVerified(dto.email())) {
            throw new RuntimeException("OTP verification required");
        }
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        userLookupService.updatePassword(dto.email(), passwordEncoder.encode(dto.newPassword()));
        otpService.clear(dto.email());
    }
}