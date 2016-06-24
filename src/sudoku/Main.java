package sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sudoku.controller.Controller;
import sudoku.game.SudokuGame;

public class Main extends Application {

    private static SudokuGame game;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/sudoku.fxml"));
        loader.setControllerFactory(c -> new Controller(game, primaryStage));
        Parent root = loader.load();
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        game = new SudokuGame();
        launch(args);
    }
}
