package szathmary.peter.mvc.observable;

import java.util.List;

public interface ITraningAlgorithmObservable extends IObservable {
    List<Double> getErrors();
}
