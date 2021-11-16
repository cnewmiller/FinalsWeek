package edu.depaul.group14;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.group14.core.StatProcessor;

public class CsvStatProcessor implements StatProcessor {

    private final Path outputDir;

    public CsvStatProcessor(Path outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void consumeStatistics(final String testName, final List<Statistic> testTimes) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("START_MILLIS,STOP_MILLIS,VALIDATION_SUCCEEDED");
        testTimes.stream()
                 .map(statistic -> String.format("%d,%d,%b",
                                                 statistic.startMillis(),
                                                 statistic.stopMillis(),
                                                 statistic.validationSucceeded()))
                 .forEach(lines::add);
        final double max = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).max()
                                    .orElseThrow(IllegalStateException::new);
        final double min = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).min()
                                    .orElseThrow(IllegalStateException::new);
        final double average = testTimes.stream().mapToLong(l -> l.stopMillis() - l.startMillis()).average()
                                        .orElseThrow(IllegalStateException::new);

        try {
            this.outputDir.toFile().mkdirs();
            final long runMillis = System.currentTimeMillis();
            final File runFile = new File(this.outputDir.toFile(), String.format("%s_%d.csv", testName, runMillis));
            final File historyFile = new File(this.outputDir.toFile(),
                                              String.format("%s_history.csv", testName, runMillis));
            Files.write(runFile.toPath(), lines);
            if (!historyFile.exists()) {
                Files.writeString(historyFile.toPath(), "RUN_TIME_MILLIS,MIN,MAX,AVERAGE\n",
                                  StandardOpenOption.CREATE_NEW);
            }
            Files.writeString(historyFile.toPath(), String.format("%d,%f,%f,%f\n", runMillis, min, max, average),
                              StandardOpenOption.APPEND);


        } catch (IOException e) {
            throw new IllegalStateException("Could not write to output path", e);
        }
    }
}
