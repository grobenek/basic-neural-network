package szathmary.peter.neuron;

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
}
