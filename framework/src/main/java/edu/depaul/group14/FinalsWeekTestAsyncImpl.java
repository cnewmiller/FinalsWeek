package edu.depaul.group14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;

import edu.depaul.group14.core.FinalsWeekProviderAsync;

public class FinalsWeekTestAsyncImpl<T, M , O> implements FinalsWeekTest<M> {

    private final FinalsWeekProviderAsync<T, M, O> testDetails;
    private T fixture;

    public FinalsWeekTestAsyncImpl(final FinalsWeekProviderAsync<T, M, O> testDetails) {
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
    public List<Long> testResponseTimes(final Function<Integer, M> messageSupplier,
                                                final int testIterations,
                                                final int messagesPerIteration) {
        List<Long> times = new ArrayList<>();
        CountDownLatch finisher = new CountDownLatch(testIterations);
        final Map<M, O> failures = new HashMap<>();
        final List<List<M>> toSend = new ArrayList<>();
        for (int i = 0; i < testIterations; i++) {
            List<M> messages = IntStream.range(i, i + messagesPerIteration)
                                        .mapToObj(messageSupplier::apply)
                                        .collect(Collectors.toList());
            toSend.add(messages);
        }
        toSend.forEach(iteration -> {
            for (M message : iteration) {
                CompletableFuture.runAsync(() -> {
                    final long start = System.currentTimeMillis();
                    testDetails.sendMessageAsync(fixture, message, (passed) -> {
                        final long end = System.currentTimeMillis();
                        times.add(end - start);
                        passed.ifPresent(o -> {
                            try {
                                if (!testDetails.validateMessage(message, o)) {
                                    failures.put(message, o);
                                }
                            } catch (Throwable t) {
                                failures.put(message, o);
                            }
                        });
                        finisher.countDown();
                    });
                });
            }
        });

        try {
            finisher.await();
        } catch (InterruptedException e) {
            Assertions.fail(e);
        }
        consumeFailures(failures);
        return times;
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
