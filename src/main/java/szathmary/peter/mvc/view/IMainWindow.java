package szathmary.peter.mvc.view;

import szathmary.peter.mvc.observable.IObserver;

public interface IMainWindow extends IObserver {
    void initializeNetwork();
    void trainNetwork(int numberOfEpochs);
    void testNetwork();
    void loadData();
    void predict(double[] inputs);
}
