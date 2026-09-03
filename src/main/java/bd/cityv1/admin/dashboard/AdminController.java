package bd.cityv1.admin.dashboard;

import bd.cityv1.admin.dashboard.dto.ComplaintSummaryDto;
import bd.cityv1.admin.register.Admin;
import bd.cityv1.admin.register.AdminRepository;
import bd.cityv1.citizen.register.CitizenRepository;
import bd.cityv1.complaint.common.Complaint;
import bd.cityv1.complaint.common.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminRepository adminRepository;
    private final CitizenRepository citizenRepository;
    private final ComplaintRepository complaintRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        Admin admin = adminRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        List<Complaint> allComplaints = complaintRepository.findAllByOrderByCreatedAtDesc();

        long totalCitizens = citizenRepository.count();

        List<ComplaintSummaryDto> complaints = allComplaints.stream()
                .map(ComplaintSummaryDto::from)
                .collect(Collectors.toList());

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("admin", admin);
        model.addAttribute("totalCitizens", totalCitizens);
        model.addAttribute("complaints", complaints);

        return "admin/adminDashboard";
    }
}