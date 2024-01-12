package szathmary.peter.neuralnetwork.network;

public class NeuralNetworkBuilder {
  private static final INeuronFactory neuronFactory = new NeuronFactory();
  private final NeuralNetwork neuralNetwork = new NeuralNetwork();

  private Layer createLayer(
          int numberOfNeuronsInLayer, int neuronInputSize, ActivationFunction activationFunctionName) {
    if (numberOfNeuronsInLayer == 0) {
      throw new IllegalArgumentException("Cannot create layer with zero neurons!");
    }

    Layer layer = new Layer();
    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      layer.addNeuron(neuronFactory.createNeuron(neuronInputSize, activationFunctionName));
    }
    return layer;
  }

  public NeuralNetworkBuilder addHiddenLayer(int numberOfNeuronsInLayer, ActivationFunction activationFunctionName) {

    int neuronInputSize;
    if (neuralNetwork.getNumberOfHiddenLayers() == 0) {
      Layer inputLayer = neuralNetwork.getInputLayer();

      if (inputLayer == null) {
        throw new IllegalStateException("Input layer must be defined first!");
      }

      neuronInputSize = inputLayer.getNeuronCount();
    } else {
      neuronInputSize = neuralNetwork.getHiddenLayer(neuralNetwork.getNumberOfHiddenLayers() - 1).getNeuronCount();
    }

    Layer layer = createLayer(numberOfNeuronsInLayer, neuronInputSize, activationFunctionName);

    neuralNetwork.addLayer(layer);

    return this;
  }

  public NeuralNetworkBuilder addInputLayer(int numberOfNeuronsInLayer, ActivationFunction activationFunctionName) {
    if (neuralNetwork.getInputLayer() != null) {
      throw new IllegalStateException("Input layer already defined!");
    }

    Layer inputLayer = new Layer();

    for (int i = 0; i < numberOfNeuronsInLayer; i++) {
      inputLayer.addNeuron(
          neuronFactory.createInputLayerNeuron(numberOfNeuronsInLayer, 1, activationFunctionName));
    }

    neuralNetwork.setInputLayer(inputLayer);

    return this;
  }

  public NeuralNetworkBuilder addOutputLayer(int numberOfNeuronsInLayer, ActivationFunction activationFunctionName) {
    if (neuralNetwork.getOutputLayer() != null) {
      throw new IllegalStateException("Output layer already defined!");
    }

    int neuronInputSize = neuralNetwork.getHiddenLayer(neuralNetwork.getNumberOfHiddenLayers() - 1).getNeuronCount();

    Layer layer = createLayer(numberOfNeuronsInLayer, neuronInputSize, activationFunctionName);

    neuralNetwork.setOutputLayer(layer);

    return this;
  }

  public NeuralNetwork build() {
    if (neuralNetwork.getOutputLayer() == null) {
      throw new IllegalStateException("No output layer defined!");
    }

    return neuralNetwork;
  }
}
