package bd.cityv1.profile.citizen.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePersonalInfoDto(
        @NotBlank(message = "Name cannot be empty") String name,
        @NotBlank(message = "Phone cannot be empty") String phone,
        @NotBlank(message = "Street cannot be empty") String street,
        @NotBlank(message = "City cannot be empty") String city,
        @NotBlank(message = "Zip code cannot be empty") String zipCode
) {
    public UpdatePersonalInfoDto() {
        this(null, null, null, null, null);
    }
}