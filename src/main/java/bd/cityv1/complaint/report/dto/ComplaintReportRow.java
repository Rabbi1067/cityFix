package bd.cityv1.complaint.report.dto;

import bd.cityv1.complaint.common.Status;

import java.time.LocalDateTime;

public record ComplaintReportRow(
        Long id,
        String location,
        String category,
        Status status,
        LocalDateTime submittedAt,
        LocalDateTime resolvedAt,
        long resolutionDays,
        Double estimatedCost,
        Double finalCost
) {
    public double costVariancePercent() {
        if (estimatedCost == null || estimatedCost == 0 || finalCost == null) {
            return 0;
        }
        return ((finalCost - estimatedCost) / estimatedCost) * 100;
    }
}
