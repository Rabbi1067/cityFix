package bd.cityv1.forgotpassword;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final PasswordResetOtpRepository otpRepository;
    private static final int OTP_VALID_MINUTES = 5;

    @Transactional
    public String generateOtp(String email) {
        otpRepository.deleteByEmail(email);

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        PasswordResetOtp entity = PasswordResetOtp.builder()
                .email(email)
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES))
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(entity);
        return otp;
    }

    public boolean isValid(String email, String otp) {
        return otpRepository.findTopByEmailOrderByIdDesc(email)
                .filter(record -> record.getOtp().equals(otp))
                .filter(record -> record.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public void markVerified(String email) {
        otpRepository.findTopByEmailOrderByIdDesc(email).ifPresent(record -> {
            record.setVerified(true);
            otpRepository.save(record);
        });
    }

    public boolean isVerified(String email) {
        return otpRepository.findTopByEmailOrderByIdDesc(email)
                .map(PasswordResetOtp::isVerified)
                .orElse(false);
    }

    @Transactional
    public void clear(String email) {
        otpRepository.deleteByEmail(email);
    }
}