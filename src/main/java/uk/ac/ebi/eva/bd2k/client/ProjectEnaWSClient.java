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
import uk.ac.ebi.ena.sra.xml.ProjectType;

import uk.ac.ebi.eva.bd2k.model.EnaProject;

import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * Client to retrieve a project information from ENA Webservices
 */
public class ProjectEnaWSClient implements ProjectClient {

    private final String projectServiceUrl;

    private final RestTemplate restTemplate;

    private ProjectType enaProjectType;

    private SimpleDateFormat simpleDateFormat;

    public ProjectEnaWSClient(String projectServiceUrl, RestTemplate restTemplate) {
        this.projectServiceUrl = projectServiceUrl;
        this.restTemplate = restTemplate;
        HttpMessageConverter<ProjectType> messageConverter = new ProjectHttpMessageConverter();
        this.restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public EnaProject getProject(String projectId) {
        enaProjectType = restTemplate.getForObject(projectServiceUrl, ProjectType.class, projectId);
        return new EnaProject(projectId, getPublicationDate(enaProjectType));
    }

    private String getPublicationDate(ProjectType project) {
        return simpleDateFormat.format(project.getFirstPublic().getTime());
    }

    public ProjectType getEnaProjectType() {
        return enaProjectType;
    }
}
