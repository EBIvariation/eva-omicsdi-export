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

import org.opencb.datastore.core.QueryResponse;
import org.opencb.datastore.core.QueryResult;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import uk.ac.ebi.eva.bd2k.model.VariantStudy;

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
