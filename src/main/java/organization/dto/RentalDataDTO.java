package organization.dto;

import lombok.Getter;
import lombok.Setter;
import organization.entity.Client;
import organization.entity.Trailer;

import java.time.LocalDate;
@Getter
@Setter
public class RentalDataDTO {

    private Client client;
    private Trailer trailer;

    private String pickupLocation;
    private String returnLocation;
    private LocalDate contractDate;

}
