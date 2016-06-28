package sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private final double EASY = 0.5;
    private final double HARD = 0.2;
    private static final int SIZE = 9;
    private final Stage primaryStage;
    private final SudokuBoard currentGame;
    private File initialState;

    @FXML
    public Button Solve;
    @FXML
    public Button New;
    @FXML
    public Button Restart;
    @FXML
    public ToolBar toolbar;
    @FXML
    public BorderPane mainBorderpane;
    @FXML
    public GridPane mainGridpane;

    public Controller(Stage primaryStage, SudokuBoard game) {
        this.primaryStage = primaryStage;
        currentGame = game;
        try {
            Path gamesaves = Paths.get("gamesaves");
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

    @FXML
    public void initialize() {
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
            SudokuBoard newGame = CSVInput.loadCSV(selectedFile);
            if (newGame != null) {
                CSVOutput.writeCSV(currentGame, initialState);
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
            CSVOutput.writeCSV(currentGame, selectedFile);
        }
    }

    public void newGame() {
        currentGame.newGame(SIZE);
        CSVOutput.writeCSV(currentGame, initialState);
    }

    public void restartGame() {
        currentGame.newGame(SIZE);
    }
}
