package uk.ac.ebi.eva.bd2k.client;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import uk.ac.ebi.ena.sra.xml.ObjectType;
import uk.ac.ebi.ena.sra.xml.PROJECTDocument;
import uk.ac.ebi.ena.sra.xml.ProjectType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ProjectHttpMessageConverter implements HttpMessageConverter<ProjectType> {
    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_XML);
    }

    @Override
    public ProjectType read(Class<? extends ProjectType> aClass,
                            HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        try {
            ObjectType xmlDocument = ObjectType.Factory.parse(httpInputMessage.getBody());

            // create a cursor over the XML document and look for the "PROJECT" node
            XmlCursor xmlCursor = xmlDocument.newCursor();
            // TODO: Search by node name instead of using "toFirstChild calls"
            xmlCursor.toFirstChild();
            xmlCursor.toFirstChild();

            PROJECTDocument document = PROJECTDocument.Factory.parse(xmlCursor.xmlText());
            return document.getPROJECT();
        } catch (XmlException e) {
            throw new HttpMessageNotReadableException(e.getMessage());
        }
    }

    @Override
    public void write(ProjectType projectType, MediaType mediaType,
                      HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        throw new HttpMessageNotWritableException(
                projectType.getClass() + " objects cannot be written by this converter");
    }
}
