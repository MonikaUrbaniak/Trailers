package organization.ui.rental;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organization.dto.RentalContractHistoryDTO;
import organization.entity.Client;
import organization.service.RentalContractService;

import java.time.LocalDate;

@Component
public class RentalHistoryController {

    @FXML
    private TableView<RentalContractHistoryDTO> historyTable;

    @FXML private TableColumn<RentalContractHistoryDTO, LocalDate> dateCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> trailerCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> pickupCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> returnCol;

    @Autowired
    private RentalContractService rentalContractService;

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("contractDate"));
        trailerCol.setCellValueFactory(new PropertyValueFactory<>("trailerName"));
        pickupCol.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        returnCol.setCellValueFactory(new PropertyValueFactory<>("returnLocation"));
    }

    public void loadHistory(Client client) {
        historyTable.getItems().setAll(
                rentalContractService.getHistoryForClient(client)
        );
    }
}
