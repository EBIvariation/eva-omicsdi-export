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
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.FULL_DATASET_LINK;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.INSTRUMENT_PLATFORM;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.PUBLICATION_DATE_TAG;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.SPECIES;
import static uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer.TECHNOLOGY_TYPE;

public class StudyTransformerTest {

    public static final String PUBLICATION_DATE = "2017-01-01";

    private ProjectClient projectClientMock;

    @Before
    public void setUp() throws Exception {
        projectClientMock = projectId -> new EnaProject(projectId, PUBLICATION_DATE);
    }

    @Test
    public void transform() throws Exception {
        String studyName = "Study 1";
        String studyId = "S1";
        String studyDescription = "This is the study 1";
        String speciesScientificName = "Homo sapiens";
        String center = "EBI";
        String type = "Case-Control";
        String platform = "Illumina";
        URI projectUrl = new URI("http://www.study1.org");
        VariantStudy variantStudy = new VariantStudy(studyId, studyName, studyDescription, center, speciesScientificName, projectUrl, platform, type);

        EvaStudyTransformer studyTransformer = new EvaStudyTransformer(projectClientMock);

        Database database = studyTransformer.transform(variantStudy);

        assertEquals("EVA", database.getName());
        assertEquals(LocalDate.now().toString(), database.getRelease());
        assertEquals(LocalDate.now().toString(), database.getReleaseDate());
        assertEquals(1, database.getEntryCount().intValue());

        Entry entry = database.getEntries().getEntry().get(0);
        assertEquals(studyId, entry.getId());
        assertEquals(studyName, entry.getName().getValue());
        assertEquals(studyDescription, entry.getDescription());
        assertEquals(center, entry.getAuthors());
        assertEquals(PUBLICATION_DATE, entry.getDates().getDateByKey(PUBLICATION_DATE_TAG).getValue());

        AdditionalFields additionalFields = entry.getAdditionalFields();
        List<Field> fields = additionalFields.getField();
        assertFieldsContainsAttribute(fields, SPECIES, speciesScientificName);
        assertFieldsContainsAttribute(fields, FULL_DATASET_LINK, projectUrl.toString());
        assertFieldsContainsAttribute(fields, INSTRUMENT_PLATFORM, platform);
        assertFieldsContainsAttribute(fields, TECHNOLOGY_TYPE, type);
        // TODO: publications
    }

    private void assertFieldsContainsAttribute(List<Field> fields, String name, final String value) {
        assertTrue(fields.stream().anyMatch(field -> field.getName().equals(name) && field.getValue().equals(value)));
    }

}