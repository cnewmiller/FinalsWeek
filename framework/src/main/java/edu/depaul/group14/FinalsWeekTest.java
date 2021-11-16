package edu.depaul.group14;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import edu.depaul.group14.core.StatProcessor;
import edu.depaul.group14.core.StatProcessor.Statistic;

public interface FinalsWeekTest<M> {


    final class Run<M, O> {
        private final long start;
        private final long end;
        private final M message;
        private final Optional<O> output;

        public Run(long start, long end, M message, Optional<O> output) {
            this.start = start;
            this.end = end;
            this.message = message;
            this.output = output;
        }

        public long start() {
            return start;
        }

        public long end() {
            return end;
        }

        public M message() {
            return message;
        }

        public Optional<O> output() {
            return output;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            var that = (Run) obj;
            return this.start == that.start &&
                   this.end == that.end &&
                   Objects.equals(this.message, that.message) &&
                   Objects.equals(this.output, that.output);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, message, output);
        }

        @Override
        public String toString() {
            return "Run[" +
                   "start=" + start + ", " +
                   "end=" + end + ", " +
                   "message=" + message + ", " +
                   "output=" + output + ']';
        }
    }

    void init();

    M initMessage(int iteration);


    StatProcessor provideStatProcessor();

    List<Statistic> testResponseTimes(final Function<Integer, M> messageSupplier,
                                      final int testIterations,
                                      final int messagesPerIteration);

}
