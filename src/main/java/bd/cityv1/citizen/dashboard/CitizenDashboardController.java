package bd.cityv1.citizen.dashboard;

import bd.cityv1.citizen.register.Citizen;
import bd.cityv1.citizen.register.CitizenRepository;
import bd.cityv1.complaint.common.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenDashboardController {

    private final CitizenRepository citizenRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Citizen citizen = citizenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Logged-in citizen not found for email: " + authentication.getName()));

        List<Complaint> complaints = citizen.getComplaints() != null
                ? citizen.getComplaints()
                : Collections.emptyList();

        model.addAttribute("activePage", "citizen-dashboard");
        model.addAttribute("citizen", citizen);
        model.addAttribute("complaints", complaints);

        return "citizen/citizenDashboard";
    }
}
