package organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organization.entity.RentalContract;
import organization.entity.Trailer;
import organization.repositoryDAO.RentalContractRepository;
import organization.repositoryDAO.TrailerRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TrailerService {
    private final RentalContractRepository rentalContractRepository;
    private final TrailerRepository trailerRepository;

    public TrailerService(
            TrailerRepository trailerRepository,
            RentalContractRepository rentalContractRepository
    ) {
        this.trailerRepository = trailerRepository;
        this.rentalContractRepository = rentalContractRepository;
    }

    public List<Trailer> getAllTrailers() {
        return trailerRepository.findAll();
    }

    public Optional<Trailer> getById(long id){
        return trailerRepository.findById(id);
    }

    public Trailer save(Trailer trailer){
        return trailerRepository.save(trailer);
    }

    @Transactional
    public void delete(Trailer trailer) {
        // 1. Znajdź wszystkie umowy z tą przyczepą
        List<RentalContract> contracts =
                rentalContractRepository.findByTrailer(trailer);
        // 2. Odczep przyczepę
        for (RentalContract c : contracts) {
            c.setTrailer(null);
        }
        rentalContractRepository.saveAll(contracts);
        // 3. Usuń przyczepę
        trailerRepository.delete(trailer);
    }

}
