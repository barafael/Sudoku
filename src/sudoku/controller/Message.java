package sudoku.controller;

/**
 * Created by ra on 01.07.16.
 * Part of Sudoku, in package sudoku.controller.
 */
public enum Message {
    NONE,
    CREATED,
    VALID_ENTERED,
    INVALID_ENTERED,
    HINT,
    RESET,
    AUTO_SOLVED,
    WON;

    private Move move;

    public Move getMove() {
        return move;
    }

    public void setMove(int row, int col, int value) {
            this.move = new Move(row, col, value);
    }
}
