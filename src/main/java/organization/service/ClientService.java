package organization.service;

import org.springframework.stereotype.Service;
import organization.entity.Client;
import organization.repositoryDAO.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getById(long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client c) {
        return clientRepository.save(c);
    }
    public void delete(Client c) {
        clientRepository.delete(c);
    }
}
