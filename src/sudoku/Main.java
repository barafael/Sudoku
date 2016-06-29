package sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sudoku.controller.Controller;
import sudoku.game.SudokuBoard;

public class Main extends Application {

    private static final int WIDTH = 720;
    private static final int HEIGHT = 1080;
    private static SudokuBoard game;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/sudoku.fxml"));
        loader.setControllerFactory(c -> new Controller(primaryStage, game));
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        setUserAgentStylesheet(STYLESHEET_MODENA);
        scene.getStylesheets()
                .add(getClass().getResource("view/sample.css")
                        .toExternalForm());
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        game = new SudokuBoard();
        launch(args);
    }
}
