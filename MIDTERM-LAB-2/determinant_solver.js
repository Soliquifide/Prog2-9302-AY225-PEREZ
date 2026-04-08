/**
 * =====================================================
 * Student Name    : Mycho Louise D. Perez
 * Course          : Math 101 — Linear Algebra
 * Assignment      : Programming Assignment 1 — 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : April 8, 2026
 * GitHub Repo     : https://github.com/Soliquifide/Prog2-9302-AY225-PEREZ.git/uphsd-cs-perez-mycholouise
 * Runtime         : Node.js (run with: node determinant_solver.js)
 *
 * Description:
 *   This is the JavaScript version of my determinant solver. It does the exact
 *   same thing as the Java version — it finds the determinant of my assigned
 *   3x3 matrix and prints every step so the solution is easy to follow.
 * =====================================================
 */

// ── SECTION 1: My Assigned Matrix ───────────────────────────────────
// Here I store the 9 numbers of my assigned matrix as a 2D array.
// Each inner array is one row. The values must match exactly what
// was given to me in the assignment — I cannot change these numbers.
const matrix = [
    [2, 3, 5],   // Row 1
    [4, 1, 6],   // Row 2
    [5, 2, 3]    // Row 3
];

// ── SECTION 2: Printing the Matrix ──────────────────────────────────
// This function displays the matrix on screen with a border around it
// so it looks neat and easy to read. Each number is right-aligned
// using padStart so the columns line up properly.
function printMatrix(m) {
    console.log(`┌               ┐`);
    m.forEach(row => {
        const fmt = row.map(v => v.toString().padStart(3)).join("  ");
        console.log(`│ ${fmt}  │`);
    });
    console.log(`└               ┘`);
}

// ── SECTION 3: 2x2 Determinant Formula ──────────────────────────────
// Before I can solve the big 3x3 determinant, I need to solve three
// smaller 2x2 ones. This function handles that.
// You pass in 4 numbers: a and b for the top row, c and d for the bottom.
// The formula is: (a times d) minus (b times c).
function computeMinor(a, b, c, d) {
    return (a * d) - (b * c);
}

// ── SECTION 4: Solving the Determinant Step by Step ─────────────────
// This is the main function that runs the whole solution.
// It uses cofactor expansion along the first row, meaning:
//   - I go through each number in Row 1 one at a time
//   - For each one, I cover its row and column to get a 2x2 sub-matrix
//   - I find the determinant of that sub-matrix (the "minor")
//   - I multiply it by the Row 1 number with the correct sign (+ or -)
//   - Finally I add all three results to get the determinant
function solveDeterminant(m) {
    const line = "=".repeat(52);

    // Print the title header and show the matrix before any calculations
    console.log(line);
    console.log("  3x3 MATRIX DETERMINANT SOLVER");
    console.log("  Student: Mycho Louise D. Perez");
    console.log("  Assigned Matrix:");
    console.log(line);
    printMatrix(m);
    console.log(line);
    console.log();
    console.log("Expanding along Row 1 (cofactor expansion):");
    console.log();

    // ── Step 1: Find Minor M₁₁ ──────────────────────────────────────
    // Cover Row 1 and Column 1. The 4 numbers left are at
    // positions [1][1], [1][2], [2][1], and [2][2].
    const minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
    console.log(
        `  Step 1 — Minor M₁₁: det([${m[1][1]},${m[1][2]}],[${m[2][1]},${m[2][2]}])` +
        ` = (${m[1][1]}×${m[2][2]}) - (${m[1][2]}×${m[2][1]}) = ${minor11}`
    );

    // ── Step 2: Find Minor M₁₂ ──────────────────────────────────────
    // Cover Row 1 and Column 2. The 4 numbers left are at
    // positions [1][0], [1][2], [2][0], and [2][2].
    const minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
    console.log(
        `  Step 2 — Minor M₁₂: det([${m[1][0]},${m[1][2]}],[${m[2][0]},${m[2][2]}])` +
        ` = (${m[1][0]}×${m[2][2]}) - (${m[1][2]}×${m[2][0]}) = ${minor12}`
    );

    // ── Step 3: Find Minor M₁₃ ──────────────────────────────────────
    // Cover Row 1 and Column 3. The 4 numbers left are at
    // positions [1][0], [1][1], [2][0], and [2][1].
    const minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
    console.log(
        `  Step 3 — Minor M₁₃: det([${m[1][0]},${m[1][1]}],[${m[2][0]},${m[2][1]}])` +
        ` = (${m[1][0]}×${m[2][1]}) - (${m[1][1]}×${m[2][0]}) = ${minor13}`
    );

    // ── Cofactor Terms ───────────────────────────────────────────────
    // Each cofactor is the Row 1 number multiplied by its minor.
    // The signs alternate: the 1st is positive, 2nd is negative, 3rd is positive.
    const c11 =  m[0][0] * minor11;   // positive
    const c12 = -m[0][1] * minor12;   // negative
    const c13 =  m[0][2] * minor13;   // positive

    console.log();
    console.log(`  Cofactor C₁₁ = (+1) × ${m[0][0]} × ${minor11} = ${c11}`);
    console.log(`  Cofactor C₁₂ = (-1) × ${m[0][1]} × ${minor12} = ${c12}`);
    console.log(`  Cofactor C₁₃ = (+1) × ${m[0][2]} × ${minor13} = ${c13}`);

    // ── Final Answer ─────────────────────────────────────────────────
    // I add the three cofactor values together to get the final determinant.
    const det = c11 + c12 + c13;
    console.log();
    console.log(`  det(M) = ${c11} + (${c12}) + ${c13}`);
    console.log(line);
    console.log(`  ✓  DETERMINANT = ${det}`);

    // ── Singular Matrix Check ────────────────────────────────────────
    // A determinant of 0 means the matrix is "singular" — it cannot be inverted.
    // I check for this and print a warning message if it happens.
    if (det === 0) {
        console.log("  ⚠ The matrix is SINGULAR — it has no inverse.");
    }
    console.log(line);
}

// ── SECTION 5: Starting the Program ─────────────────────────────────
// This line runs the program. I pass my assigned matrix into
// solveDeterminant() and it handles everything from there.
solveDeterminant(matrix);
