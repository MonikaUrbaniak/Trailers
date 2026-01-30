package organization.ui.trailer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import organization.entity.Trailer;
import organization.service.TrailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrailerSelectionController {

    @FXML private TableView<TrailerRow> trailersTable;

    @FXML private TableColumn<TrailerRow, String> nameCol;
    @FXML private TableColumn<TrailerRow, String> regCol;
    @FXML private TableColumn<TrailerRow, Number> widthCol;
    @FXML private TableColumn<TrailerRow, Number> lengthCol;
    @FXML private TableColumn<TrailerRow, String> heightCol;
    @FXML private TableColumn<TrailerRow, String> springCol;
    @FXML private TableColumn<TrailerRow, String> wheelCol;
    @FXML private TableColumn<TrailerRow, Number> price4hCol;
    @FXML private TableColumn<TrailerRow, Number> price24hCol;
    @FXML private TableColumn<TrailerRow, String> axleCol;
    @FXML private TableColumn<TrailerRow, String> addInfoCol;

    @Autowired
    private TrailerService trailerService;

    private TrailerSelectionListener selectionListener;

    @FXML
    public void initialize() {

        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        regCol.setCellValueFactory(c -> c.getValue().registrationNumberProperty());
        widthCol.setCellValueFactory(c -> c.getValue().widthProperty());
        lengthCol.setCellValueFactory(c -> c.getValue().lengthProperty());
        heightCol.setCellValueFactory(c -> c.getValue().heightProperty());


        springCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().hasSpringProperty().get() ? "TAK" : "NIE"
                )
        );

        wheelCol.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().hasSpareWheelProperty().get() ? "TAK" : "NIE"
                )
        );

        price4hCol.setCellValueFactory(c -> c.getValue().price4hProperty());
        price24hCol.setCellValueFactory(c -> c.getValue().price24hProperty());
        axleCol.setCellValueFactory(c -> c.getValue().axleCountProperty());
        addInfoCol.setCellValueFactory(c -> c.getValue().additionalInfoProperty());

        loadTrailers();

        trailersTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TrailerRow row = trailersTable.getSelectionModel().getSelectedItem();
                if (row != null && selectionListener != null) {
                    Trailer trailer = trailerService.getById(row.getId()).orElse(null);
                    if (trailer != null) {
                        selectionListener.onTrailerSelected(trailer);
                    }
                }
            }
        });
    }

    private void loadTrailers() {
        var rows = trailerService.getAllTrailers()
                .stream()
                .map(TrailerRow::fromEntity)
                .toList();

        trailersTable.setItems(FXCollections.observableArrayList(rows));
    }

    public void setSelectionListener(TrailerSelectionListener listener) {
        this.selectionListener = listener;
    }

    public interface TrailerSelectionListener {
        void onTrailerSelected(Trailer trailer);
    }
    public void reload() {
        loadTrailers();
    }

}
