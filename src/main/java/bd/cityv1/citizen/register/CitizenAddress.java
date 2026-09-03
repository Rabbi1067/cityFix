package bd.cityv1.citizen.register;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CitizenAddress {
    private String street;
    private String city;
    private String zipCode;
}