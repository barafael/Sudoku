package sudoku.game.solver;

import sudoku.game.SudokuBoard;

public class BacktrackSolver {
    public static boolean solve(SudokuBoard board) {
        return solve(board, 0, 0); // start at upper left corner, the rest is recursion
    }

    private static boolean solve(SudokuBoard board, int row, int col) {
        int SIZE = board.getSize();
        if (row == SIZE) {
            row = 0;
            if (++col == SIZE)
                return true;
        }
        if (board.isInitial(row, col))
            return solve(board, row + 1, col);

        for (int val = 1; val <= SIZE; ++val) {
            if (validPosition(board, row, col, val)) {
                board.setValue(row, col, val);
                if (solve(board, row + 1, col))
                    return true;
            }
        }

        board.setValue(row, col, 0);
        return false;
    }

    private static boolean validPosition(SudokuBoard board, int row, int col, int value) {
        return value == 0 ||
                (!board.rowContains(row, value) && !board.colContains(col, value) &&
                        !board.squareContains(row, col, value));
    }
}
