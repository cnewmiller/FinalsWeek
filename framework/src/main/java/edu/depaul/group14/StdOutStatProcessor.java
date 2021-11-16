package edu.depaul.group14;

import java.util.List;

import edu.depaul.group14.core.StatProcessor;

public class StdOutStatProcessor implements StatProcessor {

    @Override
    public void consumeStatistics(final String testName, final List<Statistic> testTimes) {
        final double max = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).max().orElseThrow(IllegalStateException::new);
        final double min = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).min().orElseThrow(IllegalStateException::new);
        final double average = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).average().orElseThrow(IllegalStateException::new);

        System.out.println(String.format("  Max: %f", max));
        System.out.println(String.format("  Min: %f", min));
        System.out.println(String.format("  Average: %f", average));
        System.out.println();
    }
}
