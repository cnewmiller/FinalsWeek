package edu.depaul.group14;

import edu.depaul.group14.core.FinalsWeekProvider;
import java.util.Optional;
import java.util.Random;
import edu.depaul.group14.DataStructure.RBT;


public class RBTTest  extends AbstractFinalsWeekTestRunner<RBT, Integer, Boolean> {

        private static final Random RANDOM_SOURCE = new Random();

        public RBTTest() {
            super(new FinalsWeekProvider<>() {
                @Override
                public RBT initFixture() {
                    return new RBT();
                }

                @Override
                public Optional<Boolean> sendMessageSynchronous(final RBT receiver, final Integer message) {
                    return Optional.of(receiver.insert(message));
                }

                @Override
                public Integer initMessage(final int sourceSeed) {
                    return sourceSeed;
                }
            });
        }
    }

