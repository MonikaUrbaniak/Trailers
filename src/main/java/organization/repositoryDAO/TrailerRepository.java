package organization.repositoryDAO;

import org.springframework.data.jpa.repository.JpaRepository;
import organization.entity.Trailer;

public interface TrailerRepository extends JpaRepository<Trailer, Long> {
}