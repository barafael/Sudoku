package sudoku.game;

import sudoku.game.generator.InitialStateGenerator;

import java.util.Arrays;
import java.util.Observable;

/**
 * Created by ra on 23.06.16.
 * Part of Sudoku, in package sudoku.game.solver.
 */
public class SudokuBoard extends Observable {
    /* Using simple 2-d array. Arrays can hold primitive types, collections cannot.
    Arrays have O(1) indexing and changing values. Size of board is constant.
    */
    private static final int SIZE = 9;

    private final int[][] board = new int[SIZE][SIZE]; // all arrays initialized with 0
    private final int[][] initial;

    public SudokuBoard(int[][] initialState) {
        if (initialState.length == SIZE && initialState[0].length == SIZE && correctRange(initialState)) {
            initial = initialState;
            for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) { // initialize board as well
                System.arraycopy(initial[rowIndex], 0, board[rowIndex], 0, SIZE);
            }
        } else {
            initial = new int[SIZE][SIZE]; // board can stay at 0
        }
        assert isPerfectSquare(SIZE);
    }

    public SudokuBoard() {
        initial = new int[SIZE][SIZE];
        assert isPerfectSquare(SIZE);
    }

    private static boolean correctRange(int[][] array) {
        return Arrays.stream(array).allMatch(
                integers -> Arrays.stream(integers).allMatch(
                        i -> i >= 0 && i <= SIZE));
    }

    public int getValue(int rowIndex, int colIndex) {
        if (rowIndex >= 0 && rowIndex < SIZE &&
                colIndex >= 0 && rowIndex < SIZE) {
            return board[rowIndex][colIndex];
        }
        System.err.println("Invalid access!");
        return 0;
    }

    public boolean isInitial(int row, int col) {
        return initial[row][col] != 0;
    }

    /**
     * Set a value in the grid and return true if arguments valid, false otherwise.
     *
     * @param rowIndex valid board index
     * @param colIndex valid board index
     * @param valIndex valid number between 1 and (including) SIZE.
     * @return false if invalid args, true otherwise.
     */
    public boolean setValue(int rowIndex, int colIndex, int valIndex) {
        if (rowIndex < SIZE && rowIndex >= 0 &&
                colIndex < SIZE && colIndex >= 0 &&
                valIndex <= SIZE && valIndex >= 0) { // valid arguments?

            if (initial[rowIndex][colIndex] != 0) {
                System.err.println("write on initial position!");
                return false;
            }
            board[rowIndex][colIndex] = valIndex;
            setChanged();
            notifyObservers(board);
            return true;
        } else {
            return false;
        }
    }

    int[] getColumn(int colIndex) {
        int[] column = new int[SIZE];
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            column[rowIndex] = board[rowIndex][colIndex];
        }
        return column;
    }

    /**
     * Returns if the specified column contains the specified value.
     *
     * @param colIndex 0..SIZE-1
     * @return true if column at colIndex contains value.
     */
    public boolean colContains(final int colIndex, final int value) {
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            if (board[rowIndex][colIndex] == value)
                return true;
        }
        return false;
    }

    /**
     * Returns if the specified row contains the specified value.
     *
     * @param rowIndex 0..SIZE-1
     * @return true if row at rowIndex contains value.
     */
    public boolean rowContains(final int rowIndex, final int value) {
        for (int colIndex = 0; colIndex < SIZE; colIndex++) {
            if (board[rowIndex][colIndex] == value)
                return true;
        }
        return false;
    }

    /**
     * Returns if the square containing specified entry contains the given val.
     *
     * @param row index of a row in the square
     * @param col index of a col in the square
     * @return true if val present in specified square.
     */
    public boolean squareContains(final int row, final int col, final int val) {
        if (row < SIZE && row >= 0 &&
                col < SIZE && row >= 0 &&
                val <= SIZE && val > 0) {
            return squareContainsHelper(row / 3, col / 3, val);
        } else return false;
    }

    /**
     * Returns if the specified square contains the given value.
     *
     * @param squareRowIndex index of the row the square starts on divided by sqrt(SIZE)
     *                       I.e., to get to the middle row in a sudoku of 9,
     *                       squareRowIndex would be 1
     * @param squareColIndex index of the column the square starts on divided by sqrt(SIZE)
     *                       I.e., to get to the first column in a sudoku of 9,
     *                       squareColIndex would be 0
     * @return true if value present in specified square.
     */
    private boolean squareContainsHelper(final int squareRowIndex, final int squareColIndex, final int value) {
        int blockSize = (int) Math.sqrt(SIZE);
        for (int row = squareRowIndex * blockSize; row < squareRowIndex * blockSize + blockSize; row++) {
            for (int col = squareColIndex * blockSize; col < squareColIndex * blockSize + blockSize; col++) {
                if (board[row][col] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPerfectSquare(int number) {
        if (number > 0) { // 0 is explicitly excluded
            int temp = (int) (Math.sqrt(number));
            return temp * temp == number;
        } else {
            return false;
        }
    }

    public int getSize() {
        return SIZE;
    }

    public String toCSV() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < SIZE - 1; colIndex++) { // all but last, ';' each
                stringBuilder.append(board[rowIndex][colIndex]).append(";");
            }
            stringBuilder.append(board[rowIndex][SIZE - 1]).append('\n'); // no ';' for last
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                stringBuilder.append(" -----------------------\n");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) stringBuilder.append("| ");
                stringBuilder.append(board[i][j] == 0
                        ? " "
                        : board[i][j]);

                stringBuilder.append(' ');
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append(" -----------------------\n");
        return stringBuilder.toString();
    }

    public void newGame(int size) {
        int[][] newBoard = InitialStateGenerator.generateInitialState(size);
        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
            System.arraycopy(newBoard, 0, board, 0, size);
        }
        setChanged();
        notifyObservers(board);
    }

    public void setBoard(int[][] board) {
        // board is final
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = board[row][col];
            }
        }
        setChanged();
        notifyObservers(board);
    }
}
//TODO delete superfluous methods
