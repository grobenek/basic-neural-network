package szathmary.peter;

import java.util.Arrays;
import szathmary.peter.csvreader.CsvReader;
import szathmary.peter.errorfunctions.IErrorFunction;
import szathmary.peter.errorfunctions.Sse;
import szathmary.peter.neuron.*;
import szathmary.peter.trainingalgorithms.BackPropagation;
import szathmary.peter.trainingalgorithms.TrainingAlgorithm;

public class Main {
  public static void main(String[] args) {
    Network network =
        new NetworkBuilder()
            .addInputLayer(1, "identity")
            .addLayer(20, "sigmoid")
            .addOutputLayer(1,"sigmoid")
            .build();

    CsvReader csvReader = new CsvReader("data.csv");
    double[][] data = csvReader.readCsv();

    IErrorFunction errorFunction = new Sse();

    TrainingAlgorithm trainingAlgorithm = new BackPropagation(0.01);

    trainingAlgorithm.train(network, errorFunction, data[0], data[1], 10, 0.01);

    int input = 1;
    network.processInput(new double[] {input});

    System.out.println("Output for input " + input + ": " + Arrays.toString(network.getOutput()));
  }
}
