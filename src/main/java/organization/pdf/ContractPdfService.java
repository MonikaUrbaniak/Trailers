package organization.pdf;

import organization.entity.Client;
import organization.entity.RentalContract;
import organization.entity.Trailer;
import org.springframework.stereotype.Service;
import organization.enums.ClientType;

@Service
public class ContractPdfService {

    public ContractPdfModel buildModel(RentalContract contract) {

        Client client = contract.getClient();
        Trailer trailer = contract.getTrailer();

        ContractPdfModel m = new ContractPdfModel();

        // --- META ---
        m.setContractDate(contract.getContractDate());
        m.setSigningPlace("Żakowice");

        // --- LESSOR ---
        m.setLessorName("P.P.H.U. EKO -G.M.I.P.  Jarosławem Urbaniakiem");
        m.setLessorCity("Żakowicach");
        m.setLessorAddress("ul. Piotrkowska 42C");
        m.setLessorPhone("888 135 388");
        m.setLessorRepresentative("Jarosława Urbaniaka");

        // --- CLIENT ---
        m.setClientName(client.getFirstName() + " " + client.getLastName());
        m.setClientAddress(client.getAddress());

        if (client.getType() == ClientType.COMPANY) {
            m.setClientCompanyLine(
                    "firma: <strong>" + client.getCompanyName() + "</strong>,<br/>"
            );
            m.setClientNipLine(
                    "NIP: <strong>" + client.getNip() + "</strong>,<br/>"
            );
        } else {
            m.setClientCompanyLine("");
            m.setClientNipLine("");
        }

        m.setClientPhone(client.getPhone());
        m.setClientIdNumber(client.getIdNumber());
        m.setClientIdIssuedBy(client.getIdIssuedBy());

        // --- TRAILER ---
        m.setTrailerRegistrationNumber(trailer.getRegistrationNumber());

        // --- §4 ---
        m.setPickupLocation(contract.getPickupLocation());
        m.setReturnLocation(contract.getReturnLocation());

        // --- §2 pkt 3 ---
        m.setHandoverNotes("");

        return m;
    }
}
