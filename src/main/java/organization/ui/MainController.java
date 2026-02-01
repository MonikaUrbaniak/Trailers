package organization.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import organization.entity.Client;
import organization.entity.Trailer;
import organization.pdf.ContractPdfFacade;
import organization.service.ClientService;
import organization.enums.ClientType;
import organization.service.RentalContractService;
import organization.ui.client.ClientCrudController;
import organization.ui.client.ClientFormValidator;
import organization.ui.client.ClientTableController;
import organization.ui.client.ClientUiActions;
import organization.ui.common.DialogService;
import organization.ui.common.PdfOpener;
import organization.ui.rental.RentalFlowController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import organization.ui.trailer.TrailerEditController;


@Component
public class MainController {

    // ===== LEWA STRONA =====
    @FXML private TextField searchField;
    @FXML private TableView<ClientRow> clientsTable; //Którego klienta użytkownik MA TERAZ zaznaczonego
    @FXML private TableColumn<ClientRow, String> nameColumn;
    @FXML private TableColumn<ClientRow, String> phoneColumn;
    @FXML private Button refreshButton;
    @FXML private Button addButton;

    // ===== ŚRODEK =====
    @FXML private ComboBox<ClientType> typeComboBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private Label companyLabel;
    @FXML private TextField companyField;
    @FXML private Label nipLabel;
    @FXML private TextField nipField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField idNumberField;
    @FXML private TextField idIssuedByField;
    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button generateContractButton;
    @FXML private Button rentTrailerButton;
    @FXML private Button historyButton;
    // ===== PRZYCZEPA + FORMULARZ =====
    @FXML private VBox trailerInfoBox;
    @FXML private Label selectedTrailerLabel;
    // ===== PRAWA STRONA =====
    @FXML private StackPane rightPanel;
   // -------------
    @FXML private StackPane trailerEditContainer;
    @FXML private Button editTrailerButton;
    @FXML private Button lastTenRentalsButton;


    @Autowired
    private ClientService clientService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RentalContractService rentalContractService;
    @Autowired
    private ContractPdfFacade contractPdfFacade;
    @Autowired
    private DialogService dialogService;
    @Autowired
    private PdfOpener pdfOpener;
    @Autowired
    private RentalFlowController rentalFlowController;
    @Autowired
    private ClientCrudController clientCrudController;
    @Autowired
    private ClientFormValidator validator;
    @Autowired
    private ClientUiActions clientUiActions;
    @Autowired
    private ClientTableController clientTableController;

    private Node trailerEditView;
    private Client currentClient;

    private TrailerEditController trailerEditController;

    @FXML
    public void initialize() {
        initTable();
        initFiltering();
        initClientTypeSelector();
        initActions();
        initInitialState();
        rentalFlowController.init(trailerInfoBox, selectedTrailerLabel, rightPanel);
        lastTenRentalsButton.setOnAction(e -> openLastFifteenPopup());
    }


    private void initTable() {
        nameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        phoneColumn.setCellValueFactory(c -> c.getValue().phoneProperty());
        clientTableController.bind(clientsTable);
        clientTableController.onSelection(row ->
                clientService.getById(row.getId()).ifPresent(this::showClient)
        );
    }

    private void initFiltering() {
        searchField.textProperty().addListener(
                (obs, oldVal, newVal) -> clientTableController.filter(newVal)
        );
    }

    private void initClientTypeSelector() {
        typeComboBox.setItems(
                FXCollections.observableArrayList(ClientType.values())
        );
        typeComboBox.valueProperty()
                .addListener((obs, o, n) -> updateVisibility(n));
    }

    private void initActions() {
        addButton.setOnAction(e -> createNewClient());
        editButton.setOnAction(e -> enableEdit(true));
        saveButton.setOnAction(e -> saveClient());
        deleteButton.setOnAction(e -> deleteClient());
        refreshButton.setOnAction(e -> loadClientsFromDb());
        rentTrailerButton.setOnAction(e -> rentalFlowController.showTrailerPanel());
        generateContractButton.setOnAction(e -> rentalFlowController.saveRentalContract());
        historyButton.setOnAction(e -> rentalFlowController.showRentalHistory());
//        editTrailerButton.setOnAction(e -> showTrailerEditPanel());
        editTrailerButton.setOnAction(e -> {
            showTrailerEditPanel();

            if (trailerEditController != null) {
                trailerEditController.startEdit();
            }
        });
    }

