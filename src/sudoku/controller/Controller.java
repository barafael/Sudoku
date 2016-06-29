package sudoku.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static final int TF_SIZE = 56;
    private final double EASY = 0.5;
    private final double HARD = 0.2;
    private static final int SIZE = 9;
    private final Stage primaryStage;
    private final SudokuBoard currentGame;
    private File initialState;
    private final Font numberFont = new Font("Comic Sans MS", 30);
    private final Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Background blueBG = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
    @FXML
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
            initialState = new File(gamesaves.toString() + "/initialState.csv"); // no toString()
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

        this.update(null, currentGame);

        currentGame.addObserver(this);
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
        if (arg instanceof SudokuBoard) {
            SudokuBoard game = (SudokuBoard) arg;
            for (int row = 0; row < game.getSize(); row++) {
                for (int col = 0; col < game.getSize(); col++) {
                    int value = game.getValue(row, col);
                    TextField textField = new TextField(value == 0 ? "" : "" + value);
                    textField.setAlignment(Pos.CENTER);
                    textField.setMaxSize(TF_SIZE, TF_SIZE);
                    textField.setMinSize(TF_SIZE, TF_SIZE);
                    textField.setFont(numberFont);
                    mainGridpane.add(textField, row, col);
                    if (game.isInitial(row, col)) {
                        textField.setBackground(blueBG);
                        textField.setEditable(false);
                    } else {
                        textField.setBackground(whiteBG);
                    }
                }
            }
        }
    }
}
