package szathmary.peter.neuralnetwork.util;

public record TrainTestSplit(
    double[][] testInputs,
    double[][] testOutputs,
    double[][] trainInputs,
    double[][] trainOutputs) {}
