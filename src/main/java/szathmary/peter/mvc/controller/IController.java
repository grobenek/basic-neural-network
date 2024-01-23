package szathmary.peter.mvc.controller;

import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public interface IController extends IObserver, INeuralNetworkObservable {
  void initializeNetwork(
      int numberOfInputNeurons,
      ActivationFunction inputLayerActivationFunction,
      int numberOfHiddenLayers,
      int[] hiddenLayersNumberOfNeurons,
      ActivationFunction[] hiddenLayersActivationFunctions,
      int numberOfOutputNeurons,
      ActivationFunction outputLayerActivationFunction);

  void trainNetwork(IErrorFunction errorFunction, int numberOfEpochs, double minErrorTreshold);

  void testNetwork(IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs);

  double[] predict(double[] input);

  int getNumberOfInputs();

  void setData(double[][] inputs, double[][] outputs, double trainTestSplitRatio);

  void setTrainingAlgorithm(double learningRate);
}
