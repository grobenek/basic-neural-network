package szathmary.peter.neuralnetwork.network;

import java.util.LinkedList;
import java.util.List;

public class Layer implements INeuronComponent {
  private final List<Neuron> neuronList;
  private double[] outputs;

  public Layer(List<Neuron> neuronList) {
    this.neuronList = neuronList;
  }

  public Layer() {
    this.neuronList = new LinkedList<>();
  }

  public Neuron getNeuron(int index) {
    if (index < 0 || index >= neuronList.size()) {
      throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for %d neurons!", index, neuronList.size()));
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
    for (int i = 0; i < neuronList.size(); i++) {
      Neuron neuron = neuronList.get(i);
      neuron.processInput(input);
      outputs[i] = neuron.getOutput();
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
}
