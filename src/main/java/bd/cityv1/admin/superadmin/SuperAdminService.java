package bd.cityv1.admin.superadmin;


import bd.cityv1.admin.register.Admin;
import bd.cityv1.admin.register.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin currentAdmin(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
    }

    public List<Admin> listAdmins(String query) {
        return (query == null || query.isBlank())
                ? adminRepository.findAll()
                : adminRepository.searchByNameOrEmail(query.trim());
    }

    public AdminStats getStats() {
        long total = adminRepository.count();
        long active = adminRepository.findAll().stream().filter(Admin::isEnabled).count();
        return new AdminStats(total, active, total - active);
    }

    public void saveAdmin(AddAdminRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("confirmPassword:Passwords do not match");
        }

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("email:This email is already registered");
        }

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPosition(request.getPosition());
        admin.setRoles(new ArrayList<>(List.of("ADMIN")));
        admin.setEnabled(true);

        adminRepository.save(admin);
    }

    public void blockPermanently(Long targetId, String currentAdminEmail) {
        applyBlock(targetId, currentAdminEmail, null);
    }

    public void blockTemporarily(Long targetId, String currentAdminEmail, int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Enter at least 1 day for a temporary block.");
        }
        applyBlock(targetId, currentAdminEmail, LocalDateTime.now().plusDays(days));
    }

    public void unblock(Long targetId) {
        Admin target = findById(targetId);
        target.setEnabled(true);
        target.setBlockedUntil(null);
        adminRepository.save(target);
    }

    public void deleteAdmin(Long targetId, String currentAdminEmail) {
        Admin target = findById(targetId);
        Admin currentAdmin = currentAdmin(currentAdminEmail);

        ensureNotSelf(target, currentAdmin, "delete");

        boolean isTargetSuperAdmin = target.getRoles().contains("SUPER_ADMIN");
        long superAdminCount = adminRepository.findAll().stream()
                .filter(a -> a.getRoles().contains("SUPER_ADMIN"))
                .count();

        if (isTargetSuperAdmin && superAdminCount <= 1) {
            throw new IllegalStateException("Cannot delete the last SUPER_ADMIN.");
        }

        adminRepository.delete(target);
    }


    private void applyBlock(Long targetId, String currentAdminEmail, LocalDateTime blockedUntil) {
        Admin target = findById(targetId);
        Admin currentAdmin = currentAdmin(currentAdminEmail);

        ensureNotSelf(target, currentAdmin, "block");

        target.setEnabled(false);
        target.setBlockedUntil(blockedUntil);
        adminRepository.save(target);
    }

    private Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + id));
    }

    private void ensureNotSelf(Admin target, Admin current, String action) {
        if (target.getId().equals(current.getId())) {
            throw new IllegalStateException("You can't " + action + " your own account.");
        }
    }
}