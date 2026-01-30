package organization.service;

import org.springframework.stereotype.Service;
import organization.entity.Trailer;
import organization.repositoryDAO.TrailerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TrailerService {
    private final TrailerRepository trailerRepository;

    public TrailerService(TrailerRepository repo){
        this.trailerRepository = repo;
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

    public void delete(Trailer trailer){
        trailerRepository.delete(trailer);
    }
}
