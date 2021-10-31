package edu.depaul.group14;

import java.util.List;
import java.util.function.Function;

public interface FinalsWeekTest<M> {

    void init();

    M initMessage(int iteration);

    List<Long> testResponseTimes(final Function<Integer, M> messageSupplier,
                                 final int testIterations,
                                 final int messagesPerIteration);

}
