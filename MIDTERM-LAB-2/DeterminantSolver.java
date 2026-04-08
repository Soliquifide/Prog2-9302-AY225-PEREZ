/**
 * =====================================================
 * Student Name    : Mycho Louise D. Perez
 * Course          : Math 101 — Linear Algebra
 * Assignment      : Programming Assignment 1 — 3x3 Matrix Determinant Solver
 * School          : University of Perpetual Help System DALTA, Molino Campus
 * Date            : April 8, 2026
 * GitHub Repo     : https://github.com/Soliquifide/Prog2-9302-AY225-PEREZ.git/uphsd-cs-perez-mycholouise
 *
 * Description:
 *   This program finds the determinant of a 3x3 matrix that was assigned
 *   to me (Mycho Louise D. Perez) for Math 101. It shows every step of
 *   the solution clearly in the console so you can follow along easily.
 * =====================================================
 */
public class DeterminantSolver {

    // ── SECTION 1: My Assigned Matrix ───────────────────────────────────
    // These are the 9 numbers from my assigned matrix written as a 3x3 grid.
    // Each row of the grid is one inner array.
    // I am not allowed to ask the user to type these in — they must be fixed here.
    static int[][] matrix = {
        { 2, 3, 5 },   // Row 1
        { 4, 1, 6 },   // Row 2
        { 5, 2, 3 }    // Row 3
    };

    // ── SECTION 2: 2x2 Determinant Formula ──────────────────────────────
    // To solve a 3x3 determinant, I need to solve smaller 2x2 determinants first.
    // This method does exactly that. You give it 4 numbers (a, b, c, d)
    // arranged like a small 2x2 matrix, and it gives back the answer using
    // the formula: (a times d) minus (b times c).
    static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    // ── SECTION 3: Printing the Matrix ──────────────────────────────────
    // This method just displays the matrix nicely on screen so it is easy
    // to read. It puts a border around the numbers so it looks like
    // a proper matrix from a textbook.
    static void printMatrix(int[][] m) {
        System.out.println("┌               ┐");
        for (int[] row : m) {
            System.out.printf("│  %2d  %2d  %2d  │%n", row[0], row[1], row[2]);
        }
        System.out.println("└               ┘");
    }

    // ── SECTION 4: Solving the Determinant Step by Step ─────────────────
    // This is the main method that does all the work.
    // It follows the cofactor expansion rule along the first row, which means:
    //   1. Take each number in Row 1 one at a time
    //   2. Cover its row and column to get a smaller 2x2 matrix
    //   3. Find the determinant of that smaller matrix (called a "minor")
    //   4. Multiply it by the Row 1 number and apply the correct sign (+ or -)
    //   5. Add all three results together to get the final answer
    static void solveDeterminant(int[][] m) {

        // I am building the separator line using a loop because String.repeat()
        // does not work on older versions of Java.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 52; i++) sb.append('=');
        String line = sb.toString();

        // Print the title and show the matrix at the top of the output
        System.out.println(line);
        System.out.println("  3x3 MATRIX DETERMINANT SOLVER");
        System.out.println("  Student: Mycho Louise D. Perez");
        System.out.println("  Assigned Matrix:");
        System.out.println(line);
        printMatrix(m);
        System.out.println(line);
        System.out.println();
        System.out.println("Expanding along Row 1 (cofactor expansion):");
        System.out.println();

        // ── Step 1: Find Minor M₁₁ ──────────────────────────────────────
        // I hide Row 1 and Column 1. The 4 numbers that are left form a 2x2 matrix.
        // Those leftover numbers are at positions [1][1], [1][2], [2][1], [2][2].
        int minor11 = computeMinor(m[1][1], m[1][2], m[2][1], m[2][2]);
        System.out.printf("  Step 1 — Minor M₁₁: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][1], m[1][2], m[2][1], m[2][2],
            m[1][1], m[2][2], m[1][2], m[2][1], minor11);

        // ── Step 2: Find Minor M₁₂ ──────────────────────────────────────
        // I hide Row 1 and Column 2. The leftover numbers are at
        // positions [1][0], [1][2], [2][0], [2][2].
        int minor12 = computeMinor(m[1][0], m[1][2], m[2][0], m[2][2]);
        System.out.printf("  Step 2 — Minor M₁₂: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][0], m[1][2], m[2][0], m[2][2],
            m[1][0], m[2][2], m[1][2], m[2][0], minor12);

        // ── Step 3: Find Minor M₁₃ ──────────────────────────────────────
        // I hide Row 1 and Column 3. The leftover numbers are at
        // positions [1][0], [1][1], [2][0], [2][1].
        int minor13 = computeMinor(m[1][0], m[1][1], m[2][0], m[2][1]);
        System.out.printf("  Step 3 — Minor M₁₃: det([%d,%d],[%d,%d]) = (%d×%d)-(%d×%d) = %d%n",
            m[1][0], m[1][1], m[2][0], m[2][1],
            m[1][0], m[2][1], m[1][1], m[2][0], minor13);

        // ── Cofactor Terms ───────────────────────────────────────────────
        // Now I multiply each Row 1 number by its minor.
        // The signs follow the pattern: plus, minus, plus.
        // So the first and third get a positive sign, the middle one is negative.
        int c11 =  m[0][0] * minor11;   // positive
        int c12 = -m[0][1] * minor12;   // negative
        int c13 =  m[0][2] * minor13;   // positive

        System.out.println();
        System.out.printf("  Cofactor C₁₁ = (+1) × %d × %d = %d%n", m[0][0], minor11, c11);
        System.out.printf("  Cofactor C₁₂ = (-1) × %d × %d = %d%n", m[0][1], minor12, c12);
        System.out.printf("  Cofactor C₁₃ = (+1) × %d × %d = %d%n", m[0][2], minor13, c13);

        // ── Final Answer ─────────────────────────────────────────────────
        // I add the three cofactor results together to get the determinant.
        int det = c11 + c12 + c13;
        System.out.printf("%n  det(M) = %d + (%d) + %d%n", c11, c12, c13);
        System.out.println(line);
        System.out.printf("  ✓  DETERMINANT = %d%n", det);

        // ── Singular Matrix Check ────────────────────────────────────────
        // If the determinant is 0, the matrix is called "singular" which means
        // it has no inverse. I print a warning if that happens.
        if (det == 0) {
            System.out.println("  ⚠ The matrix is SINGULAR — it has no inverse.");
        }
        System.out.println(line);
    }

    // ── SECTION 5: Starting the Program ─────────────────────────────────
    // This is where Java begins running. I just call solveDeterminant()
    // and pass in my assigned matrix to kick everything off.
    public static void main(String[] args) {
        solveDeterminant(matrix);
    }

}
