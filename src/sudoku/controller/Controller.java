package sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private final double EASY = 0.5;
    private final double HARD = 0.2;
    private static final int SIZE = 9;
    private final Stage primaryStage;
    private final SudokuBoard currentGame;
    private File initialState;
    private final TextField[][] textFields = new TextField[SIZE][SIZE];
    private final Label[][] labels = new Label[SIZE][SIZE];
    public VBox mainVBox;

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
        currentGame.addObserver(this);

        mainGridpane.setMaxWidth(1100);
        mainGridpane.setMinWidth(1000);
        mainGridpane.setMaxHeight(800);
        mainGridpane.setMinHeight(700);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                labels[row][col] = new Label("");
                labels[row][col].setVisible(false);
                textFields[row][col] = new TextField("");
                // textFields[row][col].setVisible(false);
                mainGridpane.add(textFields[row][col], row, col);
            }
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
            CSVInput.loadCSV(selectedFile, currentGame);
            CSVOutput.writeCSV(currentGame, initialState);
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
        CSVInput.loadCSV(initialState, currentGame);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("board changed!");
        if (arg instanceof int[][]) {
            int[][] board = (int[][]) arg;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    labels[row][col].setText(board[row][col] + "");
                    textFields[row][col].setText(board[row][col] + "");
                }
            }
        }
    }
}
