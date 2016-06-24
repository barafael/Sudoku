package sudoku.game;

import java.util.Arrays;
import java.util.Observable;

/**
 * Created by ra on 23.06.16.
 * Part of Sudoku, in package sudoku.game.solver.
 */
public class SudokuGame extends Observable {
    /* Using byte type because an int is 4 times the size. So, for 9x9 = 81, that would be 324 bytes for int,
    but only 81 for byte.
    Sudoku games with more than 127 rows/cols aren't going to be solved that soon anyway.
    Also using simple 2-d array. Arrays can hold primitive types, collections cannot.
    Arrays have O(1) indexing and changing values. Size of field is constant.
    */
    private static final byte SIZE = 9;
    private final byte[][] field = new byte[SIZE][SIZE];

    { // initialize field with poison value 0(which indicates no value set).
        for (int i = 0; i < SIZE; i++)
            Arrays.fill(field[i], (byte) 0); // compile time cast; inconvenient, but has to be done.
    }

    public SudokuGame() {
        assert isPerfectSquare(SIZE);
    }

    public byte getValue(byte rowIndex, byte colIndex) {
        if (rowIndex >= 0 && rowIndex < SIZE &&
                colIndex >= 0 && rowIndex < SIZE) {
            return field[rowIndex][colIndex];
        }
        System.err.println("Invalid access!");
        return 0;
    }

    /**
     * helper method to enable calling setValue with ints or literal values. Preferably unused.
     *
     * @param row 0..SIZE-1
     * @param col 0..SIZE-1
     * @param val 1..SIZE
     * @return null if invalid arguments, this instance otherwise.
     */
    public SudokuGame setValue(int row, int col, int val) {
        return setValue((byte) row, (byte) col, (byte) val);
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
        if (row < SIZE && row >= 0 &&
                col < SIZE && col >= 0 &&
                val <= SIZE && val > 0) { // valid arguments?
            field[row][col] = val;
            setChanged();
            notifyObservers();
            return this; // enable chaining
        } else {
            return null;
        }
    }

    public void setRow(int rowIndex, Byte[] row) {
        if (row.length == SIZE && rowIndex < SIZE && rowIndex >= 0) {
            boolean correctRange = Arrays.stream(row).allMatch(i -> i >= 0 && i <= SIZE);
            if (correctRange) {
                for (int colIndex = 0; colIndex < SIZE; colIndex++) {
                    field[rowIndex][colIndex] = row[colIndex];
                }
                setChanged();
                notifyObservers();
            } else {
                System.err.println("Incorrect range!");
            }
        }
    }

    public byte[] getRow(int rowIndex) {
        byte[] row = new byte[SIZE];
        for (int colIndex = 0; colIndex < SIZE; colIndex++) {
            row[rowIndex] = field[rowIndex][colIndex];
        }
        return row;
    }

    public byte[] getColumn(byte colIndex) {
        byte[] column = new byte[SIZE];
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            column[rowIndex] = field[rowIndex][colIndex];
        }
        return column;
    }

    public void setColumn(int colIndex, int[] col) {
        if (col.length == SIZE && colIndex < SIZE && colIndex >= 0) {
            boolean correctRange = Arrays.stream(col).allMatch(i -> i >= 0 && i <= SIZE);
            if (correctRange) {
                for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
                    field[rowIndex][colIndex] = (byte) col[rowIndex];
                }
                setChanged();
                notifyObservers();
            } else {
                System.err.println("Incorrect range!");
            }
        }
    }

    /**
     * Returns if the specified column contains the specified value.
     *
     * @param colIndex 0..SIZE-1
     * @return true if column at colIndex contains value.
     */
    public boolean columnContains(final byte value, final byte colIndex) {
        for (int rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            if (field[rowIndex][colIndex] == value)
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
    public boolean rowContains(final byte value, final byte rowIndex) {
        for (int colIndex = 0; colIndex < SIZE; colIndex++) {
            if (field[rowIndex][colIndex] == value)
                return true;
        }
        return false;
    }

    /**
     * Returns if the specified square contains the given value.
     *
     * @param rowIndex index of the row the square starts on divided by sqrt(SIZE)
     *                 I.e., to get to the middle row in a sudoku of 9,
     *                 rowIndex would be 1
     * @param colIndex index of the column the square starts on divided by sqrt(SIZE)
     *                 I.e., to get to the first column in a sudoku of 9,
     *                 colIndex would be 0
     * @return true if value present in specified square.
     */
    public boolean squareContains(final byte value, final byte rowIndex, final byte colIndex) {
        if (rowIndex * rowIndex > SIZE || colIndex * colIndex > SIZE) {
            throw new IllegalArgumentException();
        }
        byte blockSize = (byte) Math.sqrt(SIZE);
        for (int row = rowIndex * blockSize; row < rowIndex * blockSize + blockSize; row++) {
            for (int col = colIndex * blockSize; col < colIndex * blockSize + blockSize; col++) {
                if (field[row][col] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPerfectSquare(int number) {
        if (number > 0) { // 0 is explicitly excluded
            long temp = (long) (Math.sqrt(number));
            return temp * temp == number;
        } else {
            return false;
        }
    }

    public byte getSize() {
        return SIZE;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte rowIndex = 0; rowIndex < SIZE; rowIndex++) {
            for (byte colIndex = 0; colIndex < SIZE-1; colIndex++) {
                stringBuilder.append(field[rowIndex][colIndex]).append(";");
            }
            stringBuilder.append(field[rowIndex][SIZE-1]).append('\n');
        }
        return stringBuilder.toString();
    }
}
