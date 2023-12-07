package szathmary.peter.activationfunctions;

public interface IActivationFunction {
  double apply(double input);

  double applyForDerivation(double input);
}
