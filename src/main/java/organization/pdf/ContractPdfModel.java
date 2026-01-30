package organization.pdf;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ContractPdfModel {

    // META
    private LocalDate contractDate;
    private String signingPlace;

    // LESSOR
    private String lessorName;
    private String lessorCity;
    private String lessorAddress;
    private String lessorPhone;
    private String lessorRepresentative;

    //  CLIENT
    private String clientName;
    private String clientAddress;
    private String clientNip;
    private String clientPhone;
    private String clientIdNumber;
    private String clientIdIssuedBy;

    // TRAILER
    private String trailerRegistrationNumber;

    // §4
    private String pickupLocation;
    private String returnLocation;

    // §2 pkt 3
    private String handoverNotes;

    // OPTIONAL
    private String secondDriverName;
    private String secondDriverAddress;
}
