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

import uk.ac.ebi.eva.bd2k.client.ProjectFirstPublicDateNotFoundException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic omicsDI exporter that serialize studies in XML files
 * @param <T> Study class
 */
public abstract class StudyExporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(StudyExporter.class);

    private final StudyTransformer<T> transformer;

    private final OmicsDataMarshaller marshaller;

    private Map<T, Path> outputFileNames;

    public StudyExporter(StudyTransformer<T> transformer, OmicsDataMarshaller marshaller) {
        this.transformer = transformer;
        this.marshaller = marshaller;
        outputFileNames = new HashMap<>();
    }

    public void export(List<T> studies, Path outputDirectory) {
        logger.info("Exporting {} studies to {} ...", studies.size(), outputDirectory);
        try {
            for (T study : studies) {
                Database database = transformer.transform(study);
                Path studyOutputFilePath = getStudyOutputFilePath(outputDirectory, study);
                outputFileNames.put(study, studyOutputFilePath);
                marshaller.marshall(database, new FileOutputStream(studyOutputFilePath.toFile()));
            }
            logger.info("Done");
        } catch (FileNotFoundException e) {
            logger.error("Cannot create output file: {}", e.getMessage());
        } catch (ProjectFirstPublicDateNotFoundException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    protected abstract Path getStudyOutputFilePath(Path outputDirectory, T study);

    protected Path getStudyOutputFilePath(T study) {
        return outputFileNames.get(study);
    }

}
