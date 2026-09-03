package bd.cityv1.profile.admin;

import bd.cityv1.admin.register.Admin;
import bd.cityv1.admin.register.AdminRepository;
import bd.cityv1.profile.admin.dto.UpdateAdminPersonalInfoDto;
import bd.cityv1.profile.common.ChangePasswordDto;
import bd.cityv1.profile.common.PasswordChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProfileService {

    private final AdminRepository adminRepository;
    private final PasswordChangeService passwordChangeService;

    public Admin getAdmin(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public Admin updatePersonalInfo(String email, UpdateAdminPersonalInfoDto dto) {
        Admin admin = getAdmin(email);

        admin.setName(dto.name());
        admin.getContactInfo().setPhone(dto.phone());
        admin.getContactInfo().setDepartment(dto.department());

        return adminRepository.save(admin);
    }

    public void changePassword(String email, ChangePasswordDto dto) {
        Admin admin = getAdmin(email);
        admin.setPassword(passwordChangeService.validateAndEncode(dto, admin.getPassword()));
        adminRepository.save(admin);
    }
}