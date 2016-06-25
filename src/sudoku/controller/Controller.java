package sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

import static sudoku.game.generator.InitialStateGenerator.generateInitialState;

public class Controller {
    private static final double EASY = 0.3;
    private static final double HARD = 0.1;
    private static final int GAME_SIZE = 9;
    private Stage primaryStage;
    private SudokuBoard currentGame;
    private CSVInput loader;
    private CSVOutput writer;
    private File initialState;
    private Path gamesaves = Paths.get("gamesaves");

    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loader = new CSVInput();
        writer = new CSVOutput();
        try {
            Files.createDirectories(gamesaves);
            initialState = new File(gamesaves.toString() + "/initialState.csv");
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
        currentGame = new SudokuBoard(generateInitialState(GAME_SIZE));
        writer.writeCSV(currentGame, initialState);
    }

    public void restartGame() {
        currentGame = loader.loadCSV(initialState);
    }
}
