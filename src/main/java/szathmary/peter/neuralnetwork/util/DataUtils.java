package szathmary.peter.neuralnetwork.util;

import szathmary.peter.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DataUtils {
  public static TrainTestSplit splitDataToTrainAndTest(
      double[][] inputs, double[][] expectedOutputs, double trainToTestRatio) {
    if (inputs == null || expectedOutputs == null || inputs.length != expectedOutputs.length) {
      throw new IllegalArgumentException("Invalid input arrays or mismatched lengths.");
    }
    if (trainToTestRatio <= 0 || trainToTestRatio >= 1) {
      throw new IllegalArgumentException(
          "Train-to-test ratio must be between 0 and 1 (exclusive).");
    }

    int totalSize = inputs.length;
    int trainSize = (int) (totalSize * trainToTestRatio);
    int testSize = totalSize - trainSize;

    double[][] trainInputs = new double[trainSize][];
    double[][] trainOutputs = new double[trainSize][];
    double[][] testInputs = new double[testSize][];
    double[][] testOutputs = new double[testSize][];

    List<Integer> indices = new ArrayList<>(totalSize);
    IntStream.range(0, totalSize).forEach(indices::add);
    Collections.shuffle(indices, Main.random);

    for (int i = 0; i < trainSize; i++) {
      trainInputs[i] = inputs[indices.get(i)];
      trainOutputs[i] = expectedOutputs[indices.get(i)];
    }
    for (int i = 0; i < testSize; i++) {
      testInputs[i] = inputs[indices.get(trainSize + i)];
      testOutputs[i] = expectedOutputs[indices.get(trainSize + i)];
    }

    return new TrainTestSplit(testInputs, testOutputs, trainInputs, trainOutputs);
  }
}
