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

import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.nio.file.Path;

/**
 * Exports EVA studies into a format compatible with OmicsDI.
 */
public class EvaStudyExporter extends StudyExporter<VariantStudy> {

    public EvaStudyExporter(StudyTransformer<VariantStudy> transformer,
                            OmicsDataMarshaller marshaller) {
        super(transformer, marshaller);
    }

    @Override
    protected Path getStudyOutputFilePath(Path outputDirectory, VariantStudy study) {
        return outputDirectory.resolve(study.getId() + ".xml");
    }
}
