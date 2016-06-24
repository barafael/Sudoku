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

    public Controller (SudokuGame game, Stage primaryStage) {
        this.game = game;
        this.primaryStage = primaryStage;
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
        CSVInput loader = new CSVInput();
        loader.loadCSV(game, selectedFile);
        System.out.println(game.toString());
    }
}
