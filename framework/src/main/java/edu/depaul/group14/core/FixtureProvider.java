package edu.depaul.group14.core;

public interface FixtureProvider<Fixture, Message> {

    Fixture initFixture();

    Message initMessage(int iteration);
}
