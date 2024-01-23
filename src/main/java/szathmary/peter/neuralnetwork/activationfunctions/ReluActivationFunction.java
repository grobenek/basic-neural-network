package szathmary.peter.neuralnetwork.activationfunctions;

public class ReluActivationFunction implements IActivationFunction {
    @Override
    public double apply(double input) {
        return Math.max(0, input);
    }

    @Override
    public double applyForDerivation(double input) {
        return input > 0 ? 1 : 0;
    }
}
