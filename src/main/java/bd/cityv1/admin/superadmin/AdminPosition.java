package bd.cityv1.admin.superadmin;
public enum AdminPosition {
    GENERAL_ADMIN("General Admin"),
    COMPLAINT_MANAGER("Complaint Manager"),
    USER_MANAGER("User Manager"),
    IT_ADMIN("IT & Technical"),
    PUBLIC_RELATIONS("Public Relations");

    private final String label;

    AdminPosition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}