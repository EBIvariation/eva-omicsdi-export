package uk.ac.ebi.eva.bd2k.client;

import uk.ac.ebi.eva.bd2k.model.EnaProject;

public interface ProjectClient {
    EnaProject getProject(String projectId);
}
