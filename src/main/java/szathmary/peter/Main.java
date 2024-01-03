package szathmary.peter;

import java.util.Arrays;
import java.util.Random;
import szathmary.peter.neuralnetwork.csvreader.CsvReader;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.errorfunctions.Mse;
import szathmary.peter.neuralnetwork.neuron.Network;
import szathmary.peter.neuralnetwork.neuron.NetworkBuilder;
import szathmary.peter.neuralnetwork.trainingalgorithms.BackPropagation;
import szathmary.peter.neuralnetwork.trainingalgorithms.TrainingAlgorithm;

public class Main {
  public static Random random = new Random(1);

  public static void main(String[] args) {
    Network network =
        new NetworkBuilder()
            .addInputLayer(1, "identity")
            .addLayer(5, "sigmoid")
            .addOutputLayer(1, "tanh")
            .build();

    CsvReader csvReader = new CsvReader("data.csv");
    double[][] data = csvReader.readCsv();
    System.out.println(data[0].length);

    IErrorFunction errorFunction = new Mse();

    TrainingAlgorithm trainingAlgorithm = new BackPropagation(0.1);

    trainingAlgorithm.train(network, errorFunction, data[0], data[1], 10000, 0.1);

    double input = 0.590998648;
    double[] inputForTest = {0.59};

    network.processInput(inputForTest);
    System.out.println("Output for input " + input + ": " + Arrays.toString(network.getOutput()));
  }
}
