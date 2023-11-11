package szathmary.peter.activationfunctions;

public class SigmoidActivationFunction implements IActivationFunction {
  @Override
  public double apply(double input) {
    return 1.0 / (1.0 + Math.exp(-input));
  }
}
