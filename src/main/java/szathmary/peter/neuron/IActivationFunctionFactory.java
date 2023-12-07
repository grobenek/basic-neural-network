package szathmary.peter.neuron;

import szathmary.peter.activationfunctions.IActivationFunction;

public interface IActivationFunctionFactory {
  IActivationFunction getActivationFunction(String activationFunctionName);
}
