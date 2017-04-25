package uk.ac.ebi.eva.bd2k.transform;

import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import uk.ac.ebi.eva.lib.models.VariantStudy;

public interface StudyTransformer {
    Entry transform (VariantStudy variantStudy);
}
