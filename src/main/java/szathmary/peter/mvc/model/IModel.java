package szathmary.peter.mvc.model;

import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;

public interface IModel {
  void initializeNetwork(NetworkConfiguration configuration);

  void trainNetwork(
      IErrorFunction errorFunction,
      double[][] inputs,
      double[][] expectedOutputs,
      int numberOfEpochs,
      double minErrorTreshold);

  void testNetwork(IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs);

  double[] predict(double[] input);
}
