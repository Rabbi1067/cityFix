package bd.cityv1.admin.usermanagement.dto;

import bd.cityv1.citizen.register.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCitizenRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String nationalId;
    private Gender gender;
    private String occupation;

    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Zip code cannot be empty")
    private String zipCode;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}