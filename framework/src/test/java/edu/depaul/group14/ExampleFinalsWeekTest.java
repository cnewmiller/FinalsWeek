package edu.depaul.group14;

import java.util.Optional;
import java.util.Random;

import edu.depaul.group14.FinalsWeek.Homework;

public class ExampleFinalsWeekTest extends AbstractFinalsWeekTestSingleThreaded<FinalsWeek, Homework, Long> {

    private static final Random RANDOM_SOURCE = new Random();

    @Override
    protected FinalsWeek init() {
        return new FinalsWeek();
    }

    @Override
    protected Optional<Long> sendMessage(final FinalsWeek receiver, final Homework message) {
        return Optional.of(receiver.acceptHomework(message));
    }

    @Override
    protected Homework initMessage(final int sourceSeed) {
        return new Homework(RANDOM_SOURCE.nextInt(10));
    }
}
