package sudoku.game.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static sudoku.game.SudokuUtil.validPosition;
import static sudoku.game.solver.BacktrackSolver.solve;

/**
 * Created by ra on 25.06.16.
 * Part of Sudoku, in package sudoku.game.generator.
 */
public class InitialStateGenerator {
    private final static Random rng = new Random();

    public static int[][] generateInitialState(double difficulty, final int SIZE) {
        final int[][] initial = new int[SIZE][SIZE]; // Empty initial positions
        randomisedSolve(initial, SIZE, 0, 0); // get solved sudoku
        pruneBoard(initial, SIZE, difficulty);
        return initial;
    }

    /**
     * Solve empty board, using randomised candidates - generates a new solved sudoku
     *
     * @param board board to be solved
     * @param row   row to start at
     * @param col   col to start at
     * @return if board was solvable
     */
    private static boolean randomisedSolve(int[][] board, int size, int row, int col) {
        if (row == size) {
            row = 0;
            if (++col == size)
                return true;
        }
        List<Integer> randomInts = new ArrayList<>();
        for (int val = 1; val <= size; ++val) {
            randomInts.add(val);
        }
        Collections.shuffle(randomInts);

        for (Integer randomInt : randomInts) {
            if (validPosition(board, size, row, col, randomInt)) {
                board[row][col] = randomInt;
                if (randomisedSolve(board, size, row + 1, col)) {
                    return true;
                }
            }
        }
        board[row][col] = 0;
        return false;
    }

    private static void pruneBoard(int[][] board, int size, double difficulty) {
        if (difficulty < 1 && difficulty > 0) {
            int remaining = (int) (size * size * difficulty);
            int toRemove = size * size - remaining;
            while (toRemove > 0) {
                boolean removed = tryRemove(board, size);
                toRemove = removed ? toRemove - 1 : toRemove;
            }
        }
    }

    private static boolean tryRemove(int[][] board, int size) {
        int row = rng.nextInt(size);
        int col = rng.nextInt(size);
        int value = board[row][col]; // != 0?
        board[row][col] = 0;
        // see if board still solvable
        // only interested if board is solvable, so use board as initial state
        if (solve(new int[size][size], board, size)) {
            return true;
        } else {
            board[row][col] = value;
            return false;
        }
    }
}
