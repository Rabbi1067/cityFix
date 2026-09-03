package bd.cityv1.complaint.admin.dto;


import bd.cityv1.complaint.common.Priority;
import bd.cityv1.complaint.common.Status;

public record ResolveComplaintRequest(
        Status status,
        Priority priority,
        Double finalCost,
        String costNotes
) {
    public ResolveComplaintRequest() {
        this(null, null, null, null);
    }
}