//package organization.service;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import organization.dao.StatisticDao;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//public class SQLiteToExcelService {
//    public SQLiteToExcelService(){
//    }
//    public void tableGasStatisticToExcel() {
//
//        String excelFilePath = "C:\\Users\\miaus\\Desktop\\output.xlsx";
//
//             Workbook workbook = new XSSFWorkbook();
//
//            Sheet sheet = workbook.createSheet("Data");
//            int rowCount = 0;
//            String[] headers = {"data", "zapas [litry]", "zapas [tony]", "zakup [litry]", "zakup [tony]", "zakup [netto]", "sprzedaż [litry]",
//                    "sprzedaż [tony]", "sprzedaż [netto]", "pozostało[litry/szt]", "pozostało [tony]", "gaz F / T butla"};
//            Row headerRow = sheet.createRow(rowCount++);
//            for (int i = 0; i < headers.length; i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(headers[i]);
//            }
//            // Zapisz dane z tabeli
//        StatisticDao statisticDao = new StatisticDao();
//            Optional<List<List<String>>> statiscticDataOptional = statisticDao.getStatistics();
//            if (!statiscticDataOptional.isPresent()) {
//                System.out.println("Brak wyniku z bazy");
//                return;
//            }
//
//            List<List<String>> statisticDataList = statiscticDataOptional.get();
//            for (List<String> statisticRow : statisticDataList) {
//                Row row = sheet.createRow(rowCount++);
//                for (int i = 0; i < statisticRow.size(); i++) {
//                    Cell cell = row.createCell(i);
//                    cell.setCellValue(statisticRow.get(i));
//                }
//            }
//
//            // Auto-dopasowanie szerokości kolumn
//            for (int i = 0; i < statisticDataList.get(0).size(); i++) {
//                sheet.autoSizeColumn(i);
//            }
//            // Zapisz plik Excel
//            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
//                workbook.write(outputStream);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        System.out.println("Dane zostały zapisane do pliku Excel.");
//        }
//}
