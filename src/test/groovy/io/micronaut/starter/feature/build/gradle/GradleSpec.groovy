package io.micronaut.starter.feature.build.gradle

import io.micronaut.starter.feature.build.gradle.templates.annotationProcessors
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle
import io.micronaut.starter.feature.graalvm.GraalNativeImage
import io.micronaut.starter.fixture.FeatureFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.util.NameUtils
import spock.lang.Specification

class GradleSpec extends Specification implements ProjectFixture, FeatureFixture {

    void "test settings.gradle"() {
        String template = settingsGradle.template(NameUtils.parse("abc.foo")).render().toString()

        expect:
        template.contains('rootProject.name="foo"')
    }

    void "test gradle.properties"() {
        String template = gradleProperties.template([name: "Sally", age: "30"]).render().toString()

        expect:
        template.contains('name=Sally')
        template.contains('age=30')
    }

    void "test annotation processor dependencies"() {
        when:
        String template = annotationProcessors.template(buildJavaWithFeatures()).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-inject-java"')
        template.contains('annotationProcessor "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(buildKotlinWithFeatures()).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-inject-java"')
        template.contains('kapt "io.micronaut:micronaut-validation"')

        when:
        template = annotationProcessors.template(buildGroovyWithFeatures()).render().toString()

        then:
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "io.micronaut:micronaut-inject-groovy"')
    }

    void 'test graal-native-image feature'() {
        when:
        String template = buildGradle.template(buildProject(), buildJavaWithFeatures(new GraalNativeImage())).render().toString()

        then:
        template.contains('annotationProcessor platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('annotationProcessor "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), buildKotlinWithFeatures(new GraalNativeImage())).render().toString()

        then:
        template.contains('kapt platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('kapt "io.micronaut:micronaut-graal"')
        template.contains('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")')
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')

        when:
        template = buildGradle.template(buildProject(), buildGroovyWithFeatures(new GraalNativeImage())).render().toString()

        then:
        template.count('compileOnly platform("io.micronaut:micronaut-bom:\$micronautVersion")') == 1
        template.contains('compileOnly "org.graalvm.nativeimage:svm"')
    }
}
