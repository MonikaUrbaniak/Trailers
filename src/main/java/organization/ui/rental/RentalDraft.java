package organization.ui.rental;

import lombok.Getter;
import lombok.Setter;
import organization.entity.Client;
import organization.entity.Trailer;

import java.time.LocalDate;

@Getter
@Setter
public class RentalDraft {

    private Client client;
    private Trailer trailer;
    private String pickupLocation;
    private String returnLocation;
    private LocalDate contractDate;

    public void clearAll() {
        trailer = null;
        pickupLocation = null;
        returnLocation = null;
        contractDate = null;
    }

    public boolean hasClient() {
        return client != null;
    }

    public boolean hasTrailer() {
        return trailer != null;
    }

    public boolean hasLocationAndDateData() {
        return pickupLocation != null
                && returnLocation != null
                && contractDate != null;
    }

    public boolean isComplete() {
        return hasClient() && hasTrailer() && hasLocationAndDateData();
    }
}
