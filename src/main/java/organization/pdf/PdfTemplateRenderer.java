package organization.pdf;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PdfTemplateRenderer {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String renderContractHtml(ContractPdfModel model) {
        String html = loadTemplate("templates/contract.html");

        Map<String, String> values = new HashMap<>();
        values.put(
                "contractDate",
                model.getContractDate() == null
                        ? ""
                        : model.getContractDate().format(DATE_FMT)
        );

        values.put("signingPlace", safe(model.getSigningPlace()));

        values.put("lessor.name", safe(model.getLessorName()));
        values.put("lessor.city", safe(model.getLessorCity()));
        values.put("lessor.address", safe(model.getLessorAddress()));
        values.put("lessor.phone", safe(model.getLessorPhone()));
        values.put("lessor.representative", safe(model.getLessorRepresentative()));

        values.put("client.name", safe(model.getClientName()));
        values.put("client.address", safe(model.getClientAddress()));
        values.put("client.companyLine", safe(model.getClientCompanyLine())
        );
        values.put("client.nipLine", safe(model.getClientNipLine())
        );
        values.put("client.phone", safe(model.getClientPhone()));
        values.put("client.idNumber", safe(model.getClientIdNumber()));
        values.put("client.idIssuedBy", safe(model.getClientIdIssuedBy()));

        values.put("trailer.registrationNumber", safe(model.getTrailerRegistrationNumber()));

        values.put("pickupLocation", safe(model.getPickupLocation()));
        values.put("returnLocation", safe(model.getReturnLocation()));
        values.put("handoverNotes", safe(model.getHandoverNotes()));

        for (var e : values.entrySet()) {
            html = html.replace("[[${" + e.getKey() + "}]]", e.getValue());
        }

        return html;
    }

    private String loadTemplate(String path) {
        try {
            return new String(
                    new ClassPathResource(path)
                            .getInputStream()
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new RuntimeException("Nie można wczytać szablonu PDF", e);
        }
    }

    private String safe(Object v) {
        return v == null ? "" : v.toString();
    }
}
