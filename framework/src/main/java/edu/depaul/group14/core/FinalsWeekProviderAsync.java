package edu.depaul.group14.core;

import java.util.Optional;
import java.util.function.Consumer;

public interface FinalsWeekProviderAsync<Fixture, Message, Output> extends FixtureProvider<Fixture, Message> {

    void sendMessageAsync(Fixture fixture, Message message, final Consumer<Optional<Output>> complete);

    default boolean validateMessage(Message message, Output output) {
        // Default no op
        return true;
    }
}
