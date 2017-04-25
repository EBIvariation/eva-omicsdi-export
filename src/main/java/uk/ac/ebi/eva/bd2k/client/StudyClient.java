package uk.ac.ebi.eva.bd2k.client;

import uk.ac.ebi.eva.lib.models.VariantStudy;

import java.util.List;

public interface StudyClient {
    List<VariantStudy> getAllStudies();
}
