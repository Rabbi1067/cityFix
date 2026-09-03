package bd.cityv1.complaint.report;

import bd.cityv1.complaint.common.ComplaintRepository;
import bd.cityv1.complaint.report.dto.ComplaintReportRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintReportService {

    private final ComplaintRepository complaintRepository;

    public List<ComplaintReportRow> getDashboardRows() {
        return complaintRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(c -> new ComplaintReportRow(
                        c.getId(),
                        c.getLocation(),
                        c.getCategory(),
                        c.getStatus(),
                        c.getCreatedAt(),
                        c.getResolution() == null ? null : c.getResolution().getResolvedAt(),
                        c.getResolution() == null || c.getResolution().getResolvedAt() == null
                                ? 0
                                : Duration.between(c.getCreatedAt(), c.getResolution().getResolvedAt()).toDays(),
                        c.getEstimatedCost(),
                        c.getFinalCost()
                ))
                .toList();
    }

    public List<ComplaintReportRow> getResolutionReport() {
        return complaintRepository.findAllWithResolutionDetails().stream()
                .map(c -> new ComplaintReportRow(
                        c.getId(),
                        c.getLocation(),
                        c.getCategory(),
                        c.getStatus(),
                        c.getCreatedAt(),
                        c.getResolution() == null
                                ? null
                                : c.getResolution().getResolvedAt(),
                        c.getResolution() == null
                                || c.getResolution().getResolvedAt() == null
                                ? 0
                                : Duration.between(
                                c.getCreatedAt(),
                                c.getResolution().getResolvedAt()
                        ).toDays(),
                        c.getEstimatedCost(),
                        c.getFinalCost()
                ))
                .toList();
    }
}
