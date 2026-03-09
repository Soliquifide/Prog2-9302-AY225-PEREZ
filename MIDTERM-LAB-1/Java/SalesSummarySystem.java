// ============================================================
//  Main.java — MIDTERM LAB 1 | Video Game Sales 2024 Analyzer
//  Course  : Prog2-9302
//  Student : [Your Last Name]
//  Dataset : vgchartz-2024.csv (VGChartz, Kaggle 2024)
// ============================================================

import java.io.*;
import java.util.stream.*;
import java.util.*;

public class SalesSummarySystem {

    // ── ANSI color codes for formatted terminal output ───────
    static final String RESET   = "\u001B[0m";
    static final String BOLD    = "\u001B[1m";
    static final String CYAN    = "\u001B[36m";
    static final String YELLOW  = "\u001B[33m";
    static final String GREEN   = "\u001B[32m";
    static final String RED     = "\u001B[31m";
    static final String MAGENTA = "\u001B[35m";
    static final String BLUE    = "\u001B[34m";
    static final String WHITE   = "\u001B[37m";

    // ── Entry point ──────────────────────────────────────────
    public static void main(String[] args) {

        printBanner();

        Scanner input = new Scanner(System.in);
        List<DataRecord> records = null;

        // ── File path input loop: keeps asking until valid ───
        while (true) {
            System.out.print(BOLD + "\n  Enter dataset file path: " + RESET);
            String path = input.nextLine().trim().replaceAll("^\"|\"$", "").trim();

            // 1. Empty check
            if (path.isEmpty()) {
                printError("File path cannot be empty.");
                continue;
            }

            File file = new File(path);

            // 2. Existence check
            if (!file.exists()) {
                printError("File not found: \"" + path + "\"");
                continue;
            }

            // 3. Is-a-file check (not a directory)
            if (!file.isFile()) {
                printError("Path points to a directory, not a file: \"" + path + "\"");
                continue;
            }

            // 4. Extension check
            if (!path.toLowerCase().endsWith(".csv")) {
                printError("File does not have a .csv extension: \"" + path + "\"");
                continue;
            }

            // 5. Readable check
            if (!file.canRead()) {
                printError("File is not readable (permission denied): \"" + path + "\"");
                continue;
            }

            // 6. Try parsing the CSV
            try {
                System.out.println(GREEN + "\n  ✔  File found. Parsing CSV..." + RESET);
                records = loadCSV(file);
                System.out.println(GREEN + "  ✔  Successfully loaded "
                    + String.format("%,d", records.size()) + " records from:" + RESET);
                System.out.println(CYAN + "     " + file.getAbsolutePath() + RESET);
                break; // valid — exit loop
            } catch (Exception e) {
                printError("CSV Parse Error: " + e.getMessage());
                System.out.println(YELLOW + "  Please provide the correct vgchartz-2024.csv file.\n" + RESET);
            }
        }

        input.close();

        // ── Run the Sales Summary Dashboard ─────────────────
        try {
            printSalesSummary(records);
            printTopSellers(records, 10);
            printSalesByGenre(records);
            printSalesByPlatform(records);
            printTopPublishers(records, 10);
            printRegionalBreakdown(records);

            System.out.println("\n" + CYAN + hr('=', 70) + RESET);
            System.out.println(GREEN + BOLD + "  ✅  Analysis complete." + RESET);
            System.out.println(CYAN + hr('=', 70) + RESET + "\n");

        } catch (Exception e) {
            System.err.println(RED + "\n  ✗  Analysis failed: " + e.getMessage() + RESET);
        }
    }

    // ── CSV Loader ───────────────────────────────────────────
    /**
     * Reads the CSV file using BufferedReader, parses each line
     * into a DataRecord, and returns the full list.
     */
    private static List<DataRecord> loadCSV(File file) throws IOException {
        List<DataRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty.");
            }

