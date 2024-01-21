package szathmary.peter.neuralnetwork.errorfunctions;

public class Mse implements IErrorFunction {
  @Override
  public double calculateError(double[][] output, double[][] expectedOutput) {
    if (output == null || expectedOutput == null) {
      throw new IllegalArgumentException("Output arrays cannot be null.");
    }

    if (output.length != expectedOutput.length) {
      throw new IllegalArgumentException("Output arrays must have the same length.");
    }

    double sumSquaredError = 0;
    int totalCount = 0;

    for (int i = 0; i < output.length; i++) {
      if (output[i].length != expectedOutput[i].length) {
        throw new IllegalArgumentException(
            "Inner arrays at index " + i + " must have the same length.");
      }

      for (int j = 0; j < output[i].length; j++) {
        double error = expectedOutput[i][j] - output[i][j];
        sumSquaredError += error * error;
        totalCount++;
      }
    }

    if (totalCount == 0) {
      throw new ArithmeticException(
          "The total count of elements is zero. MSE cannot be calculated.");
    }

    return sumSquaredError / totalCount;
  }
}
