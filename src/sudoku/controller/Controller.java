package sudoku.controller;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.controller.csvIO.CSVInput;
import sudoku.controller.csvIO.CSVOutput;
import sudoku.game.SudokuGame;

import java.io.File;

public class Controller {
    Stage primaryStage;
    SudokuGame game;
    CSVInput loader;
    CSVOutput writer;

    public Controller(SudokuGame game, Stage primaryStage) {
        this.game = game;
        this.primaryStage = primaryStage;
        loader = new CSVInput();
        writer = new CSVOutput();
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
}
