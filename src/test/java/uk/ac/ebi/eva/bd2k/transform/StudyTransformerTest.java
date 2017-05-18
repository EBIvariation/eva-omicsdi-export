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

import org.junit.Test;
import uk.ac.ebi.ddi.xml.validator.parser.model.AdditionalFields;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Field;

import uk.ac.ebi.eva.bd2k.export.StudyTransformerImpl;
import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformerImpl.FULL_DATASET_LINK;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformerImpl.INSTRUMENT_PLATFORM;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformerImpl.SPECIES;
import static uk.ac.ebi.eva.bd2k.export.StudyTransformerImpl.TECHNOLOGY_TYPE;

public class StudyTransformerTest {
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

        StudyTransformerImpl studyTransformer = new StudyTransformerImpl();
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
        // TODO: publication date
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