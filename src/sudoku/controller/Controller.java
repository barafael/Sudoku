package sudoku.controller;

import javafx.event.ActionEvent;
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
import sudoku.game.generator.InitialStateGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private static final int SIZE = 9;
    private final double EASY = 0.5;
    private final double HARD = 0.2;

    private final SudokuBoard game;
    private final Stage primaryStage;
    private File initialState;

    private final TextField[][] textFields = new TextField[SIZE][SIZE];
    private final Font numberFont = new Font("Comic Sans MS", 30);
    private final Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Background blueBG = new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY));

    private static final int TEXTFIELD_SIZE = 58;
    @FXML
    public VBox mainVBox;
    @FXML
    public Button solveButton;
    @FXML
    public Button newGameButton;
    @FXML
    public Button restartButton;
    @FXML
    public ToolBar toolbar;
    @FXML
    public BorderPane mainBorderpane;
    @FXML
    public GridPane mainGridpane;

    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        game = new SudokuBoard(InitialStateGenerator.generateInitialState(HARD, SIZE));
        try {
            Path gamesaves = Paths.get("gamesaves");
            Files.createDirectories(gamesaves);
            initialState = new File(gamesaves.toString() + "/initialState.csv"); // no toString()
            CSVOutput.writeCSV(game, initialState);
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
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                TextField textField = new TextField("");
                limitNumberField(textField);
                textField.setAlignment(Pos.CENTER);
                textField.setMaxSize(TEXTFIELD_SIZE, TEXTFIELD_SIZE);
                textField.setMinSize(TEXTFIELD_SIZE, TEXTFIELD_SIZE);
                textField.setFont(numberFont);
                textField.setOnKeyTyped(event -> {
                    TextField source = (TextField) event.getSource();
                    int tf_row = GridPane.getRowIndex(source);
                    int tf_col = GridPane.getColumnIndex(source);
                    updateModel(event.getCharacter(), tf_row, tf_col);
                });
                mainGridpane.add(textField, col, row);
                if (game.isInitial(row, col)) {
                    textField.setBackground(blueBG);
                    textField.setEditable(false);
                } else {
                    textField.setBackground(whiteBG);
                    textField.setEditable(true);
                }
                textFields[row][col] = textField;
            }
        }
        game.addObserver(this);
        this.update(game, null);
    }

    private void updateModel(String entered, int tf_row, int tf_col) {
        try {
            int newVal = Integer.parseInt(entered);
            game.setValue(tf_row, tf_col, newVal);
        } catch (NumberFormatException ignored) {
            // ignore because textfield resets text to empty string if input not a number
        }
    }

    private static void limitNumberField(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String text = textField.getText();
            // there have to be 2 different checks, otherwise exceptions and weird behaviour occur
            if (!text.matches("\\d*")) {
                textField.setText("");
            }
            if (text.length() > 1) {
                textField.setText(text.substring(0, 1));
            }
        });
    }

    public void solveGame() {
        game.solve();
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
            CSVInput.loadCSV(selectedFile, game);
            CSVOutput.writeCSV(game, initialState);
        }
    }

    public void saveCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile != null) {
            CSVOutput.writeCSV(game, selectedFile);
        }
    }

    public void newGame() {
        game.newGeneratedGame(SIZE);
        CSVOutput.writeCSV(game, initialState);
    }

    public void restartGame() {
        CSVInput.loadCSV(initialState, game);
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
        if (o instanceof SudokuBoard) {
            SudokuBoard game = (SudokuBoard) o;
            for (int row = 0; row < game.getSize(); row++) {
                for (int col = 0; col < game.getSize(); col++) {
                    int value = game.getValue(row, col);
                    textFields[row][col].setText(value == 0 ? "" : value + "");
                    if (game.isInitial(row, col)) {
                        textFields[row][col].setBackground(blueBG);
                        textFields[row][col].setEditable(false);
                    } else {
                        textFields[row][col].setBackground(whiteBG);
                        textFields[row][col].setEditable(true);
                    }
                }
            }
        }
    }

    public void printModel(ActionEvent actionEvent) {
        System.out.println(game);
    }
}
// TODO problem: solver in game, thats not good
// TODO problem: pass coordinates to update method?
