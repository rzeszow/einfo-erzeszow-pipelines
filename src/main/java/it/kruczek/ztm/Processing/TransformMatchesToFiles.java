package it.kruczek.ztm.Processing;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.io.IOException;

public class TransformMatchesToFiles extends PTransform<PCollection<FileIO.ReadableFile>, PCollection<KV<String, String>>> {
    @Override
    public PCollection<KV<String, String>> expand(PCollection<FileIO.ReadableFile> readableFilePCollection) {
        return readableFilePCollection.apply(
                MapElements
                        .into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                        .via((FileIO.ReadableFile f) -> {
                            try {
                                return KV.of(
                                        f.getMetadata().resourceId().toString(),
                                        f.readFullyAsUTF8String()
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }));
    }

    static public TransformMatchesToFiles create() {
        return new TransformMatchesToFiles();
    }
}
