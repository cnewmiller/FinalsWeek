package edu.depaul.group14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;

import edu.depaul.group14.core.FinalsWeekProvider;
import edu.depaul.group14.core.StatProcessor;
import edu.depaul.group14.core.StatProcessor.Statistic;

public class FinalsWeekTestSingleThreadedImpl<T, M, O> implements FinalsWeekTest<M> {
    private final FinalsWeekProvider<T, M, O> testDetails;
    private T fixture;

    public FinalsWeekTestSingleThreadedImpl(final FinalsWeekProvider<T, M, O> testDetails) {
        this.testDetails = testDetails;
    }

    @Override
    public void init() {
        fixture = testDetails.initFixture();
    }

    @Override
    public M initMessage(final int iteration) {
        return testDetails.initMessage(iteration);
    }

    @Override
    public StatProcessor provideStatProcessor() {
        return testDetails.provideStatProcessor();
    }

    @Override
    public List<Statistic> testResponseTimes(final Function<Integer, M> messageSupplier,
                                             final int testIterations,
                                             final int messagesPerIteration) {
        final List<Run<M, O>> runs = new ArrayList<>();
        final Map<M, O> failures = new HashMap<>();
        final List<List<M>> toSend = new ArrayList<>();
        for (int i = 0; i < testIterations; i++) {
            List<M> messages = IntStream.range(i, i + messagesPerIteration)
                                        .mapToObj(messageSupplier::apply)
                                        .collect(Collectors.toList());
            toSend.add(messages);
        }
        // Iterate in another loop to message initialization doesn't slow things down
        toSend.forEach(iteration -> {
            for (M message : iteration) {
                final long start = System.currentTimeMillis();
                final Optional<O> passed = testDetails.sendMessageSynchronous(fixture, message);
                final long end = System.currentTimeMillis();
                runs.add(new Run<>(start, end, message, passed));
            }
        });
        final List<Statistic> stats = runs.stream().map(run -> {
            boolean passed = run.output().map(o -> {
                try {
                    if (!testDetails.validateMessage(run.message(), o)) {
                        failures.put(run.message(), o);
                        return false;
                    }
                } catch (Throwable t) {
                    failures.put(run.message(), o);
                    return false;
                }
                return true;
            }).orElse(true);
            return new Statistic(run.start(), run.end(), passed);
        }).collect(Collectors.toUnmodifiableList());

        consumeFailures(failures);
        return stats;
    }

    public void consumeFailures(final Map<M, O> failures) {
        if (failures.isEmpty()) {
            return;
        }
        failures.forEach((m, o) -> {
            System.out.println(String.format("Failed Message: '%s', Failed output: '%s'", m, o));
        });
        Assertions.fail();
    }
}
