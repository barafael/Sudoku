package sudoku.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ra on 01.07.16.
 * Part of Sudoku, in package sudoku.controller.
 */
public enum Message {
    NONE,
    CREATED,
    VALID_ENTERED,
    INVALID_ENTERED,
    RESET,
    AUTO_SOLVED,
    WON;

    private final List<Move> args = new ArrayList<>();
    private Move move;

    public List<Move> getArgs() {
        return args;
    }

    public void setArgs(Move... args) {
        Arrays.stream(args).forEach(this.args::add);
    }

    public Move getMove() {
        return move;
    }

    public void setMove(int row, int col, int value) {
            this.move = new Move(row, col, value);
    }
}
