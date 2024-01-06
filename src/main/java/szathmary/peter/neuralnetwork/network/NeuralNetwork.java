package szathmary.peter.neuralnetwork.network;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements INeuronComponent {
  private final List<Layer> hiddenLayers;
  private Layer inputLayer;
  private Layer outputLayer;
  private double[] output;

  public NeuralNetwork() {
    this.hiddenLayers = new ArrayList<>();
  }

  void setInputLayer(Layer layer) {
    inputLayer = layer;
  }

  void setOutputLayer(Layer layer) {
    outputLayer = layer;
  }

  void addLayer(Layer layer) {
    hiddenLayers.add(layer);
  }

  public Layer getHiddenLayer(int index) {
    if (index < 0 || index >= hiddenLayers.size()) {
      throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for %d layers!", index, hiddenLayers.size()));
    }

    return hiddenLayers.get(index);
  }

  public int getNumberOfLayers() {
    return 2 + hiddenLayers.size(); // input, output layer + hidden layers
  }

  public int getNumberOfHiddenLayers() {
    return hiddenLayers.size(); // input, output layer + hidden layers
  }

  @Override
  public void processInput(double[] input) {
    if (inputLayer == null) {
      throw new IllegalStateException("Input layer is null!");
    }

    if (outputLayer == null) {
      throw new IllegalStateException("Output layer is null!");
    }

    if (hiddenLayers.isEmpty()) {
      throw new IllegalStateException("Layers are empty!");
    }

    inputLayer.processInput(input);
    double[] outputOfLastLayer = inputLayer.getOutputs();

    for (Layer layer : hiddenLayers) {
      layer.processInput(outputOfLastLayer);
      outputOfLastLayer = layer.getOutputs();
    }

    outputLayer.processInput(outputOfLastLayer);

    output = outputLayer.getOutputs();
  }

  public double[] getOutput() {
    return output;
  }

  public Layer getInputLayer() {
    return inputLayer;
  }

  public Layer getOutputLayer() {
    return outputLayer;
  }
}
