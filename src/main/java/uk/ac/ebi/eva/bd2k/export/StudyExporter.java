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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StudyExporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(StudyExporter.class);

    private final StudyTransformer<T> transformer;

    private final OmicsDataMarshaller marshaller;

    private Map<T, String> outputFileNames;

    public StudyExporter(StudyTransformer<T> transformer, OmicsDataMarshaller marshaller) {
        this.transformer = transformer;
        this.marshaller = marshaller;
        outputFileNames = new HashMap<T, String>();
    }

    public void export(List<T> studies, String outputDirectory) throws FileNotFoundException {
        logger.info("Exporting {} studies to {} ...", studies.size(), outputDirectory);
        for (T study : studies) {
            Database database = transformer.transform(study);
            String studyOutputFileName = getFileName(outputDirectory, study);
            outputFileNames.put(study, studyOutputFileName);
            marshaller.marshall(database, new FileOutputStream(studyOutputFileName));
        }
        logger.info("Done");
    }

    protected abstract String getFileName(String outputDirectory, T study);

    protected String getFileName(T study) {
        return outputFileNames.get(study);
    }

}
