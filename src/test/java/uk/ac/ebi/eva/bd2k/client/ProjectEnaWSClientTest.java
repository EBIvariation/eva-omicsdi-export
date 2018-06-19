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

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ena.sra.xml.AttributeType;
import uk.ac.ebi.ena.sra.xml.ProjectType;
import uk.ac.ebi.ena.sra.xml.PublicationType;
import uk.ac.ebi.ena.sra.xml.URLType;
import uk.ac.ebi.ena.sra.xml.XRefType;

import uk.ac.ebi.eva.bd2k.model.EnaProject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ProjectEnaWSClientTest {

    private static final String PROJECT_WS_URL = "MOCKSERVER/ena/data/view/{projectId}&display=xml";

    private static final String PROJECT_ID = "PRJEB6042";

    private static final String PRIVATE_PROJECT_ID = "PRJEBPRIVATE";

    private MockRestServiceServer server;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @After
    public void verifyServerCalls() throws Exception {
        server.verify();
    }

    @Test
    public void getProjectType() throws Exception {
        ProjectEnaWSClient enaProjectWSClient = createClientBoundToMockServer("project-ws-response.xml", PROJECT_ID);

        enaProjectWSClient.getProject(PROJECT_ID);
        ProjectType project = enaProjectWSClient.getEnaProjectType();

        assertEquals("PRJEB6042", project.getAccession());
        assertEquals(
                "CENTER FOR GENOMIC REGULATION - CRG (BARCELONA), INSTITUTE OF HUMAN GENETICS - HELMHOLTZ ZENTRUM " +
                        "(MUNICH)",
                project.getCenterName());
        assertEquals("Geuvadis", project.getAlias());
        assertEquals("GEUVADIS: Genetic European Variation in Disease", project.getTITLE());
        assertEquals(
                "GEUVADIS: Genetic European Variation in Disease (http://www.geuvadis.org), is a European Medical " +
                        "Sequencing Consortium aiming at sharing capacity across Europe in high-throughput sequencing" +
                        " technology" + " to explore genetic variation in health and disease. It is funded by the " +
                        "European Commission 7th framework program under the Coordination and Support Action scheme. " +
                        "It started on the 1st October 2010, and ends on 31st December 2013. The project Coordinator " +
                        "is Xavier Estivill from Center for Genomic Regulation, Barcelona.",
                project.getDESCRIPTION());

        // dates
        AttributeType[] attributes = project.getPROJECTATTRIBUTES().getPROJECTATTRIBUTEArray();
        Arrays.stream(attributes)
              .anyMatch(attr -> attr.getTAG().equals("ENA-FIRST-PUBLIC") && attr.getVALUE().equals("2014-04-04"));
        Arrays.stream(attributes)
              .anyMatch(attr -> attr.getTAG().equals("ENA-LAST-UPDATE") && attr.getVALUE().equals("2016-05-20"));

        // project links
        ProjectType.PROJECTLINKS.PROJECTLINK[] projectlinks = project.getPROJECTLINKS().getPROJECTLINKArray();
        List<XRefType> xRefLinks = Arrays.stream(projectlinks).filter(link -> link.isSetXREFLINK())
                                         .map(link -> link.getXREFLINK()).collect(Collectors.toList());
        assertTrue(xRefLinks.stream().anyMatch(
                xRef -> xRef.getDB().equals("ENA-SUBMISSION") && xRef.getID().equals("ERA298142")));
        assertTrue(xRefLinks.stream().anyMatch(
                xRef -> xRef.getDB().equals("ENA-ANALYSIS") && xRef.getID().equals("ERZ019882")));

        String enaFastqFilesXRef = "http://www.ebi.ac" +
                ".uk/ena/data/warehouse/filereport?accession=PRJEB6042&result=read_run&fields=run_accession," +
                "fastq_ftp,fastq_md5,fastq_bytes";
        assertTrue(xRefLinks.stream().anyMatch(
                xRef -> xRef.getDB().equals("ENA-FASTQ-FILES") && xRef.getID().equals(enaFastqFilesXRef)));

        String enaSubmittedFilesXRef = "http://www.ebi.ac" +
                ".uk/ena/data/warehouse/filereport?accession=PRJEB6042&result=read_run&fields=run_accession," +
                "submitted_ftp,submitted_md5,submitted_bytes,submitted_format";
        assertTrue(xRefLinks.stream().anyMatch(
                xRef -> xRef.getDB().equals("ENA-SUBMITTED-FILES") && xRef.getID().equals(enaSubmittedFilesXRef)));

        List<URLType> urlLinks = Arrays.stream(projectlinks).filter(link -> link.isSetURLLINK())
                                       .map(link -> link.getURLLINK()).collect(Collectors.toList());
        String projectUrl = "http://www.ebi.ac.uk/ena/data/view/PRJEB3366";
        assertTrue(urlLinks.stream().anyMatch(
                url -> url.getLABEL().equals("PRJEB3366") && url.getURL().equals(projectUrl)));

        // publications
        ProjectType.PUBLICATIONS publications = project.getPUBLICATIONS();
        assertEquals(1, publications.sizeOfPUBLICATIONArray());
        PublicationType.PUBLICATIONLINKS publicationlinks = publications.getPUBLICATIONArray(0).getPUBLICATIONLINKS();
        assertEquals(1, publicationlinks.sizeOfPUBLICATIONLINKArray());
        XRefType xreflink = publicationlinks.getPUBLICATIONLINKArray(0).getXREFLINK();
        assertEquals("PMID", xreflink.getDB());
        assertEquals("24037378", xreflink.getID());
    }

    @Test
    public void getProject() throws Exception {
        ProjectEnaWSClient enaProjectWSClient = createClientBoundToMockServer("project-ws-response.xml", PROJECT_ID);
        EnaProject project = enaProjectWSClient.getProject(PROJECT_ID);

        assertEquals(PROJECT_ID, project.getId());
        assertEquals("2014-04-04", project.getPublicationDate());
    }

    @Test
    public void privateProjectWillProduceException() throws Exception {
        ProjectEnaWSClient enaProjectWSClient = createClientBoundToMockServer("private-project.xml",
                                                                              PRIVATE_PROJECT_ID);
        thrown.expect(HttpMessageNotReadableException.class);
        enaProjectWSClient.getProject(PRIVATE_PROJECT_ID);
    }

    private ProjectEnaWSClient createClientBoundToMockServer(String fileName, String projectId) throws Exception {
        String xmlTestFileBody = Files.readAllLines(
                Paths.get(ProjectEnaWSClientTest.class.getResource("/" + fileName).toURI())).stream()
                                      .reduce((s, s2) -> s + s2).get();

        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();

        server.expect(ExpectedCount.times(1), requestTo(PROJECT_WS_URL.replace("{projectId}", projectId)))
              .andExpect(method(HttpMethod.GET)).andRespond(withSuccess(xmlTestFileBody, MediaType.APPLICATION_XML));

        return new ProjectEnaWSClient(PROJECT_WS_URL, restTemplate);
    }
}