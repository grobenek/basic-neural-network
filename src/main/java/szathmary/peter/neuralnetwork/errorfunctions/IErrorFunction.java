package szathmary.peter.neuralnetwork.errorfunctions;

public interface IErrorFunction {
  double calculateError(double[][] output, double[][] expectedOutput);
}
