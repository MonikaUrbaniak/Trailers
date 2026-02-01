package organization.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RentalContractHistoryDTO {

    private LocalDate contractDate;
    private String trailerName;
    private String pickupLocation;
    private String returnLocation;
    private Long contractId;
}
