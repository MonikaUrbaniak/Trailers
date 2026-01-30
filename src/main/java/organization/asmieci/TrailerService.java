//package organization.service;
//
//import org.springframework.stereotype.Service;
//import organization.entity.Trailer;
//import organization.repository.TrailerRepository;
//
//import java.util.List;
//
//@Service
//public class TrailerService {
//
//    private final TrailerRepository repository;
//
//    public TrailerService(TrailerRepository repository) {
//        this.repository = repository;
//    }
//
//    public List<Trailer> findAll() {
//        return repository.findAll();
//    }
//
//    public Trailer findById(Long id) {
//        return repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Trailer not found"));
//    }
//
//    public Trailer save(Trailer trailer) {
//        return repository.save(trailer);
//    }
//
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }
//}