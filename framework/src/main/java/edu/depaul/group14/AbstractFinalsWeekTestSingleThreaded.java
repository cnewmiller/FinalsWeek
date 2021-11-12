package edu.depaul.group14;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Abstract class that provides three basic stress test scenarios. Implement this class to stress test a class.
 * This implementation is single-threaded, meaning the "end" of the processing time is when {@link #sendMessage(Object, Object)}
 * returns. This also means that all messages are sent serially.
 * @param <T> The class to be stress tested
 * @param <M> The "message" type to send to the class
 */
public abstract class AbstractFinalsWeekTestSingleThreaded<T, M, O> {

    public static final int BUSY_SOURCE_SEED = 0;
    private final T fixture;

    public AbstractFinalsWeekTestSingleThreaded() {
        fixture = init();
    }

    protected abstract T init();

    protected abstract Optional<O> sendMessage(T receiver, M message);

    protected boolean validateMessage(M message, O output) {
        // no-op, override to implement validation
        return true;
    }
    protected abstract M initMessage(int sourceSeed);

    protected long testIterations() {
        return 500L;
    }

    private List<Long> popularTest(T fixture) {
        List<Long> times = new ArrayList<>();
        Map<M, O> failures = new HashMap<>();
        for (int i = 0; i < testIterations(); i++) {
            final M uniqueMessage = initMessage(i);
            times.add(testHelper(fixture, uniqueMessage));
        }
        consumeFailures(failures);
        return times;
    }
    private List<Long> busyTest(T fixture) {
        List<Long> times = new ArrayList<>();
        final Map<M, O> failures = new HashMap<>();
        final M repeatedMessage = initMessage(BUSY_SOURCE_SEED);
        for (int i = 0; i < testIterations(); i++) {
            times.add(testHelper(fixture, repeatedMessage));
        }
        consumeFailures(failures);
        return times;
    }
    public long testHelper(T fixture, M message) {
        Map<M, O> failures = new HashMap<>();
        final long start = System.currentTimeMillis();
        final Optional<O> output = sendMessage(fixture, message);
        final long end = System.currentTimeMillis();
        output.ifPresent(o -> {
            if (!validateMessage(message, o)) {
                failures.put(message, o);
            }
        });
        return end - start;
    }

    private List<Long> finalsTest(T fixture) {
        List<Long> times = new ArrayList<>();
        final HashMap<M, O> failures = new HashMap<>();
        for (int i = 0; i < testIterations(); i++) {
            List<M> messages = IntStream.range(i, i + 10).mapToObj(this::initMessage).collect(Collectors.toList());
            for(M message: messages) {
                final long start = System.currentTimeMillis();
                final Optional<O> output = sendMessage(fixture, message);
                final long end = System.currentTimeMillis();
                times.add(end - start);
                output.ifPresent(o -> {
                    if (!validateMessage(message, o)) {
                        failures.put(message, o);
                    }
                });
            }
        }
        consumeFailures(failures);
        return times;
    }

    @Test
    public void popular() {
        System.out.println("Starting: popular - " + fixture.getClass().getSimpleName());
        final List<Long> times = popularTest(fixture);

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        acceptStatistics("Popular - " + fixture.getClass().getSimpleName(), max, min, average);

    }

    @Test
    public void busy() {
        System.out.println("Starting: Busy - " + fixture.getClass().getSimpleName());
        final List<Long> times = busyTest(fixture);

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        acceptStatistics("Busy - " + fixture.getClass().getSimpleName(), max, min, average);

    }

    @Test
    public void finals() {
        System.out.println("Starting: Finals - " + fixture.getClass().getSimpleName());
        final List<Long> times = finalsTest(fixture);

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        acceptStatistics("Finals - " + fixture.getClass().getSimpleName(), max, min, average);
    }

    public void acceptStatistics(String header, double max, double min, double average) {
        System.out.println(String.format("------- %s -------", header));
        System.out.println(String.format("  Max: %f", max));
        System.out.println(String.format("  Min: %f", min));
        System.out.println(String.format("  Average: %f", average));
        System.out.println();
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
