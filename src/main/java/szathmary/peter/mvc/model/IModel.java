package szathmary.peter.mvc.model;

import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;

public interface IModel extends IObserver, INeuralNetworkObservable {
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
