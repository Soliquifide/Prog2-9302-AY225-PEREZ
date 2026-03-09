// ============================================================
//  SalesSummarySystem.js — MIDTERM LAB 1 | Video Game Sales 2024 Analyzer
//  Author : [Mycho Louise D. Perez]
//  Course : Prog2-9302
// ============================================================

"use strict";

const fs      = require("fs");
const path    = require("path");
const readline = require("readline");

const {
  parseCSV,
  getTopSellers,
  getSalesByGenre,
  getSalesByPlatform,
  getTopPublishers,
  getRegionalSales,
  getSummaryStats,
} = require("./dataRecord");

// ── Console color helpers (ANSI) ─────────────────────────────
const C = {
  reset  : "\x1b[0m",
  bright : "\x1b[1m",
  cyan   : "\x1b[36m",
  yellow : "\x1b[33m",
  green  : "\x1b[32m",
  red    : "\x1b[31m",
  blue   : "\x1b[34m",
  magenta: "\x1b[35m",
  white  : "\x1b[37m",
};

const color = (str, code) => `${code}${str}${C.reset}`;
const bold  = (str)        => color(str, C.bright);
const hr    = (ch = "─", w = 70) => ch.repeat(w);

// ── Readline interface ───────────────────────────────────────
const rl = readline.createInterface({
  input : process.stdin,
  output: process.stdout,
});

function prompt(question) {
  return new Promise(resolve => rl.question(question, resolve));
}

// ── File Validation ──────────────────────────────────────────
/**
 * If the user types just a filename (e.g. vgchartz-2024.csv),
 * we search for it in:
 *   1. Current working directory
 *   2. Parent directory
 *   3. Same directory as main.js
 * Then validates the found file.
 */
