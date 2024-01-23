package szathmary.peter.mvc.model;

import szathmary.peter.neuralnetwork.network.ActivationFunction;

public final class NetworkConfiguration {
  private final int numberOfInputNeurons;
  private final ActivationFunction inputLayerActivationFunction;
  private final int numberOfHiddenLayers;
  private final int[] hiddenLayersNumberOfNeurons;
  private final ActivationFunction[] hiddenLayersActivationFunctions;
  private final int numberOfOutputNeurons;
  private final ActivationFunction outputLayerActivationFunction;

  public NetworkConfiguration(
      int numberOfInputNeurons,
      ActivationFunction inputLayerActivationFunction,
      int numberOfHiddenLayers,
      int[] hiddenLayersNumberOfNeurons,
      ActivationFunction[] hiddenLayersActivationFunctions,
      int numberOfOutputNeurons,
      ActivationFunction outputLayerActivationFunction) {
    this.numberOfInputNeurons = numberOfInputNeurons;
    this.inputLayerActivationFunction = inputLayerActivationFunction;
    this.numberOfHiddenLayers = numberOfHiddenLayers;
    this.hiddenLayersNumberOfNeurons = hiddenLayersNumberOfNeurons;
    this.hiddenLayersActivationFunctions = hiddenLayersActivationFunctions;
    this.numberOfOutputNeurons = numberOfOutputNeurons;
    this.outputLayerActivationFunction = outputLayerActivationFunction;
  }

  public int numberOfInputNeurons() {
    return numberOfInputNeurons;
  }

  public ActivationFunction inputLayerActivationFunction() {
    return inputLayerActivationFunction;
  }

  public int numberOfHiddenLayers() {
    return numberOfHiddenLayers;
  }

  public int[] hiddenLayersNumberOfNeurons() {
    return hiddenLayersNumberOfNeurons;
  }

  public ActivationFunction[] hiddenLayersActivationFunctions() {
    return hiddenLayersActivationFunctions;
  }

  public int numberOfOutputNeurons() {
    return numberOfOutputNeurons;
  }

  public ActivationFunction outputLayerActivationFunction() {
    return outputLayerActivationFunction;
  }

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
