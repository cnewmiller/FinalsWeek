package edu.depaul.group14;

import java.io.File;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import edu.depaul.group14.FinalsWeek.Homework;
import edu.depaul.group14.core.FinalsWeekProviderAsync;
import edu.depaul.group14.core.StatProcessor;

public class ExampleFinalsWeekTestMultiThreaded extends AbstractFinalsWeekTestRunner<FinalsWeek, Homework, Long> {

    private static final Random RANDOM_SOURCE = new Random();

    public ExampleFinalsWeekTestMultiThreaded() {
        super(new FinalsWeekProviderAsync<>() {

            @Override
            public FinalsWeek initFixture() {
                return new FinalsWeek();
            }

            @Override
            public void sendMessageAsync(final FinalsWeek receiver, final Homework message,
                                         final Consumer<Optional<Long>> complete) {
                final long l = receiver.acceptHomework(message);
                complete.accept(Optional.of(l));
            }

            @Override
            public boolean validateMessage(final Homework message, final Long output) {
                return message.getNum() == output;
            }

            @Override
            public Homework initMessage(final int sourceSeed) {
                return new Homework(RANDOM_SOURCE.nextInt(10));
            }

            @Override
            public StatProcessor provideStatProcessor() {
                return new CsvStatProcessor(new File("build/test_output/multi").toPath());
            }
        });
    }

}
