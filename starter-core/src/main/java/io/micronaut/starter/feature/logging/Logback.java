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
package io.micronaut.starter.feature.logging;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.logging.template.logback;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

@Singleton
public class Logback implements LoggingFeature, DefaultFeature {

    @Override
    public String getName() {
        return "logback";
    }

    @Override
    public String getDescription() {
        return "Adds Logback Logging";
    }

    @Override
    public boolean shouldApply(MicronautCommand micronautCommand, Language language, TestFramework testFramework, BuildTool buildTool, List<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(f -> f instanceof LoggingFeature);
    }

    @Override
    public void apply(CommandContext commandContext) {
        String osName = System.getProperty("os.name");
        boolean jansi = false;
        if (osName == null || !osName.toLowerCase(Locale.ENGLISH).contains("windows")) {
            jansi = true;
        }
        commandContext.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/logback.xml", logback.template(jansi)));
    }
}
