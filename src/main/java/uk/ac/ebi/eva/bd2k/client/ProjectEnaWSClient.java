package uk.ac.ebi.eva.bd2k.client;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.ProjectType;

import uk.ac.ebi.eva.bd2k.model.EnaProject;

import java.util.Arrays;
import java.util.Collections;

public class ProjectEnaWSClient implements ProjectClient {

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
        return new EnaProject(projectId, getPublicationDate(enaProjectType));
    }

    private String getPublicationDate(ProjectType project) {
        AttributeType[] attributes = project.getPROJECTATTRIBUTES().getPROJECTATTRIBUTEArray();
        return Arrays.stream(attributes).filter(a -> a.getTAG().equals("ENA-FIRST-PUBLIC"))
                     .map(AttributeType::getVALUE).findFirst().get();
    }

    public ProjectType getEnaProjectType() {
        return enaProjectType;
    }
}
