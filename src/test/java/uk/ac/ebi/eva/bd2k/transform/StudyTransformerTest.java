package uk.ac.ebi.eva.bd2k.transform;

import org.junit.Test;
import org.opencb.biodata.models.variant.VariantSource;
import uk.ac.ebi.ddi.xml.validator.parser.model.AdditionalFields;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Field;

import uk.ac.ebi.eva.lib.models.VariantStudy;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;
import static uk.ac.ebi.eva.bd2k.transform.StudyTransformerImpl.FULL_DATASET_LINK;
import static uk.ac.ebi.eva.bd2k.transform.StudyTransformerImpl.INSTRUMENT_PLATFORM;
import static uk.ac.ebi.eva.bd2k.transform.StudyTransformerImpl.SPECIES;
import static uk.ac.ebi.eva.bd2k.transform.StudyTransformerImpl.TECHNOLOGY_TYPE;

public class StudyTransformerTest {
    @Test
    public void transform() throws Exception {
        String studyName = "Study 1";
        String studyId = "S1";
        List<VariantSource> sources = null;
        String studyDescription = "This is the study 1";
        int[] taxonomyId = null;
        String speciesCommonName = "Human";
        String speciesScientificName = "Homo sapiens";
        String sourceType = null;
        String center = "EBI";
        String material = "DNA";
        String scope = null;
        VariantStudy.StudyType type = VariantStudy.StudyType.CASE_CONTROL;
        String experimentType = "Whole Genome Sequencing";
        String experimentTypeAbbrevation = "WGS";
        String referenceAssembly = "Grch37";
        String platform = "Illumina";
        URI projectUrl = new URI("http://www.study1.org");
        String[] publications = null;
        int numVariants = 1000000;
        int numSamples = 10;
        VariantStudy variantStudy = new VariantStudy(studyName, studyId, sources, studyDescription, taxonomyId,
                                                     speciesCommonName, speciesScientificName, sourceType, center, material,
                                                     scope, type, experimentType, experimentTypeAbbrevation,
                                                     referenceAssembly, platform, projectUrl, publications, numVariants,
                                                     numSamples);

        StudyTransformerImpl studyTransformer = new StudyTransformerImpl();
        Entry entry = studyTransformer.transform(variantStudy);

        assertEquals(studyId, entry.getId());
        assertEquals(studyName, entry.getName().getValue());
        assertEquals(studyDescription, entry.getDescription());
        assertEquals(center, entry.getAuthors());
        // TODO: publication date
        AdditionalFields additionalFields = entry.getAdditionalFields();
        List<Field> fields = additionalFields.getField();
        // TODO: common name or scientific?
        assertFieldsContainsAttribute(fields, SPECIES, speciesCommonName);
        assertFieldsContainsAttribute(fields, FULL_DATASET_LINK, projectUrl.toString());
        assertFieldsContainsAttribute(fields, INSTRUMENT_PLATFORM, platform);
        assertFieldsContainsAttribute(fields, TECHNOLOGY_TYPE, type.toString());
        // TODO: publications
    }

    private void assertFieldsContainsAttribute(List<Field> fields, String name, final String value) {
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals(name) && field.getValue().equals(value)));
    }

}