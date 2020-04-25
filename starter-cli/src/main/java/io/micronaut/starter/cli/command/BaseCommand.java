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
package io.micronaut.starter.cli.command;

import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.cli.CommonOptionsMixin;
import picocli.CommandLine;

public class BaseCommand implements ConsoleOutput {

    @CommandLine.Spec
    @ReflectiveAccess
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Mixin
    @ReflectiveAccess
    protected CommonOptionsMixin commonOptions = new CommonOptionsMixin();

    public void out(String message) {
        spec.commandLine().getOut().println(CommandLine.Help.Ansi.AUTO.string(message));
    }

    public void err(String message) {
        spec.commandLine().getErr().println(CommandLine.Help.Ansi.AUTO.string("@|bold,red | Error|@ " + message));
    }

    public void warning(String message) {
        spec.commandLine().getOut().println(CommandLine.Help.Ansi.AUTO.string("@|bold,red | Warning|@ " + message));
    }

    public boolean showStacktrace() {
        return commonOptions.showStacktrace;
    }

    public boolean verbose() {
        return commonOptions.verbose;
    }

}
