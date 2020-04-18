/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.command;

import io.micronaut.starter.Options;
import io.micronaut.starter.Project;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.util.VersionInfo;
import io.micronaut.starter.build.BuildProperties;

import java.util.*;

public class CommandContext {

    private final Project project;
    private final BuildProperties buildProperties = new BuildProperties();
    private final Map<String, Object> configuration = new LinkedHashMap<>();
    private final Map<String, Object> bootstrapConfig = new LinkedHashMap<>();
    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final MicronautCommand command;
    private final Features features;
    private final Options options;

    public CommandContext(Project project,
                          MicronautCommand command,
                          Options options,
                          List<Feature> features) {
        this.command = command;
        this.project = project;
        this.features = new Features(features);
        this.options = options;
        String micronautVersion = VersionInfo.getVersion();
        if (options.getBuildTool() == BuildTool.gradle) {
            buildProperties.put("micronautVersion", micronautVersion);
        } else if (options.getBuildTool() == BuildTool.maven) {
            buildProperties.put("micronaut.version", micronautVersion);
        }
    }

    public void addTemplate(String name, Template template) {
        templates.put(name, template);
    }

    public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public Map<String, Object> getBootstrapConfig() {
        return bootstrapConfig;
    }

    public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    public Language getLanguage() {
        return options.getLanguage();
    }

    public TestFramework getTestFramework() {
        return options.getTestFramework();
    }

    public BuildTool getBuildTool() {
        return options.getBuildTool();
    }

    public Project getProject() {
        return project;
    }

    public MicronautCommand getCommand() {
        return command;
    }

    public Features getFeatures() {
        return features;
    }

    public void applyFeatures() {
        List<Feature> features = new ArrayList<>(this.features.getFeatures());
        features.sort(Comparator.comparingInt(Feature::getOrder));

        for (Feature feature: features) {
            feature.apply(this);
        }
    }

    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        return features.getFeatures().stream()
                .map(Feature::getClass)
                .anyMatch(feature::isAssignableFrom);
    }
}
