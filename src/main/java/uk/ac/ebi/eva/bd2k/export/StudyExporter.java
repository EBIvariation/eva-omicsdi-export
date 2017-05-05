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

import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;

import uk.ac.ebi.eva.bd2k.client.StudyClient;
import uk.ac.ebi.eva.bd2k.transform.StudyTransformer;
import uk.ac.ebi.eva.lib.models.VariantStudy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collections;

public class StudyExporter {

    private final StudyClient studyClient;

    private final StudyTransformer transformer;

    private final OmicsDataMarshaller marshaller;

    private String outputDirectory;

    public StudyExporter(StudyClient studyClient, StudyTransformer transformer, OmicsDataMarshaller marshaller, String outputDirectory) {
        this.studyClient = studyClient;
        this.transformer = transformer;
        this.marshaller = marshaller;
        this.outputDirectory = outputDirectory;
    }

    public void export() throws FileNotFoundException {
        for (VariantStudy study : studyClient.getAllStudies()) {
            OutputStream os = new FileOutputStream(outputDirectory + "/" + study.getId() + ".xml");
            Database database = buildDatabase(study);
            marshaller.marshall(database, os);
        }
    }

    // TODO: extract to class
    private Database buildDatabase(VariantStudy study) {
        Database database = new Database();
        // TODO: review those fields
        database.setName("EVA");
        database.setRelease("BETA");
        database.setReleaseDate(LocalDate.now().toString());
        database.setEntries(Collections.singletonList(transformer.transform(study)));
        return database;
    }
}
