package it.kruczek.ztm.Processing;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.fs.EmptyMatchTreatment;
import org.apache.beam.sdk.transforms.Watch;
import org.joda.time.Duration;

public abstract class FilesMatchingConfigurationFactory {
    public static FileIO.Match create(String filePattern) {
        return FileIO.match()
                .filepattern(filePattern)
                .withEmptyMatchTreatment(EmptyMatchTreatment.DISALLOW)
                .continuously(
                        Duration.standardSeconds(5),
                        Watch.Growth.afterTimeSinceNewOutput(Duration.standardHours(1))
                );
    }
}
