package szathmary.peter.neuralnetwork.trainingalgorithms;

import szathmary.peter.neuralnetwork.network.Layer;
import szathmary.peter.neuralnetwork.network.NeuralNetwork;
import szathmary.peter.neuralnetwork.network.Neuron;

public class BackPropagation extends TrainingAlgorithm {

  public BackPropagation(double learningRate) {
    super(learningRate);
  }

  @Override
  protected void forwardPropagate(NeuralNetwork neuralNetwork, double[] input) {
    neuralNetwork.processInput(input);
  }

  @Override
  protected void backPropagate(NeuralNetwork neuralNetwork, double[] error) {
    if (neuralNetwork == null) {
      throw new IllegalArgumentException("Cannot use back-propagation on null neuralNetwork!");
    }

    if (neuralNetwork.getNumberOfLayers() != 3) {
      throw new IllegalStateException(
          "Back propagation algorithm not implemented for neural networks with more than 3 layers!");
    }

    calculateWeightsForUpperLayer(neuralNetwork, error);
    calculateWeightsForBottomLayer(neuralNetwork, error);
  }

  private void calculateWeightsForUpperLayer(NeuralNetwork neuralNetwork, double[] errors) {
    Layer outputLayer = neuralNetwork.getOutputLayer();
    Layer lastHiddenLayer =
        neuralNetwork.getHiddenLayer(neuralNetwork.getNumberOfHiddenLayers() - 1);

    for (int i = 0; i < outputLayer.getNeuronCount(); i++) {
      Neuron outputNeuron = outputLayer.getNeuron(i);

      for (int j = 0; j < outputNeuron.getWeights().length; j++) {
        double derivative =
            outputNeuron
                .getActivationFunction()
                .applyForDerivation(outputNeuron.getWeightedInput());
        double gradient = -errors[i] * derivative * lastHiddenLayer.getOutputs()[j];
        double updatedWeight = outputNeuron.getWeights()[j] - learningRate * gradient;
        outputNeuron.setWeight(updatedWeight, j);
      }
    }
  }

  //  private void calculateWeightsForBottomLayer(NeuralNetwork neuralNetwork, double[] errors) {
  //    Layer firstHiddenLayer = neuralNetwork.getHiddenLayer(0);
  //    Layer outputLayer = neuralNetwork.getOutputLayer();
  //
  //    for (Neuron hiddenNeuron : firstHiddenLayer.getNeuronList()) {
  //      double totalError = 0;
  //      for (int i = 0; i < outputLayer.getNeuronCount(); i++) {
  //        Neuron outputNeuron = outputLayer.getNeuron(i);
  //        double outputNeuronWeight =
  // outputNeuron.getWeights()[firstHiddenLayer.getNeuronList().indexOf(hiddenNeuron)];
  //        double derivative =
  // hiddenNeuron.getActivationFunction().applyForDerivation(hiddenNeuron.getWeightedInput());
  //        totalError += outputNeuronWeight * derivative * errors[i];
  //      }
  //      for (int j = 0; j < hiddenNeuron.getWeights().length; j++) {
  //        double inputVal = hiddenNeuron.getInputs()[j];
  //        double gradient = -totalError * inputVal;
  //        double updatedWeight = hiddenNeuron.getWeights()[j] - learningRate * gradient;
  //        hiddenNeuron.setWeight(updatedWeight, j);
  //      }
  //    }
  //  }

  private void calculateWeightsForBottomLayer(NeuralNetwork neuralNetwork, double[] errors) {
    Layer firstHiddenLayer = neuralNetwork.getHiddenLayer(0);
    Layer outputLayer = neuralNetwork.getOutputLayer();

    for (int hiddenIndex = 0; hiddenIndex < firstHiddenLayer.getNeuronCount(); hiddenIndex++) {
      Neuron hiddenNeuron = firstHiddenLayer.getNeuron(hiddenIndex);
      double totalError = 0;

      for (int outputIndex = 0; outputIndex < outputLayer.getNeuronCount(); outputIndex++) {
        Neuron outputNeuron = outputLayer.getNeuron(outputIndex);
        double outputNeuronWeight = outputNeuron.getWeights()[hiddenIndex];
        double derivative =
            outputNeuron
                .getActivationFunction()
                .applyForDerivation(outputNeuron.getWeightedInput());
        totalError += outputNeuronWeight * derivative * errors[outputIndex];
      }

      double hiddenDerivative =
          hiddenNeuron.getActivationFunction().applyForDerivation(hiddenNeuron.getWeightedInput());

      for (int inputIndex = 0; inputIndex < hiddenNeuron.getWeights().length; inputIndex++) {
        double inputVal = hiddenNeuron.getInputs()[inputIndex];
        double gradient = -totalError * hiddenDerivative * inputVal;
        double updatedWeight = hiddenNeuron.getWeights()[inputIndex] - learningRate * gradient;
        hiddenNeuron.setWeight(updatedWeight, inputIndex);
      }
    }
  }
}
