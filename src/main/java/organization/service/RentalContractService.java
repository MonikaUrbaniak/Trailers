package organization.service;

import organization.dto.RentalContractHistoryDTO;
import organization.dto.RentalDataDTO;
import organization.entity.RentalContract;
import organization.repositoryDAO.RentalContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organization.entity.Client;
import java.util.List;


@Service
public class RentalContractService {

    private final RentalContractRepository rentalContractRepository;

    public RentalContractService(RentalContractRepository rentalContractRepository) {
        this.rentalContractRepository = rentalContractRepository;
    }

    @Transactional
    public RentalContract saveContract(RentalDataDTO dto) {

        RentalContract contract = new RentalContract();
        // POWIĄZANIA
        contract.setClient(dto.getClient());
        contract.setTrailer(dto.getTrailer());
        // DANE UMOWY
        contract.setPickupLocation(dto.getPickupLocation());
        contract.setReturnLocation(dto.getReturnLocation());
        contract.setContractDate(dto.getContractDate());
        return rentalContractRepository.save(contract);
    }

    @Transactional(readOnly = true)
    public List<RentalContractHistoryDTO> getHistoryForClient(Client client) {
        if (client == null) {
            return List.of();
        }
        return rentalContractRepository
                .findByClientOrderByContractDateDesc(client)
                .stream()
                .map(this::toHistoryDto)
                .toList();
    }

    private RentalContractHistoryDTO toHistoryDto(RentalContract c) {
        RentalContractHistoryDTO dto = new RentalContractHistoryDTO();
        dto.setContractId(c.getId());
        dto.setContractDate(c.getContractDate());
//        dto.setTrailerName(c.getTrailer().getName());
        dto.setTrailerName(
                c.getTrailer() != null
                        ? c.getTrailer().getName()
                        : "[Usunięta]"
        );

        dto.setPickupLocation(c.getPickupLocation());
        dto.setReturnLocation(c.getReturnLocation());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<RentalContract> getLast15Contracts() {
        return rentalContractRepository.findTop15ByOrderByContractDateDesc();
    }


}

