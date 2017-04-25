package uk.ac.ebi.eva.bd2k.client;

import org.opencb.datastore.core.QueryResponse;
import org.opencb.datastore.core.QueryResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import uk.ac.ebi.eva.lib.models.VariantStudy;

import java.util.List;

public class StudyEvaWSClient implements StudyClient {
    private String studyWsUrl;

    private RestTemplate restTemplate;

    public StudyEvaWSClient(String studyWsUrl, RestTemplate restTemplate) {
        this.studyWsUrl = studyWsUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<VariantStudy> getAllStudies() {
        ResponseEntity<QueryResponse<QueryResult<VariantStudy>>> response = restTemplate
                .exchange(studyWsUrl, HttpMethod.GET, null,
                          new ParameterizedTypeReference<QueryResponse<QueryResult<VariantStudy>>>() {
                          });
        QueryResult<VariantStudy> queryResult = response.getBody().getResponse().get(0);
        return queryResult.getResult();
    }
}
