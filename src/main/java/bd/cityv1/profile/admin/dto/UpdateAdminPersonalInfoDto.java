package bd.cityv1.profile.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAdminPersonalInfoDto(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Phone number is required") String phone,
        @NotBlank(message = "Department is required") String department
) {
    public UpdateAdminPersonalInfoDto() {
        this(null, null, null);
    }
}