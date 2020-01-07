package it.kruczek.ztm.Processing;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.fs.EmptyMatchTreatment;
import org.apache.beam.sdk.transforms.Watch;
import org.joda.time.Duration;

public abstract class FilesMatchingConfigurationFactory {
    public static FileIO.Match createWatcher(String filePattern) {
        return FilesMatchingConfigurationFactory
                .createFileMatcher(filePattern)
                .continuously(
                        Duration.standardSeconds(5),
                        Watch.Growth.afterTimeSinceNewOutput(Duration.standardHours(1))
                );
    }

    public static FileIO.Match createOnce(String filePattern) {
        return FilesMatchingConfigurationFactory.createFileMatcher(filePattern);
    }

    private static FileIO.Match createFileMatcher(String filePattern) {
        return FileIO.match()
                .filepattern(filePattern)
                .withEmptyMatchTreatment(EmptyMatchTreatment.DISALLOW);
    }
}
