package edu.depaul.group14.core;

import java.util.List;
import java.util.Objects;

public interface StatProcessor {
    static final class Statistic {
        private final long startMillis;
        private final long stopMillis;
        private final boolean validationSucceeded;

        public Statistic(long startMillis, long stopMillis, boolean validationSucceeded) {
            this.startMillis = startMillis;
            this.stopMillis = stopMillis;
            this.validationSucceeded = validationSucceeded;
        }

        public long startMillis() {
            return startMillis;
        }

        public long stopMillis() {
            return stopMillis;
        }

        public boolean validationSucceeded() {
            return validationSucceeded;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            var that = (Statistic) obj;
            return this.startMillis == that.startMillis &&
                   this.stopMillis == that.stopMillis &&
                   this.validationSucceeded == that.validationSucceeded;
        }

        @Override
        public int hashCode() {
            return Objects.hash(startMillis, stopMillis, validationSucceeded);
        }

        @Override
        public String toString() {
            return "Statistic[" +
                   "startMillis=" + startMillis + ", " +
                   "stopMillis=" + stopMillis + ", " +
                   "validationSucceeded=" + validationSucceeded + ']';
        }


    }


    void consumeStatistics(String testName, List<Statistic> testTimes);
}
