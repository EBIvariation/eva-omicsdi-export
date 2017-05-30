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
package uk.ac.ebi.eva.bd2k.model;

import java.net.URI;

/**
 * Each object of this class represents a EVA study
 */
public class VariantStudy {

    private String id;

    private String name;

    private String description;

    private String center;

    private String speciesScientificName;

    private URI url;

    private String platform;

    private String experimentType;

    public VariantStudy() {
    }

    public VariantStudy(String id, String name, String description, String center, String speciesScientificName,
                        URI url, String platform, String experimentType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.center = center;
        this.speciesScientificName = speciesScientificName;
        this.url = url;
        this.platform = platform;
        this.experimentType = experimentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getSpeciesScientificName() {
        return speciesScientificName;
    }

    public void setSpeciesScientificName(String speciesScientificName) {
        this.speciesScientificName = speciesScientificName;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantStudy that = (VariantStudy) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
