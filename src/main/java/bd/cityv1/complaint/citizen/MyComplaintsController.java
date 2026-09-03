package bd.cityv1.complaint.citizen;

import bd.cityv1.citizen.register.Citizen;
import bd.cityv1.citizen.register.CitizenRepository;
import bd.cityv1.complaint.ComplaintService;
import bd.cityv1.complaint.common.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class MyComplaintsController {

    private final ComplaintService complaintService;
    private final CitizenRepository citizenRepository;

    @PostMapping("/complaints/{id}/rating")
    public String saveRating(@PathVariable Long id,
                             @RequestParam Integer rating,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        Citizen citizen = citizenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Logged-in citizen not found for email: " + authentication.getName()));

        try {
            complaintService.saveCitizenRating(id, citizen.getId(), rating);
            redirectAttributes.addFlashAttribute("ratingSuccess", "Thank you. Your feedback has been recorded.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("ratingError", ex.getMessage());
        }

        return "redirect:/citizen/my-complaints";
    }

    @GetMapping("/my-complaints")
    public String myComplaints(Authentication authentication, Model model) {

        Citizen citizen = citizenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Logged-in citizen not found for email: " + authentication.getName()));

        Long citizenId = citizen.getId();
        List<Complaint> complaints = complaintService.findAllForCitizen(citizenId);

        model.addAttribute("citizen", citizen);
        model.addAttribute("complaints", complaints);
        model.addAttribute("activeCount", complaintService.countActive(citizenId));
        model.addAttribute("resolvedCount", complaintService.countResolved(citizenId));
        model.addAttribute("activePage", "my-complaints");

        return "citizen/myComplaints";
    }
}
