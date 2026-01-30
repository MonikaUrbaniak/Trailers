package organization.repositoryDAO;

import org.springframework.data.jpa.repository.JpaRepository;
import organization.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

}