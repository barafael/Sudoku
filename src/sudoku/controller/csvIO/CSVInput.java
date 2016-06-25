package sudoku.controller.csvIO;

import com.opencsv.CSVReader;
import javafx.scene.control.Alert;
import sudoku.game.SudokuBoard;

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
     * @param file CSV file containing all entries, directly mapped to a sudoku field
     * @return a sudoku board with entries from file
     */
    public SudokuBoard loadCSV(File file) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file), ';')) {
            List<String[]> lines = csvReader.readAll();
            List<Integer[]> rows = parseStringLines(lines);
            int size = checkDimensions(rows);
            if (csvValidate(size, rows)) {
                int[][] newBoard = new int[size][size];
                for (int rowIndex = 0; rowIndex < size; rowIndex++) {
                    Integer[] row = rows.get(rowIndex);
                    for (int colIndex = 0; colIndex < row.length; colIndex++) {
                        newBoard[rowIndex][colIndex] = row[colIndex];
                    }
                }
                return new SudokuBoard(newBoard);
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
        return null;
    }

    private int checkDimensions(List<Integer[]> rows) {
        int length = rows.get(0).length;
        boolean correct = true;
        for (Integer[] integers: rows) {
            if (integers.length != length) {
                correct = false;
            }
        }
        if(rows.size() != length) { // not square
            correct = false;
        }
        return correct ? length : 0;
    }

    private List<Integer[]> parseStringLines(List<String[]> lines) throws NumberFormatException {
        List<Integer[]> rows = new ArrayList<>(lines.size());
        for (String[] strings : lines) {
            Integer[] row = new Integer[strings.length];
            for (int i = 0; i < strings.length; i++) {
                row[i] = Integer.parseInt(strings[i]);
            }
            rows.add(row);
        }
        return rows;
    }

    private static boolean csvValidate(int size, List<Integer[]> rows) {
        boolean correctLength = rows.stream().allMatch(intArr -> intArr.length == size);
        boolean correctRange = rows.stream().allMatch(ints -> Arrays.stream(ints).allMatch(i -> (i >= 0) && (i <= size)));
        return correctLength && correctRange;
    }
}
