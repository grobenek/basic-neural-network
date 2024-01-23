package szathmary.peter.neuralnetwork.network;

import java.util.HashMap;
import szathmary.peter.neuralnetwork.activationfunctions.*;

public class ActivationFunctionFactory implements IActivationFunctionFactory {
  private static final HashMap<ActivationFunction, IActivationFunction> activationFunctions;

  static {
    activationFunctions = new HashMap<>();

    activationFunctions.put(ActivationFunction.SIGMOID, new SigmoidActivationFunction());
    activationFunctions.put(ActivationFunction.IDENTITY, new IdentityActivationFunction());
    activationFunctions.put(ActivationFunction.RELU, new ReluActivationFunction());
    activationFunctions.put(ActivationFunction.TANH, new TanhActivationFunction());
  }

  @Override
  public IActivationFunction getActivationFunction(ActivationFunction activationFunctionName) {
    IActivationFunction activationFunction = activationFunctions.get(activationFunctionName);

    if (activationFunction == null) {
      throw new IllegalArgumentException(
          String.format("No activation function with name %s was found!", activationFunctionName));
    }

    return activationFunction;
  }
}
