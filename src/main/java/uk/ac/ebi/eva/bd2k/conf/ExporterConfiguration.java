package uk.ac.ebi.eva.bd2k.conf;


import java.util.Properties;

public class ExporterConfiguration {
    private static final String EVA_STUDIES_WS_PROPERTY = "evaStudiesURL";

    private static final String OUTPUT_DIRECTORY_PROPERTY = "outputDirectory";

    private String evaStudiesWSURL;

    private String outputDirectory;

    public ExporterConfiguration(Properties properties) {
        evaStudiesWSURL = properties.getProperty(EVA_STUDIES_WS_PROPERTY);
        outputDirectory = properties.getProperty(OUTPUT_DIRECTORY_PROPERTY);
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getEvaStudiesWSURL() {
        return evaStudiesWSURL;
    }
}
