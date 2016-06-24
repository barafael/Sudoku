package sudoku.game;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by ra on 23.06.16.
 * Part of Sudoku, in package sudoku.game.
 */
public class SudokuGameTest {
    SudokuGame game;

    @org.junit.Before
    public void setUp() throws Exception {
        game = new SudokuGame();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        game = null;
    }

    @org.junit.Test
    public void setValue() throws Exception {
        game.setValue(1, 0, 5);
        byte[] zeroColumn = new byte[]{0, 5, 0, 0, 0, 0, 0, 0, 0};
        assert Arrays.equals(zeroColumn, game.getColumn((byte) 0));
        System.out.println("tests run!");
    }

    @org.junit.Test
    public void squareContains() throws Exception {
        game.setValue(0, 1, 1);
        game.setValue(2, 0, 3);
        game.setValue(1, 1, 5);
        assert game.squareContains((byte) 1, (byte) 0, (byte) 0);
        assert game.squareContains((byte) 3, (byte) 0, (byte) 0);
        assert game.squareContains((byte) 5, (byte) 0, (byte) 0);
        assert !game.squareContains((byte) 1, (byte) 0, (byte) 1);
    }

    @org.junit.Test
    public void columnContains() throws Exception {
        game.setValue(0, 1, 1);
        game.setValue(8, 1, 3);
        game.setValue(3, 1, 5);
        assert game.columnContains((byte) 3, (byte) 1);
        assert game.columnContains((byte) 5, (byte) 1);
        assert game.columnContains((byte) 0, (byte) 8);
    }

    @org.junit.Test
    public void setRow() throws Exception {
        game.setRow(1, new Byte[]{-1, 1, 2, 3, 4, 5, 6, 7, 8});
        System.out.println("Supposed to be incorrect!"); // mark error output as desired
        assert Arrays.equals(game.getRow(1), new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        game.setRow(3, new Byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        for (byte colIndex = 0; colIndex < 9; colIndex++) {
            assert game.getValue((byte) 3, colIndex) == colIndex + 1;
        }
    }

    @org.junit.Test
    public void getColumn() throws Exception {
        game.setValue(0, 0, 1);
        game.setValue(8, 0, 1);
        game.setValue(0, 8, 3);
        assert Arrays.equals(game.getColumn((byte) 0), new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 1});
        assert Arrays.equals(game.getColumn((byte) 8), new byte[]{3, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    @org.junit.Test
    public void perfectSquare() throws Exception {

        Method method = SudokuGame.class.getDeclaredMethod("isPerfectSquare", Integer.TYPE);
        method.setAccessible(true);
        assert !method.invoke(game, 10).equals(true);
        assert method.invoke(game, 81).equals(true);
    }
}
