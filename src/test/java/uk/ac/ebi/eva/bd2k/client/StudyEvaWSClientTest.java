package uk.ac.ebi.eva.bd2k.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import uk.ac.ebi.eva.lib.models.VariantStudy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class StudyEvaWSClientTest {

    private RestTemplate restTemplate;

    private static final String STUDY_WS_URL = "MOCKSERVER/eva/webservices/rest/v1/meta/studies/all";

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        String body = Files.readAllLines(
                Paths.get(this.getClass().getResource("/allStudiesWSResponse.json").toURI())).stream().reduce((s, s2) -> s + s2).get();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo(STUDY_WS_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(body, MediaType.APPLICATION_JSON));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAllStudies() throws Exception {
        StudyClient evaStudyWSClient = new StudyEvaWSClient(STUDY_WS_URL, restTemplate);
        List<VariantStudy> studies = evaStudyWSClient.getAllStudies();

        assertEquals(3, studies.size());
        // TODO: test other fields
    }

}