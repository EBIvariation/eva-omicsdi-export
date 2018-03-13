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
package uk.ac.ebi.eva.bd2k.client;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.ProjectType;

import uk.ac.ebi.eva.bd2k.model.EnaProject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * Client to retrieve a project information from ENA Webservices
 */
public class ProjectEnaWSClient implements ProjectClient {

    private static final String ENA_FIRST_PUBLIC = "ENA-FIRST-PUBLIC";

    private final String projectServiceUrl;

    private final RestTemplate restTemplate;

    private ProjectType enaProjectType;

    public ProjectEnaWSClient(String projectServiceUrl, RestTemplate restTemplate) {
        this.projectServiceUrl = projectServiceUrl;
        this.restTemplate = restTemplate;
        HttpMessageConverter<ProjectType> messageConverter = new ProjectHttpMessageConverter();
        this.restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    }

    @Override
    public EnaProject getProject(String projectId) {
        enaProjectType = restTemplate.getForObject(projectServiceUrl, ProjectType.class, projectId);
        return new EnaProject(projectId, getPublicationDate(enaProjectType, projectId));
    }

    private String getPublicationDate(ProjectType project, String projectId) {
        Optional<AttributeType> enaFirstPublicDate =
                Stream.of(project.getPROJECTATTRIBUTES().getPROJECTATTRIBUTEArray()).
                        filter(a -> a.getTAG().equals(ENA_FIRST_PUBLIC)).findFirst();
        if (enaFirstPublicDate.isPresent()) {
            return enaFirstPublicDate.get().getVALUE();
        } else {
            throw new NoSuchElementException(
                    ENA_FIRST_PUBLIC + " attribute not present in ENA Project " + projectId + " XML");
        }
    }

    public ProjectType getEnaProjectType() {
        return enaProjectType;
    }
}
