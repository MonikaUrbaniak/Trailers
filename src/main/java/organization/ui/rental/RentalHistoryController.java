package organization.ui.rental;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organization.dto.RentalContractHistoryDTO;
import organization.entity.Client;
import organization.service.RentalContractService;
import organization.ui.common.DialogService;
import organization.ui.common.PdfOpener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class RentalHistoryController {

    @FXML
    private TableView<RentalContractHistoryDTO> historyTable;

    @FXML private TableColumn<RentalContractHistoryDTO, LocalDate> dateCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> trailerCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> pickupCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> returnCol;
    @FXML private TableColumn<RentalContractHistoryDTO, String> idCol;

    @Autowired
    private PdfOpener pdfOpener;

    @Autowired
    private RentalContractService rentalContractService;
    @Autowired
    private DialogService dialogService;

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("contractDate"));
        trailerCol.setCellValueFactory(new PropertyValueFactory<>("trailerName"));
        pickupCol.setCellValueFactory(new PropertyValueFactory<>("pickupLocation"));
        returnCol.setCellValueFactory(new PropertyValueFactory<>("returnLocation"));
        idCol.setCellValueFactory(cellData -> {
            Long id = cellData.getValue().getContractId();
            String text = (id == null)
                    ? ""
                    : "umowa_" + id;
            return new SimpleStringProperty(text);
        });
        historyTable.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                RentalContractHistoryDTO selected =
                        historyTable.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    openPdf(selected);
                }
            }
        });
    }

    public void loadHistory(Client client) {
        historyTable.getItems().setAll(
                rentalContractService.getHistoryForClient(client)
        );
    }

    private void openPdf(RentalContractHistoryDTO dto) {

        if (dto == null || dto.getContractId() == null) return;

        Path path = Path.of("contracts", "umowa_" + dto.getContractId() + ".pdf");

        if (!Files.exists(path)) {
            dialogService.warn("Brak pliku", "Nie znaleziono PDF");
            return;
        }

        pdfOpener.openPdf(path);
    }

}
