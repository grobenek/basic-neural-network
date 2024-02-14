package szathmary.peter.neuralnetwork.trainingalgorithms;

import szathmary.peter.neuralnetwork.network.*;

public class BackPropagation extends TrainingAlgorithm {

  public BackPropagation(double learningRate) {
    super(learningRate);
  }

  private static void backPropagateOutputLayer(
      double[] targetOutputs,
      double[][] layerErrors,
      int numberOfHiddenLayersAndOutputLayer,
      Layer outputLayer) {
    layerErrors[numberOfHiddenLayersAndOutputLayer - 1] = new double[outputLayer.getNeuronCount()];
    for (int i = 0; i < outputLayer.getNeuronCount(); i++) {
      Neuron neuron = outputLayer.getNeuron(i);
      double output = neuron.getOutput();
      layerErrors[numberOfHiddenLayersAndOutputLayer - 1][i] =
          (targetOutputs[i] - output) * neuron.getActivationFunction().applyForDerivation(output);
    }
  }

  @Override
  protected void forwardPropagate(NeuralNetwork neuralNetwork, double[] input) {
    neuralNetwork.processInput(input);
  }

  @Override
  public void backPropagate(NeuralNetwork neuralNetwork, double[] targetOutputs) {
    int numberOfHiddenLayersAndOutputLayer = neuralNetwork.getNumberOfHiddenLayers() + 1;
    double[][] layerErrors = new double[numberOfHiddenLayersAndOutputLayer][];
    Layer outputLayer = neuralNetwork.getOutputLayer();

    // Compute errors for output layer
    backPropagateOutputLayer(
        targetOutputs, layerErrors, numberOfHiddenLayersAndOutputLayer, outputLayer);

    // Backpropagate errors through hidden layers
    for (int layerIndex = numberOfHiddenLayersAndOutputLayer - 2;
        layerIndex >= 0;
        layerIndex--) { // Exclude output layer
      Layer currentLayer = neuralNetwork.getHiddenLayer(layerIndex);
      int nextLayerIndex = layerIndex + 1;
      Layer nextLayer;
      // next layer is output layer
      if (nextLayerIndex == numberOfHiddenLayersAndOutputLayer - 1) {
        nextLayer = outputLayer;
      } else {
        nextLayer = neuralNetwork.getHiddenLayer(layerIndex + 1);
      }
      layerErrors[layerIndex] = new double[currentLayer.getNeuronCount()];

      backPropagateLayer(currentLayer, nextLayer, layerErrors, layerIndex);
    }
  }

  private void backPropagateLayer(
      Layer currentLayer, Layer nextLayer, double[][] layerErrors, int layerIndex) {
    for (int neuronIndex = 0; neuronIndex < currentLayer.getNeuronCount(); neuronIndex++) {
      Neuron neuron = currentLayer.getNeuron(neuronIndex);
      double weightedErrorSum = 0.0;
      for (int nextLayerNeuronIndex = 0;
          nextLayerNeuronIndex < nextLayer.getNeuronCount();
          nextLayerNeuronIndex++) {
        weightedErrorSum +=
            nextLayer.getNeuron(nextLayerNeuronIndex).getWeights()[neuronIndex]
                * layerErrors[layerIndex + 1][nextLayerNeuronIndex];
      }
      layerErrors[layerIndex][neuronIndex] =
          weightedErrorSum * neuron.getActivationFunction().applyForDerivation(neuron.getOutput());

      // Update weights for the current neuron
      updateWeights(neuron, neuronIndex, layerErrors[layerIndex]);
    }
  }

  private void updateWeights(Neuron neuron, int neuronIndex, double[] layerErrors) {
    for (int weightIndex = 0; weightIndex < neuron.getWeights().length; weightIndex++) {
      double deltaWeight =
          learningRate * layerErrors[neuronIndex] * neuron.getInputs()[weightIndex];
      neuron.setWeight(neuron.getWeights()[weightIndex] + deltaWeight, weightIndex);
    }
  }
}
