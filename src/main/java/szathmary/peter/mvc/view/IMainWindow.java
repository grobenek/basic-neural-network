package szathmary.peter.mvc.view;

import szathmary.peter.mvc.observable.IObserver;

public interface IMainWindow extends IObserver {
    void initializeNetwork();
    void trainNetwork();
    void testNetwork();
    void loadData();
}
