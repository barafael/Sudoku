package sudoku.controller;

/**
 * Created by ra on 01.07.16.
 * Part of Sudoku, in package sudoku.controller.
 */
public class Move {
    public final int row;
    public final int col;
    public final int value;

    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
