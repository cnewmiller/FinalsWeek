package edu.depaul.group14;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.depaul.group14.core.FinalsWeekProvider;
import edu.depaul.group14.core.FinalsWeekProviderAsync;


public abstract class AbstractFinalsWeekTestRunner<T, M, O> {

    public static final int BUSY_SOURCE_SEED = 0;
    public static final int FINALS_MULTIPLIER = 10;
    private final FinalsWeekTest<M> testDetails;

    public AbstractFinalsWeekTestRunner(FinalsWeekProvider<T, M, O> testDetails) {
        this.testDetails = new FinalsWeekTestSingleThreadedImpl<>(testDetails);
    }

    public AbstractFinalsWeekTestRunner(FinalsWeekProviderAsync<T, M, O> testDetails) {
        this.testDetails = new FinalsWeekTestAsyncImpl<>(testDetails);
    }

    @BeforeEach
    public void init() {
        testDetails.init();
    }

    protected int testIterations() {
        return 500;
    }


    @Test
    public void popular() {
        final M constantMessage = testDetails.initMessage(BUSY_SOURCE_SEED);
        final List<Long> times = testDetails.testResponseTimes(i -> constantMessage, testIterations(), 1);
        acceptStatistics(times);

    }

    @Test
    public void busy() {
        final List<Long> times = testDetails.testResponseTimes(testDetails::initMessage, testIterations(), 1);
        acceptStatistics(times);

    }

    @Test
    public void finals() {
        final List<Long> times = testDetails.testResponseTimes(testDetails::initMessage, testIterations(),
                                                               FINALS_MULTIPLIER);
        acceptStatistics(times);
    }

    public void acceptStatistics(List<Long> times) {

        final double max = times.stream().mapToLong(l -> l).max().orElseThrow(IllegalStateException::new);
        final double min = times.stream().mapToLong(l -> l).min().orElseThrow(IllegalStateException::new);
        final double average = times.stream().mapToLong(l -> l).average().orElseThrow(IllegalStateException::new);

        System.out.println(String.format("  Max: %f", max));
        System.out.println(String.format("  Min: %f", min));
        System.out.println(String.format("  Average: %f", average));
        System.out.println();
    }


}