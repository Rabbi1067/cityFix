package bd.cityv1.admin.dashboard.dto;

import bd.cityv1.complaint.common.Complaint;

public record ComplaintSummaryDto(
        Long id,
        String title,
        String category,
        String status,
        String priority,
        String location,
        String citizenName,
        String createdAt
) {
    public static ComplaintSummaryDto from(Complaint c) {
        return new ComplaintSummaryDto(
                c.getId(),
                c.getTitle(),
                c.getCategory(),
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getPriority() != null ? c.getPriority().name() : null,
                c.getLocation(),
                c.getCitizen() != null ? c.getCitizen().getName() : null,
                c.getCreatedAt() != null ? c.getCreatedAt().toString() : null
        );
    }
}