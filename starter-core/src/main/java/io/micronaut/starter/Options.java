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
package io.micronaut.starter;

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.value.ConvertibleValues;
import io.micronaut.core.convert.value.ConvertibleValuesMap;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.*;

public class Options implements ConvertibleValues<Object> {

    private final Language language;
    private final TestFramework testFramework;
    private final BuildTool buildTool;
    private final ConvertibleValuesMap<Object> additionalOptions;

    public Options(Language language, TestFramework testFramework, BuildTool buildTool) {
        this(language, testFramework, buildTool, Collections.emptyMap());
    }

    public Options(Language language, TestFramework testFramework, BuildTool buildTool, Map<String, Object> additionalOptions) {
        this.language = language;
        this.testFramework = testFramework;
        this.buildTool = buildTool;
        this.additionalOptions = new ConvertibleValuesMap<>(additionalOptions);
    }

    public Language getLanguage() {
        return language;
    }

    public TestFramework getTestFramework() {
        return testFramework;
    }

    public BuildTool getBuildTool() {
        return buildTool;
    }

    @Override
    public Set<String> names() {
        return additionalOptions.names();
    }

    @Override
    public Collection<Object> values() {
        return additionalOptions.values();
    }

    @Override
    public <T> Optional<T> get(CharSequence name, ArgumentConversionContext<T> conversionContext) {
        return additionalOptions.get(name, conversionContext);
    }
}
