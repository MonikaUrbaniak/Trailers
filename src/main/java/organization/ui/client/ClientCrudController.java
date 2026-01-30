package organization.ui.client;

import org.springframework.stereotype.Component;
import organization.entity.Client;
import organization.service.ClientService;

@Component
public class ClientCrudController {
    private final ClientService clientService;

    public ClientCrudController(ClientService clientService) {
        this.clientService = clientService;
    }

    public Client save(Client client) {
        return clientService.save(client);
    }

    public void delete(Client client) {
        if (client == null) return;
        clientService.delete(client);
    }
    //anuluj pozniej dodam, nic nie zwroci, wyczysci formularz
}