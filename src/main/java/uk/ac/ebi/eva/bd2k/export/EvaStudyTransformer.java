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
package uk.ac.ebi.eva.bd2k.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import uk.ac.ebi.eva.bd2k.model.VariantStudy;

import java.time.LocalDate;
import java.util.Collections;

public class EvaStudyTransformer extends StudyTransformer<VariantStudy> {

    private static final Logger logger = LoggerFactory.getLogger(EvaStudyTransformer.class);

    public static final String SPECIES = "species";

    public static final String FULL_DATASET_LINK = "full_dataset_link";

    public static final String INSTRUMENT_PLATFORM = "instrument_platform";

    public static final String TECHNOLOGY_TYPE = "technology_type";

    @Override
    protected Entry transformStudy(VariantStudy variantStudy) {
        logger.info("Transforming study {} ...", variantStudy.getId());
        Entry entry = new Entry();

        entry.setId(variantStudy.getId());
        entry.setName(variantStudy.getName());
        entry.setDescription(variantStudy.getDescription());
        entry.setAuthors(variantStudy.getCenter());

        entry.addAdditionalField(SPECIES, variantStudy.getSpeciesScientificName());
        entry.addAdditionalField(FULL_DATASET_LINK, variantStudy.getUrl().toString());
        entry.addAdditionalField(INSTRUMENT_PLATFORM, variantStudy.getPlatform());
        entry.addAdditionalField(TECHNOLOGY_TYPE, variantStudy.getType());

        return entry;
    }

    @Override
    protected Database buildSingleEntryDatabase(Entry entry) {
        Database database = new Database();

        database.setName("EVA");
        database.setRelease(LocalDate.now().toString());
        database.setReleaseDate(LocalDate.now().toString());
        database.setEntries(Collections.singletonList(entry));
        database.setEntryCount(1);

        return database;
    }
}
