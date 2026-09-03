package bd.cityv1.forgotpassword;

import bd.cityv1.admin.register.AdminRepository;
import bd.cityv1.citizen.register.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLookupService {

    private final AdminRepository adminRepository;
    private final CitizenRepository citizenRepository;

    public boolean emailExists(String email) {
        return adminRepository.findByEmail(email).isPresent()
                || citizenRepository.findByEmail(email).isPresent();
    }

    public void updatePassword(String email, String encodedPassword) {
        adminRepository.findByEmail(email).ifPresentOrElse(
                admin -> {
                    admin.setPassword(encodedPassword);
                    adminRepository.save(admin);
                },
                () -> citizenRepository.findByEmail(email).ifPresent(citizen -> {
                    citizen.setPassword(encodedPassword);
                    citizenRepository.save(citizen);
                })
        );
    }
}