package organization.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import organization.entity.RentalContract;
import organization.service.RentalContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organization.ui.common.PdfOpener;

import java.nio.file.Path;

@Component
public class LastRentalsPopupController {

    @FXML private TableView<RentalContract> table;
    @FXML private TableColumn<RentalContract, String> dateCol;
    @FXML private TableColumn<RentalContract, String> clientCol;
    @FXML private TableColumn<RentalContract, String> phoneCol;
    @FXML private TableColumn<RentalContract, String> trailerCol;
    @FXML private ComboBox<Integer> limitBox;

    @Autowired
    private PdfOpener pdfOpener;

    @Autowired
    private RentalContractService rentalContractService;

    @FXML
    public void initialize() {

        dateCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getContractDate() != null
                                ? c.getValue().getContractDate().toString()
                                : ""
                )
        );

        clientCol.setCellValueFactory(c -> {
            var client = c.getValue().getClient();
            String name = client == null
                    ? "-"
                    : client.getFirstName() + " " + client.getLastName();
            return new SimpleStringProperty(name);
        });

        phoneCol.setCellValueFactory(c -> {
            var client = c.getValue().getClient();
            String phone = client == null
                    ? "-"
                    : client.getPhone();
            return new SimpleStringProperty(phone);
        });

        trailerCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getTrailer() != null
                                ? c.getValue().getTrailer().getName()
                                : "-"
                )
        );

        limitBox.getItems().addAll(5, 10, 15, 20, 25, 30);
        limitBox.setValue(10);

        loadData(10);

        limitBox.setOnAction(e ->
                loadData(limitBox.getValue())
        );

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                RentalContract selected =
                        table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openPdf(selected);
                }
            }
        });
    }

    private void loadData(int limit) {
        table.setItems(
                FXCollections.observableArrayList(
                        rentalContractService.getLastContracts(limit)
                )
        );
    }

    private void openPdf(RentalContract contract) {
        Long id = contract.getId();
        if (id == null) {
            return;
        }
        Path path = Path.of(
                "contracts",
                "umowa_" + id + ".pdf"
        );
        pdfOpener.openPdf(path);
    }
}

