package organization.pdf;

import organization.entity.RentalContract;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class ContractPdfFacade {

    private final ContractPdfService modelService;
    private final PdfTemplateRenderer templateRenderer;
    private final PdfGenerator pdfGenerator;

    public ContractPdfFacade(
            ContractPdfService modelService,
            PdfTemplateRenderer templateRenderer,
            PdfGenerator pdfGenerator
    ) {
        this.modelService = modelService;
        this.templateRenderer = templateRenderer;
        this.pdfGenerator = pdfGenerator;
    }

    public byte[] generateContractPdf(RentalContract contract) {

        ContractPdfModel model = modelService.buildModel(contract);
        String html = templateRenderer.renderContractHtml(model);

        return pdfGenerator.generatePdf(html);
    }

    public Path generateAndSaveContractPdf(RentalContract contract) {
        byte[] pdf = generateContractPdf(contract);

        try {
            Path dir = Path.of("contracts");
            Files.createDirectories(dir);

            Path file = dir.resolve("umowa_" + contract.getId() + ".pdf");

            Files.write(
                    file,
                    pdf,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return file;

        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zapisać PDF", e);
        }
    }
}