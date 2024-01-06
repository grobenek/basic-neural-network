package szathmary.peter.mvc.controller;

import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.network.ActivationFunction;

public interface IController {
  void initializeNetwork(
      int numberOfInputNeurons,
      ActivationFunction inputLayerActivationFunction,
      int numberOfHiddenLayers,
      int[] hiddenLayersNumberOfNeurons,
      ActivationFunction[] hiddenLayersActivationFunctions,
      int numberOfOutputNeurons,
      ActivationFunction outputLayerActivationFunction);

  void trainNetwork(
      IErrorFunction errorFunction,
      double[][] inputs,
      double[][] expectedOutputs,
      int numberOfEpochs,
      double minErrorTreshold);

  void testNetwork(IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs);

  double[] predict(double[] input);
}
