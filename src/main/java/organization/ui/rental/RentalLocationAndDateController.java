package organization.ui.rental;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RentalLocationAndDateController {

    public static final String DEFAULT_LOCATION =
            "Żakowice ul. Piotrkowska 42c, 95-040 Koluszki";

    @FXML private ComboBox<String> pickupLocationBox;
    @FXML private ComboBox<String> returnLocationBox;
    @FXML private DatePicker contractDatePicker;

    @FXML
    public void initialize() {

        pickupLocationBox.setItems(FXCollections.observableArrayList(
                DEFAULT_LOCATION,
                ""
        ));

        returnLocationBox.setItems(FXCollections.observableArrayList(
                DEFAULT_LOCATION,
                ""
        ));

        pickupLocationBox.setEditable(true);
        returnLocationBox.setEditable(true);

        pickupLocationBox.setValue(DEFAULT_LOCATION);
        returnLocationBox.setValue(DEFAULT_LOCATION);

        contractDatePicker.setValue(LocalDate.now());
    }

    // ===== GETTERY (PDF / zapis) =====

    public String getPickupLocation() {
        return pickupLocationBox.getValue();
    }

    public String getReturnLocation() {
        return returnLocationBox.getValue();
    }

    public LocalDate getContractDate() {
        return contractDatePicker.getValue();
    }
}
