package sudoku.game;

import java.util.Observable;

/**
 * The game of Sudoku.
 * This class provides a model of a sudoku game. The class can be constructed of an initial board.
 * The entries of the initial board are immutable.
 * The board array always keeps all entries, even the ones from the initial board, but they are protected.
 * Since the class inherits from Observable, observers can be added to it.
 * They are notified whenever an entry in the backing array changes.
 */
public class SudokuGame extends Observable {
    /* Using simple 2-d array. Arrays can hold primitive types, collections cannot.
    Arrays have O(1) indexing and changing values. Size of board is constant.
    */
    private static final int SIZE = 9;

    // all arrays initialized with 0
    private final int[][] board = new int[SIZE][SIZE];
    private final int[][] initial = new int[SIZE][SIZE];

    public SudokuGame(int[][] initialState) {
        generateInitial(initialState);
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
     * @param value    valid number between 1 and (including) SIZE.
     * @return false if invalid args, true otherwise.
     */
    public boolean setValue(int rowIndex, int colIndex, int value) {
        if (rowIndex < SIZE && rowIndex >= 0 &&
                colIndex < SIZE && colIndex >= 0 &&
                value <= SIZE && value >= 0) { // valid arguments?

            if (initial[rowIndex][colIndex] != 0) {
                System.err.println("write on initial position!");
                return false;
            }
            board[rowIndex][colIndex] = value;
            setChanged();
            notifyObservers();
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
                stringBuilder.append(" ———————————————————————\n");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) stringBuilder.append("| ");
                stringBuilder.append(board[i][j] == 0
                        ? " "
                        : board[i][j]);

                stringBuilder.append(' ');
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append(" ———————————————————————\n");
        return stringBuilder.toString();
    }

    private void generateInitial(int[][] initial) {
        if (isPerfectSquare(initial.length) && isPerfectSquare(initial[0].length) &&
                initial.length == initial[0].length &&
                initial.length == SIZE) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    this.board[row][col] = 0;
                    this.initial[row][col] = 0;
                    if (initial[row][col] != 0) {
                        this.initial[row][col] = initial[row][col];
                        this.board[row][col] = initial[row][col];
                    }
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    public boolean solve() {
        if (solve(0, 0)) {
            setChanged();
            notifyObservers();
            return true;
        } else
            return false; // are manual entries overwritten?
    }

    private boolean solve(int row, int col) {
        if (row == SIZE) {
            row = 0;
            if (++col == SIZE)
                return true;
        }
        if (isInitial(row, col))
            return solve(row + 1, col);

        for (int val = 1; val <= SIZE; ++val) {
            if (validPosition(row, col, val)) {
                board[row][col] = val;
                if (solve(row + 1, col))
                    return true;
            }
        }
        board[row][col] = 0;
        return false;

    }

    private boolean validPosition(int row, int col, int value) {
        return value == 0 ||
                (!rowContains(row, value) && !colContains(col, value) &&
                        !squareContains(row, col, value));
    }

    public boolean isSolved() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!validPosition(row, col, board[row][col]))
                    return false;
            }
        }
        return true;
    }

    public int[][] getBoard() {
        int[][] arr = new int[SIZE][SIZE];
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            System.arraycopy(board, 0, arr, 0, SIZE);
        }
        return arr;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if(!isInitial(row, col)) {
                    board[row][col] = 0;
                }
            }
        }
        setChanged();
        notifyObservers();
    }
}
// TODO simplify squarecontains lookup
