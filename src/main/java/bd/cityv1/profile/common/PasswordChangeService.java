package bd.cityv1.profile.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {

    private final PasswordEncoder passwordEncoder;

    public String validateAndEncode(ChangePasswordDto dto, String storedHash) {
        if (!passwordEncoder.matches(dto.currentPassword(), storedHash)) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        if (dto.newPassword().equals(dto.currentPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }
        return passwordEncoder.encode(dto.newPassword());
    }
}