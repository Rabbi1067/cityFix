package bd.cityv1.admin.superadmin;

import bd.cityv1.admin.register.Admin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/admins")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping
    public String listAdmins(@RequestParam(value = "query", required = false) String query,
                             Model model, Authentication authentication) {

        Admin currentAdmin = superAdminService.currentAdmin(authentication.getName());
        List<Admin> admins = superAdminService.listAdmins(query);
        AdminStats stats = superAdminService.getStats();

        model.addAttribute("activePage", "manage-admins");
        model.addAttribute("admin", currentAdmin);
        model.addAttribute("admins", admins);
        model.addAttribute("stats", stats);
        model.addAttribute("currentAdminId", currentAdmin.getId());
        model.addAttribute("query", query);
        model.addAttribute("positions", AdminPosition.values());

        if (!model.containsAttribute("addAdminRequest")) {
            model.addAttribute("addAdminRequest", new AddAdminRequest());
        }

        return "admin/admins";
    }

    @PostMapping("/add")
    public String saveAdmin(@Valid @ModelAttribute("addAdminRequest") AddAdminRequest request,
                            BindingResult result,
                            Model model,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return listAdmins(null, model, authentication);
        }

        try {
            superAdminService.saveAdmin(request);
        } catch (IllegalArgumentException e) {
            String[] parts = e.getMessage().split(":", 2);
            result.rejectValue(parts[0], "error", parts[1]);
            return listAdmins(null, model, authentication);
        }

        redirectAttributes.addFlashAttribute("success", "New admin added successfully!");
        return "redirect:/admin/admins";
    }

    @PostMapping("/{id}/block")
    public String blockPermanently(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.blockPermanently(id, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Admin blocked permanently.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/admins";
    }

    @PostMapping("/{id}/block-temp")
    public String blockTemporarily(@PathVariable Long id,
                                   @RequestParam("days") int days,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            superAdminService.blockTemporarily(id, authentication.getName(), days);
            redirectAttributes.addFlashAttribute("success", "Admin blocked for " + days + " day(s).");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/admins";
    }

    @PostMapping("/{id}/unblock")
    public String unblock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        superAdminService.unblock(id);
        redirectAttributes.addFlashAttribute("success", "Admin unblocked.");
        return "redirect:/admin/admins";
    }

    @PostMapping("/{id}/delete")
    public String deleteAdmin(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            superAdminService.deleteAdmin(id, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Admin deleted.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/admins";
    }
}