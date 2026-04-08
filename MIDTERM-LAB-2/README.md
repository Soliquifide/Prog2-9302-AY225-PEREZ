# Programming Assignment 1 — 3×3 Matrix Determinant Solver

## Student Information
- **Full Name:** Mycho Louise D. Perez
- **Section:** 1203L 9302-AY225
- **Course:** Math 101 – Linear Algebra, UPHSD Molino Campus
- **Assignment:** Programming Assignment 1 — 3x3 Matrix Determinant Solver
- **Academic Year:** 2025–2026
- **Date Completed:* April,8,2026

---

## Assigned Matrix

My assigned matrix (Student #30):

```
| 2  3  5 |
| 4  1  6 |
| 5  2  3 |
```

---

## How to Run the Java Program

**Step 1 — Compile the program:**
```bash
javac DeterminantSolver.java
```

**Step 2 — Run the compiled program:**
```bash
java DeterminantSolver
```

> Make sure you are inside the `linear-algebra/assignment-01/` folder when running these commands.

---

## How to Run the JavaScript Program

**Run with Node.js:**
```bash
node determinant_solver.js
```

> Requires Node.js to be installed. No additional packages needed.

---

## Sample Output

Both programs produce the same result. Below is the sample output:

```
====================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: Mycho Louise D. Perez
  Assigned Matrix:
====================================================
┌               ┐
│   2   3   5   │
│   4   1   6   │
│   5   2   3   │
└               ┘
====================================================

Expanding along Row 1 (cofactor expansion):

  Step 1 — Minor M₁₁: det([1,6],[2,3]) = (1×3) - (6×2) = -9
  Step 2 — Minor M₁₂: det([4,6],[5,3]) = (4×3) - (6×5) = -18
  Step 3 — Minor M₁₃: det([4,1],[5,2]) = (4×2) - (1×5) = 3

  Cofactor C₁₁ = (+1) × 2 × -9 = -18
  Cofactor C₁₂ = (-1) × 3 × -18 = 54
  Cofactor C₁₃ = (+1) × 5 × 3 = 15

  det(M) = -18 + (54) + 15
====================================================
  ✓  DETERMINANT = 51
====================================================
```

---

## Final Determinant Value

**det(M) = 51**

Since the determinant is not zero, the matrix is **non-singular** and has an inverse.

---

## Files in This Repository

| File | Description |
|------|-------------|
| `DeterminantSolver.java` | Java solution — computes the determinant with step-by-step console output |
| `determinant_solver.js` | JavaScript (Node.js) solution — identical logic and output |
| `README.md` | This documentation file |

---

## Repository

**GitHub:** `https://github.com/[your-username]/uphsd-cs-perez-mychoulouise`
