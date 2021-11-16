package edu.depaul.group14.core;

import edu.depaul.group14.StdOutStatProcessor;

public interface FixtureProvider<Fixture, Message> {

    Fixture initFixture();

    Message initMessage(int iteration);

    default StatProcessor provideStatProcessor() {
        return new StdOutStatProcessor();
    }
}
