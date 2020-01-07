package it.kruczek.ztm.Processing;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface ProcessingOptions extends PipelineOptions {
    @Description("An absolute path of input directory")
    @Validation.Required
    String getInputDirectory();
    void setInputDirectory(String value);

    @Description("ElasticSearch hostname url e.g. http://localhost:9200")
    @Validation.Required
    String getElasticSearchUrl();
    void setElasticSearchUrl(String value);

    @Description("ElasticSearch username")
    @Validation.Required
    String getElasticSearchUsername();
    void setElasticSearchUsername(String value);

    @Description("ElasticSearch password")
    @Validation.Required
    String getElasticSearchPassword();
    void setElasticSearchPassword(String value);

    @Description("ElasticSearch index")
    @Validation.Required
    String getElasticSearchIndex();
    void setElasticSearchIndex(String value);


//    @TODO ELASTIC SEARCH params needed
//
//    @Description("MongoDB URL")
//    @Validation.Required
//    String getOutputMongoDbUrl();
//    void setOutputMongoDbUrl(String value);
//
//    @Description("MongoDB collection")
//    @Validation.Required
//    String getOutputMongoDbCollection();
//    void setOutputMongoDbCollection(String value);
}
