package sudoku.game;

import java.lang.reflect.Method;

/**
 * Created by ra on 23.06.16.
 * Part of Sudoku, in package sudoku.game.
 */
public class SudokuGameTest {
    private SudokuGame game;

    @org.junit.Before
    public void setUp() throws Exception {
        int initialState[][] = {
                {0, 2, 0, 4, 0, 0, 7, 0, 0},
                {7, 0, 0, 0, 0, 6, 0, 0, 8},
                {0, 8, 3, 0, 0, 0, 0, 0, 1},
                {0, 0, 2, 6, 0, 0, 0, 0, 0},
                {0, 5, 0, 0, 0, 0, 0, 7, 0},
                {0, 0, 0, 0, 0, 3, 9, 0, 0},
                {9, 0, 0, 0, 0, 0, 8, 3, 0},
                {3, 0, 0, 5, 0, 0, 0, 0, 7},
                {0, 0, 1, 0, 0, 4, 0, 6, 0},
        };
        game = new SudokuGame(initialState);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        game = null;
    }

    @org.junit.Test
    public void setValue() throws Exception {
        assert !game.setValue(1, 0, 5);
        System.out.println("supposed to do that."); // mark error message
        assert game.setValue(8, 8, 3);
        assert game.setValue(3, 8, 8);
        assert !game.setValue(8, 0, 10);
        assert !game.setValue(12, 3, 1);
    }

    @org.junit.Test
    public void squareContains() throws Exception {
        game.setValue(0, 2, 1);
        game.setValue(2, 0, 3);
        game.setValue(1, 1, 5);
        assert game.squareContains(1, 1, 1);
        assert game.squareContains(2,  2,  3);
        assert game.squareContains(0,  0,  5);
        assert !game.squareContains( 2,  2,  4);
    }

    @org.junit.Test
    public void columnContains() throws Exception {
        game.setValue(1, 1, 1);
        game.setValue(8, 1, 3);
        game.setValue(3, 1, 5);
        assert game.colContains(1, 3);
        assert game.colContains(1, 5);
        assert game.colContains(8, 7);
        assert!game.colContains(7, 1);
    }

    @org.junit.Test
    public void perfectSquare() throws Exception {
        Method method = SudokuGame.class.getDeclaredMethod("isPerfectSquare", Integer.TYPE);
        method.setAccessible(true);
        assert!method.invoke(game, 10).equals(true);
        assert method.invoke(game, 81).equals(true);
    }
}
