package szathmary.peter.neuralnetwork.trainingalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.mvc.observable.ITraningAlgorithmObservable;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.NeuralNetwork;
import szathmary.peter.neuralnetwork.network.Neuron;

public abstract class TrainingAlgorithm implements ITraningAlgorithmObservable {
  protected final double learningRate;
  private final List<IObserver> observerList = new ArrayList<>();
  private List<Double> trainingErrorList = Collections.emptyList();
  private List<Double> testingErrorList = Collections.emptyList();
  private double percentageOfCompletedEpochs;

  public TrainingAlgorithm(double learningRate) {
    this.learningRate = learningRate;
  }

  public final void train(
      NeuralNetwork neuralNetwork,
      IErrorFunction errorFunction,
      double[][] inputs,
      double[][] expectedOutputs,
      double[][] testingInputs,
      double[][] testingOutputs,
      int numberOfEpochs) {

    if (inputs.length != expectedOutputs.length) {
      throw new IllegalArgumentException(
          String.format(
              "Input length %d does not match expectedOutputs length %d!",
              inputs.length, expectedOutputs.length));
    }

    List<Neuron> bestInputNeurons = neuralNetwork.getClonedInputNeurons();
    List<List<Neuron>> bestHiddenLayersNeurons = neuralNetwork.getClonedHiddenLayersNeurons();
    List<Neuron> bestOutputNeurons = neuralNetwork.getClonedOutputNeuronList();

    trainingErrorList = new ArrayList<>(numberOfEpochs);
    testingErrorList = new ArrayList<>(numberOfEpochs);
    double lowestTrainingError = Double.MAX_VALUE;

    for (int epoch = 1; epoch != numberOfEpochs; epoch++) {
      double[][] outputsAfterTraining = new double[inputs.length][inputs[0].length];
      double[][] outputsAfterTesting = new double[testingOutputs.length][testingInputs[0].length];

      // train data
      for (int i = 0; i < inputs.length; i++) {
        double[] input = inputs[i];
        double[] expectedOutput = expectedOutputs[i];

        trainOnSelectedData(neuralNetwork, input, expectedOutput);

        outputsAfterTraining[i] = neuralNetwork.getOutput();
      }

      // test data
      for (int i = 0; i < testingInputs.length; i++) {
        double[] input = testingInputs[i];

        neuralNetwork.processInput(input);
        outputsAfterTesting[i] = neuralNetwork.getOutput();
      }

      double calculatedTrainingError =
          errorFunction.calculateError(outputsAfterTraining, expectedOutputs);
      double calculatedTestingError =
          errorFunction.calculateError(outputsAfterTesting, testingOutputs);

      // remembering best weights
      if (calculatedTrainingError < lowestTrainingError) {
        lowestTrainingError = calculatedTrainingError;

        bestInputNeurons = neuralNetwork.getClonedInputNeurons();
        bestHiddenLayersNeurons = neuralNetwork.getClonedHiddenLayersNeurons();
        neuralNetwork.getClonedOutputNeuronList();
      }

      trainingErrorList.add(calculatedTrainingError);
      testingErrorList.add(calculatedTestingError);
      System.out.println(
          "Epoch "
              + epoch
              + " : "
              + errorFunction.getClass().getSimpleName()
              + " error = "
              + calculatedTrainingError);

      // send learning progress after each 10% epochs
      sendTrainingInfo(numberOfEpochs, epoch);
    }

    System.out.println("LOWEST ERROR: " + lowestTrainingError);
    neuralNetwork.setNeurons(bestInputNeurons, bestHiddenLayersNeurons, bestOutputNeurons);
  }

  private void sendTrainingInfo(int numberOfEpochs, int epoch) {
    if (epoch % (numberOfEpochs / 10) == 0) {
      percentageOfCompletedEpochs = ((double) epoch / numberOfEpochs) * 100;
      sendNotifications();
    }
  }

  private void trainOnSelectedData(
      NeuralNetwork neuralNetwork, double[] input, double[] expectedOutput) {
    forwardPropagate(neuralNetwork, input);

    double error = expectedOutput[0] - neuralNetwork.getOutput()[0];
    backPropagate(neuralNetwork, error);
  }

  protected abstract void forwardPropagate(NeuralNetwork neuralNetwork, double[] input);

  protected abstract void backPropagate(NeuralNetwork neuralNetwork, double error);

  @Override
  public void attach(IObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observerList.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observerList) {
      observer.update(this);
    }
  }

  @Override
  public List<Double> getTrainingErrors() {
    return trainingErrorList;
  }

  @Override
  public List<Double> getTestingErrors() {
    return testingErrorList;
  }

  @Override
  public double getPercentageOfCompletedTraining() {
    return percentageOfCompletedEpochs;
  }
}
