package szathmary.peter.neuralnetwork.activationfunctions;

public class IdentityActivationFunction implements IActivationFunction {
    @Override
    public double apply(double input) {
        return input;
    }

    @Override
    public double applyForDerivation(double input) {
        return 1;
    }
}
