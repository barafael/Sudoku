package sudoku.game;

/**
 * Created by ra on 01.07.16.
 * Part of Sudoku, in package sudoku.game.
 */
public class SudokuUtil {
    /**
     * Returns if the specified row contains the specified value.
     *
     * @param rowIndex 0..SIZE-1
     * @return true if row at rowIndex contains value.
     */
    static boolean rowContains(final int[][] board, int size, final int rowIndex, final int value) {
        for (int colIndex = 0; colIndex < size; colIndex++) {
            if (board[rowIndex][colIndex] == value)
                return true;
        }
        return false;
    }

    /**
     * Returns if the specified column contains the specified value.
     *
     * @param colIndex 0..SIZE-1
     * @return true if column at colIndex contains value.
     */
    static boolean colContains(final int[][] board, int size, final int colIndex, final int value) {
        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
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
    static boolean squareContains(int[][] board, int size, int row, int col, int value) {
        if (row < size && row >= 0 &&
                col < size && row >= 0 &&
                value <= size && value >/*=*/ 0) {
            int squareRowIndex = row / 3;
            int squareColIndex = col / 3;

            int blockSize = (int) Math.sqrt(size);
            for (int rowIndex = squareRowIndex * blockSize; rowIndex < squareRowIndex * blockSize + blockSize; rowIndex++) {
                for (int colIndex = squareColIndex * blockSize; colIndex < squareColIndex * blockSize + blockSize;
                     colIndex++) {
                    if (board[rowIndex][colIndex] == value) {
                        return true;
                    }
                }
            }
            return false;
        } else return false;
    }

    public static boolean validPosition(int[][] board, int size, int row, int col, int value) {
        return value == 0 ||
                (!rowContains(board, size, row, value) && !colContains(board, size, col, value) &&
                        !squareContains(board, size, row, col, value));
    }

    static boolean isPerfectSquare(int number) {
        if (number > 0) { // 0 is explicitly excluded
            int temp = (int) (Math.sqrt(number));
            return temp * temp == number;
        } else {
            return false;
        }
    }
}
