package szathmary.peter.neuralnetwork.activationfunctions;

public class TanhActivationFunction implements IActivationFunction {
  @Override
  public double apply(double input) {
    return Math.tanh(input);
  }

  @Override
  public double applyForDerivation(double input) {
    double tanh = Math.tanh(input);
    return 1 - tanh * tanh;
  }
}
