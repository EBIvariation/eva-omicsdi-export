package uk.ac.ebi.eva.bd2k.client;

import uk.ac.ebi.ena.sra.xml.ProjectType;

public interface ProjectClient {
    ProjectType getProject(String projectId);
}
