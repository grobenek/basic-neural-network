package szathmary.peter.csvreader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
  private String filePath;
  private List<Double> inputList;
  private List<Double> expectedOuputList;

  public CsvReader(String filePath) {
    this.filePath = filePath;
    this.inputList = new ArrayList<>();
    this.expectedOuputList = new ArrayList<>();
  }

  public double[][] readCsv() {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";");
                inputList.add(Double.parseDouble(columns[0].replace(',', '.')));
                expectedOuputList.add(Double.parseDouble(columns[1].replace(',', '.')));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double[] xArray = inputList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = expectedOuputList.stream().mapToDouble(Double::doubleValue).toArray();

        return new double[][]{xArray, yArray};
  }
}
