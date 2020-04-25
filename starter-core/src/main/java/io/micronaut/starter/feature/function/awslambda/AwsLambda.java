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
package io.micronaut.starter.feature.function.awslambda;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerGroovyJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKotlin;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerJavaJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKotlinJunit;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerKotlinTest;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookRequestHandlerSpock;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedGroovy;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedJava;
import io.micronaut.starter.feature.function.awslambda.template.awsLambdaBookSavedKotlin;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class AwsLambda implements Feature, DefaultFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";

    @Override
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to be deployed to AWS Lambda";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return (applicationType == ApplicationType.FUNCTION);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        addBook(generatorContext, project);
        addBookSaved(generatorContext, project);
        addRequestHandler(generatorContext, project);
        addTest(generatorContext, project);
    }

    private void addTest(GeneratorContext generatorContext, Project project) {
        String testSource =  generatorContext.getTestSourcePath("/{packagePath}/BookRequestHandler");
        generatorContext.addTestTemplate("testBookRequestHandler", testSource,
                awsLambdaBookRequestHandlerJavaJunit.template(project),
                awsLambdaBookRequestHandlerKotlinJunit.template(project),
                awsLambdaBookRequestHandlerGroovyJunit.template(project),
                awsLambdaBookRequestHandlerKotlinTest.template(project),
                awsLambdaBookRequestHandlerSpock.template(project));
    }

    private void addBook(GeneratorContext generatorContext, Project project) {
        String bookFile = generatorContext.getSourcePath("/{packagePath}/Book");
        generatorContext.addTemplate("book", bookFile, awsLambdaBookJava.template(project), awsLambdaBookKotlin.template(project), awsLambdaBookGroovy.template(project));
    }

    private void addBookSaved(GeneratorContext generatorContext, Project project) {
        String bookSavedFile = generatorContext.getSourcePath("/{packagePath}/BookSaved");
        generatorContext.addTemplate("bookSaved", bookSavedFile, awsLambdaBookSavedJava.template(project),
                awsLambdaBookSavedKotlin.template(project), awsLambdaBookSavedGroovy.template(project));
    }

    private void addRequestHandler(GeneratorContext generatorContext, Project project) {
        String awsLambdaBookRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/BookRequestHandler");
        generatorContext.addTemplate("bookMicronautRequestHandler", awsLambdaBookRequestHandlerFile, awsLambdaBookRequestHandlerJava.template(project), awsLambdaBookRequestHandlerKotlin.template(project), awsLambdaBookRequestHandlerGroovy.template(project));
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return applicationType == ApplicationType.FUNCTION && selectedFeatures.stream().noneMatch(feature ->
                feature instanceof FunctionFeature && !feature.getName().equals(getName())
        );
    }
}
