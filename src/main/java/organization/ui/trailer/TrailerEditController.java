package organization.ui.trailer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import organization.entity.Trailer;
import organization.service.TrailerService;
import organization.ui.common.DialogService;

@Component
public class TrailerEditController {

    @FXML private ComboBox<Trailer> trailerSelector;
    @FXML private TextField nameField;
    @FXML private TextField plateField;
    @FXML private TextField price4h;
    @FXML private TextField price24h;
    @FXML private TextField widthField;
    @FXML private TextField lengthField;
    @FXML private TextField heightField;
    @FXML private ComboBox<String> resor;
    @FXML private ComboBox<String> wheel;
    @FXML private ComboBox<String> axleCount;

    @FXML private TextField notesArea;

    @FXML private Button saveTrailerButton;
    @FXML private Button deleteTrailerButton;
    @FXML private Button cancelButton;

    @Autowired
    private TrailerService trailerService;
    @Autowired
    private DialogService dialogService;
    @Autowired
    private TrailerChangeChecker changeChecker;


    private Runnable onCancel;
    private Runnable onSave;
    private boolean loading = false;
    private boolean editMode = false;


    private Trailer currentTrailer;
    private Trailer originalTrailer;


    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void initialize() {
        loadTrailersToCombo();

        resor.getItems().setAll("TAK", "NIE");
        wheel.getItems().setAll("TAK", "NIE");
        axleCount.getItems().setAll("JEDNOOSIOWA", "DWUOSIOWA");


        trailerSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                setTrailer(newVal);
            }
        });
        saveTrailerButton.setOnAction(e -> saveTrailer());

        cancelButton.setOnAction(e -> {

            reloadFromEntity(); // cofamy zmiany
            refreshSaveButton();

            if (onCancel != null) {
                onCancel.run(); // chowamy panel
            }
        });
        addChangeListeners();
        saveTrailerButton.setDisable(true);

    }

    public void startEdit() {
        if (currentTrailer == null) return;

        editMode = true;
        originalTrailer = copyTrailer(currentTrailer);
        saveTrailerButton.setDisable(true);
    }

    public void setTrailer(Trailer t) {
        if (t == null) return;

        loading = true;

        this.currentTrailer = t;

        nameField.setText(t.getName());
        plateField.setText(t.getRegistrationNumber());
        price4h.setText(String.valueOf(t.getPrice4h()));
        price24h.setText(String.valueOf(t.getPrice24h()));
        widthField.setText(String.valueOf(t.getWidth()));
        lengthField.setText(String.valueOf(t.getLength()));
        heightField.setText(t.getHeight());
        resor.setValue(Boolean.TRUE.equals(t.getHasSpring()) ? "TAK" : "NIE");
        wheel.setValue(Boolean.TRUE.equals(t.getHasSpareWheel()) ? "TAK" : "NIE");
        axleCount.setValue(t.getAxleCount());
        notesArea.setText(t.getAdditionalInfo());

        loading = false;

        resetChangeState();
    }



    private void loadTrailersToCombo() {

        var trailers = trailerService.getAllTrailers();

        trailerSelector.getItems().setAll(trailers);

        // Co ma się wyświetlać w ComboBoxie
        trailerSelector.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Trailer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (" + item.getRegistrationNumber() + ")");
                }
            }
        });

        trailerSelector.setButtonCell(trailerSelector.getCellFactory().call(null));
    }

    private void saveTrailer() {
        if (!isChanged()) {
            return;
        }
        if (currentTrailer == null) {
            dialogService.warn("Brak danych", "Nie wybrano przyczepy");
            return;
        }

        try {
            currentTrailer.setName(nameField.getText());
            currentTrailer.setRegistrationNumber(plateField.getText());
            currentTrailer.setPrice4h(Integer.parseInt(price4h.getText()));
            currentTrailer.setPrice24h(Integer.parseInt(price24h.getText()));
            currentTrailer.setWidth(Double.parseDouble(widthField.getText()));
            currentTrailer.setLength(Double.parseDouble(lengthField.getText()));
            currentTrailer.setHeight(heightField.getText());
            currentTrailer.setHasSpring("TAK".equals(resor.getValue()));
            currentTrailer.setHasSpareWheel("TAK".equals(wheel.getValue()));
            currentTrailer.setAxleCount(axleCount.getValue());
            currentTrailer.setAdditionalInfo(notesArea.getText());

            trailerService.save(currentTrailer);
//            Trailer reloaded = trailerService.getById(currentTrailer.getId())
//                    .orElse(currentTrailer);
//
//            setTrailer(reloaded); // ← reset + czysty formularz
            Trailer selected = trailerSelector.getValue();
            trailerSelector.setValue(null);
            trailerSelector.setValue(selected);
            dialogService.info("Sukces", "Zapisano zmiany");

            if (onSave != null) {
                onSave.run();
            }

        } catch (Exception e) {

            dialogService.warn(
                    "Błąd zapisu",
                    "Sprawdź poprawność danych (liczby, puste pola)"
            );

            e.printStackTrace();
        }
//        originalTrailer = copyTrailer(currentTrailer);
//        refreshSaveButton();

    }
    public void reloadFromEntity() {
        if (currentTrailer != null) {
            setTrailer(currentTrailer);
        }
    }

    private Trailer copyTrailer(Trailer t) {

        Trailer copy = new Trailer();

        copy.setName(t.getName());
        copy.setRegistrationNumber(t.getRegistrationNumber());
        copy.setPrice4h(t.getPrice4h());
        copy.setPrice24h(t.getPrice24h());

        copy.setWidth(t.getWidth());
        copy.setLength(t.getLength());
        copy.setHeight(t.getHeight());

        copy.setHasSpring(t.getHasSpring());
        copy.setHasSpareWheel(t.getHasSpareWheel());

        copy.setAxleCount(t.getAxleCount());
        copy.setAdditionalInfo(t.getAdditionalInfo());

        return copy;
    }

    private boolean isChanged() {

        if (originalTrailer == null) return false;

        return changeChecker.isChanged(
                originalTrailer,
                nameField.getText(),
                plateField.getText(),
                price4h.getText(),
                price24h.getText(),
                widthField.getText(),
                lengthField.getText(),
                heightField.getText(),
                resor.getValue(),
                wheel.getValue(),
                axleCount.getValue(),
                notesArea.getText()
        );
    }


    private void addChangeListeners() {

        nameField.textProperty().addListener((a,b,c) -> refreshSaveButton());
        plateField.textProperty().addListener((a,b,c) -> refreshSaveButton());
        price4h.textProperty().addListener((a,b,c) -> refreshSaveButton());
        price24h.textProperty().addListener((a,b,c) -> refreshSaveButton());
        widthField.textProperty().addListener((a,b,c) -> refreshSaveButton());
        lengthField.textProperty().addListener((a,b,c) -> refreshSaveButton());
        heightField.textProperty().addListener((a,b,c) -> refreshSaveButton());
        resor.valueProperty().addListener((a,b,c) -> refreshSaveButton());
        wheel.valueProperty().addListener((a,b,c) -> refreshSaveButton());
        axleCount.valueProperty().addListener((a,b,c) -> refreshSaveButton());
        notesArea.textProperty().addListener((a,b,c) -> refreshSaveButton());
    }

    private void refreshSaveButton() {
        if (loading) {
            return;
        }
        saveTrailerButton.setDisable(!isChanged());
    }

    public void resetChangeState() {
        if (currentTrailer != null) {
            originalTrailer = copyTrailer(currentTrailer);
            saveTrailerButton.setDisable(true);
        }
    }

    @FXML
    private void onEditClicked() {
        if (!editMode) {
            saveTrailerButton.setDisable(true);
            return;
        }

        saveTrailerButton.setDisable(!isChanged());
    }


}












