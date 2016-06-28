package sudoku.game.solver;

import sudoku.game.SudokuBoard;

public class BacktrackSolver {

    private final SudokuBoard board;
    private final int SIZE;

    public BacktrackSolver(SudokuBoard board) {
        this.board = board;
        this.SIZE = board.getSize();
    }

    public boolean solve() {
        return solve(0, 0); // start at upper left corner, the rest is recursion
    }

    private boolean solve(int row, int col) {
        if (row == SIZE) {
            row = 0;
            if (++col == SIZE)
                return true;
        }
        if (board.isInitial(row, col))
            return solve(row + 1, col);

        for (int val = 1; val <= SIZE; ++val) {
            if (validPosition(row, col, val)) {
                board.setValue(row, col, val);
                if (solve(row + 1, col))
                    return true;
            }
        }

        board.setValue(row, col, 0);
        return false;
    }

    private boolean validPosition(int row, int col, int value) {
        return value == 0 ||
                (!board.rowContains(row, value) && !board.colContains(col, value) &&
                        !board.squareContains(row, col, value));
    }
}
