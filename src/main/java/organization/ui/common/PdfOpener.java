package organization.ui.common;


import org.springframework.stereotype.Component;
import java.nio.file.Path;

@Component
public class PdfOpener {
    private final DialogService dialogService;

    public PdfOpener(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    public void openPdf(Path pdfPath) {
        if (pdfPath == null) {
            dialogService.warn("Błąd PDF", "Ścieżka do pliku PDF jest pusta.");
            return;
        }

        try {
            String absolutePath = pdfPath.toAbsolutePath().toString();

            // Windows – używamy systemowego "start"
            new ProcessBuilder(
                    "cmd", "/c", "start", "\"\"", "\"" + absolutePath + "\""
            ).start();

        } catch (Exception e) {
            e.printStackTrace();
            dialogService.warn(
                    "Błąd PDF",
                    "Nie udało się otworzyć pliku PDF:\n" + pdfPath.toAbsolutePath()
            );
        }
    }
}
