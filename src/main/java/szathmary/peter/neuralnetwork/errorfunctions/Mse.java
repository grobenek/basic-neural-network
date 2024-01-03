package szathmary.peter.neuralnetwork.errorfunctions;

public class Mse implements IErrorFunction {
    @Override
    public double calculateError(double[] output, double[] expectedOutput) {
        if (output.length != expectedOutput.length) {
            throw new IllegalArgumentException("Output and expected output have different lengths!");
        }

        double mse = 0.0;
        for (int i = 0; i < output.length; i++) {
            double individualError = expectedOutput[i] - output[i];
            mse += individualError * individualError; // Squaring the individual error and summing
        }

        return mse / output.length; // Returning the average of the squared errors
    }
}
