package uk.ac.ebi.eva.bd2k;


import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;

import uk.ac.ebi.eva.bd2k.client.StudyEvaWSClient;
import uk.ac.ebi.eva.bd2k.conf.ExporterConfiguration;
import uk.ac.ebi.eva.bd2k.export.StudyExporter;
import uk.ac.ebi.eva.bd2k.transform.StudyTransformerImpl;

import java.io.IOException;
import java.util.Properties;

public class StudyExporterAppl {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(StudyExporterAppl.class.getResourceAsStream("/application.properties"));
            ExporterConfiguration configuration = new ExporterConfiguration(properties);
            StudyExporter exporter = new StudyExporter(new StudyEvaWSClient(configuration.getEvaStudiesWSURL(), new RestTemplate()), new StudyTransformerImpl(), new OmicsDataMarshaller(), configuration.getOutputDirectory());
            exporter.export();
        } catch (IOException e) {
            System.out.printf("ERROR loading properties: " + e.getMessage());
        }
    }
}
