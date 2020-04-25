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
package io.micronaut.starter.cli.feature.database;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.core.util.functional.ThrowingSupplier;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.cli.CodeGenConfig;
import io.micronaut.starter.cli.command.CodeGenCommand;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.OutputHandler;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CommandLine.Command(name = "create-repository", description = "Creates a repository")
@Prototype
public class CreateRepositoryCommand extends CodeGenCommand {

    private static final List<String> VALID_NO_PKG_ID_TYPES = Arrays.asList("Integer", "Long", "String");

    @ReflectiveAccess
    @CommandLine.Parameters(index = "0", paramLabel = "REPOSITORY-NAME", description = "The name of the repository to create")
    String repositoryName;

    @ReflectiveAccess
    @CommandLine.Option(names = {"-i", "--idType"}, description = "Specify custom id type [Integer, Long, String] or full package name [ie. com.corp.Book] - Defaults to Long")
    String idType = "Long";

    @Inject
    public CreateRepositoryCommand(@Parameter CodeGenConfig config) {
        super(config);
    }

    public CreateRepositoryCommand(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
        super(config, outputHandlerSupplier, consoleOutput);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("data");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(repositoryName);

        boolean jdbcRepository = config.getFeatures().contains("data-jdbc");
        String idTypeImport = null;

        if (idType.contains(".")) {
            idTypeImport = idType;
        } else if (!VALID_NO_PKG_ID_TYPES.contains(idType)) {
            throw new IllegalArgumentException("Code generation not supported for the specified id type: " + idType + ". Please specify the fully qualified class name.");
        }

        TemplateRenderer templateRenderer = getTemplateRenderer(project);


        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.JAVA) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}Repository.java", javaRepository.template(project, idTypeImport, idType, jdbcRepository)), overwrite);
        } else if (config.getSourceLanguage() == Language.GROOVY) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}Repository.groovy", groovyRepository.template(project, idTypeImport, idType, jdbcRepository)), overwrite);
        } else if (config.getSourceLanguage() == Language.KOTLIN) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}Repository.kt", kotlinRepository.template(project, idTypeImport, idType, jdbcRepository)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered repository to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
