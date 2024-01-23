package szathmary.peter.neuralnetwork.activationfunctions;

public interface IActivationFunction {
  double apply(double input);

  double applyForDerivation(double input);
}
