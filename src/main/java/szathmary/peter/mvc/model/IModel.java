package szathmary.peter.mvc.model;

import szathmary.peter.mvc.observable.INeuralNetworkObservable;
import szathmary.peter.mvc.observable.IObserver;
import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.trainingalgorithms.TrainingAlgorithm;

public interface IModel extends IObserver, INeuralNetworkObservable {
  void initializeNetwork(NetworkConfiguration configuration);

  void trainNetwork(IErrorFunction errorFunction, int numberOfEpochs, double minErrorTreshold);

  void testNetwork(IErrorFunction errorFunction, double[][] inputs, double[][] expectedOutputs);

  double[] predict(double[] input);

  int getNumberOfInputs();

  void setTrainingData(double[][] inputs, double[][] outputs);

  void setTrainingAlgorithm(TrainingAlgorithm trainingAlgorithm);

  void setTestingData(double[][] inputs, double[][] outputs);
}
