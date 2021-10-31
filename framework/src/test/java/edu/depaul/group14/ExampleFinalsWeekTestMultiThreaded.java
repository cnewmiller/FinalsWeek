package edu.depaul.group14;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import edu.depaul.group14.FinalsWeek.Homework;

public class ExampleFinalsWeekTestMultiThreaded extends AbstractFinalsWeekTestMultiThreaded<FinalsWeek, Homework, Long> {

    private static final Random RANDOM_SOURCE = new Random();

    @Override
    protected FinalsWeek init() {
        return new FinalsWeek();
    }

    @Override
    protected void sendMessage(final FinalsWeek receiver, final Homework message, final Consumer<Optional<Long>> complete) {
        final long l = receiver.acceptHomework(message);
        complete.accept(Optional.of(l));
    }

    @Override
    protected boolean validateMessage(final Homework message, final Long output) {
        return message.getNum() == output;
    }

    @Override
    protected Homework initMessage(final int sourceSeed) {
        return new Homework(RANDOM_SOURCE.nextInt(10));
    }
}
