package organization.ui.search;

import java.util.HashSet;
import java.util.Set;

public class TrigramUtil {

    public static Set<String> trigrams(String input) {
        Set<String> result = new HashSet<>();
        if (input == null) return result;
        String s = normalize(input);
        if (s.length() < 3) {
            result.add(s);
            return result;
        }

        for (int i = 0; i <= s.length() - 3; i++) {
            result.add(s.substring(i, i + 3));
        }
        return result;
    }

    public static double queryCoverageScore(String query, String value) {
        Set<String> q = trigrams(query);
        Set<String> v = trigrams(value);

        if (q.isEmpty()) return 0;

        long matches = q.stream().filter(v::contains).count();
        return (double) matches / q.size();
    }

    public static boolean matches(String query, String value) {
        if (query == null || query.isBlank()) return true;
        if (value == null) return false;

        String q = normalize(query);
        String v = normalize(value);

        // 1️⃣ jak Google
        if (v.contains(q)) {
            return true;
        }

        // 2️⃣ jak w Twoim PHP
        return queryCoverageScore(q, v) >= 0.3;
    }

    private static String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]", "");
    }
}
