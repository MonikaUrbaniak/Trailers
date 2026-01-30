package organization.ui.common;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Component;

@Component
public class DialogService {
    // ui helpers
    public void warn(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public void info(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
