package bd.cityv1.admin.usermanagement;


import bd.cityv1.admin.register.Admin;
import bd.cityv1.admin.register.AdminRepository;
import bd.cityv1.admin.usermanagement.dto.AddCitizenRequest;
import bd.cityv1.admin.usermanagement.dto.UpdateCitizenRequest;
import bd.cityv1.citizen.register.Citizen;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CitizenAdminController {

    private final CitizenAdminService citizenAdminService;
    private final AdminRepository adminRepository;

    @GetMapping("/users")
    public String manageUsers(Model model, Authentication authentication) {

        List<Citizen> citizens = citizenAdminService.listCitizens();
        long newThisMonth = citizenAdminService.countNewThisMonth();

        Admin admin = adminRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        model.addAttribute("activePage", "manage-users");
        model.addAttribute("citizens", citizens);
        model.addAttribute("totalRegistered", citizens.size());
        model.addAttribute("newThisMonth", newThisMonth);
        model.addAttribute("admin", admin);

        if (!model.containsAttribute("newCitizen")) {
            model.addAttribute("newCitizen", new AddCitizenRequest());
        }

        return "admin/users";
    }

    @PostMapping("/users/add")
    public String saveUser(@Valid @ModelAttribute("newCitizen") AddCitizenRequest request,
                           BindingResult result,
                           Model model,
                           Authentication authentication) {

        if (result.hasErrors()) {
            return manageUsers(model, authentication);
        }

        try {
            citizenAdminService.addCitizen(request);
        } catch (IllegalArgumentException e) {
            String[] parts = e.getMessage().split(":", 2);
            result.rejectValue(parts[0], "error", parts[1]);
            return manageUsers(model, authentication);
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        citizenAdminService.deleteCitizen(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id, @ModelAttribute UpdateCitizenRequest request) {
        citizenAdminService.updateCitizen(id, request);
        return "redirect:/admin/users";
    }
}