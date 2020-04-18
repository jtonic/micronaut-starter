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
package io.micronaut.starter.feature.cache;

import io.micronaut.starter.command.CommandContext;

import javax.inject.Singleton;

@Singleton
public class Hazelcast implements CacheFeature {

    @Override
    public String getName() {
        return "cache-hazelcast";
    }

    @Override
    public String getDescription() {
        return "Adds support for cache using Hazelcast (https://hazelcast.org/)";
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().put("hazelcast.network.addresses", "['121.0.0.1:5701']");
    }

}
