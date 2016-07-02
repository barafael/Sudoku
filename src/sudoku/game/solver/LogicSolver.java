package sudoku.game.solver;

import sudoku.controller.Move;
import sudoku.game.SudokuUtil;

import java.time.Instant;

import static sudoku.game.SudokuUtil.isFull;

public class LogicSolver {
    /**
     * This solver finds places where only one number is possible, and falls back on backtracking when no unique found.
     */
    public static boolean solve(int[][] board, int size) {
        Instant tic = Instant.now();
        if (solveHelper(board, size)) {
            return true;
        } else {
            int[][] copy = new int[size][size];
            for (int row = 0; row < size; row++) {
                System.arraycopy(board[row], 0, copy[row], 0, size);
            }
            boolean solution = BacktrackSolver.solve(board, copy, size);
            // System.out.println("Logic: " + Duration.between(tic, Instant.now()));
            return solution;
        }
    }

    private static boolean solveHelper(int[][] board, int size) {
        boolean change = true;
        while (!isFull(board) && change) {
            change = false;
            boolean[][][] occurrences = new boolean[size][size][size];
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (board[row][col] == 0) {
                        for (int value = 0; value < size; value++) {
                            occurrences[row][col][value] = occurs(board, size, row, col, value + 1);
                        }
                        int value = onlyOne(occurrences[row][col]);
                        if (value == -1) {
                            System.err.println("no vacant slots!");
                            return false;
                        } else if (value > 0) {
                            board[row][col] = value;
                            change = true;
                        }

                    }
                }
            }
        }
        // if board is not full, there was a non-unique situation. Probably, the board has still been partially solved
        return isFull(board);
    }


    private static int onlyOne(boolean[] booleans) {
        int count = 0;
        int value = -1;
        for (int index = 0; index < booleans.length; index++) {
            if (!booleans[index]) { // booleans is false at index
                count++;
                value = index + 1;
            }
        }
        if (count == 1) { // there was only one!
            return value;
        } else if (count == 0) { // all numbers taken, invalid board!
            return -1;
        } else {
            return 0; // not just one
        }
    }

    private static boolean occurs(int[][] board, int size, int row, int col, int value) {
        return !SudokuUtil.validPosition(board, size, row, col, value);
    }

    public static Move createHint(int[][] board, int size) {
        boolean[][][] occurrences = new boolean[size][size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    for (int value = 0; value < size; value++) {
                        occurrences[row][col][value] = occurs(board, size, row, col, value + 1);
                    }
                    int value = onlyOne(occurrences[row][col]);
                    if (value == -1) {
                        System.err.println("no vacant slots!");
                        return null;
                    } else if (value > 0) {
                        return new Move(row, col, value);
                    }
                }
            }
        }
        return null;
    }
}
