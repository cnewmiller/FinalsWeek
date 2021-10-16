package edu.depaul.group14;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public abstract class AbstractFinalsWeekTestSingleThreaded<T, M> {

    public static final int BUSY_SOURCE_SEED = 0;
    private final T fixture;

    public AbstractFinalsWeekTestSingleThreaded() {
        fixture = init();
    }

    protected abstract T init();

    protected abstract void sendMessage(T receiver, M message);

    protected abstract M initMessage(int sourceSeed);

    protected long testIterations() {
        return 500L;
    }

    private List<Long> popularTest(T fixture) {
        List<Long> times = new ArrayList<>();
        // int progressIteration = (int) (testIterations() / 100);
        for (int i = 0; i < testIterations(); i++) {
            final M uniqueMessage = initMessage(i);
            final long start = System.currentTimeMillis();
            // Entrypoint into the app - TODO enable multithreading
            sendMessage(fixture, uniqueMessage);
            final long end = System.currentTimeMillis();
            times.add(end - start);
        }
        return times;
    }

    private List<Long> busyTest(T fixture) {
        List<Long> times = new ArrayList<>();
        final M repeatedMessage = initMessage(BUSY_SOURCE_SEED);
        for (int i = 0; i < testIterations(); i++) {
            final long start = System.currentTimeMillis();
            // Entrypoint into the app - TODO enable multithreading
            sendMessage(fixture, repeatedMessage);
            final long end = System.currentTimeMillis();
            times.add(end - start);
        }
        return times;
    }
    private List<Long> finalsTest(T fixture) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < testIterations(); i++) {
            List<M> messages = IntStream.range(i, i + 10).mapToObj(this::initMessage).toList();
            final long start = System.currentTimeMillis();
            // Entrypoint into the app - TODO enable multithreading
            for(M message: messages) {
                sendMessage(fixture, message);
            }
            final long end = System.currentTimeMillis();
            times.add(end - start);
        }
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

}
