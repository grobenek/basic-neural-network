package szathmary.peter.neuron;

import java.util.HashMap;
import szathmary.peter.activationfunctions.*;

public class ActivationFunctionFactory implements IActivationFunctionFactory {
  private static final HashMap<String, IActivationFunction> activationFunctions;

  static {
    activationFunctions = new HashMap<>();

    activationFunctions.put("sigmoid", new SigmoidActivationFunction());
    activationFunctions.put("identity", new IdentityActivationFunction());
    activationFunctions.put("relu", new ReluActivationFunction());
    activationFunctions.put("tanh", new TanhActivationFunction());
  }

  @Override
  public IActivationFunction getActivationFunction(String activationFunctionName) {
    IActivationFunction activationFunction = activationFunctions.get(activationFunctionName);

    if (activationFunction == null) {
      throw new IllegalArgumentException(
          String.format("No activation function with name %s was found!", activationFunctionName));
    }

    return activationFunction;
  }
}
