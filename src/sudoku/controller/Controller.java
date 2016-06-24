package sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.controller.csvIO.CSVInput;
import sudoku.controller.csvIO.CSVOutput;
import sudoku.game.SudokuGame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {
    private static final double EASY = 0.3;
    private static final double HARD = 0.1;
    Stage primaryStage;
    SudokuGame game;
    CSVInput loader;
    CSVOutput writer;
    File sudokuInitialState;
    Path gamesaves = Paths.get("gamesaves");

    public Controller(SudokuGame game, Stage primaryStage) {
        this.game = game;
        this.primaryStage = primaryStage;
        loader = new CSVInput();
        writer = new CSVOutput();
        try {
            Files.createDirectories(gamesaves);
            sudokuInitialState = new File(gamesaves.toString() + "/initialState.csv");
            newGame();
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
            loader.loadCSV(game, selectedFile);
            writer.writeCSV(game, sudokuInitialState);
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
            writer.writeCSV(game, selectedFile);
        }
    }

    public void newGame() {
        game.populateRandom(EASY);
        writer.writeCSV(game, sudokuInitialState);
    }

    public void restartGame() {
        loader.loadCSV(game, sudokuInitialState);
    }
}
