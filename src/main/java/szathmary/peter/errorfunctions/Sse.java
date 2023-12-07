package szathmary.peter.errorfunctions;


public class Sse implements IErrorFunction {
  @Override
  public double calculateError(double[] output, double[] expectedOutput) {
    if (output.length != expectedOutput.length) {
      throw new IllegalArgumentException("Output and expected output have different lengths!");
    }

    double sse = 0.0;
    for (int i = 0; i < output.length; i++) {
      double individualError = expectedOutput[i] - output[i];
      sse += individualError * individualError; // Squaring the individual error and summing
    }

    return sse;
  }
}