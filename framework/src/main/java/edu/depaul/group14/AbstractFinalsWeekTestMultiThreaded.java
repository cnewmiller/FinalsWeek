package edu.depaul.group14;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

/**
 * Abstract class that provides three basic stress test scenarios. Implement this class to stress test a class.
 * This class is multi-threaded, and passes a "completion" token with each message. Each class under test must
 * handle pairing the appropriate message to its completion token if it does not complete processing the message
 * inside the {@link #sendMessage(Object, Object, Runnable)} call itself.
 *
 * @param <T> The class to be stress tested
 * @param <M> The "message" type to send to the class
 */
public abstract class AbstractFinalsWeekTestMultiThreaded<T, M> {

    public static final int BUSY_SOURCE_SEED = 0;
    public static final int FINALS_MULTIPLIER = 10;
    private final T fixture;

    public AbstractFinalsWeekTestMultiThreaded() {
        fixture = init();
    }

    protected abstract T init();

    protected abstract void sendMessage(T receiver, M message, Runnable complete);

    protected abstract M initMessage(int sourceSeed);

    protected int testIterations() {
        return 500;
    }

    private List<Long> popularTest(T fixture) throws InterruptedException {
        List<Long> times = new ArrayList<>();
        CountDownLatch finisher = new CountDownLatch(testIterations());
        for (int i = 0; i < testIterations(); i++) {
            final M uniqueMessage = initMessage(i);
            final long start = System.currentTimeMillis();
            CompletableFuture.runAsync(() -> {
                sendMessage(fixture, uniqueMessage, () -> {
                    final long end = System.currentTimeMillis();
                    times.add(end - start);
                    finisher.countDown();
                });
            });
        }
        finisher.await();
        return times;
    }

    private List<Long> busyTest(T fixture) throws InterruptedException {
        List<Long> times = new ArrayList<>();
        final M repeatedMessage = initMessage(BUSY_SOURCE_SEED);
        CountDownLatch finisher = new CountDownLatch(testIterations());
        for (int i = 0; i < testIterations(); i++) {
            final long start = System.currentTimeMillis();
            CompletableFuture.runAsync(() -> {
                sendMessage(fixture, repeatedMessage, () -> {
                    final long end = System.currentTimeMillis();
                    times.add(end - start);
                    finisher.countDown();
                });
            });
        }
        finisher.await();
        return times;
    }
    private List<Long> finalsTest(T fixture) throws InterruptedException {
        List<Long> times = new ArrayList<>();
        CountDownLatch finisher = new CountDownLatch(testIterations() * FINALS_MULTIPLIER);
        for (int i = 0; i < testIterations(); i++) {
            List<M> messages = IntStream.range(i, i + FINALS_MULTIPLIER).mapToObj(this::initMessage).toList();
            final long start = System.currentTimeMillis();
            for(M message: messages) {
                CompletableFuture.runAsync(() -> {
                    sendMessage(fixture, message, () -> {
                        final long end = System.currentTimeMillis();
                        times.add(end - start);
                        finisher.countDown();
                    });
                });
            }
        }
        finisher.await();
        return times;
    }

    @Test
    public void popular() throws InterruptedException {
        System.out.println("Starting: popular - " + fixture.getClass().getSimpleName());
        final List<Long> times = popularTest(fixture);

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        acceptStatistics("Popular - " + fixture.getClass().getSimpleName(), max, min, average);

    }

    @Test
    public void busy() throws InterruptedException {
        System.out.println("Starting: Busy - " + fixture.getClass().getSimpleName());
        final List<Long> times = busyTest(fixture);

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        acceptStatistics("Busy - " + fixture.getClass().getSimpleName(), max, min, average);

    }

    @Test
    public void finals() throws InterruptedException {
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