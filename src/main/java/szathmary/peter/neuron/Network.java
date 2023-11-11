package szathmary.peter.neuron;

import java.util.ArrayList;
import java.util.List;

public class Network implements INeuronComponent {
  private final List<Layer> layers; // hidden layers
  private Layer inputLayer;
  private Layer outputLayer;
  private double[] output;

  public Network() {
    this.layers = new ArrayList<>();
  }

  void setInputLayer(Layer layer) {
    inputLayer = layer;
  }

  void setOutputLayer(Layer layer) {
    outputLayer = layer;
  }

  void addLayer(Layer layer) {
    layers.add(layer);
  }

  @Override
  public void processInput(double[] input) {
    if (inputLayer == null) {
      throw new IllegalStateException("Input layer is null!");
    }

    if (outputLayer == null) {
      throw new IllegalStateException("Output layer is null!");
    }

    if (layers.isEmpty()) {
      throw new IllegalStateException("Layers are empty!");
    }

    inputLayer.processInput(input);
    double[] outputOfLastLayer = inputLayer.getOutputs();

    for (Layer layer : layers) {
      layer.processInput(outputOfLastLayer);
      outputOfLastLayer = layer.getOutputs();
    }

    outputLayer.processInput(outputOfLastLayer);

    output = outputLayer.getOutputs();
  }

  public double[] getOutput() {
    return output;
  }
}
