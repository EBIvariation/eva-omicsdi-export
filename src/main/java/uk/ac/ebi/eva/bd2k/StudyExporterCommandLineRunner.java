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
package uk.ac.ebi.eva.bd2k;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;

import uk.ac.ebi.eva.bd2k.client.StudyEvaWSClient;
import uk.ac.ebi.eva.bd2k.conf.ExporterConfigurationProperties;
import uk.ac.ebi.eva.bd2k.export.EvaStudyExporter;
import uk.ac.ebi.eva.bd2k.export.StudyExporter;
import uk.ac.ebi.eva.bd2k.export.EvaStudyTransformer;
import uk.ac.ebi.eva.bd2k.model.VariantStudy;

@Component
public class StudyExporterCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ExporterConfigurationProperties exporterConfiguration;

    @Override
    public void run(String... strings) throws Exception {
        StudyExporter<VariantStudy> exporter = new EvaStudyExporter(new EvaStudyTransformer(), new OmicsDataMarshaller());
        StudyEvaWSClient studyEvaWSClient = new StudyEvaWSClient(exporterConfiguration.getEvaStudiesUrl(), new RestTemplate());
        exporter.export(studyEvaWSClient.getAllStudies(), exporterConfiguration.getOutputDirectory());
    }
}
