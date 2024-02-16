package szathmary.peter.mvc.observable;

import java.util.Optional;
import szathmary.peter.mvc.model.NetworkConfiguration;

public interface INeuralNetworkObservable extends IObservable, ITraningAlgorithmObservable {
  Optional<NetworkConfiguration> getNeuralNetworkConfiguration();
}
