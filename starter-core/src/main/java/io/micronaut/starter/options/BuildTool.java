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
package io.micronaut.starter.options;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum BuildTool {

    GRADLE("build/libs", "build.gradle"),
    MAVEN("target", "pom.xml"),
    GRADLE_KOTLIN("build/libs", "build.gradle.kts");


  private final String jarDirectory;
    private final String fileName;

    BuildTool(String jarDirectory, String fileName) {
        this.jarDirectory = jarDirectory;
        this.fileName = fileName;
    }

    public String getJarDirectory() {
        return jarDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Nonnull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
