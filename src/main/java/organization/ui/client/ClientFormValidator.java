package organization.ui.client;

import org.springframework.stereotype.Component;
import organization.enums.ClientType;

@Component
public class ClientFormValidator {
    public ValidationResult validate(
            ClientType type,
            String firstName,
            String lastName,
            String company,
            String nip,
            String address,
            String phone,
            String idNumber
    ) {

        if (type == null) {
            return ValidationResult.error("Brak typu", "Wybierz typ klienta.");
        }

        if (isBlank(firstName)) {
            return ValidationResult.error("Brak imienia", "Imię jest wymagane.");
        }

        if (isBlank(lastName)) {
            return ValidationResult.error("Brak nazwiska", "Nazwisko jest wymagane.");
        }

        if (isBlank(address)) {
            return ValidationResult.error("Brak adresu", "Adres jest wymagany.");
        }

        if (isBlank(phone)) {
            return ValidationResult.error("Brak telefonu", "Telefon jest wymagany.");
        }

        if (isBlank(idNumber)) {
            return ValidationResult.error("Brak dokumentu", "Numer dokumentu jest wymagany.");
        }

        if (type == ClientType.COMPANY) {
            if (isBlank(company)) {
                return ValidationResult.error("Brak firmy", "Nazwa firmy jest wymagana.");
            }
            if (isBlank(nip)) {
                return ValidationResult.error("Brak NIP", "NIP jest wymagany.");
            }
        }

        return ValidationResult.ok();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
