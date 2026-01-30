package organization.repositoryDAO;

import org.springframework.stereotype.Repository;
import organization.entity.RentalContract;
import organization.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface RentalContractRepository extends JpaRepository<RentalContract, Long> {

    // Historia umów dla klienta
    List<RentalContract> findByClientOrderByContractDateDesc(Client client);
}
