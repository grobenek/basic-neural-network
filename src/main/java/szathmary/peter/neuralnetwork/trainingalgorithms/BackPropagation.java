//package szathmary.peter.trainingalgorithms;
//
//import szathmary.peter.errorfunctions.IErrorFunction;
//import szathmary.peter.neuron.Layer;
//import szathmary.peter.neuron.Network;
//import szathmary.peter.neuron.Neuron;
//
//public class BackPropagation extends TrainingAlgorithm {
//
//  public BackPropagation(double learningRate) {
//    super(learningRate);
//  }
//
//  @Override
//  protected void forwardPropagate(Network network, double[] input) {
//    network.processInput(input);
//  }
//
//  @Override
//  protected double calculateError(
//      Network network, double[] expectedOutput, IErrorFunction errorFunction) {
//
//    return errorFunction.calculateError(network.getOutput(), expectedOutput);
//  }
//
//  @Override
//  protected void backPropagate(Network network, double error) {
//    if (network.getNumberOfLayers() != 3) {
//      throw new IllegalStateException(
//          "Back propagation algorithm not implemented for neural network with more than 3 layers!");
//    }
//
//    calculateWeightsForUpperLayer(network, error);
//    calculateWeightsForBottomLayer(network, error);
//  }
//
//  private void calculateWeightsForUpperLayer(Network network, double error) {
//    Neuron outputNeuron = network.getOutputLayer().getNeuron(0);
//    Layer hiddenLayer = network.getHiddenLayer(0);
//
//    for (int i = 0; i < outputNeuron.getWeights().length; i++) {
//      double gradient =
//              (error)
//              * outputNeuron
//                  .getActivationFunction()
//                  .applyForDerivation(outputNeuron.getWeightedInput())
//              * hiddenLayer.getOutputs()[i];
//      double updatedWeight = outputNeuron.getWeights()[i] - (learningRate * gradient);
//      outputNeuron.setWeight(updatedWeight, i);
//    }
//  }
//
//  private void calculateWeightsForBottomLayer(Network network, double error) {
//    Neuron inputNeuron = network.getInputLayer().getNeuron(0);
//    Neuron outputNeuron = network.getOutputLayer().getNeuron(0);
//    Layer hiddenLayer = network.getHiddenLayer(0);
//
//    for (int i = 0; i < hiddenLayer.getNeuronCount(); i++) {
//      Neuron hiddenNeuron = hiddenLayer.getNeuron(i);
//
//      for (int j = 0; j < hiddenNeuron.getWeights().length; j++) {
//        double derivative =
//            hiddenNeuron.getActivationFunction().applyForDerivation(hiddenNeuron.getWeightedInput());
//        double gradient =
//                (error) * outputNeuron.getWeights()[i] * derivative * inputNeuron.getInputs()[j];
//        double updatedWeight = hiddenNeuron.getWeights()[j] - (learningRate * gradient);
//        hiddenNeuron.setWeight(updatedWeight, j);
//      }
//    }
//  }
//}

package szathmary.peter.neuralnetwork.trainingalgorithms;

import szathmary.peter.neuralnetwork.errorfunctions.IErrorFunction;
import szathmary.peter.neuralnetwork.neuron.Layer;
import szathmary.peter.neuralnetwork.neuron.Network;
import szathmary.peter.neuralnetwork.neuron.Neuron;

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
    calculateWeightsForBottomLayer(network);
  }

  private void calculateWeightsForUpperLayer(Network network, double error) {
    Layer outputLayer = network.getOutputLayer();
    Layer hiddenLayer = network.getHiddenLayer(0);

    Neuron outputNeuron = outputLayer.getNeuron(0);

    for (int i = 0; i < outputNeuron.getWeights().length; i++) {
      double derivative = outputNeuron.getActivationFunction().applyForDerivation(outputNeuron.getWeightedInput());
      double gradient = error * derivative * hiddenLayer.getOutputs()[i];
      double updatedWeight = outputNeuron.getWeights()[i] - learningRate * gradient;
      outputNeuron.setWeight(updatedWeight, i);
    }
  }

  private void calculateWeightsForBottomLayer(Network network) {
    Layer hiddenLayer = network.getHiddenLayer(0);
    Layer outputLayer = network.getOutputLayer();
    Neuron outputNeuron = outputLayer.getNeuron(0);

    for (Neuron hiddenNeuron : hiddenLayer.getNeuronList()) {
      double outputNeuronWeight = outputNeuron.getWeights()[hiddenLayer.getNeuronList().indexOf(hiddenNeuron)];
      double derivative = hiddenNeuron.getActivationFunction().applyForDerivation(hiddenNeuron.getWeightedInput());

      double errorForHiddenNeuron = outputNeuronWeight * derivative * outputNeuron.getOutput();

      for (int i = 0; i < hiddenNeuron.getWeights().length; i++) {
        double inputVal = hiddenNeuron.getInputs()[i];
        double gradient = errorForHiddenNeuron * inputVal;
        double updatedWeight = hiddenNeuron.getWeights()[i] - learningRate * gradient;
        hiddenNeuron.setWeight(updatedWeight, i);
      }
    }
  }
}
