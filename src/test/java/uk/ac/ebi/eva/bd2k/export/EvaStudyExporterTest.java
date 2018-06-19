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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;

import uk.ac.ebi.eva.bd2k.client.ProjectClient;
import uk.ac.ebi.eva.bd2k.model.EnaProject;
import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EvaStudyExporterTest {

    private static final String STUDY_1_ID = "s1";

    private static final String STUDY_2_ID = "s2";

    private static final String PRIVATE_STUDY_ID = "PRIVATE_PROJECT";

    private OmicsDataMarshaller marshaller;

    private ProjectClient enaProjectClientMock;

    private VariantStudy study1;

    private VariantStudy study2;

    private VariantStudy privateStudy;

    private String evaWebUrl;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        study1 = new VariantStudy(STUDY_1_ID, "study 1", "Study 1 desc", "EBI", "Homo Sapiens",
                                  new URI("www.study1.org"), "Illumina", "Case-Control");
        study2 = new VariantStudy(STUDY_2_ID, "study 2", "Study 2 desc", "EBI", "Homo Sapiens",
                                  new URI("www.study2.org"), "Illumina", "Case-Control");
        privateStudy = new VariantStudy(PRIVATE_STUDY_ID, "Private project", "Private project desc", "EBI",
                                        "Homo Sapiens", new URI("www.private-project.org"), "Illumina", "Case-Control");

        evaWebUrl = "http://eva-host/eva/?eva-study={0}";
        marshaller = mock(OmicsDataMarshaller.class);
        enaProjectClientMock = studyId -> {
            if (studyId.equals(PRIVATE_STUDY_ID)) {
                throw new RuntimeException("The project is private and cannot be exported");
            } else {
                return new EnaProject(studyId, "2017-01-01");
            }
        };
    }

    @Test
    public void export() throws Exception {
        StudyExporter<VariantStudy> exporter = new EvaStudyExporter(
                new EvaStudyTransformer(enaProjectClientMock, evaWebUrl), marshaller);
        System.out.println("temporaryFolder = " + temporaryFolder.getRoot().toString());
        Path outputDirectory = temporaryFolder.getRoot().toPath();
        exporter.export(Arrays.asList(study1, study2, privateStudy), outputDirectory);

        verify(marshaller, times(1))
                .marshall(argThat(d -> ((Database) d).getEntries().getEntry().get(0).getId().equals(STUDY_1_ID)),
                          any(OutputStream.class));
        verify(marshaller, times(1))
                .marshall(argThat(d -> ((Database) d).getEntries().getEntry().get(0).getId().equals(STUDY_2_ID)),
                          any(OutputStream.class));
        verify(marshaller, times(0))
                .marshall(argThat(d -> ((Database) d).getEntries().getEntry().get(0).getId().equals(PRIVATE_STUDY_ID)),
                          any(OutputStream.class));

        assertEquals(outputDirectory.resolve(STUDY_1_ID + ".xml"), exporter.getStudyOutputFilePath(study1));
        assertEquals(outputDirectory.resolve(STUDY_2_ID + ".xml"), exporter.getStudyOutputFilePath(study2));
        assertEquals(null, exporter.getStudyOutputFilePath(privateStudy));
    }

}