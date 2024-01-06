package szathmary.peter;

import java.util.Arrays;
import java.util.Random;
import szathmary.peter.mvc.model.IModel;
import szathmary.peter.mvc.model.NetworkConfiguration;
import szathmary.peter.mvc.model.NeuralNetworkModel;
import szathmary.peter.neuralnetwork.csvreader.CsvReader;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.errorfunctions.Mse;
import szathmary.peter.neuralnetwork.network.ActivationFunction;
import szathmary.peter.neuralnetwork.trainingalgorithms.BackPropagation;
import szathmary.peter.neuralnetwork.trainingalgorithms.TrainingAlgorithm;

public class Main {
  public static Random random = new Random(1);

  public static void main(String[] args) {
    TrainingAlgorithm trainingAlgorithm = new BackPropagation(0.1);

    IModel model = new NeuralNetworkModel(trainingAlgorithm);

    model.initializeNetwork(
        new NetworkConfiguration(
            1,
            ActivationFunction.IDENTITY,
            1,
            new int[] {5},
            new ActivationFunction[] {ActivationFunction.SIGMOID},
            1,
            ActivationFunction.TANH));

    CsvReader csvReader = new CsvReader("sin_data.csv");
    double[][] data = csvReader.readCsv();
    System.out.println(data[0].length);

    IErrorFunction errorFunction = new Mse();

    double[][] inputs = new double[data[0].length][1];
    double[][] outputs = new double[data[1].length][1];

    for (int i = 0; i < data[0].length; i++) {
      inputs[i][0] = data[0][i];
      outputs[i][0] = data[0][i];
    }

    model.trainNetwork(errorFunction, inputs, outputs, 10000, 0.1);

    double input = 0.590998648;
    double[] inputForTest = {0.1};

    double[] prediction = model.predict(inputForTest);
    System.out.println("Output for input " + input + ": " + Arrays.toString(prediction));
  }
}
