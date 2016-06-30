package sudoku.game.generator;

import sudoku.game.SudokuGame;
import sudoku.game.solver.BacktrackSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ra on 25.06.16.
 * Part of Sudoku, in package sudoku.game.generator.
 */
public class InitialStateGenerator {
    private final static Random rng = new Random();

    public static int[][] generateInitialState(double difficulty, final int SIZE) {
        final SudokuGame board = new SudokuGame(new int[SIZE][SIZE]); // Empty initial positions
        randomisedSolve(board, 0, 0); // get solved sudoku
        System.out.println("randomised solve:");
        System.out.println(board);
        pruneResult(board, difficulty);
        System.out.println("pruned randomised solve:");
        System.out.println(board);
        return board.getBoard();
    }

    private static void pruneResult(SudokuGame board, double difficulty) {
        if (difficulty < 1 && difficulty > 0) {
            int remaining = (int) (board.getSize() * board.getSize() * difficulty);
            int toRemove = board.getSize() * board.getSize() - remaining;
            while (toRemove > 0) {
                boolean removed = tryRemove(board);
                toRemove = removed ? toRemove - 1 : toRemove;
            }
        }
    }

    private static boolean tryRemove(SudokuGame board) {
        int row = rng.nextInt(board.getSize());
        int col = rng.nextInt(board.getSize());
        int value = board.getValue(row, col); // != 0?
        board.setValue(row, col, 0);
        // see if board still solvable
        if (BacktrackSolver.solve(new SudokuGame(board.getBoard()))) {
            return true;
        }
        else {
            board.setValue(row, col, value);
            return false;
        }
    }

    /**
     * Solve empty board, using randomised candidates - generates a new solved sudoku
     *
     * @param board board to be solved
     * @param row row to start at
     * @param col col to start at
     * @return if board was solvable
     */
    private static boolean randomisedSolve(SudokuGame board, int row, int col) {
        if (row == board.getSize()) {
            row = 0;
            if (++col == board.getSize())
                return true;
        }
        List<Integer> randomInts = new ArrayList<>();
        for (int val = 1; val <= board.getSize(); ++val) {
            randomInts.add(val);
        }
        Collections.shuffle(randomInts);

        for (Integer randomInt : randomInts) {
            if (validPosition(board, row, col, randomInt)) {
                board.setValue(row, col, randomInt);
                if (randomisedSolve(board, row + 1, col)) {
                    return true;
                }
            }
        }
        board.setValue(row, col, 0);
        return false;
    }

    private static boolean validPosition(SudokuGame board, int row, int col, int value) {
        return value == 0 ||
                (!board.rowContains(row, value) && !board.colContains(col, value) &&
                        !board.squareContains(row, col, value));
    }
}


