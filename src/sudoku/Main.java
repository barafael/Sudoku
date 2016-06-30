package sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sudoku.controller.Controller;

public class Main extends Application {

    private static final int WIDTH = 810;
    private static final int HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/sudoku.fxml"));
        loader.setControllerFactory(c -> new Controller(primaryStage));
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        setUserAgentStylesheet(STYLESHEET_MODENA);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
