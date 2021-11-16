package edu.depaul.group14;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import edu.depaul.group14.core.StatProcessor;
import edu.depaul.group14.core.StatProcessor.Statistic;

public interface FinalsWeekTest<M> {


    record Run<M, O>(long start, long end, M message, Optional<O> output) {}

    void init();

    M initMessage(int iteration);


    StatProcessor provideStatProcessor();

    List<Statistic> testResponseTimes(final Function<Integer, M> messageSupplier,
                                      final int testIterations,
                                      final int messagesPerIteration);

}
