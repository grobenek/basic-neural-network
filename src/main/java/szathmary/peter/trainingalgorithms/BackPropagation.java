package szathmary.peter.trainingalgorithms;

import szathmary.peter.errorfunctions.IErrorFunction;
import szathmary.peter.neuron.Layer;
import szathmary.peter.neuron.Network;
import szathmary.peter.neuron.Neuron;

public class BackPropagation extends TrainingAlgorithm {

  public BackPropagation(double learningRate) {
    super(learningRate);
  }

  @Override
  protected void forwardPropagate(Network network, double[] input) {
    network.processInput(input);
  }

  @Override
  protected double calculateError(
      Network network, double[] expectedOutput, IErrorFunction errorFunction) {

    return errorFunction.calculateError(network.getOutput(), expectedOutput);
  }

  @Override
  protected void backPropagate(Network network, double error) {
    if (network.getNumberOfLayers() != 3) {
      throw new IllegalStateException(
          "Back propagation algorithm not implemented for neural network with more than 3 layers!");
    }

    calculateWeightsForUpperLayer(network, error);
    calculateWeightsForBottomLayer(network, error);
  }

  private void calculateWeightsForUpperLayer(Network network, double error) {
    // update top weights (from second layer to third layer)
    Layer hiddenLayer = network.getLayer(0);
    Neuron outputNeuron = network.getOutputLayer().getNeuron(0);

    for (int i = 0; i < outputNeuron.getWeights().length; i++) {
      double gradient =
          (-error)
              * outputNeuron
                  .getActivationFunction()
                  .applyForDerivation(outputNeuron.getWeightedInput())
              * hiddenLayer.getNeuron(i).getOutput();

      //      System.out.println("Upper layer gradient = " + gradient);

      double weight = outputNeuron.getWeights()[i] - (learningRate * gradient);
      outputNeuron.setWeight(weight, i);
    }
  }

  private void calculateWeightsForBottomLayer(Network network, double error) {
    // update bottom weights (from fist layer to second layer)
    Layer hiddenLayer = network.getLayer(0);
    Neuron inputNeuron = network.getInputLayer().getNeuron(0);
    Neuron outputNeuron = network.getOutputLayer().getNeuron(0);

    for (int i = 0; i < hiddenLayer.getNeuronCount(); i++) {
      Neuron hiddenNeuron = hiddenLayer.getNeuron(i);

      double gradient =
          outputNeuron.getWeights()[i]
              * hiddenNeuron
                  .getActivationFunction()
                  .applyForDerivation(hiddenNeuron.getWeightedInput())
              * inputNeuron.getInputs()[0];

      //      System.out.println("Bottom layer gradient = " + gradient);

      double weight = hiddenNeuron.getWeights()[0] - (learningRate * gradient);

      hiddenNeuron.setWeight(weight, 0);
    }
  }
}