//
//
//
//
//
//
//
//
//package organization.ui.trailer;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//        import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import organization.entity.Trailer;
//import organization.service.TrailerService;
//import organization.ui.common.DialogService;
//
//@Component
//public class TrailerEditController {
//
//    @FXML private ComboBox<Trailer> trailerSelector;
//    @FXML private TextField nameField;
//    @FXML private TextField plateField;
//    @FXML private TextField price4h;
//    @FXML private TextField price24h;
//    @FXML private TextField widthField;
//    @FXML private TextField lengthField;
//    @FXML private TextField heightField;
//    @FXML private ComboBox<String> resor;
//    @FXML private ComboBox<String> wheel;
//    @FXML private ComboBox<String> axleCount;
//
//    @FXML private TextField notesArea;
//
//    @FXML private Button saveTrailerButton;
//    @FXML private Button deleteTrailerButton;
//    @FXML private Button cancelButton;
//
//    @Autowired
//    private TrailerService trailerService;
//    @Autowired
//    private DialogService dialogService;
//    @Autowired
//    private TrailerChangeChecker changeChecker;
//
//
//    private Runnable onCancel;
//    private Runnable onSave;
//    private boolean loading = false;
//
//    private Trailer currentTrailer;
//    private Trailer originalTrailer;
//
//
//    public void setOnCancel(Runnable onCancel) {
//        this.onCancel = onCancel;
//    }
//
//    public void setOnSave(Runnable onSave) {
//        this.onSave = onSave;
//    }
//
//    @FXML
//    private void initialize() {
//        loadTrailersToCombo();
//
//        resor.getItems().setAll("TAK", "NIE");
//        wheel.getItems().setAll("TAK", "NIE");
//        axleCount.getItems().setAll("JEDNOOSIOWA", "DWUOSIOWA");
//
//
//        trailerSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null) {
//                setTrailer(newVal);
//            }
//        });
//        saveTrailerButton.setOnAction(e -> saveTrailer());
//
//        cancelButton.setOnAction(e -> {
//
//            reloadFromEntity(); // cofamy zmiany
//            refreshSaveButton();
//
//            if (onCancel != null) {
//                onCancel.run(); // chowamy panel
//            }
//        });
//        addChangeListeners();
//        saveTrailerButton.setDisable(true);
//
//    }
//
//    public void setTrailer(Trailer t) {
//        if (t == null) return;
//        loading = true;   // <<< START
//        this.currentTrailer = t;
//        this.originalTrailer = copyTrailer(t);
//
//        nameField.setText(t.getName());
//        plateField.setText(t.getRegistrationNumber());
//        price4h.setText(String.valueOf(t.getPrice4h()));
//        price24h.setText(String.valueOf(t.getPrice24h()));
//        widthField.setText(String.valueOf(t.getWidth()));
//        lengthField.setText(String.valueOf(t.getLength()));
//        heightField.setText(t.getHeight());
//        resor.setValue(Boolean.TRUE.equals(t.getHasSpring()) ? "TAK" : "NIE");
//        wheel.setValue(Boolean.TRUE.equals(t.getHasSpareWheel()) ? "TAK" : "NIE");
//        axleCount.setValue(t.getAxleCount());
//        notesArea.setText(t.getAdditionalInfo());
//        loading = false;
//
//        originalTrailer = copyTrailer(currentTrailer);
//        saveTrailerButton.setDisable(true);
//    }
//
//
//    private void loadTrailersToCombo() {
//
//        var trailers = trailerService.getAllTrailers();
//
//        trailerSelector.getItems().setAll(trailers);
//
//        // Co ma się wyświetlać w ComboBoxie
//        trailerSelector.setCellFactory(cb -> new ListCell<>() {
//            @Override
//            protected void updateItem(Trailer item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(item.getName() + " (" + item.getRegistrationNumber() + ")");
//                }
//            }
//        });
//
//        trailerSelector.setButtonCell(trailerSelector.getCellFactory().call(null));
//    }
//
//    private void saveTrailer() {
//        if (!isChanged()) {
//            return;
//        }
//        if (currentTrailer == null) {
//            dialogService.warn("Brak danych", "Nie wybrano przyczepy");
//            return;
//        }
//
//        try {
//            currentTrailer.setName(nameField.getText());
//            currentTrailer.setRegistrationNumber(plateField.getText());
//            currentTrailer.setPrice4h(Integer.parseInt(price4h.getText()));
//            currentTrailer.setPrice24h(Integer.parseInt(price24h.getText()));
//            currentTrailer.setWidth(Double.parseDouble(widthField.getText()));
//            currentTrailer.setLength(Double.parseDouble(lengthField.getText()));
//            currentTrailer.setHeight(heightField.getText());
//            currentTrailer.setHasSpring("TAK".equals(resor.getValue()));
//            currentTrailer.setHasSpareWheel("TAK".equals(wheel.getValue()));
//            currentTrailer.setAxleCount(axleCount.getValue());
//            currentTrailer.setAdditionalInfo(notesArea.getText());
//
//            trailerService.save(currentTrailer);
//            Trailer reloaded = trailerService.getById(currentTrailer.getId())
//                    .orElse(currentTrailer);
//
//            setTrailer(reloaded);
//
//            dialogService.info("Sukces", "Zapisano zmiany");
//
//            if (onSave != null) {
//                onSave.run();
//            }
//
//        } catch (Exception e) {
//
//            dialogService.warn(
//                    "Błąd zapisu",
//                    "Sprawdź poprawność danych (liczby, puste pola)"
//            );
//
//            e.printStackTrace();
//        }
//        originalTrailer = copyTrailer(currentTrailer);
//        refreshSaveButton();
//
//    }
//    public void reloadFromEntity() {
//        if (currentTrailer != null) {
//            setTrailer(currentTrailer);
//        }
//    }
//
//    private Trailer copyTrailer(Trailer t) {
//
//        Trailer copy = new Trailer();
//
//        copy.setName(t.getName());
//        copy.setRegistrationNumber(t.getRegistrationNumber());
//        copy.setPrice4h(t.getPrice4h());
//        copy.setPrice24h(t.getPrice24h());
//
//        copy.setWidth(t.getWidth());
//        copy.setLength(t.getLength());
//        copy.setHeight(t.getHeight());
//
//        copy.setHasSpring(t.getHasSpring());
//        copy.setHasSpareWheel(t.getHasSpareWheel());
//
//        copy.setAxleCount(t.getAxleCount());
//        copy.setAdditionalInfo(t.getAdditionalInfo());
//
//        return copy;
//    }
//
//    private boolean isChanged() {
//
//        if (originalTrailer == null) return false;
//
//        return changeChecker.isChanged(
//                originalTrailer,
//                nameField.getText(),
//                plateField.getText(),
//                price4h.getText(),
//                price24h.getText(),
//                widthField.getText(),
//                lengthField.getText(),
//                heightField.getText(),
//                resor.getValue(),
//                wheel.getValue(),
//                axleCount.getValue(),
//                notesArea.getText()
//        );
//    }
//
//
//    private void addChangeListeners() {
//
//        nameField.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        plateField.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        price4h.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        price24h.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        widthField.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        lengthField.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        heightField.textProperty().addListener((a,b,c) -> refreshSaveButton());
//        resor.valueProperty().addListener((a,b,c) -> refreshSaveButton());
//        wheel.valueProperty().addListener((a,b,c) -> refreshSaveButton());
//        axleCount.valueProperty().addListener((a,b,c) -> refreshSaveButton());
//        notesArea.textProperty().addListener((a,b,c) -> refreshSaveButton());
//    }
//
//    private void refreshSaveButton() {
//        if (loading) {
//            return;
//        }
//        saveTrailerButton.setDisable(!isChanged());
//    }
//
//    public void resetChangeState() {
//        if (currentTrailer != null) {
//            originalTrailer = copyTrailer(currentTrailer);
//            saveTrailerButton.setDisable(true);
//        }
//    }
//
//
//}
