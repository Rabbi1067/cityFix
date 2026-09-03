package bd.cityv1.complaint.report;


import bd.cityv1.admin.register.Admin;
import bd.cityv1.admin.register.AdminRepository;
import bd.cityv1.complaint.report.dto.ComplaintReportRow;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/complaints/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ComplaintReportService complaintReportService;
    private final AdminRepository adminRepository;

    @GetMapping
    public String reports(Model model, Authentication authentication) {
        Admin admin = adminRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        model.addAttribute("admin", admin);

        List<ComplaintReportRow> rows = complaintReportService.getResolutionReport();
        model.addAttribute("rows", rows);
        model.addAttribute("dashboardRows", complaintReportService.getDashboardRows());

        return "admin/complaintReports";
    }
}
