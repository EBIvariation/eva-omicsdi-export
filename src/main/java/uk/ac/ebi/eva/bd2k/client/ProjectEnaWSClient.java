package uk.ac.ebi.eva.bd2k.client;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ena.sra.xml.ProjectType;

import java.util.Collections;

public class ProjectEnaWSClient implements ProjectClient {

    private final String projectServiceUrl;

    private final RestTemplate restTemplate;

    public ProjectEnaWSClient(String projectServiceUrl, RestTemplate restTemplate) {
        this.projectServiceUrl = projectServiceUrl;
        this.restTemplate = restTemplate;
        HttpMessageConverter<ProjectType> messageConverter = new ProjectHttpMessageConverter();
        this.restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    }

    @Override
    public ProjectType getProject(String projectId) {
        ProjectType project = restTemplate.getForObject(projectServiceUrl, ProjectType.class, projectId);
        return project;
    }
}
