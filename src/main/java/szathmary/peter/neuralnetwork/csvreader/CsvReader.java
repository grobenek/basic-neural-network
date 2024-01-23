package szathmary.peter.neuralnetwork.csvreader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private final String filePath;
    private final List<List<Double>> inputDataList;
    private final List<List<Double>> expectedOutputList;
    private String delimiter;
    private boolean hasHeader = false;
    private boolean isDataLoaded = false;

    public CsvReader(String filePath) {
        this.filePath = filePath;
        this.inputDataList = new ArrayList<>();
        this.expectedOutputList = new ArrayList<>();
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public void readCsv(int numberOfInputColumns, int numberOfOutputColumns) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            if (hasHeader) {
                br.readLine(); // skip the header
            }

            while ((line = br.readLine()) != null) {
                String[] columns = line.split(delimiter);

                List<Double> inputDataRow = new ArrayList<>();
                for (int i = 0; i < numberOfInputColumns; i++) {
                    inputDataRow.add(parseDouble(columns[i]));
                }
                inputDataList.add(inputDataRow);

                List<Double> outputDataRow = new ArrayList<>();
                for (int i = numberOfInputColumns; i < numberOfInputColumns + numberOfOutputColumns; i++) {
                    outputDataRow.add(parseDouble(columns[i]));
                }
                expectedOutputList.add(outputDataRow);
            }
            isDataLoaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
    }

    public double[][] getInputDataArray() {
        if (!isDataLoaded) {
            throw new IllegalStateException("Data has not been loaded. Call readCsv first.");
        }

        double[][] inputDataArray = new double[inputDataList.size()][];
        for (int i = 0; i < inputDataList.size(); i++) {
            inputDataArray[i] = inputDataList.get(i).stream().mapToDouble(Double::doubleValue).toArray();
        }
        return inputDataArray;
    }

    public double[][] getExpectedOutputArray() {
        if (!isDataLoaded) {
            throw new IllegalStateException("Data has not been loaded. Call readCsv first.");
        }

        double[][] expectedOutputArray = new double[expectedOutputList.size()][];
        for (int i = 0; i < expectedOutputList.size(); i++) {
            expectedOutputArray[i] = expectedOutputList.get(i).stream().mapToDouble(Double::doubleValue).toArray();
        }
        return expectedOutputArray;
    }

    private double parseDouble(String data) {
        return Double.parseDouble(data.trim().replace("\uFEFF", "").replace(',', '.'));
    }
}
