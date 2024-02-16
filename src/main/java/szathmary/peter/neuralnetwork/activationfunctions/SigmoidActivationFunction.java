package szathmary.peter.neuralnetwork.activationfunctions;

public class SigmoidActivationFunction implements IActivationFunction {
  @Override
  public double apply(double input) {
    return 1.0 / (1.0 + Math.exp(-input));
  }

  @Override
  public double applyForDerivation(double input) {
    double sigmoid = apply(input);
    return sigmoid * (1 - sigmoid);
  }
}
