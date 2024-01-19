package szathmary.peter;

import java.util.Random;
import szathmary.peter.mvc.controller.Controller;
import szathmary.peter.mvc.controller.IController;
import szathmary.peter.mvc.model.IModel;
import szathmary.peter.mvc.model.NeuralNetworkModel;
import szathmary.peter.mvc.view.MainWindow;

public class Main {
  public static Random random = new Random(1);

  public static void main(String[] args) {
    IModel model = new NeuralNetworkModel();
    IController controller = new Controller(model);
    MainWindow mainWindow = new MainWindow(controller);
  }
}
