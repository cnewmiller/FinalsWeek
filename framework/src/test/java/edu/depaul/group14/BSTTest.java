package edu.depaul.group14;

import edu.depaul.group14.core.FinalsWeekProvider;
import java.util.Optional;
import java.util.Random;
import edu.depaul.group14.DataStructure.BST;


public class BSTTest extends AbstractFinalsWeekTestRunner<BST, Integer, Boolean> {

    private static final Random RANDOM_SOURCE = new Random();

    public BSTTest() {
        super(new FinalsWeekProvider<>() {
            @Override
            public BST initFixture() {
                return new BST();
            }

            @Override
            public Optional<Boolean> sendMessageSynchronous(final BST receiver, final Integer message) {
                return Optional.of(receiver.add(message));
            }

            @Override
            public Integer initMessage(final int sourceSeed) {
                return sourceSeed;
            }
        });
    }
}



