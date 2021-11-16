package edu.depaul.group14;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.depaul.group14.core.FinalsWeekProvider;
import edu.depaul.group14.core.FinalsWeekProviderAsync;
import edu.depaul.group14.core.StatProcessor.Statistic;


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
        final List<Statistic> times = testDetails.testResponseTimes(i -> constantMessage, testIterations(), 1);
        testDetails.provideStatProcessor().consumeStatistics("popular", times);
    }

    @Test
    public void busy() {
        final List<Statistic> times = testDetails.testResponseTimes(testDetails::initMessage, testIterations(), 1);
        testDetails.provideStatProcessor().consumeStatistics("busy", times);
    }

    @Test
    public void finals() {
        final List<Statistic> times = testDetails.testResponseTimes(testDetails::initMessage, testIterations(),
                                                               FINALS_MULTIPLIER);
        testDetails.provideStatProcessor().consumeStatistics("finals", times);
    }

}