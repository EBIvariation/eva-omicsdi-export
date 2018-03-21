/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.bd2k.transform;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.xml.validator.parser.model.AdditionalFields;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Field;

import uk.ac.ebi.eva.bd2k.client.ProjectClient;
import uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer;
import uk.ac.ebi.eva.bd2k.model.EnaProject;
import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.DATABASE_DESCRIPTION;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.EUROPEAN_VARIATION_ARCHIVE;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.EVA_FIRST_PUBLISHED_DATE;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.FULL_DATASET_LINK;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.GENOMICS;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.INSTRUMENT_PLATFORM;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.PUBLICATION_DATE;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.SPECIES;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.TECHNOLOGY_TYPE;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformer.OMICS_TYPE;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformer.REPOSITORY;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformer.SUBMITTER;

public class EvaStudyTransformerTest {

    public static final String EVA_STUDY_PUBLICATION_DATE = "2017-01-01";

    public static final String PRE_EVA_STUDY_PUBLICATION_DATE = "2014-01-01";

    private ProjectClient projectClientMock;

    private ProjectClient preEvaProjectClientMock;

    private VariantStudy variantStudy;

    @Before
    public void setUp() throws Exception {
        projectClientMock = projectId -> new EnaProject(projectId, EVA_STUDY_PUBLICATION_DATE);
        preEvaProjectClientMock = projectId -> new EnaProject(projectId, PRE_EVA_STUDY_PUBLICATION_DATE);

        variantStudy = new VariantStudy("S1", "Study 1", "This is the study 1", "EBI", "Homo sapiens",
                                        new URI("http://www.study1.org"), "Illumina", "Case-Control");
    }

    @Test
    public void transform() throws Exception {
        EvaStudyTransformer studyTransformer = new EvaStudyTransformer(projectClientMock);

        Database database = studyTransformer.transform(variantStudy);

        assertEquals("EVA", database.getName());
        assertEquals(DATABASE_DESCRIPTION, database.getDescription());
        assertEquals(LocalDate.now().toString(), database.getRelease());
        assertEquals(LocalDate.now().toString(), database.getReleaseDate());
        assertEquals(1, database.getEntryCount().intValue());

        Entry entry = database.getEntries().getEntry().get(0);
        assertEquals(variantStudy.getId(), entry.getId());
        assertEquals(variantStudy.getId(), entry.getAcc());
        assertEquals(variantStudy.getName(), entry.getName().getValue());
        assertEquals(variantStudy.getDescription(), entry.getDescription());
        assertEquals(EVA_STUDY_PUBLICATION_DATE, entry.getDates().getDateByKey(PUBLICATION_DATE).getValue());

        AdditionalFields additionalFields = entry.getAdditionalFields();
        List<Field> fields = additionalFields.getField();
        assertFieldsContainsAttribute(fields, SPECIES, variantStudy.getSpeciesScientificName());
        assertFieldsContainsAttribute(fields, FULL_DATASET_LINK, variantStudy.getUrl().toString());
        assertFieldsContainsAttribute(fields, INSTRUMENT_PLATFORM, variantStudy.getPlatform());
        assertFieldsContainsAttribute(fields, TECHNOLOGY_TYPE, variantStudy.getExperimentType());
        assertFieldsContainsAttribute(fields, OMICS_TYPE, GENOMICS);
        assertFieldsContainsAttribute(fields, REPOSITORY, "EVA");
        assertFieldsContainsAttribute(fields, SUBMITTER, variantStudy.getCenter());
        // TODO: publications
    }

    @Test
    public void testNoDatePreviousToEvaIsUsed() throws Exception {
        EvaStudyTransformer studyTransformer = new EvaStudyTransformer(preEvaProjectClientMock);

        Database database = studyTransformer.transform(variantStudy);
        Entry entry = database.getEntries().getEntry().get(0);

        assertEquals(EVA_FIRST_PUBLISHED_DATE, entry.getDates().getDateByKey(PUBLICATION_DATE).getValue());
    }

    private void assertFieldsContainsAttribute(List<Field> fields, String name, final String value) {
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals(name) && field.getValue().equals(value)));
    }

}