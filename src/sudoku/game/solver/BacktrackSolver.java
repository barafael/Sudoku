package sudoku.game.solver;

import sudoku.game.SudokuUtil;

public class BacktrackSolver {
    public static boolean solve(int[][] board, final int[][] initial, final int size) {
        // start at upper left corner, the rest is recursion
        return solve(board, initial, size, 0, 0);
    }

    private static boolean solve(int[][] board, final int[][] initial, int size, int row, int col) {
        if (row == size) {
            row = 0;
            if (++col == size)
                return true;
        }
        if (initial[row][col] != 0)
            return solve(board, initial, size, row + 1, col);

        for (int val = 1; val <= size; ++val) {
            if (SudokuUtil.validPosition(board, size, row, col, val)) {
                board[row][col] = val;
                if (solve(board, initial, size, row + 1, col))
                    return true;
            }
        }

        board[row][col] = 0; // reset position! This branch was not solvable
        return false;
    }
}
