package szathmary.peter.mvc.view;

import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public interface IMainWindow extends IObserver {
  void initializeNetwork(
      int numberOfInputNeurons,
      ActivationFunction inputLayerActivationFunction,
      int numberOfHiddenLayers,
      int[] hiddenLayersNumberOfNeurons,
      ActivationFunction[] hiddenLayersActivationFunctions,
      int numberOfOutputNeurons,
      ActivationFunction outputLayerActivationFunction);

  void trainNetwork(int numberOfEpochs);

  void testNetwork();

  void loadData();

  void predict(double[] inputs);
  void setTrainingAlgorithm(double learningRate);
  void showErrorMessage(String message);
}
