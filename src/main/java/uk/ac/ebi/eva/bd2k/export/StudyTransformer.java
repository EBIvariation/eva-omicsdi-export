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

import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

/**
 * A generic class that transform studies into "Database" omicsDI objects
 * @param <T> Study class
 */
public abstract class StudyTransformer<T> {


    public static final String SPECIES = "species";

    public static final String FULL_DATASET_LINK = "full_dataset_link";

    public static final String INSTRUMENT_PLATFORM = "instrument_platform";

    public static final String TECHNOLOGY_TYPE = "dataset_type";

    public static final String OMICS_TYPE = "omics_type";

    public static final String REPOSITORY = "repository";

    public static final String PUBLICATION_DATE = "publication";

    public static final String SUBMITTER = "submitter";

    public Database transform (T study){
        Entry entry = transformStudy(study);
        Database database = buildSingleEntryDatabase(entry);
        return database;
    }

    protected abstract Entry transformStudy(T study);

    protected abstract Database buildSingleEntryDatabase(Entry entry);

}
