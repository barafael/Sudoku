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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sudoku.controller.csvIO.CSVInput;
import sudoku.controller.csvIO.CSVOutput;
import sudoku.game.SudokuGame;
import sudoku.game.generator.InitialStateGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static sudoku.controller.Message.CREATED;

public class Controller implements Observer {
    private static final int SIZE = 9;
    private final double EASY = 0.5;
    private final double HARD = 0.2;

    private SudokuGame game;
    private final Stage primaryStage;
    private File initialState;

    private final TextField[][] textFields = new TextField[SIZE][SIZE];
    private final Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Background blueBG = new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Background redBG = new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY));

    private static final int TEXTFIELD_SIZE = 58;
    @FXML
    public VBox mainVBox;
    @FXML
    public ToolBar toolbar;
    @FXML
    public Button newGameButton;
    @FXML
    public Button restartButton;
    @FXML
    public Button solveButton;
    @FXML
    public BorderPane mainBorderpane;
    @FXML
    public GridPane mainGridpane;

    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        game = new SudokuGame(InitialStateGenerator.generateInitialState(HARD, SIZE));
        try {
            Path gamesaves = Paths.get("gamesaves");
            Files.createDirectories(gamesaves);
            initialState = new File(gamesaves + "/initialState.csv");
            CSVOutput.saveCSV(game, initialState);
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
                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                textField.setMaxSize(TEXTFIELD_SIZE, TEXTFIELD_SIZE);
                textField.setMinSize(TEXTFIELD_SIZE, TEXTFIELD_SIZE);
                textField.setStyle("-fx-font-family: Cairo; -fx-font-size: 30;");
                limitNumberField(textField);
                textField.setOnKeyTyped(event -> {
                    TextField source = (TextField) event.getSource();
                    int tf_row = GridPane.getRowIndex(source);
                    int tf_col = GridPane.getColumnIndex(source);
                    updateModel(event.getCharacter(), tf_row, tf_col);
                });
                mainGridpane.add(textField, col, row);
                textFields[row][col] = textField;
            }
        }
        game.addObserver(this);
        this.update(game, CREATED);
    }

    private static void limitNumberField(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String text = textField.getText();
            // there have to be 2 different checks, otherwise exceptions and weird behaviour occur
            if (!text.matches("[1-9]\\d*")) {
                textField.setText("");
            }
            if (text.length() > 1) {
                textField.setText(text.substring(0, 1));
            }
        });
    }

    private void updateModel(String entered, int row, int col) {
        try {
            int newVal = Integer.parseInt(entered);
            game.setValue(row, col, newVal);
        } catch (NumberFormatException ignored) {
            // ignore because textfield resets text to empty string if input not a number
            if ((int) entered.charAt(0) == 8) { // Backspace
                textFields[row][col].setBackground(whiteBG);
            }
        }
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
        if (o instanceof SudokuGame && arg instanceof Message) {
            SudokuGame game = (SudokuGame) o;
            Message msg = (Message) arg;
            Move move = msg.getMove();
            switch (msg) {
                case CREATED:
                    updateAllTextfields(game);
                    break;
                case RESET:
                    resetTextfields(game);
                    break;
                case VALID_ENTERED:
                    if (move != null) {
                        textFields[move.row][move.col].setText(move.value + "");
                        textFields[move.row][move.col].setBackground(whiteBG);
                    } else updateAllTextfields(game);
                    break;
                case INVALID_ENTERED:
                    if (move != null) {
                        List<Move> moves = msg.getArgs();
                        if (moves != null) {// look at length
                        }
                        textFields[move.row][move.col].setBackground(redBG);
                    }
                    break;
                case AUTO_SOLVED:
                    updateAllTextfields(game);
                    break;
                case WON:
                    if (move != null) {
                        textFields[move.row][move.col].setText(move.value + "");
                        textFields[move.row][move.col].setBackground(whiteBG);
                    } else updateAllTextfields(game);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Congratulations!");
                    alert.setHeaderText("Puzzle solved.");
                    alert.showAndWait();
                    break;
                case NONE:
                    System.err.println("None shouldn't be used!");
                    break;
                default:
                    System.err.println("Enums were added without matching them in this switch case!");
            }
        }
    }

    private void resetTextfields(SudokuGame game) {
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                if (!game.isInitial(row, col)) {
                    textFields[row][col].setBackground(whiteBG);
                    textFields[row][col].setEditable(true);
                    textFields[row][col].setText("");
                }
            }
        }
    }

    private void updateAllTextfields(SudokuGame game) {
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

    public void newRandomGame() {
        newGame(InitialStateGenerator.generateInitialState(EASY, SIZE));
    }

    private void newGame(int[][] initial) {
        game.deleteObserver(this); // delete from old game
        game = new SudokuGame(initial); // realloc
        game.addObserver(this); // add to new game
        initialize();
        CSVOutput.saveCSV(game, initialState);
    }

    public void restartGame() {
        game.reset();
    }

    public void solveGame() {
        if (!game.solve()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Unsolvable Sudoku");
            alert.setContentText("This Sudoku was not solvable by the used algorithm.");
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
            int[][] newInitial = CSVInput.loadCSV(selectedFile);
            if (newInitial != null) {
                newGame(newInitial);
                CSVOutput.saveCSV(game, initialState);
            }
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
            CSVOutput.saveCSV(game, selectedFile);
        }
    }

    public void printModel() {
        System.out.println(game);
    }
}
// TODO problem: pass coordinates to update method?
