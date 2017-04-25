package uk.ac.ebi.eva.bd2k.export;

import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;

import uk.ac.ebi.eva.bd2k.client.StudyClient;
import uk.ac.ebi.eva.bd2k.transform.StudyTransformer;
import uk.ac.ebi.eva.lib.models.VariantStudy;

public class StudyExporter {

    private final StudyClient studyClient;

    private final StudyTransformer transformer;

    private final OmicsDataMarshaller marshaller;

    public StudyExporter(StudyClient studyClient, StudyTransformer transformer, OmicsDataMarshaller marshaller) {
        this.studyClient = studyClient;
        this.transformer = transformer;
        this.marshaller = marshaller;
    }

    public void export() {
        for (VariantStudy study : studyClient.getAllStudies()) {
            marshaller.marshall(transformer.transform(study));
        }
    }
}
