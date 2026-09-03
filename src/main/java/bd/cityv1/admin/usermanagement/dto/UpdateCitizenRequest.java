package bd.cityv1.admin.usermanagement.dto;

import bd.cityv1.citizen.register.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCitizenRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    private String nationalId;
    private Gender gender;
    private String occupation;

    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Zip code cannot be empty")
    private String zipCode;
}