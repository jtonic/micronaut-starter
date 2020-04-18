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
package io.micronaut.starter.feature.messaging.rabbitmq;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.starter.CodeGenConfig;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CodeGenCommand;
import io.micronaut.starter.feature.messaging.rabbitmq.template.producer.groovyProducer;
import io.micronaut.starter.feature.messaging.rabbitmq.template.producer.javaProducer;
import io.micronaut.starter.feature.messaging.rabbitmq.template.producer.kotlinProducer;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.RenderResult;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.TemplateRenderer;
import picocli.CommandLine;

@CommandLine.Command(name = "create-rabbitmq-producer", description = "Creates a producer class for RabbitMQ")
@Prototype
public class CreateRabbitMQProducer extends CodeGenCommand {

    @CommandLine.Parameters(paramLabel = "PRODUCER", description = "The name of the producer to create")
    String producerName;

    public CreateRabbitMQProducer(@Parameter CodeGenConfig config) {
        super(config);
    }

    @Override
    public boolean applies() {
        return config.getFeatures().contains("rabbitmq");
    }

    @Override
    public Integer call() throws Exception {
        Project project = getProject(producerName);

        TemplateRenderer templateRenderer = getTemplateRenderer(project);

        RenderResult renderResult = null;
        if (config.getSourceLanguage() == Language.java) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/java/{packagePath}/{className}.java", javaProducer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.groovy) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/groovy/{packagePath}/{className}.groovy", groovyProducer.template(project)), overwrite);
        } else if (config.getSourceLanguage() == Language.kotlin) {
            renderResult = templateRenderer.render(new RockerTemplate("src/main/kotlin/{packagePath}/{className}.kt", kotlinProducer.template(project)), overwrite);
        }

        if (renderResult != null) {
            if (renderResult.isSuccess()) {
                out("@|blue ||@ Rendered RabbitMQ producer to " + renderResult.getPath());
            } else if (renderResult.isSkipped()) {
                warning("Rendering skipped for " + renderResult.getPath() + " because it already exists. Run again with -f to overwrite.");
            } else if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }

        return 0;
    }
}
