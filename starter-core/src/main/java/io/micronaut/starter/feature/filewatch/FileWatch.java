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
package io.micronaut.starter.feature.filewatch;

import io.micronaut.context.condition.OperatingSystem;
import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;

import javax.inject.Singleton;

@Singleton
public class FileWatch implements Feature {

    private final FileWatchOsx fileWatchOsx;

    public FileWatch(FileWatchOsx fileWatchOsx) {
        this.fileWatchOsx = fileWatchOsx;
    }

    @Override
    public String getName() {
        return "file-watch";
    }

    @Override
    public String getDescription() {
        return "Adds automatic restarts and file watch";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OperatingSystem.getCurrent().isMacOs()) {
            featureContext.addFeature(fileWatchOsx);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("micronaut.io.watch.paths", "src/main");
        commandContext.getConfiguration().put("micronaut.io.watch.restart", true);
    }

}
