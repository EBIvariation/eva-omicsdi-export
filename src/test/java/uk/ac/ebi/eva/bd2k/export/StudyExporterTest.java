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
package uk.ac.ebi.eva.bd2k.export;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;

import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class StudyExporterTest {

    @Mock
    private OmicsDataMarshaller marshaller;

    private static final String STUDY_1_ID = "s1";

    private static final String STUDY_2_ID = "s2";

    private VariantStudy study1;

    private VariantStudy study2;

    @Before
    public void setUp() throws Exception {
        study1 = new VariantStudy(STUDY_1_ID, "study 1", "Study 1 desc", "EBI", "Homo Sapiens",
                                  new URI("www.study1.org"), "Illumina", "Case-Control");
        study2 = new VariantStudy(STUDY_2_ID, "study 2", "Study 2 desc", "EBI", "Homo Sapiens",
                                  new URI("www.study2.org"), "Illumina", "Case-Control");

        marshaller = mock(OmicsDataMarshaller.class);
    }

    @Test
    public void export() throws Exception {
        StudyExporter exporter = new StudyExporter(new StudyTransformerImpl(), marshaller);

        exporter.export(Arrays.asList(study1, study2), "/tmp");

        verify(marshaller, times(1))
                .marshall(argThat(d -> ((Database) d).getEntries().getEntry().get(0).getId().equals(STUDY_1_ID)),
                          any(OutputStream.class));
        verify(marshaller, times(1))
                .marshall(argThat(d -> ((Database) d).getEntries().getEntry().get(0).getId().equals(STUDY_2_ID)),
                          any(OutputStream.class));
    }


}