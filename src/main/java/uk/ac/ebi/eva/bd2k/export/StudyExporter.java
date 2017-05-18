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

import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyExporter {

    private final StudyTransformer transformer;

    private final OmicsDataMarshaller marshaller;

    private Map<String, String> outputFileNames;

    public StudyExporter(StudyTransformer transformer, OmicsDataMarshaller marshaller) {
        this.transformer = transformer;
        this.marshaller = marshaller;
        outputFileNames = new HashMap<>();
    }

    public void export(List<VariantStudy> studies, String outputDirectory) throws FileNotFoundException {
        for (VariantStudy study : studies) {
            Database database = transformer.transform(study);
            String outputFileName = outputDirectory + "/" + study.getId() + ".xml";
            outputFileNames.put(study.getId(), outputFileName);
            marshaller.marshall(database, new FileOutputStream(outputFileName));
        }
    }

    public String getOutputFileName(String study) {
        return outputFileNames.get(study);
    }
}
