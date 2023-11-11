package szathmary.peter;

import java.util.Arrays;
import szathmary.peter.neuron.Network;
import szathmary.peter.neuron.NetworkBuilder;
import szathmary.peter.neuron.SigmoidNeuronFactory;

public class Main {
  public static void main(String[] args) {
    Network network =
        new NetworkBuilder()
            .addInputLayer(1, new SigmoidNeuronFactory(1))
            .addLayer(5, new SigmoidNeuronFactory(1))
            .addOutputLayer(1, new SigmoidNeuronFactory(5))
            .build();

    double[] input = {1};

    network.processInput(input);

    System.out.println(Arrays.toString(network.getOutput()));
  }
}
