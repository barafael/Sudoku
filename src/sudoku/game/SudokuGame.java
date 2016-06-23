package sudoku.game;

import java.util.Arrays;

/**
 * Created by ra on 23.06.16.
 * Part of Sudoku, in package sudoku.game.solver.
 */
public class SudokuGame {
    // Using bytes because an int is 4 times the size. So, for 9x9 = 81, that would be 324 bytes for int, but only 81
    // for byte.
    // Sudoku games with more than 127 rows/cols aren't going to be solved that soon anyway.
    private static final byte SIZE = 9;
    byte[][] field = new byte[SIZE][SIZE];

    { // initialize field with poison value 0(which indicates no value set).
        for (int i = 0; i < SIZE; i++)
            Arrays.fill(field[i], (byte) 0); // compile time cast; inconvenient, but has to be done.
    }

    /**
     * Set a value in the grid and return the game if arguments valid, null otherwise.
     * This method can be used like this:
     * game.setValue(0,0,4).setValue(0,1,5);
     *
     * @param row valid field index
     * @param col valid field index
     * @param val valid number between 1 and (including) SIZE.
     * @return null if invalid args,the game otherwise.
     */
    public SudokuGame setValue(byte row, byte col, byte val) {
        //@formatter:off
        if (row < SIZE && row >= 0 &&
            col < SIZE && col >= 0 &&
            val <= SIZE &&  val > 0) { // valid arguments
            // formatter:on
            field[row][col] = val;
            return this; // enable chaining
        } else {
            return null;
        }
    }
    /** return an array that is the column at colIndex. Changes don't write through.
     * For that, the setValue() method has to be used.
     * The returned array contains a 0 value if no value has been set.
     * @param colIndex
     * @return
     */
    public byte[] getColumn(final byte colIndex) {
        byte[] column = new byte[SIZE];
        for(int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            column[rowIndex] = field[rowIndex][colIndex];
        }
        return column;
    }
}
