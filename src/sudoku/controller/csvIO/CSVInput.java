package sudoku.controller.csvIO;

import com.opencsv.CSVReader;
import javafx.scene.control.Alert;
import sudoku.game.SudokuGame;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ra on 24.06.16.
 * Part of Sudoku, in package sudoku.controller.csvIO.
 */
public class CSVInput {
    /**
     * Method to load a .csv file.
     *
     * @param game game field to be filled with csv entries
     * @param file csv file containing all entries, directly mapped to a sudoku field
     */
    public void loadCSV(SudokuGame game, File file) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file), ';')) {
            List<String[]> lines = csvReader.readAll();
            List<Byte[]> rows = parseStringLines(lines);
            if (csvValidate(game.getSize(), rows)) {
                for (byte rowIndex = 0; rowIndex < game.getSize(); rowIndex++) {
                    game.setRow(rowIndex, rows.get(rowIndex));
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid file content!");
            alert.setContentText("The file you tried to open contained invalid input!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid file!");
            alert.setContentText("There was an error during opening of the file you requested.");
            alert.showAndWait();
        }
    }

    private List<Byte[]> parseStringLines(List<String[]> lines) throws NumberFormatException {
        List<Byte[]> rows = new ArrayList<>(lines.size());
        for (String[] strings : lines) {
            Byte[] row = new Byte[strings.length];
            for (int i = 0; i < strings.length; i++) {
                row[i] = Byte.parseByte(strings[i]);
            }
            rows.add(row);
        }
        return rows;
    }

    private static boolean csvValidate(byte size, List<Byte[]> rows) {
        boolean correctLength = rows.stream().allMatch(byteArr -> byteArr.length == size);
        boolean correctRange = rows.stream().allMatch(bytes -> Arrays.stream(bytes).allMatch(b -> b >= 0 && b <= size));
        return correctLength && correctRange;
    }
}
