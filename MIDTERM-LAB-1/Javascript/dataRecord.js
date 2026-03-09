// ============================================================
//  dataRecord.js — Data model + analysis for Video Game Sales
//  Schema: vgchartz-2024.csv
//  Columns: img, title, console, genre, publisher, developer,
//           critic_score, total_sales, na_sales, jp_sales,
//           pal_sales, other_sales, release_date, last_update
// ============================================================

"use strict";

// ── DataRecord class ─────────────────────────────────────────
class DataRecord {
  constructor(row, index) {
    this.rank        = index + 1; // derived rank (1-based row position)
    this.img         = (row["img"]          || "").trim();
    this.title       = (row["title"]        || "").trim();
    this.console     = (row["console"]      || "").trim();
    this.genre       = (row["genre"]        || "").trim();
    this.publisher   = (row["publisher"]    || "").trim();
    this.developer   = (row["developer"]    || "").trim();
    this.criticScore = parseFloat(row["critic_score"]) || 0;
    this.totalSales  = parseFloat(row["total_sales"])  || 0;
    this.naSales     = parseFloat(row["na_sales"])     || 0;
    this.jpSales     = parseFloat(row["jp_sales"])     || 0;
    this.palSales    = parseFloat(row["pal_sales"])    || 0;
    this.otherSales  = parseFloat(row["other_sales"])  || 0;
    this.releaseDate = (row["release_date"] || "").trim();
    this.lastUpdate  = (row["last_update"]  || "").trim();
  }

  toSummary() {
    return `[${String(this.rank).padStart(5)}] ${this.title.substring(0, 40).padEnd(40)} | ` +
           `${this.console.padEnd(6)} | ${this.genre.padEnd(15)} | ` +
           `Sales: ${this.totalSales.toFixed(2).padStart(8)}M`;
  }
}

// ── CSV Parser ───────────────────────────────────────────────
function parseCSV(csvText) {
  const lines = csvText.split(/\r?\n/).filter(line => line.trim() !== "");
  if (lines.length < 2) {
    throw new Error("CSV file appears to be empty or has no data rows.");
  }

  const headers = splitCSVLine(lines[0]).map(h => h.toLowerCase().trim());
  validateHeaders(headers);

  const records = [];
  for (let i = 1; i < lines.length; i++) {
    const values = splitCSVLine(lines[i]);
    if (values.length < 2) continue;

    const row = {};
    headers.forEach((header, idx) => {
      row[header] = values[idx] !== undefined ? values[idx].trim() : "";
    });

    if (!row["title"] || row["title"] === "") continue;

    records.push(new DataRecord(row, records.length));
  }

  if (records.length === 0) {
    throw new Error("No valid data records found in the CSV file.");
  }

  return records;
}

function splitCSVLine(line) {
  const result = [];
  let current  = "";
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    if (ch === '"') {
      if (inQuotes && line[i + 1] === '"') {
        current += '"';
        i++;
      } else {
        inQuotes = !inQuotes;
      }
    } else if (ch === "," && !inQuotes) {
      result.push(current);
      current = "";
    } else {
      current += ch;
    }
  }
  result.push(current);
  return result;
}

function validateHeaders(headers) {
  const required = ["title", "console", "genre", "total_sales"];
  const missing  = required.filter(col => !headers.includes(col));
  if (missing.length > 0) {
    throw new Error(
      `CSV is missing required column(s): ${missing.join(", ")}.\n` +
      "Please make sure you are using the vgchartz-2024.csv dataset from Kaggle."
    );
  }
}

// ── Analysis Functions ───────────────────────────────────────

function getTopSellers(records, n = 10) {
  return [...records]
    .filter(r => r.totalSales > 0)
    .sort((a, b) => b.totalSales - a.totalSales)
    .slice(0, n);
}

function getSalesByGenre(records) {
  const map = {};
  for (const r of records) {
    const g = r.genre || "Unknown";
    map[g] = (map[g] || 0) + r.totalSales;
  }
  return Object.entries(map)
    .sort((a, b) => b[1] - a[1])
    .map(([genre, sales]) => ({ genre, sales: +sales.toFixed(2) }));
}

function getSalesByPlatform(records) {
  const map = {};
  for (const r of records) {
    const p = r.console || "Unknown";
    map[p] = (map[p] || 0) + r.totalSales;
  }
  return Object.entries(map)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 15)
    .map(([platform, sales]) => ({ platform, sales: +sales.toFixed(2) }));
}

function getTopPublishers(records, n = 10) {
  const map = {};
  for (const r of records) {
    const p = r.publisher || "Unknown";
    map[p] = (map[p] || 0) + r.totalSales;
  }
  return Object.entries(map)
    .sort((a, b) => b[1] - a[1])
    .slice(0, n)
    .map(([publisher, sales]) => ({ publisher, sales: +sales.toFixed(2) }));
}

function getRegionalSales(records) {
  let na = 0, jp = 0, pal = 0, other = 0;
  for (const r of records) {
    na    += r.naSales;
    jp    += r.jpSales;
    pal   += r.palSales;
    other += r.otherSales;
  }
  const total = na + jp + pal + other;
  return {
    "North America" : { sales: +na.toFixed(2),    pct: pct(na, total)    },
    "Japan"         : { sales: +jp.toFixed(2),    pct: pct(jp, total)    },
    "PAL Region"    : { sales: +pal.toFixed(2),   pct: pct(pal, total)   },
    "Other"         : { sales: +other.toFixed(2), pct: pct(other, total) },
  };
}

function getSummaryStats(records) {
  const withSales  = records.filter(r => r.totalSales > 0);
  const totalSales = withSales.reduce((s, r) => s + r.totalSales, 0);

  // Average sales per transaction
  const avgSales = withSales.length ? totalSales / withSales.length : 0;

  // Highest single transaction
  const highest = withSales.reduce((max, r) =>
    r.totalSales > max.totalSales ? r : max, withSales[0] || null);

  // Lowest single transaction
  const lowest = withSales.reduce((min, r) =>
    r.totalSales < min.totalSales ? r : min, withSales[0] || null);

  const withScore  = records.filter(r => r.criticScore > 0);
  const avgCritic  = withScore.length
    ? withScore.reduce((s, r) => s + r.criticScore, 0) / withScore.length
    : 0;

  const genreCount = {};
  for (const r of records) {
    const g = r.genre || "Unknown";
    genreCount[g] = (genreCount[g] || 0) + 1;
  }
  const topGenre = Object.entries(genreCount).sort((a,b) => b[1]-a[1])[0]?.[0] || "N/A";

  return {
    totalRecords     : records.length,
    recordsWithSales : withSales.length,
    totalSales       : +totalSales.toFixed(2),
    avgSales         : +avgSales.toFixed(4),
    highestSale      : highest ? { sales: highest.totalSales, title: highest.title, console: highest.console } : null,
    lowestSale       : lowest  ? { sales: lowest.totalSales,  title: lowest.title,  console: lowest.console  } : null,
    avgCriticScore   : +avgCritic.toFixed(2),
    uniquePlatforms  : new Set(records.map(r => r.console)).size,
    uniqueGenres     : new Set(records.map(r => r.genre)).size,
    uniquePublishers : new Set(records.map(r => r.publisher)).size,
    mostCommonGenre  : topGenre,
  };
}

function pct(val, total) {
  return total > 0 ? +((val / total) * 100).toFixed(1) : 0;
}

module.exports = {
  DataRecord,
  parseCSV,
  getTopSellers,
  getSalesByGenre,
  getSalesByPlatform,
  getTopPublishers,
  getRegionalSales,
  getSummaryStats,
};
