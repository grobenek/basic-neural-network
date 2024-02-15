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

  void addLayer(Layer layer) {
    hiddenLayers.add(layer);
  }

  public Layer getHiddenLayer(int index) {
    if (index < 0 || index >= hiddenLayers.size()) {
      throw new IndexOutOfBoundsException(
          String.format("Index %d is out of bounds for %d layers!", index, hiddenLayers.size()));
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

  void setInputLayer(Layer layer) {
    inputLayer = layer;
    inputLayer.setIsInputLayer(true);
  }

  public Layer getOutputLayer() {
    return outputLayer;
  }

  void setOutputLayer(Layer layer) {
    outputLayer = layer;
  }

  public List<Neuron> getClonedInputNeurons() {
    return inputLayer.getNeuronList().stream().map(Neuron::clone).toList();
  }

  public List<List<Neuron>> getClonedHiddenLayersNeurons() {
    List<List<Neuron>> hiddenLayersNeuronList = new ArrayList<>(getNumberOfHiddenLayers());

    for (Layer hiddenLayer : hiddenLayers) {
      List<Neuron> hiddenLayerNeuronList = hiddenLayer.getNeuronList();

      List<Neuron> clonedList = hiddenLayerNeuronList.stream().map(Neuron::clone).toList();

      hiddenLayersNeuronList.add(clonedList);
    }

    return hiddenLayersNeuronList;
  }

  public List<Neuron> getClonedOutputNeuronList() {
    return outputLayer.getNeuronList().stream().map(Neuron::clone).toList();
  }

  public void setNeurons(
      List<Neuron> inputNeurons,
      List<List<Neuron>> hiddenLayersNeurons,
      List<Neuron> outputNeurons) {
    inputLayer.setNeurons(inputNeurons);

    for (int i = 0; i < hiddenLayers.size(); i++) {
      Layer hiddenLayer = hiddenLayers.get(i);
      hiddenLayer.setNeurons(hiddenLayersNeurons.get(i));
    }

    outputLayer.setNeurons(outputNeurons);
  }

  public List<Layer> getHiddenLayers() {
    return hiddenLayers;
  }
}
