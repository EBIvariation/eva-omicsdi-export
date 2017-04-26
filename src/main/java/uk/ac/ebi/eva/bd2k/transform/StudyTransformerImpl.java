package uk.ac.ebi.eva.bd2k.transform;

import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import uk.ac.ebi.eva.lib.models.VariantStudy;

public class StudyTransformerImpl implements StudyTransformer {

    public static final String SPECIES = "species";
    public static final String FULL_DATASET_LINK = "full_dataset_link";
    public static final String INSTRUMENT_PLATFORM = "instrument_platform";
    public static final String TECHNOLOGY_TYPE = "technology_type";

    @Override
    public Entry transform(VariantStudy variantStudy) {
        Entry entry = new Entry();

        entry.setId(variantStudy.getId());
        entry.setName(variantStudy.getName());
        entry.setDescription(variantStudy.getDescription());
        entry.setAuthors(variantStudy.getCenter());

        entry.addAdditionalField(SPECIES, variantStudy.getSpeciesScientificName());
        entry.addAdditionalField(FULL_DATASET_LINK, variantStudy.getUrl().toString());
        entry.addAdditionalField(INSTRUMENT_PLATFORM, variantStudy.getPlatform());
        entry.addAdditionalField(TECHNOLOGY_TYPE, variantStudy.getType().toString());

        return entry;
    }
}
