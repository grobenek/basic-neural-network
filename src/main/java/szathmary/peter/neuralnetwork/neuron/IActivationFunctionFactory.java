package szathmary.peter.neuralnetwork.neuron;

import szathmary.peter.neuralnetwork.activationfunctions.IActivationFunction;

public interface IActivationFunctionFactory {
  IActivationFunction getActivationFunction(String activationFunctionName);
}
