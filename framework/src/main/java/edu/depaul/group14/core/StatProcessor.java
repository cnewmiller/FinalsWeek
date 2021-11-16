package edu.depaul.group14.core;

import java.util.List;

public interface StatProcessor {
    record Statistic(long startMillis, long stopMillis, boolean validationSucceeded) {

    }


    void consumeStatistics(String testName, List<Statistic> testTimes);
}
