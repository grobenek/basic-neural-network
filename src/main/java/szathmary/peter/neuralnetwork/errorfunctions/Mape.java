package szathmary.peter.neuralnetwork.errorfunctions;

public class Mape implements IErrorFunction {
  @Override
  public double calculateError(double[][] output, double[][] expectedOutput) {
    double totalSum = 0;
    int totalCount = 0;

    for (int i = 0; i < output.length; i++) {
      double sum = 0;
      int count = output[i].length;

      for (int j = 0; j < output[i].length; j++) {
        if (expectedOutput[i][j] != 0) {
          sum += Math.abs((expectedOutput[i][j] - output[i][j]) / expectedOutput[i][j]);
        } else {
          count -= 1;
        }
      }

      totalSum += sum;
      totalCount += count;
    }

    if (totalCount == 0) {
      throw new ArithmeticException(
          "The count for non-zero expected output values is zero. MAPE cannot be calculated.");
    }

    return (totalSum / totalCount) * 100; // Convert to percentage
  }
}
