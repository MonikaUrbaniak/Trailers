package organization.ui.rental;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import organization.dto.RentalDataDTO;
import organization.entity.Client;
import organization.entity.RentalContract;
import organization.entity.Trailer;
import organization.pdf.ContractPdfFacade;
import organization.service.RentalContractService;
import organization.ui.MainController;
import organization.ui.common.DialogService;
import organization.ui.common.PdfOpener;
import organization.ui.trailer.TrailerSelectionController;

import java.nio.file.Path;

@Component
public class RentalFlowController {

    private final ApplicationContext applicationContext;
    private final RentalContractService rentalContractService;
    private final ContractPdfFacade contractPdfFacade;
    private final DialogService dialogService;
    private final PdfOpener pdfOpener;
    private Trailer selectedTrailer;
    private RentalLocationAndDateController rentalLocationAndDateController;
    private Node rentalFormNode;
    private RentalHistoryController rentalHistoryController;
    private TrailerSelectionController trailerSelectionController;

    private VBox trailerInfoBox;
    private Label selectedTrailerLabel;
    private StackPane rightPanel;
    private Runnable onTrailerSelectedCallback;


    private final RentalDraft rentalDraft = new RentalDraft();

    public RentalFlowController(
            ApplicationContext applicationContext,
            RentalContractService rentalContractService,
            ContractPdfFacade contractPdfFacade,
            DialogService dialogService,
            PdfOpener pdfOpener
    ) {
        this.applicationContext = applicationContext;
        this.rentalContractService = rentalContractService;
        this.contractPdfFacade = contractPdfFacade;
        this.dialogService = dialogService;
        this.pdfOpener = pdfOpener;
    }

    public void init(
            VBox trailerInfoBox,
            Label selectedTrailerLabel,
            StackPane rightPanel
    ) {
        this.trailerInfoBox = trailerInfoBox;
        this.selectedTrailerLabel = selectedTrailerLabel;
        this.rightPanel = rightPanel;
    }

    public void setClient(Client client) {
        rentalDraft.setClient(client);
    }

    public void showTrailerPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource("/ui/trailer/TrailerSelectionPane.fxml")
            );
            loader.setControllerFactory(applicationContext::getBean);
            Node panel = loader.load();
            trailerSelectionController = loader.getController();
            trailerSelectionController.setSelectionListener(this::onTrailerSelected);
            rightPanel.getChildren().setAll(panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTrailerList() {
        if (trailerSelectionController != null) {
            trailerSelectionController.reload();
        }
    }

    private void onTrailerSelected(Trailer trailer) {
        setSelectedTrailer(trailer);
        showRentalForm();
        if (onTrailerSelectedCallback != null) {
            onTrailerSelectedCallback.run();
        }
    }

    public void setOnTrailerSelectedCallback(Runnable callback) {
        this.onTrailerSelectedCallback = callback;
    }


    // got selected client and selected trailer, -> prepare form
    private void setSelectedTrailer(Trailer trailer) {
        this.selectedTrailer = trailer;
        rentalDraft.setTrailer(trailer);

        selectedTrailerLabel.setText(
                trailer.getName() + " nr rej.: " + trailer.getRegistrationNumber()
        );
        trailerInfoBox.setVisible(true);
    }

    private void showRentalForm() {
        if (rentalFormNode != null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource("/ui/rental/RentalFormPane.fxml")
            );
            loader.setControllerFactory(applicationContext::getBean);
            rentalFormNode = loader.load();
            rentalLocationAndDateController = loader.getController();
            trailerInfoBox.getChildren().add(rentalFormNode); // to wlasciwie chyba nie potrzebne??
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSelectedTrailer() {
        selectedTrailer = null;
        trailerInfoBox.setVisible(false);
        selectedTrailerLabel.setText("");
        trailerInfoBox.getChildren().remove(rentalFormNode);
        rentalFormNode = null;
        rentalLocationAndDateController = null;
    }

    public void saveRentalContract() {
        if (rentalDraft.getClient() == null) {
            dialogService.warn("Brak klienta", "Wybierz klienta.");
            return;
        }

        if (rentalLocationAndDateController == null) {
            dialogService.warn(
                    "Niekompletne dane",
                    "Wybierz przyczepę i uzupełnij dane wynajmu."
            );
            return;
        }

        syncDraftFromForm();

        if (!rentalDraft.isComplete()) {
            dialogService.warn(
                    "Niekompletne dane",
                    "Wybierz przyczepę i uzupełnij dane wynajmu."
            );
            return;
        }
//        (UI → Draft → DTO → Service → Entity)
        // 1. zapis umowy
        RentalDataDTO dto = buildDtoFromDraft();
        RentalContract contract = rentalContractService.saveContract(dto);
        // 2. PDF
        Path pdfPath = contractPdfFacade.generateAndSaveContractPdf(contract);
        pdfOpener.openPdf(pdfPath);
        dialogService.info(
                "Umowa zapisana",
                "Umowa została zapisana w bazie i otwarta jako PDF."
        );

        // 3. ODŚWIEŻ HISTORIĘ TYLKO JEŚLI JEST OTWARTA
        if (rentalHistoryController != null) {
            Client client = rentalDraft.getClient(); // TEN klient jest pewny
            rentalHistoryController.loadHistory(client);
        }
        resetAfterSave();
    }

    private void syncDraftFromForm() {
        rentalDraft.setPickupLocation(
                rentalLocationAndDateController.getPickupLocation()
        );
        rentalDraft.setReturnLocation(
                rentalLocationAndDateController.getReturnLocation()
        );
        rentalDraft.setContractDate(
                rentalLocationAndDateController.getContractDate()
        );
    }

    private RentalDataDTO buildDtoFromDraft() {
        RentalDataDTO dto = new RentalDataDTO();
        dto.setClient(rentalDraft.getClient());
        dto.setTrailer(rentalDraft.getTrailer());
        dto.setPickupLocation(rentalDraft.getPickupLocation());
        dto.setReturnLocation(rentalDraft.getReturnLocation());
        dto.setContractDate(rentalDraft.getContractDate());
        return dto;
    }

    private void resetAfterSave() {
        rentalDraft.clearAll();
        clearSelectedTrailer();
    }


    public void showRentalHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/rental/RentalHistoryPane.fxml")
            );
            loader.setControllerFactory(applicationContext::getBean);
            Node panel = loader.load();
            rentalHistoryController = loader.getController();

            Client client = rentalDraft.getClient();
            if (client == null) {
                dialogService.warn("Brak klienta", "Wybierz klienta.");
                return;
            }
            rentalHistoryController.loadHistory(client);
            rightPanel.getChildren().setAll(panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshHistoryIfVisible() {
        if (rentalHistoryController == null) {
            return; // historia nie jest otwarta
        }
        Client client = rentalDraft.getClient();
        if (client == null) {
            return;
        }
        rentalHistoryController.loadHistory(client);
    }
}
