package edu.depaul.group14.core;

import java.util.Optional;

public interface FinalsWeekProvider <Fixture, Message, Output> extends FixtureProvider<Fixture, Message> {

    Optional<Output> sendMessageSynchronous(Fixture fixture, Message message);

    default boolean validateMessage(Message message, Output output) {
        // Default no op
        return true;
    }
}
