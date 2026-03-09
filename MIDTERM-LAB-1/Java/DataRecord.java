// ============================================================
//  DataRecord.java — Model class for a single VGChartz record
//  Course : Prog2-9302
//  Dataset : vgchartz-2024.csv
// ============================================================

public class DataRecord {

    // ── Fields ───────────────────────────────────────────────
    private String img;
    private String title;
    private String console;
    private String genre;
    private String publisher;
    private String developer;
    private double criticScore;
    private double totalSales;
    private double naSales;
    private double jpSales;
    private double palSales;
    private double otherSales;
    private String releaseDate;
    private String lastUpdate;

    // ── Constructor ──────────────────────────────────────────
    public DataRecord(String img, String title, String console, String genre,
                      String publisher, String developer, double criticScore,
                      double totalSales, double naSales, double jpSales,
                      double palSales, double otherSales,
                      String releaseDate, String lastUpdate) {
        this.img         = img;
        this.title       = title;
        this.console     = console;
        this.genre       = genre;
        this.publisher   = publisher;
        this.developer   = developer;
        this.criticScore = criticScore;
        this.totalSales  = totalSales;
        this.naSales     = naSales;
        this.jpSales     = jpSales;
        this.palSales    = palSales;
        this.otherSales  = otherSales;
        this.releaseDate = releaseDate;
        this.lastUpdate  = lastUpdate;
    }

    // ── Factory method: parse one CSV line into a DataRecord ─
    /**
     * Parses a single CSV line (already split into tokens) into a DataRecord.
     * Returns null if the title field is empty/missing.
     */
    public static DataRecord fromTokens(String[] tokens) {
        if (tokens.length < 2) return null;

        String img         = get(tokens, 0);
        String title       = get(tokens, 1);
        if (title.isEmpty()) return null;

        String console     = get(tokens, 2);
        String genre       = get(tokens, 3);
        String publisher   = get(tokens, 4);
        String developer   = get(tokens, 5);
        double criticScore = parseDouble(get(tokens, 6));
        double totalSales  = parseDouble(get(tokens, 7));
        double naSales     = parseDouble(get(tokens, 8));
        double jpSales     = parseDouble(get(tokens, 9));
        double palSales    = parseDouble(get(tokens, 10));
        double otherSales  = parseDouble(get(tokens, 11));
        String releaseDate = get(tokens, 12);
        String lastUpdate  = get(tokens, 13);

        return new DataRecord(img, title, console, genre, publisher, developer,
                              criticScore, totalSales, naSales, jpSales,
                              palSales, otherSales, releaseDate, lastUpdate);
    }

    // ── Getters ──────────────────────────────────────────────
    public String getTitle()       { return title; }
    public String getConsole()     { return console; }
    public String getGenre()       { return genre; }
    public String getPublisher()   { return publisher; }
    public String getDeveloper()   { return developer; }
    public double getCriticScore() { return criticScore; }
    public double getTotalSales()  { return totalSales; }
    public double getNaSales()     { return naSales; }
    public double getJpSales()     { return jpSales; }
    public double getPalSales()    { return palSales; }
    public double getOtherSales()  { return otherSales; }
    public String getReleaseDate() { return releaseDate; }

    // ── toString (one-line summary) ──────────────────────────
    @Override
    public String toString() {
        return String.format("%-42s | %-6s | %-15s | %8.2f M",
            title.length() > 40 ? title.substring(0, 40) : title,
            console, genre, totalSales);
    }

    // ── Private helpers ──────────────────────────────────────
    private static String get(String[] arr, int idx) {
        return (idx < arr.length) ? arr[idx].trim() : "";
    }

    private static double parseDouble(String s) {
        try {
            return s.isEmpty() ? 0.0 : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
