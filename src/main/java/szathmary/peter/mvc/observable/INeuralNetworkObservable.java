package szathmary.peter.mvc.observable;

import szathmary.peter.mvc.model.NetworkConfiguration;

import java.util.Optional;

public interface INeuralNetworkObservable extends IObservable, ITraningAlgorithmObservable {
    Optional<NetworkConfiguration> getNeuralNetworkConfiguration();
}
