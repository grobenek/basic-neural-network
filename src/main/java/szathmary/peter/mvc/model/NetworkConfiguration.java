package szathmary.peter.mvc.model;

import szathmary.peter.neuralnetwork.network.ActivationFunction;

public record NetworkConfiguration(
    int numberOfInputNeurons,
    ActivationFunction inputLayerActivationFunction,
    int numberOfHiddenLayers,
    int[] hiddenLayersNumberOfNeurons,
    ActivationFunction[] hiddenLayersActivationFunctions,
    int numberOfOutputNeurons,
    ActivationFunction outputLayerActivationFunction) {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Input layer: ")
        .append(numberOfInputNeurons)
        .append(" neurons, ")
        .append(inputLayerActivationFunction.name())
        .append("\n")
        .append(numberOfHiddenLayers)
        .append(" hidden layers:")
        .append("\n");
    for (int i = 0; i < numberOfHiddenLayers; i++) {
      sb.append(i + 1)
          .append(". layer: ")
          .append(hiddenLayersNumberOfNeurons[i])
          .append(" neurons, ")
          .append(hiddenLayersActivationFunctions[i].name())
          .append("\n");
    }

    sb.append("Output layer: ")
        .append(numberOfOutputNeurons)
        .append(" neurons, ")
        .append(outputLayerActivationFunction.name())
        .append("\n");

    return sb.toString();
  }
}
