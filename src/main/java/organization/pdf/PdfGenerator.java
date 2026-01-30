package organization.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;


@Service
public class PdfGenerator {

    public byte[] generatePdf(String html) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            builder.useFont(
                    () -> getClass()
                            .getResourceAsStream("/fonts/DejaVuSans.ttf"),
                    "DejaVu Sans"
            );

            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania PDF", e);
        }
    }
}