            // Validate that required columns exist
            String[] headers = splitCSVLine(headerLine);
            validateHeaders(headers);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = splitCSVLine(line);
                DataRecord record = DataRecord.fromTokens(tokens);
                if (record != null) {
                    records.add(record);
                }
            }
        }

        if (records.isEmpty()) {
            throw new IOException("No valid data records found in the CSV file.");
        }

        return records;
    }

    /** Validates that required columns are present in the header */
    private static void validateHeaders(String[] headers) throws IOException {
        List<String> required = Arrays.asList("title", "console", "genre", "total_sales");
        List<String> headerList = new ArrayList<>();
        for (String h : headers) headerList.add(h.toLowerCase().trim());

        List<String> missing = new ArrayList<>();
        for (String req : required) {
            if (!headerList.contains(req)) missing.add(req);
        }

        if (!missing.isEmpty()) {
            throw new IOException("CSV missing required columns: " + String.join(", ", missing));
        }
    }

    /**
     * Splits a single CSV line respecting double-quoted fields.
     */
    private static String[] splitCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(ch);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    // ── Sales Summary Dashboard ──────────────────────────────

    /** ANALYTICS 1: Core sales summary */
    private static void printSalesSummary(List<DataRecord> records) {
        printSection("📊  SALES SUMMARY DASHBOARD");

        List<DataRecord> withSales = new ArrayList<>();
        for (DataRecord r : records) {
            if (r.getTotalSales() > 0) withSales.add(r);
        }

        double total   = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        DataRecord highRec = null, lowRec = null;

        for (DataRecord r : withSales) {
            double s = r.getTotalSales();
            total += s;
            if (s > highest) { highest = s; highRec = r; }
            if (s < lowest)  { lowest  = s; lowRec  = r; }
        }

        double average = withSales.isEmpty() ? 0 : total / withSales.size();

        // Critic score average
        List<DataRecord> withScore = new ArrayList<>();
        for (DataRecord r : records) if (r.getCriticScore() > 0) withScore.add(r);
        double avgScore = 0;
        for (DataRecord r : withScore) avgScore += r.getCriticScore();
        if (!withScore.isEmpty()) avgScore /= withScore.size();

        printRow("Total Records",           String.format("%,d", records.size()));
        printRow("Records w/ Sales Data",   String.format("%,d", withSales.size()));
        printRow("Total Global Sales",      String.format("%,.2f M units", total));
        printRow("Average Sales / Record",  String.format("%.4f M units", average));
        printRow("Highest Single Sale",     String.format("%.2f M  →  %s (%s)",
            highest, highRec != null ? highRec.getTitle() : "N/A",
                     highRec != null ? highRec.getConsole() : ""));
        printRow("Lowest Single Sale",      String.format("%.4f M  →  %s (%s)",
            lowest,  lowRec  != null ? lowRec.getTitle()  : "N/A",
                     lowRec  != null ? lowRec.getConsole() : ""));
        printRow("Avg Critic Score",        withScore.isEmpty() ? "N/A"
            : String.format("%.2f / 10", avgScore));
        printRow("Unique Platforms",        String.valueOf(countUnique(records, "console")));
        printRow("Unique Genres",           String.valueOf(countUnique(records, "genre")));
        printRow("Unique Publishers",       String.valueOf(countUnique(records, "publisher")));
        printRow("Most Common Genre",       getMostCommonGenre(records));
    }

    /** ANALYTICS 2: Top N best-selling games */
    private static void printTopSellers(List<DataRecord> records, int n) {
        printSection("🏆  TOP " + n + " BEST-SELLING GAMES (Global)");

        List<DataRecord> sorted = new ArrayList<>();
        for (DataRecord r : records) if (r.getTotalSales() > 0) sorted.add(r);
        sorted.sort((a, b) -> Double.compare(b.getTotalSales(), a.getTotalSales()));

        System.out.println("  " + WHITE + BOLD
            + pad("#",    5)
            + pad("Title", 43)
            + pad("Platform", 10)
            + pad("Genre", 17)
            + "Sales (M)" + RESET);
        System.out.println("  " + hr('-', 68));


        int limit = Math.min(n, sorted.size());
        for (int i = 0; i < limit; i++) {
            DataRecord r = sorted.get(i);
            System.out.printf("  %s%-5d%s%-43s%-10s%-17s%s%8.2f M%s%n",
                MAGENTA, i + 1, RESET,
                truncate(r.getTitle(), 41),
                truncate(r.getConsole(), 8),
                truncate(r.getGenre(), 15),
                GREEN, r.getTotalSales(), RESET);
        }
    }

    /** ANALYTICS 3: Sales by Genre */
    private static void printSalesByGenre(List<DataRecord> records) {
        printSection("🎯  SALES BY GENRE");

        Map<String, Double> map = new LinkedHashMap<>();
        for (DataRecord r : records) {
            String g = r.getGenre().isEmpty() ? "Unknown" : r.getGenre();
            map.put(g, map.getOrDefault(g, 0.0) + r.getTotalSales());
        }

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        double max = sorted.isEmpty() ? 1 : sorted.get(0).getValue();
        for (Map.Entry<String, Double> e : sorted) {
            int barLen = (int) Math.round((e.getValue() / max) * 30);
            String bar = BLUE + repeatChar('█', Math.max(barLen, 0)) + RESET;
            System.out.printf("  %-18s %s %s%.2f M%s%n",
                e.getKey(), bar, GREEN, e.getValue(), RESET);
        }
    }

    /** ANALYTICS 4: Top 15 platforms by sales */
    private static void printSalesByPlatform(List<DataRecord> records) {
        printSection("🕹️   TOP 15 PLATFORMS BY SALES");

        Map<String, Double> map = new LinkedHashMap<>();
        for (DataRecord r : records) {
            String p = r.getConsole().isEmpty() ? "Unknown" : r.getConsole();
            map.put(p, map.getOrDefault(p, 0.0) + r.getTotalSales());
        }

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        if (sorted.size() > 15) sorted = sorted.subList(0, 15);

        double max = sorted.isEmpty() ? 1 : sorted.get(0).getValue();
        for (Map.Entry<String, Double> e : sorted) {
            int barLen = (int) Math.round((e.getValue() / max) * 28);
            String bar = CYAN + repeatChar('█', Math.max(barLen, 0)) + RESET;
            System.out.printf("  %-10s %s %s%.2f M%s%n",
                e.getKey(), bar, GREEN, e.getValue(), RESET);
        }
    }

    /** ANALYTICS 5: Regional sales breakdown */
    private static void printRegionalBreakdown(List<DataRecord> records) {
        printSection("🌍  REGIONAL SALES BREAKDOWN");

        double na = 0, jp = 0, pal = 0, other = 0;
        for (DataRecord r : records) {
            na    += r.getNaSales();
            jp    += r.getJpSales();
            pal   += r.getPalSales();
            other += r.getOtherSales();
        }
        double total = na + jp + pal + other;

        printRegionBar("North America", na,    total);
        printRegionBar("Japan",         jp,    total);
        printRegionBar("PAL Region",    pal,   total);
        printRegionBar("Other",         other, total);
    }

    private static void printRegionBar(String region, double sales, double total) {
        double max = total * 0.55; // NA is ~50%, scale to fill bar nicely
        int barLen = (int) Math.round((sales / max) * 30);
        String bar = MAGENTA + repeatChar('█', Math.max(barLen, 0)) + RESET;
        double pct = total > 0 ? (sales / total) * 100 : 0;
        System.out.printf("  %-16s %s %s%10.2f M%s  (%s%.1f%%%s)%n",
            region, bar, GREEN, sales, RESET, YELLOW, pct, RESET);
    }

    // ── Utility methods ──────────────────────────────────────

    /** Returns the most frequently occurring genre */
    private static String getMostCommonGenre(List<DataRecord> records) {
        Map<String, Integer> map = new HashMap<>();
        for (DataRecord r : records) {
            String g = r.getGenre().isEmpty() ? "Unknown" : r.getGenre();
            map.put(g, map.getOrDefault(g, 0) + 1);
        }
        return map.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }

    /** ANALYTICS 6: Top N publishers by total sales */
    private static void printTopPublishers(List<DataRecord> records, int n) {
        printSection("\uD83C\uDFE2  TOP " + n + " PUBLISHERS BY SALES");

        Map<String, Double> map = new LinkedHashMap<>();
        for (DataRecord r : records) {
            String p = r.getPublisher().isEmpty() ? "Unknown" : r.getPublisher();
            map.put(p, map.getOrDefault(p, 0.0) + r.getTotalSales());
        }

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        if (sorted.size() > n) sorted = sorted.subList(0, n);

        System.out.println("  " + WHITE + BOLD
            + pad("#", 4) + pad("Publisher", 32) + "Total Sales" + RESET);
        System.out.println("  " + hr('-', 50));

        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("  %s%-4d%s%-32s%s%.2f M%s%n",
                MAGENTA, i + 1, RESET,
                truncate(sorted.get(i).getKey(), 30),
                GREEN, sorted.get(i).getValue(), RESET);
        }
    }

    private static int countUnique(List<DataRecord> records, String field) {
        Set<String> set = new HashSet<>();
        for (DataRecord r : records) {
            switch (field) {
                case "console":   set.add(r.getConsole());   break;
                case "genre":     set.add(r.getGenre());     break;
                case "publisher": set.add(r.getPublisher()); break;
            }
        }
        return set.size();
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) : s;
    }

    private static String pad(String s, int width) {
        return String.format("%-" + width + "s", s);
    }

    private static String hr(char ch, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) sb.append(ch);
        return sb.toString();
    }

    private static String repeatChar(char ch, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(ch);
        return sb.toString();
    }

    // ── Print helpers ────────────────────────────────────────
    private static void printBanner() {
        System.out.println("\n" + CYAN + hr('=', 70) + RESET);
        System.out.println(CYAN + BOLD + "  🎮  VIDEO GAME SALES 2024 — Data Analyzer" + RESET);
        System.out.println(CYAN + "       MIDTERM LAB 1 | Prog2-9302" + RESET);
        System.out.println(CYAN + hr('=', 70) + RESET);
    }

    private static void printSection(String title) {
        System.out.println("\n" + YELLOW + hr('-', 70) + RESET);
        System.out.println(YELLOW + BOLD + "  " + title + RESET);
        System.out.println(YELLOW + hr('-', 70) + RESET);
    }

    private static void printRow(String label, String value) {
        System.out.printf("  %s%-26s%s : %s%s%s%n",
            BOLD, label, RESET, GREEN, value, RESET);
    }

    private static void printError(String msg) {
        System.out.println(RED + "\n  ✗  ERROR: " + msg + RESET);
        System.out.println(YELLOW + "  Please try again." + RESET);
    }
}