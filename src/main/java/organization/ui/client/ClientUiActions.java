package organization.ui.client;

import org.springframework.stereotype.Component;
import organization.entity.Client;
import organization.service.ClientType;
import organization.ui.common.DialogService;

import java.util.Optional;

@Component
public class ClientUiActions {
    private final ClientCrudController clientCrudController;
    private final ClientFormValidator validator;
    private final DialogService dialogService;

    public ClientUiActions(
            ClientCrudController clientCrudController,
            ClientFormValidator validator,
            DialogService dialogService
    ) {
        this.clientCrudController = clientCrudController;
        this.validator = validator;
        this.dialogService = dialogService;
    }

    public Optional<Client> saveClient(
            Client currentClient,
            ClientType type,
            String firstName,
            String lastName,
            String companyName,
            String nip,
            String address,
            String phone,
            String idNumber,
            String idIssuedBy
    ) {
        var result = validator.validate(
                type,
                firstName,
                lastName,
                companyName,
                nip,
                address,
                phone,
                idNumber
        );

        if (!result.isValid()) {
            dialogService.warn(result.getTitle(), result.getMessage());
            return Optional.empty();
        }

        Client toSave = (currentClient != null) ? currentClient : new Client();

        toSave.setType(type);
        toSave.setFirstName(firstName);
        toSave.setLastName(lastName);
        toSave.setCompanyName(companyName);
        toSave.setNip(nip);
        toSave.setAddress(address);
        toSave.setPhone(phone);
        toSave.setIdNumber(idNumber);
        toSave.setIdIssuedBy(idIssuedBy);

        Client saved = clientCrudController.save(toSave);
        return Optional.of(saved);
    }

    public void deleteClient(Client client) {
        if (client == null) return;
        clientCrudController.delete(client);
    }
}