package uk.ac.ebi.eva.bd2k.conf;


import java.util.Properties;

public class ExporterConfiguration {

    private static final String EVA_STUDIES_WS_PROPERTY = "eva.studies.url";

    private String evaStudiesWSURL;

    public ExporterConfiguration(Properties properties) {
        evaStudiesWSURL = properties.getProperty(EVA_STUDIES_WS_PROPERTY);
    }

    public String getEvaStudiesWSURL() {
        return evaStudiesWSURL;
    }
}
