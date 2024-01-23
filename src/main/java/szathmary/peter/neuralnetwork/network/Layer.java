package szathmary.peter.neuralnetwork.network;

import java.util.LinkedList;
import java.util.List;

public class Layer implements INeuronComponent {
  private List<Neuron> neuronList;
  private boolean isInputLayer = false;
  private double[] outputs;

  public Layer(List<Neuron> neuronList) {
    this.neuronList = neuronList;
  }

  public Layer() {
    this.neuronList = new LinkedList<>();
  }

  public Neuron getNeuron(int index) {
    if (index < 0 || index >= neuronList.size()) {
      throw new IndexOutOfBoundsException(
          String.format("Index %d is out of bounds for %d neurons!", index, neuronList.size()));
    }

    return neuronList.get(index);
  }

  public int getNeuronCount() {
    return neuronList.size();
  }

  @Override
  public void processInput(double[] input) {
    if (neuronList.isEmpty()) {
      throw new IllegalStateException("Neuron list is empty!");
    }

    outputs = new double[neuronList.size()];

    if (isInputLayer) {
      // each neuron gets a single input
      if (input.length != neuronList.size()) {
        throw new IllegalArgumentException(
            "Input size must match the number of neurons in the input layer");
      }
      for (int i = 0; i < neuronList.size(); i++) {
        Neuron neuron = neuronList.get(i);
        neuron.processInput(new double[] {input[i]}); // pass a single input to each neuron
        outputs[i] = neuron.getOutput();
      }
    } else {
      for (int i = 0; i < neuronList.size(); i++) {
        Neuron neuron = neuronList.get(i);
        neuron.processInput(input);
        outputs[i] = neuron.getOutput();
      }
    }
  }

  public void addNeuron(Neuron neuron) {
    neuronList.add(neuron);
  }

  public double[] getOutputs() {
    return outputs;
  }

  public List<Neuron> getNeuronList() {
    return neuronList;
  }

  public void setNeurons(List<Neuron> pNeuronList) {
    neuronList = pNeuronList;
  }

  public void setIsInputLayer(boolean isInputLayer) {
    this.isInputLayer = isInputLayer;
  }
}
