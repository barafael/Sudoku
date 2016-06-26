package sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.controller.csvIO.CSVInput;
import sudoku.controller.csvIO.CSVOutput;
import sudoku.game.SudokuBoard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    private final double EASY = 0.3;
    private final double HARD = 0.1;
    private static final int GAME_SIZE = 9;
    private final Stage primaryStage;
    private final SudokuBoard currentGame;
    private final CSVInput loader;
    private final CSVOutput writer;
    private File initialState;
    private final Path gamesaves = Paths.get("gamesaves");
    public Button New;
    public Button Restart;
    @FXML
    private VBox mainVBox;
    @FXML
    public ToolBar toolbar;
    @FXML
    public Separator separator;
    @FXML
    public GridPane mainGridpane;

    public Controller(Stage primaryStage, SudokuBoard game) {
        this.primaryStage = primaryStage;
        currentGame = game;
        loader = new CSVInput();
        writer = new CSVOutput();
        try {
            Files.createDirectories(gamesaves);
            initialState = new File(gamesaves.toString() + "/initialState.csv");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("File I/O Error");
            alert.setContentText("There was an error during gamesave directory opening/writing.");
            alert.showAndWait();
        }
    }

    public void loadCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            SudokuBoard newGame = loader.loadCSV(selectedFile);
            if (newGame != null) {
                writer.writeCSV(currentGame, initialState);
            }
        }
    }

    public void writeCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile != null) {
            writer.writeCSV(currentGame, selectedFile);
        }
    }

    public void newGame() {
        currentGame.newGame(GAME_SIZE);
        writer.writeCSV(currentGame, initialState);
    }

    public void restartGame() {
        currentGame.newGame(GAME_SIZE);
    }
}
