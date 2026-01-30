//package organization.chartUtil;
//
//import org.knowm.xchart.CategoryChart;
//import org.knowm.xchart.CategoryChartBuilder;
//import org.knowm.xchart.style.CategoryStyler;
//
//import java.awt.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Pomocnicza klasa do tworzenia wykresu słupkowego wg miesięcy i produktów.
// */
//public class ChartGenerator {
//
//    /**
//     * Buduje wykres (CategoryChart) z surowych danych.
//     *
//     * @param rawData   lista Object[] { "YYYY-MM", "nazwaProduktu", ilość }
//     * @param startDate granica początkowa (YYYY-MM-DD)
//     * @param endDate   granica końcowa (YYYY-MM-DD)
//     * @return skonfigurowany CategoryChart
//     */
//    public static CategoryChart buildMonthlyChart(
//            List<Object[]> rawData,
//            String startDate,
//            String endDate
//    ) {
//        // 1) Stwórz listę miesięcy od startDate do endDate
//        DateTimeFormatter parser     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter monthFmt   = DateTimeFormatter.ofPattern("yyyy-MM");
//        LocalDate s = LocalDate.parse(startDate, parser).withDayOfMonth(1);
//        LocalDate e = LocalDate.parse(endDate,   parser).withDayOfMonth(1);
//        List<String> months = new ArrayList<>();
//        for (LocalDate d = s; !d.isAfter(e); d = d.plusMonths(1)) {
//            months.add(d.format(monthFmt));
//        }
//
//        // 2) Zbiór nazw produktów
//        Set<String> products = new LinkedHashSet<>();
//        for (Object[] row : rawData) {
//            products.add((String) row[1]);
//        }
//
//        // 3) Mapa: miesiąc -> (produkt->ilość)
//        Map<String, Map<String, Double>> sales = new LinkedHashMap<>();
//        for (String m : months) {
//            sales.put(m, new HashMap<>());
//        }
//        for (Object[] row : rawData) {
//            String ym   = (String) row[0];
//            String prod = (String) row[1];
//            Double qty  = ((Number) row[2]).doubleValue();
//            if (sales.containsKey(ym)) {
//                sales.get(ym).put(prod, qty);
//            }
//        }
//
//        // 4) Seria danych: produkt -> lista wartości
//        Map<String, List<Double>> series = new LinkedHashMap<>();
//        for (String prod : products) {
//            List<Double> vals = new ArrayList<>();
//            for (String m : months) {
//                vals.add(sales.get(m).getOrDefault(prod, 0.0));
//            }
//            series.put(prod, vals);
//        }
//
//        // 5) Budowa wykresu
//        CategoryChart chart = new CategoryChartBuilder()
//                .width(1000).height(600)
//                .title("Ilość sprzedanych butli miesięcznie: " + startDate + " – " + endDate)
//                .xAxisTitle("Miesiąc").yAxisTitle("Ilość")
//                .build();
//
//        CategoryStyler st = chart.getStyler();
//        st.setChartBackgroundColor(Color.WHITE);
//        st.setPlotBackgroundColor(new Color(245, 245, 245));
//        st.setPlotGridLinesVisible(true);
//        st.setPlotGridLinesColor(new Color(210, 210, 210));
//        st.setLabelsVisible(true);
//        st.setLabelsFont(new Font("SansSerif", Font.BOLD, 18));
//        st.setLabelsFontColor(Color.BLACK);
//        st.setLabelsFontColorAutomaticEnabled(false); // 🔥 wyłącza auto kolor
//        st.setAxisTitleFont(new Font("SansSerif", Font.BOLD, 20));
//        st.setAxisTickLabelsFont(new Font("SansSerif", Font.PLAIN, 16));
//        st.setXAxisLabelRotation(30);
//        st.setAvailableSpaceFill(0.6);
//        st.setOverlapped(false);
//        st.setLegendVisible(true);
//        st.setLegendPosition(CategoryStyler.LegendPosition.OutsideE);
//        st.setLegendFont(new Font("SansSerif", Font.PLAIN, 16));
//        st.setChartPadding(30);     // 🧱 więcej miejsca na górze wykresu
//
//        // 6) Dodaj serie z kolorami
//        int idx = 0;
//        for (Map.Entry<String, List<Double>> en : series.entrySet()) {
//            Color c = idx == 0 ? new Color(0, 76, 153)
//                    : idx == 1 ? new Color(0, 169, 180)
//                    : Color.GRAY;
//            chart.addSeries(en.getKey(), months, en.getValue())
//                    .setFillColor(c);
//            idx++;
//        }
//
//        return chart;
//    }
//    public static CategoryChart buildMonthlyChart(List<MonthlyGasBottleAmountDto> monthlyData) {
//
//        // --- 1) Grupowanie ilości po miesiącach ---
//        Map<String, Double> qtyByMonth = monthlyData.stream()
//                .collect(Collectors.groupingBy(
//                        MonthlyGasBottleAmountDto::yearMonth,
//                        Collectors.summingDouble(MonthlyGasBottleAmountDto::quantity)
//                ));
//
//        // posortowane miesiące
//        List<String> months = new ArrayList<>(qtyByMonth.keySet());
//        Collections.sort(months);
//
//        // wartości do wykresu
//        List<Double> values = months.stream()
//                .map(qtyByMonth::get)
//                .collect(Collectors.toList());
//
//        // --- 2) Tworzenie wykresu ---
//        CategoryChart chart = new CategoryChartBuilder()
//                .width(1000)
//                .height(600)
//                .title("Ilość sprzedanych butli miesięcznie")
//                .xAxisTitle("Miesiąc")
//                .yAxisTitle("Ilość")
//                .build();
//
//        // --- 3) Ustawienia stylu ---
//        CategoryStyler st = chart.getStyler();
//        st.setChartBackgroundColor(Color.WHITE);
//        st.setPlotBackgroundColor(new Color(245, 245, 245));
//        st.setPlotGridLinesVisible(true);
//        st.setPlotGridLinesColor(new Color(210, 210, 210));
//        st.setLabelsVisible(true);
//        st.setXAxisLabelRotation(30);
//
//        // --- 4) Seria danych ---
//        chart.addSeries("Ilość sprzedanych butli", months, values)
//                .setFillColor(new Color(0, 76, 153));
//
//        return chart;
//    }
//}