    private void initInitialState() {
        rentTrailerButton.setDisable(true);
        trailerInfoBox.setVisible(false);
        enableEdit(true);
        Platform.runLater(this::loadClientsFromDb);
    }

    private void updateVisibility(ClientType type) {
        boolean company = type == ClientType.COMPANY;
        companyLabel.setVisible(company);
        companyField.setVisible(company);
        nipLabel.setVisible(company);
        nipField.setVisible(company);

        if (!company) {
            companyField.clear();
            nipField.clear();
        }
    }

    private void createNewClient() {
        currentClient = null;
        clearForm();
        typeComboBox.setValue(ClientType.PRIVATE);
        enableEdit(true);
        rentTrailerButton.setDisable(true);
// tu -disable genereatecontract
        rightPanel.getChildren().clear();
        rentalFlowController.clearSelectedTrailer();
        rentalFlowController.setClient(null);
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        companyField.clear();
        nipField.clear();
        addressField.clear();
        phoneField.clear();
        idNumberField.clear();
        idIssuedByField.clear();
    }

    private void saveClient() {

        var savedOpt = clientUiActions.saveClient(
                currentClient,
                typeComboBox.getValue(),
                firstNameField.getText(),
                lastNameField.getText(),
                companyField.getText(),
                nipField.getText(),
                addressField.getText(),
                phoneField.getText(),
                idNumberField.getText(),
                idIssuedByField.getText()
        );

        if (savedOpt.isEmpty()) {
            return; // walidacja już pokazała warn
        }

        currentClient = savedOpt.get();
        rentalFlowController.setClient(currentClient);

        loadClientsFromDb();
        clientTableController.selectById(currentClient.getId());
        enableEdit(false);
    }

    private void deleteClient() {
        if (currentClient == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Usunąć klienta?");
        if (a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            clientUiActions.deleteClient(currentClient);
            loadClientsFromDb();
        }
    }

    private static String newVal(String s) {
        return s == null ? "" : s;
    }

    private void showClient(Client c) { //ui
        currentClient = c;
        rentalFlowController.setClient(c);

        typeComboBox.setValue(c.getType());
        updateVisibility(c.getType());

        firstNameField.setText(newVal(c.getFirstName()));
        lastNameField.setText(newVal(c.getLastName()));
        companyField.setText(newVal(c.getCompanyName()));
        nipField.setText(newVal(c.getNip()));
        addressField.setText(newVal(c.getAddress()));
        phoneField.setText(newVal(c.getPhone()));
        idNumberField.setText(newVal(c.getIdNumber()));
        idIssuedByField.setText(newVal(c.getIdIssuedBy()));

        rentTrailerButton.setDisable(false);
        enableEdit(false);
        historyButton.setDisable(false);

        rentalFlowController.refreshHistoryIfVisible();
    }

    private void enableEdit(boolean editable) {
        typeComboBox.setDisable(!editable);
        firstNameField.setDisable(!editable);
        lastNameField.setDisable(!editable);
        companyField.setDisable(!editable);
        nipField.setDisable(!editable);
        addressField.setDisable(!editable);
        phoneField.setDisable(!editable);
        idNumberField.setDisable(!editable);
        idIssuedByField.setDisable(!editable);

        saveButton.setDisable(!editable);
        deleteButton.setDisable(!editable || currentClient == null);
        editButton.setDisable(editable);

        rentTrailerButton.setDisable(editable);
        generateContractButton.setDisable(editable);
    }

    private void loadClientsFromDb() {
        clientTableController.refresh(
                clientService.getAllClients(),
                currentClient
        );
    }

    private void loadTrailerEdit() {

        if (trailerEditView != null) return;

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/ui/TrailerEditPane.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            trailerEditView = loader.load();
            trailerEditController = loader.getController();
            trailerEditController.setOnCancel(this::hideTrailerEditPanel);
            trailerEditController.setOnSave(this::afterTrailerSaved);
            trailerEditContainer.getChildren().setAll(trailerEditView);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showTrailerEditPanel() {
        loadTrailerEdit();
        trailerEditContainer.setManaged(true);
        trailerEditContainer.setVisible(true);
    }

    private void hideTrailerEditPanel() {
        trailerEditContainer.setVisible(false);
        trailerEditContainer.setManaged(false);
    }

    private void afterTrailerSaved() {
        hideTrailerEditPanel();
        rentalFlowController.refreshTrailerList();
    }

    private void openLastFifteenPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/LastFifteenPopup.fxml")
            );
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ostatnie 10 wynajętych");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}