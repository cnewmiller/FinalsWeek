package edu.depaul.group14;

import java.io.File;
import java.util.Optional;
import java.util.Random;

import edu.depaul.group14.FinalsWeek.Homework;
import edu.depaul.group14.core.FinalsWeekProvider;
import edu.depaul.group14.core.StatProcessor;

public class ExampleFinalsWeekTest extends AbstractFinalsWeekTestRunner<FinalsWeek, Homework, Long> {

    private static final Random RANDOM_SOURCE = new Random();

    public ExampleFinalsWeekTest() {
        super(new FinalsWeekProvider<>() {
            @Override
            public FinalsWeek initFixture() {
                return new FinalsWeek();
            }

            @Override
            public Optional<Long> sendMessageSynchronous(final FinalsWeek receiver, final Homework message) {
                return Optional.of(receiver.acceptHomework(message));
            }

            @Override
            public Homework initMessage(final int sourceSeed) {
                return new Homework(RANDOM_SOURCE.nextInt(10));
            }

            @Override
            public StatProcessor provideStatProcessor() {
                return new CsvStatProcessor(new File("build/test_output/single").toPath());
            }
        });
    }
}
