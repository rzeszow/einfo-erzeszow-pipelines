package it.kruczek.ztm.Processing.Pipelines;

import it.kruczek.ztm.Processing.FilesMatchingConfigurationFactory;
import it.kruczek.ztm.Processing.ParseXmlFiles;
import it.kruczek.ztm.Processing.ProcessingOptions;
import it.kruczek.ztm.Processing.TransformMatchesToFiles;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.io.File;

public class ImportLocalFilesWatcher {
    public static void main(String[] args) {
        ProcessingOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(ProcessingOptions.class);
        Pipeline pipeline = Pipeline.create(options);
        ElasticsearchIO.ConnectionConfiguration connectionConfiguration = ElasticsearchIO.ConnectionConfiguration.create(
                new String[]{options.getElasticSearchUrl()},
                options.getElasticSearchIndex(),
                "positions"
        );

        connectionConfiguration = connectionConfiguration
                .withUsername(options.getElasticSearchUsername())
                .withPassword(options.getElasticSearchPassword());

        PCollection<KV<String, String>> foundFiles = pipeline
                .apply(FilesMatchingConfigurationFactory.createWatcher(options.getInputDirectory()))
                .apply(FileIO.readMatches())
                .apply(TransformMatchesToFiles.create());


        foundFiles
                .apply(ParseXmlFiles.create())
                .apply(ElasticsearchIO.write().withConnectionConfiguration(connectionConfiguration));

        foundFiles
                .apply(ParDo.of(new DoFn<KV<String, String>, KV<String, String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        File file = new File(c.element().getKey());
                        file.delete();
                    }
                }));

        pipeline.run().waitUntilFinish();
    }
}
