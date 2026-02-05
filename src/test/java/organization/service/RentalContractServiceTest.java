package organization.service;

import organization.dto.RentalDataDTO;
import organization.entity.Client;
import organization.entity.RentalContract;
import organization.entity.Trailer;
import organization.repositoryDAO.RentalContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalContractServiceTest {

    @Mock
    private RentalContractRepository repository;

    @InjectMocks
    private RentalContractService service;

    @Test
    void shouldSaveRentalContract() {
        // GIVEN
        Client client = new Client();
        client.setId(1L);

        Trailer trailer = new Trailer();
        trailer.setId(2L);

        RentalDataDTO dto = new RentalDataDTO();
        dto.setClient(client);
        dto.setTrailer(trailer);

        RentalContract saved = new RentalContract();
        saved.setId(10L);

        when(repository.save(any()))
                .thenReturn(saved);
        // WHEN
        RentalContract result =
                service.saveContract(dto);
        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);

        verify(repository).save(any());
    }
}