function validateFile(filePath) {
  try {
    // 1 — Check empty input
    if (!filePath || filePath.trim() === "") {
      return { valid: false, error: "File path cannot be empty." };
    }

    // Strip surrounding quotes (from Windows Copy as path)
    const inputClean = filePath.trim().replace(/^["']|["']$/g, "").trim();

    // 2 — Must be a full path (must contain backslash or forward slash)
    if (!inputClean.includes("\\") && !inputClean.includes("/")) {
      return { valid: false, error: `Please enter the FULL file path.\n  Example: C:\\Users\\Student\\Downloads\\vgchartz-2024.csv` };
    }

    const resolved = path.resolve(inputClean);

    // 3 — Check extension
    if (path.extname(resolved).toLowerCase() !== ".csv") {
      return { valid: false, error: `File does not have a .csv extension: "${resolved}"` };
    }

    // 3 — Check existence
    if (!fs.existsSync(resolved)) {
      return { valid: false, error: `File not found: "${resolved}"` };
    }

    // 4 — Check readability
    try {
      fs.accessSync(resolved, fs.constants.R_OK);
    } catch {
      return { valid: false, error: `File is not readable (permission denied): "${resolved}"` };
    }

    // 5 — Check it is a file (not a directory)
    const stat = fs.statSync(resolved);
    if (!stat.isFile()) {
      return { valid: false, error: `Path points to a directory, not a file: "${resolved}"` };
    }

    // 6 — Check file is not empty
    if (stat.size === 0) {
      return { valid: false, error: `File is empty: "${resolved}"` };
    }

    // 7 — Try reading content
    const content = fs.readFileSync(resolved, "utf-8");
    return { valid: true, content, resolved };

  } catch (err) {
    return { valid: false, error: `Unexpected error while reading file: ${err.message}` };
  }
}

// ── Output Formatters ────────────────────────────────────────
function printBanner() {
  console.log("\n" + color(hr("═"), C.cyan));
  console.log(color("  🎮  VIDEO GAME SALES 2024 — Data Analyzer", C.cyan + C.bright));
  console.log(color("       MIDTERM LAB 1 | Prog2-9302", C.cyan));
  console.log(color(hr("═"), C.cyan));
}

function printSection(title) {
  console.log("\n" + color(hr("─"), C.yellow));
  console.log(color(`  ${title}`, C.yellow + C.bright));
  console.log(color(hr("─"), C.yellow));
}

function printSummaryStats(stats) {
  printSection("📊  SALES SUMMARY DASHBOARD");
  const high = stats.highestSale;
  const low  = stats.lowestSale;
  const rows = [
    ["Total Records",           stats.totalRecords.toLocaleString()],
    ["Records w/ Sales Data",   stats.recordsWithSales.toLocaleString()],
    ["Total Sales / Revenue",   `${stats.totalSales.toLocaleString()} M units`],
    ["Avg Sales / Transaction", `${stats.avgSales} M units`],
    ["Highest Single Sale",     high ? `${high.sales.toFixed(2)} M  →  ${high.title} (${high.console})` : "N/A"],
    ["Lowest Single Sale",      low  ? `${low.sales.toFixed(4)} M  →  ${low.title} (${low.console})`   : "N/A"],
    ["Avg Critic Score",        stats.avgCriticScore > 0 ? stats.avgCriticScore.toString() : "N/A"],
    ["Unique Platforms",        stats.uniquePlatforms.toString()],
    ["Unique Genres",           stats.uniqueGenres.toString()],
    ["Most Common Genre",       stats.mostCommonGenre],
    ["Unique Publishers",       stats.uniquePublishers.toString()],
  ];
  for (const [label, value] of rows) {
    console.log(`  ${bold(label.padEnd(22))} : ${color(value, C.green)}`);
  }
}

function printTopSellers(records) {
  printSection("🏆  TOP 10 BEST-SELLING GAMES (Global)");
  console.log(
    color(
      "  " + "Rank".padEnd(6) + "Title".padEnd(42) + "Platform".padEnd(8) +
      "Genre".padEnd(16) + "Sales (M)",
      C.white + C.bright
    )
  );
  console.log("  " + hr("-", 68));
  records.forEach((r, i) => {
    const rank    = color(String(i + 1).padEnd(6),       C.magenta);
    const title   = r.title.substring(0, 40).padEnd(42);
    const console_= r.console.padEnd(8);
    const genre   = r.genre.padEnd(16);
    const sales   = color(r.totalSales.toFixed(2).padStart(8) + " M", C.green);
    console.log(`  ${rank}${title}${console_}${genre}${sales}`);
  });
}

function printGenreSales(genreData) {
  printSection("🎯  SALES BY GENRE");
  const max = genreData[0]?.sales || 1;
  genreData.forEach(({ genre, sales }) => {
    const barLen  = Math.round((sales / max) * 30);
    const bar     = color("█".repeat(barLen), C.blue);
    console.log(`  ${genre.padEnd(18)} ${bar} ${color(sales.toFixed(2) + " M", C.green)}`);
  });
}

function printPlatformSales(platformData) {
  printSection("🕹️   TOP 15 PLATFORMS BY SALES");
  const max = platformData[0]?.sales || 1;
  platformData.forEach(({ platform, sales }) => {
    const barLen  = Math.round((sales / max) * 28);
    const bar     = color("█".repeat(barLen), C.cyan);
    console.log(`  ${platform.padEnd(10)} ${bar} ${color(sales.toFixed(2) + " M", C.green)}`);
  });
}

function printTopPublishers(pubData) {
  printSection("🏢  TOP 10 PUBLISHERS BY SALES");
  console.log(color("  " + "#".padEnd(4) + "Publisher".padEnd(32) + "Total Sales", C.white + C.bright));
  console.log("  " + hr("-", 50));
  pubData.forEach(({ publisher, sales }, i) => {
    console.log(
      `  ${color(String(i + 1).padEnd(4), C.magenta)}` +
      `${publisher.substring(0, 30).padEnd(32)}` +
      `${color(sales.toFixed(2) + " M", C.green)}`
    );
  });
}

function printRegionalSales(regional) {
  printSection("🌍  REGIONAL SALES BREAKDOWN");
  const max = Math.max(...Object.values(regional).map(v => v.sales));
  for (const [region, { sales, pct }] of Object.entries(regional)) {
    const barLen = Math.round((sales / max) * 30);
    const bar    = color("█".repeat(barLen), C.magenta);
    console.log(
      `  ${region.padEnd(16)} ${bar} ` +
      `${color(sales.toFixed(2).padStart(8) + " M", C.green)}  ` +
      `(${color(pct + "%", C.yellow)})`
    );
  }
}

// ── Main Program ─────────────────────────────────────────────
async function main() {
  printBanner();

  let records = null;

  // ── File input loop: keeps asking until a valid CSV is given ──
  while (records === null) {
    console.log();
    let filePath = await prompt(
      color("  Enter dataset file path: ", C.bright)
    );

    // Strip surrounding quotes (added by Windows "Copy as path")
    filePath = filePath.trim().replace(/^["']|["']$/g, '').trim();

    const validation = validateFile(filePath);

    if (!validation.valid) {
      console.log(color(`\n  ✗  ERROR: ${validation.error}`, C.red));
      console.log(color("  Please try again.\n", C.yellow));
      continue;
    }

    // Try parsing the CSV
    try {
      console.log(color("\n  ✔  File found. Parsing CSV...", C.green));
      records = parseCSV(validation.content);
      console.log(
        color(`  ✔  Successfully loaded ${records.length.toLocaleString()} records from:`, C.green)
      );
      console.log(color(`     ${validation.resolved}\n`, C.cyan));
    } catch (parseErr) {
      console.log(color(`\n  ✗  CSV PARSE ERROR: ${parseErr.message}`, C.red));
      console.log(color("  Please provide a valid Video Game Sales 2024 CSV file.\n", C.yellow));
      records = null;
    }
  }

  rl.close();

  // ── Run all analyses ──────────────────────────────────────
  try {
    const stats      = getSummaryStats(records);
    const topGames   = getTopSellers(records, 10);
    const byGenre    = getSalesByGenre(records);
    const byPlatform = getSalesByPlatform(records);
    const byPub      = getTopPublishers(records, 10);
    const regional   = getRegionalSales(records);

    printSummaryStats(stats);
    printTopSellers(topGames);
    printGenreSales(byGenre);
    printPlatformSales(byPlatform);
    printTopPublishers(byPub);
    printRegionalSales(regional);

    console.log("\n" + color(hr("═"), C.cyan));
    console.log(color("  ✅  Analysis complete.", C.green + C.bright));
    console.log(color(hr("═"), C.cyan) + "\n");

  } catch (err) {
    console.error(color(`\n  ✗  Analysis failed: ${err.message}`, C.red));
    process.exit(1);
  }
}

main().catch(err => {
  console.error(color(`\nFatal error: ${err.message}`, C.red));
  process.exit(1);
});
