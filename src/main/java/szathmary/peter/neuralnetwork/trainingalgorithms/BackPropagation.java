package szathmary.peter.neuralnetwork.trainingalgorithms;

import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
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
  protected void backPropagate(NeuralNetwork neuralNetwork, double error) {
    if (neuralNetwork.getNumberOfLayers() != 3) {
      throw new IllegalStateException(
          "Back propagation algorithm not implemented for neural neuralNetwork with more than 3 layers!");
    }

    calculateWeightsForUpperLayer(neuralNetwork, error);
    calculateWeightsForBottomLayer(neuralNetwork, error);
  }

  private void calculateWeightsForUpperLayer(NeuralNetwork neuralNetwork, double error) {
    Layer outputLayer = neuralNetwork.getOutputLayer();
    Layer hiddenLayer = neuralNetwork.getHiddenLayer(0);

    Neuron outputNeuron = outputLayer.getNeuron(0);

    for (int i = 0; i < outputNeuron.getWeights().length; i++) {
      double derivative =
          outputNeuron.getActivationFunction().applyForDerivation(outputNeuron.getWeightedInput());
      double gradient = -error * derivative * hiddenLayer.getOutputs()[i];
      double updatedWeight = outputNeuron.getWeights()[i] - learningRate * gradient;
      outputNeuron.setWeight(updatedWeight, i);
    }
  }

    private void calculateWeightsForBottomLayer(NeuralNetwork neuralNetwork, double error) {
    Layer hiddenLayer = neuralNetwork.getHiddenLayer(0);
    Layer outputLayer = neuralNetwork.getOutputLayer();
    Neuron outputNeuron = outputLayer.getNeuron(0);

    for (Neuron hiddenNeuron : hiddenLayer.getNeuronList()) {
      double outputNeuronWeight =
          outputNeuron.getWeights()[hiddenLayer.getNeuronList().indexOf(hiddenNeuron)];
      double derivative =
          hiddenNeuron.getActivationFunction().applyForDerivation(hiddenNeuron.getWeightedInput());
      double propagatedError = outputNeuronWeight * derivative * error;
      for (int i = 0; i < hiddenNeuron.getWeights().length; i++) {
        double inputVal = hiddenNeuron.getInputs()[i];
        double gradient = -propagatedError * inputVal;
        double updatedWeight = hiddenNeuron.getWeights()[i] - learningRate * gradient;
        hiddenNeuron.setWeight(updatedWeight, i);
      }
    }
  }

}
