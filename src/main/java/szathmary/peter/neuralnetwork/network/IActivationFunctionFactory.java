package szathmary.peter.neuralnetwork.network;

import szathmary.peter.neuralnetwork.activationfunctions.IActivationFunction;

public interface IActivationFunctionFactory {
  IActivationFunction getActivationFunction(ActivationFunction activationFunctionName);
}
