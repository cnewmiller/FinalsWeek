package edu.depaul.group14;

import java.util.Random;

import edu.depaul.group14.FinalsWeek.Homework;

public class ExampleFinalsWeekTest<T, M> extends AbstractFinalsWeekTestSingleThreaded<FinalsWeek, Homework> {

    private static final Random RANDOM_SOURCE = new Random();

    @Override
    protected FinalsWeek init() {
        return new FinalsWeek();
    }

    @Override
    protected void sendMessage(final FinalsWeek receiver, final Homework message) {
        receiver.acceptHomework(message);
    }

    @Override
    protected Homework initMessage(final int sourceSeed) {
        return new Homework(RANDOM_SOURCE.nextInt(10));
    }
}
