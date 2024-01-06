package szathmary.peter.mvc.model;

import szathmary.peter.neuralnetwork.network.ActivationFunction;

public record NetworkConfiguration(
    int numberOfInputNeurons,
    ActivationFunction inputLayerActivationFunction,
    int numberOfHiddenLayers,
    int[] hiddenLayersNumberOfNeurons,
    ActivationFunction[] hiddenLayersActivationFunctions,
    int numberOfOutputNeurons,
    ActivationFunction outputLayerActivationFunction) {}
